package com.zzu.gfms.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.blankj.utilcode.util.LogUtils;
import com.xdja.ms.keep.MacrobioticService;
import com.zzu.gfms.event.HeartbeatSuccess;

import org.greenrobot.eventbus.EventBus;

public class HeartbeatService extends MacrobioticService {
    public HeartbeatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        EventBus.getDefault().post(new HeartbeatSuccess());
        return START_STICKY;
    }
}
