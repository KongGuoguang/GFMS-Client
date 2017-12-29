package com.xdja.ms.revive;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import java.util.List;


/**
 * Alarm管理类，负责设置Alarm,取消Alarm
 */
public class RabbitAlarmManager {
    private static final int TIME_INTERVAL = 60 * 60 * 1000;
    private static final String UNIFY_ALARM_ACTION = "com.rabbit.unify.alarm.action";
    /**
     * 心跳间隔
     */
    private final static int keepAliveInterval = TIME_INTERVAL;

    private static final int ALARM_REQ_CODE = 100009;

    private static final int JOB_ID = 100010;
    /**
     * 注册心跳唤醒， {@link #keepAliveInterval}间隔后发送一次广播
     *
     * @param context context
     */
    public static void schedule(Context context) {

        if (context == null) return;

        long nextAlarmInMilliseconds = System.currentTimeMillis() + keepAliveInterval;

        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextAlarmInMilliseconds, pendingIntent);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, nextAlarmInMilliseconds, pendingIntent);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            buildJobScheduler(context);
        }
    }

    public static void cancel(Context context) {
        AlarmManager am = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent(context);
        am.cancel(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cancelJobScheduler(context);
        }
    }

    private static PendingIntent getPendingIntent(Context context) {
        Intent intentAlarm = new Intent(UNIFY_ALARM_ACTION);
        return PendingIntent.getBroadcast(context.getApplicationContext(), ALARM_REQ_CODE, intentAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void buildJobScheduler(Context context) {
        //先取消已经存在的任务
        cancelJobScheduler(context);

        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(context.getApplicationContext().getPackageName(), JobSchedulerService.class.getName()));
        builder.setPeriodic(TIME_INTERVAL);
        builder.setPersisted(true);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        JobInfo jobInfo = builder.build();

        JobScheduler jobScheduler =
                (JobScheduler) context.getApplicationContext()
                        .getSystemService(Context.JOB_SCHEDULER_SERVICE);

        int scheduleResult = jobScheduler.schedule(jobInfo);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static void cancelJobScheduler(Context context) {
        boolean isExistScheduler = IsExistScheduler(context);
        if (isExistScheduler) {
            JobScheduler jobScheduler = (JobScheduler) context.getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            jobScheduler.cancel(JOB_ID);
        }

    }

    @NonNull
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static boolean IsExistScheduler(Context context) {
        JobScheduler jobScheduler = (JobScheduler) context.getApplicationContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
        if (allPendingJobs != null || !allPendingJobs.isEmpty()) {
            for (JobInfo info : allPendingJobs) {
                if (info != null) {
                    ComponentName componentName = info.getService();
                    if (componentName != null) {
                        String packageName = componentName.getPackageName();
                        String className = componentName.getClassName();
                        if (packageName.equals(context.getApplicationContext().getPackageName()) && className.equals(JobSchedulerService.class.getName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
