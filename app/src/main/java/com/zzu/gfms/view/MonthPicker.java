package com.zzu.gfms.view;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import com.zzu.gfms.R;
import com.zzu.gfms.utils.ViewUtil;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Author:kongguoguang
 * Date:2017-12-07
 * Time:17:12
 * Summary:
 */

public class MonthPicker extends FrameLayout {
    public MonthPicker(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MonthPicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        Calendar calendar = Calendar.getInstance();
        LayoutInflater.from(context).inflate(R.layout.view_date_picker, this, true);
        DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
        hideDay(datePicker);
        ViewUtil.setDatePickerDividerColor(context, datePicker);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (onMonthChangedListener != null){
                            onMonthChangedListener.onMonthChanged(MonthPicker.this, year, monthOfYear+1);
                        }
                    }
                });
    }

    private void hideDay(DatePicker mDatePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = mDatePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OnMonthChangedListener onMonthChangedListener;

    public void setOnMonthChangedListener(OnMonthChangedListener onMonthChangedListener) {
        this.onMonthChangedListener = onMonthChangedListener;
    }

    /**
     * The callback used to indicate the user changes\d the date.
     */
    public interface OnMonthChangedListener {

        /**
         * Called upon a date change.
         *
         * @param view        The view associated with this listener.
         * @param year        The year that was set.
         * @param month The month that was set (0-11) for compatibility
         *                    with {@link java.util.Calendar}.
         */
        void onMonthChanged(MonthPicker view, int year, int month);
    }
}
