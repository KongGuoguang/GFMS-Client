package com.zzu.gfms.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.xdja.ms.keep.MacrobioticService;

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

        return START_STICKY;
    }
}
