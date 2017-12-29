package com.xdja.ms.revive;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import com.xdja.ms.MacrobioticDemand;


/**
 * <P>Summery:</P>
 * <P>Description:</P>
 * <P>Auth:fanjiandong</P>
 * <P>Package:com.rabbit.core</P>
 * <P>Date:2016/9/8</P>
 * <P>Time:17:38</P>
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {

    private static final String TAG = JobSchedulerService.class.getSimpleName();

    int what = Long.valueOf(System.currentTimeMillis()).intValue();

    @Override
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage(Message.obtain(mJobHandler, what, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(what);
        return true;
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            try {
                MacrobioticDemand macrobioticDemand = (MacrobioticDemand) getApplicationContext();
                macrobioticDemand.macrobioticService();
            } catch (ClassCastException ignored) {}
            jobFinished((JobParameters) msg.obj, true);
            return true;
        }
    });
}
