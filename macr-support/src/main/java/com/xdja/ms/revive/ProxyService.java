package com.xdja.ms.revive;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.xdja.ms.MacrobioticDemand;

import static android.content.ContentValues.TAG;

/**
 * <b>Description : </b>
 * <p>Created by <a href="mailto:fanjiandong@outlook.com">fanjiandong</a> on 2016/11/16 9:55.</p>
 */

public class ProxyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            MacrobioticDemand macrobioticDemand = (MacrobioticDemand) getApplicationContext();
            macrobioticDemand.macrobioticService();

            stopSelf();
        } catch (ClassCastException ignored) {}
        return super.onStartCommand(intent, flags, startId);
    }
}
