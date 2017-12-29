package com.xdja.ms.keep;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.LogUtils;
import com.xdja.ms.daemon.Daemon;
import com.xdja.ms.revive.ProxyService;
import com.xdja.ms.revive.ScreenLockMonitor;

public class MacrobioticService extends Service {

    private final static int GRAY_SERVICE_ID = 1008611;

    public static final String TAG = MacrobioticService.class.getSimpleName();

    private ScreenLockMonitor screenLockMonitor;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG, "@保活服务开始启动~~~");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Daemon.run(getApplicationContext(), ProxyService.class, Daemon.INTERVAL_ONE_MINUTE * 3);
        }
        screenLockMonitor = new ScreenLockMonitor();
        registScreenMonitor();
        attachForegroundService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegistScreenMonitor();
    }

    private void registScreenMonitor() {
        LogUtils.d(TAG, "@注册屏幕亮灭广播监听~~~");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenLockMonitor, filter);
    }

    private void unRegistScreenMonitor() {
        LogUtils.d(TAG, "@注销屏幕亮灭广播监听~~~");
        unregisterReceiver(screenLockMonitor);
    }

    private void attachForegroundService() {
        LogUtils.d(TAG, "@启动前台服务~~~");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else {
            startForeground(GRAY_SERVICE_ID, new Notification());
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
        }
    }


    public static class GrayInnerService extends Service {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
