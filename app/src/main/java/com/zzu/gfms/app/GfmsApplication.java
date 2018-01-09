package com.zzu.gfms.app;

import android.app.Application;
import android.content.Intent;

import com.blankj.utilcode.util.Utils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.xdja.ms.MacrobioticDemand;
import com.xdja.ms.MacrobioticSupport;
import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.service.HeartbeatService;


/**
 * Created by Administrator on 2017/10/22.
 */

public class GfmsApplication extends Application implements MacrobioticDemand {

    @Override
    public void onCreate() {
        super.onCreate();
        DataRepository.init(this);
        Utils.init(this);
        //启动进程保活
        MacrobioticSupport.runWithSchedule(this);
    }

    @Override
    public void macrobioticService() {
        startService(new Intent(this, HeartbeatService.class));
    }
}
