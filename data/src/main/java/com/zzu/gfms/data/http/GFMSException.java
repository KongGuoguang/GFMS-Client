package com.zzu.gfms.data.http;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:19:04
 * Summary:
 */

public class GFMSException extends Exception {
    private int errorCode;

    private String errorMessage;

    public GFMSException(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
