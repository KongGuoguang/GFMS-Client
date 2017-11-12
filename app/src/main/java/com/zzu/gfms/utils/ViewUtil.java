package com.zzu.gfms.utils;

import android.view.View;

/**
 * Created by Administrator on 2017/11/12.
 */

public class ViewUtil {
    /**
     * 设置控件不可用,颜色变灰
     * @param view
     * @param flag
     */
    public static void setViewEnable(View view, boolean flag){
        if (view == null){
            return;
        }
        if (flag) {
            view.setEnabled(true);
            view.setAlpha(1f);
            return;
        }
        view.setEnabled(false);
        view.setAlpha(0.5f);
    }
}
