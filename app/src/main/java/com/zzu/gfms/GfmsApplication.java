package com.zzu.gfms;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by Administrator on 2017/10/22.
 */

public class GfmsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
