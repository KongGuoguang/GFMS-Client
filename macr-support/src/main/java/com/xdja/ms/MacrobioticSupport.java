package com.xdja.ms;

import android.content.Context;
import android.support.annotation.NonNull;
import com.xdja.ms.revive.RabbitAlarmManager;

import static android.content.ContentValues.TAG;

/**
 * <b>Description : </b>
 * <p>Created by <a href="mailto:fanjiandong@outlook.com">fanjiandong</a> on 2016/10/20 10:42.</p>
 */

public class MacrobioticSupport {

    /**
     * 开启服务并启动定时检测
     * @param context 上下文
     */
    public static void runWithSchedule(@NonNull Context context) {
        try {
            MacrobioticDemand macrobioticDemand = (MacrobioticDemand) context.getApplicationContext();
            macrobioticDemand.macrobioticService();
            schedule(context);
        } catch (ClassCastException ignored) {
        }
    }

    /**
     * 启动定时检测
     * @param context 上下文
     */
    public static void schedule(@NonNull Context context) {
        RabbitAlarmManager.schedule(context.getApplicationContext());
    }

    public static void cancel(@NonNull Context context) {
        RabbitAlarmManager.cancel(context.getApplicationContext());
    }
}
