package com.zzu.gfms.data.http;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;


/**
 * Author:kongguoguang
 * Date:2017-05-02
 * Time:11:26
 * Summary:
 */
class OkHttp3Util {

    private static final int READ_TIMEOUT = 15;

    private static final int CONNECT_TIMEOUT = 15;

    private static final int WRITE_TIMEOUT = 15;

    static OkHttpClient createOkHttpClient(final Context context, boolean isHttps) {
        OkHttpClient okHttpClient = null;
        if (isHttps) {
            try {
//                // 服务端证书
//                InputStream certInput = context.getAssets().open("trust.bks");
//                KeyStore mKeyStore = KeyStore.getInstance("BKS");
//                String passWord = "123456";//证书密码
//                mKeyStore.load(certInput, passWord.toCharArray());
//                certInput.close();
//
//                // 单向认证,因此不需要KeyManagerFactory
//                TrustManagerFactory mTrustManagerFactor = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//                mTrustManagerFactor.init(mKeyStore);
//
//                // SSLContext初始化
//                SSLContext mSSLContext = SSLContext.getInstance("TLS");
//                mSSLContext.init(null, mTrustManagerFactor.getTrustManagers(), new SecureRandom());

                //服务端使用公司签发的证书，且在各地部署的证书有可能不一样，故此处采取默认信任所有证书的做法

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new TrustAllCertsManager()}, new SecureRandom());

                okHttpClient = new OkHttpClient.Builder()
                        .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                        .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                        .addInterceptor(new LoggingInterceptor())
                        .sslSocketFactory(sslContext.getSocketFactory(), new TrustAllCertsManager())
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String hostname, SSLSession session) {
                                return true;
                            }
                        })
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            okHttpClient = new OkHttpClient.Builder()
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(new LoggingInterceptor())
                    .build();
        }

        return okHttpClient;
    }

    private static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();
            String method = request.method();
            if ("POST".equals(method)) {//打印post请求体
                Buffer buffer = new Buffer();
                request.body().writeTo(buffer);
                if (buffer.size() > 300){
                    LogUtils.d("OkHttpLog", String.format("发送请求:%s %n requestBody:%s",
                            request.url(), buffer.readUtf8(300)));
                }else {
                    LogUtils.d("OkHttpLog", String.format("发送请求:%s %n requestBody:%s",
                            request.url(), buffer.readUtf8()));
                }

            } else {
                LogUtils.d("OkHttpLog", String.format("发送请求:%s", request.url()));
            }

            long t1 = System.nanoTime();//请求发起的时间

            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间

            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(300);

            LogUtils.d("OkHttpLog",
                    String.format("接收响应:%s %n" +
                                    "httpCode:%s %n" +
                                    "responseBody:%s %n" +
                                    "time consuming:%.1fms %n",
                            response.request().url(),
                            response.code(),
                            responseBody.string(),
                            (t2 - t1) / 1e6d));
            return response;
        }
    }

    private static class TrustAllCertsManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.d("TrustAllCertsManager","checkClientTrusted");
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            Log.d("TrustAllCertsManager","checkServerTrusted");
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {return new X509Certificate[0];}
    }

    /** 拦截器压缩http请求体，许多服务器无法解析 */
    static class GzipRequestInterceptor implements Interceptor {
        @Override public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override public MediaType contentType() {
                    return body.contentType();
                }

                @Override public long contentLength() {
                    return -1; // 无法知道压缩后的数据大小
                }

                @Override public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }
}
