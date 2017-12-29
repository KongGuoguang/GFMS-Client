package com.xdja.ms.keep;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;

/**
 * <P>Summery:</P>
 * <P>Description:</P>
 * <P>Auth:fanjiandong</P>
 * <P>Package:com.rabbit.core</P>
 * <P>Date:2016/9/8</P>
 * <P>Time:11:07</P>
 */
public final class KeepAliveActivity extends Activity {

    private static final String TAG = KeepAliveActivity.class.getSimpleName();

    private boolean isABaby = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        KeepAliveManager.getManager().addKeepAliveActivity(this);

        Window window = getWindow();
        if (window != null) {
            window.setGravity(Gravity.LEFT | Gravity.TOP);
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.x = 0;
            attributes.y = 0;
            attributes.width = 1;
            attributes.height = 1;
            window.setAttributes(attributes);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!this.isABaby) {

            try {
                finish();
            } catch (Exception e) {
                LogUtils.w(TAG, e.getMessage());
            }
        }else{
            this.isABaby = false;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeepAliveManager.getManager().removeKeepAliveActivity(this);
    }
}
