package com.zzu.gfms.data.http;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:17:13
 * Summary:
 */

public class HttpReply {

    private int status;

    private String message;

    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
