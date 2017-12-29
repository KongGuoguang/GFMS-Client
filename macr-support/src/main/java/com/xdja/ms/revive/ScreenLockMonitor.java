package com.xdja.ms.revive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.xdja.ms.keep.KeepAliveManager;

/**
 * 解锁屏幕事件
 */

public class ScreenLockMonitor extends BroadcastReceiver {

    public static final String TAG = ScreenLockMonitor.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) return;

        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            LogUtils.d(TAG,"@接收到亮屏广播~~~");
            KeepAliveManager.getManager().destroyActivities();
        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            LogUtils.d(TAG,"@接收到熄屏广播~~~");
            KeepAliveManager.getManager().startActivity(context.getApplicationContext());
        }

    }
}
