package com.zzu.gfms.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import com.zzu.gfms.R;
import com.zzu.gfms.utils.DayUtil;
import com.zzu.gfms.utils.ViewUtil;

import java.util.Calendar;

/**
 * Author:kongguoguang
 * Date:2017-12-07
 * Time:16:23
 * Summary:
 */

public class SpinnerDatePicker extends FrameLayout{


    public SpinnerDatePicker(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SpinnerDatePicker(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private int year;

    private int monthOfYear;

    private int dayOfMonth;

    private void init(Context context){
        Calendar calendar = Calendar.getInstance();
        year = DayUtil.getYear(calendar);
        monthOfYear = DayUtil.getMonth(calendar);
        dayOfMonth = DayUtil.getDayOfMonth(calendar);

        LayoutInflater.from(context).inflate(R.layout.view_date_picker, this, true);
        DatePicker datePicker = (DatePicker) findViewById(R.id.date_picker);
        ViewUtil.setDatePickerDividerColor(context, datePicker);
        datePicker.init(year, monthOfYear -1, dayOfMonth, new DatePicker.OnDateChangedListener() {
                            @Override
                            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                SpinnerDatePicker.this.year = year;
                                SpinnerDatePicker.this.monthOfYear = monthOfYear+1;
                                SpinnerDatePicker.this.dayOfMonth = dayOfMonth;
                                notifyDateChanged();
                            }
                        });
    }

    private OnDateChangedListener onDateChangedListener;

    public void setOnDateChangedListener(OnDateChangedListener onDateChangedListener){
        this.onDateChangedListener = onDateChangedListener;
        notifyDateChanged();
    }

    private void notifyDateChanged(){
        if (onDateChangedListener != null){
            onDateChangedListener.onDateChanged(SpinnerDatePicker.this, year, monthOfYear, dayOfMonth);
        }
    }


    /**
     * The callback used to indicate the user changes\d the date.
     */
    public interface OnDateChangedListener {

        /**
         * Called upon a date change.
         *
         * @param view        The view associated with this listener.
         * @param year        The year that was set.
         * @param month The monthOfYear that was set (0-11) for compatibility
         *                    with {@link java.util.Calendar}.
         * @param day  The dayOfMonth of the monthOfYear that was set.
         */
        void onDateChanged(SpinnerDatePicker view, int year, int month, int day);
    }
}
