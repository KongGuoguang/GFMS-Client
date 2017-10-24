package com.zzu.gfms.data.http;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:19:04
 * Summary:
 */

public class GFMSException extends Exception {
    private int errorCode;

    public GFMSException(int errorCode){
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
