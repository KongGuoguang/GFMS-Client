package com.xdja.ms.revive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.LogUtils;
import com.xdja.ms.MacrobioticDemand;


public class EasyTriggerReceiver extends BroadcastReceiver {

    public static final String TAG = EasyTriggerReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d(TAG, "@触发了 " + intent.getAction() + "广播~~~");
        if (context != null) {
            try {
                MacrobioticDemand macrobioticDemand = (MacrobioticDemand) context.getApplicationContext();
                macrobioticDemand.macrobioticService();
            } catch (ClassCastException e) {
                LogUtils.d(TAG,e.getLocalizedMessage());
            }
        }
    }
}
