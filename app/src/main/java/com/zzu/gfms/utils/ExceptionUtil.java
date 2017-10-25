package com.zzu.gfms.utils;

import com.blankj.utilcode.util.LogUtils;
import com.zzu.gfms.data.http.GFMSException;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import retrofit2.HttpException;

/**
 * Author:kongguoguang
 * Date:2017-10-25
 * Time:15:27
 * Summary:
 */

public class ExceptionUtil {

    public static String parseErrorMessage(Throwable e){
        if (e instanceof HttpException){
            return "服务器异常，请稍候重试（错误码：" + ((HttpException) e).code() + "）";
        }

        if (e instanceof SocketException){
            return "网络异常，连接不到服务器";
        }

        if (e instanceof SocketTimeoutException){
            return "网络超时，请稍候重试";
        }

        if (e instanceof GFMSException){
            return parseServerError(((GFMSException) e).getErrorCode());
        }

        LogUtils.d(e.getMessage());
        return "未知错误，请联系管理员";
    }

    private static String parseServerError(int errorCode){
        return "";
    }
}
