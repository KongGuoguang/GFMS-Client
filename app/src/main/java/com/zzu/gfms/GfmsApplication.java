package com.zzu.gfms;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.zzu.gfms.data.DataRepository;


/**
 * Created by Administrator on 2017/10/22.
 */

public class GfmsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DataRepository.init(this);
    }
}
