package com.zzu.gfms.data.http;

import com.zzu.gfms.data.dbflow.Worker;

/**
 * Created by Administrator on 2017/11/3.
 */

public class LoginReply extends HttpReply {

    private Worker data;

    public Worker getData() {
        return data;
    }

    public void setData(Worker data) {
        this.data = data;
    }
}
