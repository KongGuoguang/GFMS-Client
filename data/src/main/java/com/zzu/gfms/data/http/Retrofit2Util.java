package com.zzu.gfms.data.http;

import android.content.Context;
import android.text.TextUtils;


import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Author:kongguoguang
 * Date:2017-05-02
 * Time:11:37
 * Summary:
 */

public class Retrofit2Util {

    private static ServerInterface serverInterface;

    public static void createServerInterface(Context context) {

        String baseUrl = "http://11.12.109.115:8080/workRecord/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkHttp3Util.createOkHttpClient(context, false))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        serverInterface = retrofit.create(ServerInterface.class);
    }

    public static ServerInterface getServerInterface(){
        if (serverInterface == null){
            throw new RuntimeException("Retrofit2Util, serverInterface not created!");
        }
        return serverInterface;
    }
}
