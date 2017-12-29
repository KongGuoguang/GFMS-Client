package com.xdja.ms.keep;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;

/**
 * <P>Summery:</P>
 * <P>Description:</P>
 * <P>Auth:fanjiandong</P>
 * <P>Package:com.rabbit.core</P>
 * <P>Date:2016/9/8</P>
 * <P>Time:11:47</P>
 */
public final class KeepAliveManager {

    private static final String TAG = KeepAliveManager.class.getSimpleName();

    private static KeepAliveManager manager = null;

    public static KeepAliveManager getManager() {
        if (manager == null) {
            synchronized (KeepAliveManager.class) {
                if (manager == null) {
                    manager = new KeepAliveManager();
                }
            }
        }
        return manager;
    }

    private final List<KeepAliveActivity> keepAliveActivities;

    private KeepAliveManager() {
        keepAliveActivities = new ArrayList<>();
    }

    public void addKeepAliveActivity(KeepAliveActivity activity) {
        if (activity == null) return;
        this.keepAliveActivities.add(activity);
    }

    public void removeKeepAliveActivity(KeepAliveActivity activity) {
        if (activity == null) return;
        if (this.keepAliveActivities.contains(activity)) {
            this.keepAliveActivities.remove(activity);
        }
    }

    public void startActivity(Context context) {
        if (context == null) return;
        Intent intent = new Intent(context.getApplicationContext(), KeepAliveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        context.getApplicationContext().startActivity(intent);
    }

    public void destroyActivities() {
        if (!this.keepAliveActivities.isEmpty()) {
            for (KeepAliveActivity ka : keepAliveActivities) {
                if (ka != null) {
                    try {
                        ka.finish();
                    }catch (Exception ignored){}
                }
            }
        }
    }
}
