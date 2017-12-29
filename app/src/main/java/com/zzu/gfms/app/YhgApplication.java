package com.zzu.gfms.app;

import android.app.Application;
import android.content.Intent;

import com.xdja.ms.MacrobioticDemand;
import com.xdja.ms.MacrobioticSupport;
import com.zzu.gfms.service.HeartbeatService;

/**
 * Author:kongguoguang
 * Date:2017-12-29
 * Time:19:03
 * Summary:
 */

public class YhgApplication extends Application implements MacrobioticDemand {
    @Override
    public void macrobioticService() {
        startService(new Intent(this, HeartbeatService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //启动进程保活
        //MacrobioticSupport.runWithSchedule(this);
    }
}
