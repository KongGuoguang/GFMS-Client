package com.zzu.gfms.data.http;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:17:13
 * Summary:
 */

public class HttpReply<T> {

    private int status;

    private String message;

    private T data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
