package com.zzu.gfms.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.zzu.gfms.R;

import java.lang.reflect.Field;

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

    /**
     * 设置NumberPicker分隔线颜色
     */
    public static void setNumberPickerDividerColor(Context context, NumberPicker... numberPickers) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {//设置分割线的颜色值
                    for (NumberPicker numberPicker : numberPickers){
                        pf.set(numberPicker, new ColorDrawable(ContextCompat.getColor(context, R.color.app_color_blue_disabled)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 设置DatePicker的分割线颜色
     *
     * @param datePickers
     */
    public static void setDatePickerDividerColor(Context context, DatePicker... datePickers) {

        for (DatePicker datePicker: datePickers){
            // 获取 mSpinners
            LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);

            // 获取 NumberPicker
            LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
            for (int i = 0; i < mSpinners.getChildCount(); i++) {
                NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);

                Field[] pickerFields = NumberPicker.class.getDeclaredFields();
                for (Field pf : pickerFields) {
                    if (pf.getName().equals("mSelectionDivider")) {
                        pf.setAccessible(true);
                        try {
                            pf.set(picker, new ColorDrawable(ContextCompat.getColor(context, R.color.app_color_blue_disabled)));//设置分割线颜色
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }

    }

}
