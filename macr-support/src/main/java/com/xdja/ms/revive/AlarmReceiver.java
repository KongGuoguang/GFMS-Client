package com.xdja.ms.revive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.xdja.ms.MacrobioticDemand;

import static android.content.ContentValues.TAG;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "@触发了 Alarm 定时服务~~~");
        com.xdja.ms.revive.RabbitAlarmManager.schedule(context);
        if (context != null) {
            try {
                MacrobioticDemand macrobioticDemand = (MacrobioticDemand) context.getApplicationContext();
                macrobioticDemand.macrobioticService();
            } catch (ClassCastException e) {
                LogUtils.d(TAG, e.getLocalizedMessage());
            }
        }
    }
}
