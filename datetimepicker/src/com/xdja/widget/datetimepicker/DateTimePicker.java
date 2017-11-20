package com.xdja.widget.datetimepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/25.
 */

public class DateTimePicker extends LinearLayout implements NumberPicker.OnValueChangeListener{
    private NumberPicker mYearPicker;
    private NumberPicker mMonthPicker;
    private NumberPicker mDayOfMonthPicker;
    private NumberPicker mHourPicker;
    private NumberPicker mMinutePicker;
    private NumberPicker mSecondPicker;

    private TextView mYear;
    private TextView mMonth;
    private TextView mDay;
    private TextView mHour;
    private TextView mMinute;

    private View view;


    private Calendar mCalendar;

    private OnDateTimeChangedListener onDateTimeChangedListener;

    private LayoutInflater mLayoutInflater;


    private int color;

    private int background;

    private float numberSize;

    private float separatorSize;

    private int startYear;

    private int endYear;

    private int numberSpacing;

    private float mDensity;

    private float dateTimeSpacing;

    public DateTimePicker(Context context) {
        this(context, null);
    }

    public DateTimePicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mDensity = getResources().getDisplayMetrics().density;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        readAttrs(context, attrs);
        init();
    }

    private void readAttrs(Context context, AttributeSet attrs){
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DateTimePicker);
        color = a.getColor(R.styleable.DateTimePicker_dtp_color, Color.DKGRAY);
        numberSize = a.getDimension(R.styleable.DateTimePicker_dtp_numberSize, 16 * mDensity);
        separatorSize = a.getDimension(R.styleable.DateTimePicker_dtp_separatorSize, 18 * mDensity);
        startYear = a.getInt(R.styleable.DateTimePicker_dtp_startYear, 1900);
        endYear = a.getInt(R.styleable.DateTimePicker_dtp_endYear, 2100);
        numberSpacing = (int) a.getDimension(R.styleable.DateTimePicker_dtp_numberSpacing, 32 * mDensity);
        dateTimeSpacing = a.getDimension(R.styleable.DateTimePicker_dtp_dateTimeSpacing, 30 * mDensity);
        background = a.getColor(R.styleable.DateTimePicker_dtp_background, Color.WHITE);
        a.recycle();
    }

    private void init() {

        mLayoutInflater.inflate(R.layout.date_time_picker_layout, this, true);

        mYearPicker = (NumberPicker) findViewById(R.id.year_picker);
        mYearPicker.setTextColor(color);
        mYearPicker.setTextSize(numberSize);
        mYearPicker.setStartNumber(startYear);
        mYearPicker.setEndNumber(endYear);
        mYearPicker.setVerticalSpacing(numberSpacing);

        mMonthPicker = (NumberPicker) findViewById(R.id.month_picker);
        mMonthPicker.setTextColor(color);
        mMonthPicker.setTextSize(numberSize);
        mMonthPicker.setVerticalSpacing(numberSpacing);

        mDayOfMonthPicker = (NumberPicker) findViewById(R.id.day_picker);
        mDayOfMonthPicker.setTextColor(color);
        mDayOfMonthPicker.setTextSize(numberSize);
        mDayOfMonthPicker.setVerticalSpacing(numberSpacing);

        mHourPicker = (NumberPicker) findViewById(R.id.hour_picker);
        mHourPicker.setTextColor(color);
        mHourPicker.setTextSize(numberSize);
        mHourPicker.setVerticalSpacing(numberSpacing);

        mMinutePicker = (NumberPicker) findViewById(R.id.minute_picker);
        mMinutePicker.setTextColor(color);
        mMinutePicker.setTextSize(numberSize);
        mMinutePicker.setVerticalSpacing(numberSpacing);

        mSecondPicker = (NumberPicker) findViewById(R.id.second_picker);
        mSecondPicker.setTextColor(color);
        mSecondPicker.setTextSize(numberSize);
        mSecondPicker.setVerticalSpacing(numberSpacing);

        mYear = (TextView) findViewById(R.id.year_text);
        mYear.setTextColor(color);
        mYear.setTextSize(TypedValue.COMPLEX_UNIT_PX, separatorSize);

        mMonth = (TextView) findViewById(R.id.month_text);
        mMonth.setTextColor(color);
        mMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX, separatorSize);

        mDay = (TextView) findViewById(R.id.day_text);
        mDay.setTextColor(color);
        mDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, separatorSize);

        mHour = (TextView) findViewById(R.id.hour_text);
        mHour.setTextColor(color);
        mHour.setTextSize(TypedValue.COMPLEX_UNIT_PX, separatorSize);

        mMinute = (TextView) findViewById(R.id.minute_text);
        mMinute.setTextColor(color);
        mMinute.setTextSize(TypedValue.COMPLEX_UNIT_PX, separatorSize);

        view = findViewById(R.id.view);
        LinearLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.height = (int) dateTimeSpacing;
        view.setLayoutParams(params);

        mYearPicker.setOnValueChangeListener(this);
        mMonthPicker.setOnValueChangeListener(this);
        mDayOfMonthPicker.setOnValueChangeListener(this);
        mHourPicker.setOnValueChangeListener(this);
        mMinutePicker.setOnValueChangeListener(this);
        mSecondPicker.setOnValueChangeListener(this);

        mCalendar = Calendar.getInstance();
        setDateTime(mCalendar.getTime());
        setBackground(background);
    }

    public DateTimePicker setDateTime(Date date){
        mCalendar.setTime(date);
        mDayOfMonthPicker.setEndNumber(mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        mYearPicker.setCurrentNumber(mCalendar.get(Calendar.YEAR));
        mMonthPicker.setCurrentNumber(mCalendar.get(Calendar.MONTH) + 1);
        mDayOfMonthPicker.setCurrentNumber(mCalendar.get(Calendar.DAY_OF_MONTH));

        mHourPicker.setCurrentNumber(mCalendar.get(Calendar.HOUR_OF_DAY));
        mMinutePicker.setCurrentNumber(mCalendar.get(Calendar.MINUTE));
        mSecondPicker.setCurrentNumber(mCalendar.get(Calendar.SECOND));

        return this;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (picker == mYearPicker) {
            int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
            mCalendar.set(newVal, mCalendar.get(Calendar.MONTH), 1);
            int lastDayOfMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (dayOfMonth > lastDayOfMonth) {
                dayOfMonth = lastDayOfMonth;
            }
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mDayOfMonthPicker.setEndNumber(lastDayOfMonth);
        } else if (picker == mMonthPicker) {
            int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);
            mCalendar.set(mCalendar.get(Calendar.YEAR), newVal - 1, 1);
            int lastDayOfMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            if (dayOfMonth > lastDayOfMonth) {
                dayOfMonth = lastDayOfMonth;
            }
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            mDayOfMonthPicker.setEndNumber(lastDayOfMonth);
        } else if (picker == mDayOfMonthPicker) {
            mCalendar.set(Calendar.DAY_OF_MONTH, newVal);
        }

        notifyDateTimeChanged();
    }

    /**
     * The callback used to indicate the user changes\d the date.
     */
    public interface OnDateTimeChangedListener {

        /**
         * Called upon a date change.
         *
         * @param view        The view associated with this listener.
         * @param year        The year that was set.
         * @param month The month that was set (0-11) for compatibility
         *                    with {@link java.util.Calendar}.
         * @param day  The day of the month that was set.
         */
        void onDateTimeChanged(DateTimePicker view, int year, int month, int day,
                               int hour, int minute, int second);
    }

    public DateTimePicker setOnDateTimeChangedListener(OnDateTimeChangedListener l) {
        onDateTimeChangedListener = l;
        return this;
    }

    private void notifyDateTimeChanged() {
        if (onDateTimeChangedListener != null) {
            onDateTimeChangedListener.onDateTimeChanged(this, getYear(), getMonth(), getDay(),
                    getHour(), getMinute(), getSecond());
        }
    }

    public int getYear() {
        return mYearPicker.getCurrentNumber();
    }

    public int getMonth() {
        return mMonthPicker.getCurrentNumber();
    }

    public int getDay() {
        return mDayOfMonthPicker.getCurrentNumber();
    }

    public int getHour(){return mHourPicker.getCurrentNumber();}

    public int getMinute(){return mMinutePicker.getCurrentNumber();}

    public int getSecond(){return mSecondPicker.getCurrentNumber();}

    public DateTimePicker setColor(int color) {
        this.color = color;
        mYearPicker.setTextColor(color);
        mMonthPicker.setTextColor(color);
        mDayOfMonthPicker.setTextColor(color);
        mYear.setTextColor(color);
        mMonth.setTextColor(color);
        mDay.setTextColor(color);
        mHourPicker.setTextColor(color);
        mMinutePicker.setTextColor(color);
        mSecondPicker.setTextColor(color);
        mHour.setTextColor(color);
        mMinute.setTextColor(color);
        return this;
    }

    public DateTimePicker setNumberSize(float size) {
        numberSize = size * mDensity;
        mYearPicker.setTextSize(numberSize);
        mMonthPicker.setTextSize(numberSize);
        mDayOfMonthPicker.setTextSize(numberSize);
        mHourPicker.setTextSize(numberSize);
        mMinutePicker.setTextSize(numberSize);
        mSecondPicker.setTextSize(numberSize);
        return this;
    }

    public DateTimePicker setNumberSpacing(int spacing) {
        numberSpacing = (int) (spacing * mDensity);
        mYearPicker.setVerticalSpacing(numberSpacing);
        mMonthPicker.setVerticalSpacing(numberSpacing);
        mDayOfMonthPicker.setVerticalSpacing(numberSpacing);
        mHourPicker.setVerticalSpacing(numberSpacing);
        mMinutePicker.setVerticalSpacing(numberSpacing);
        mSecondPicker.setVerticalSpacing(numberSpacing);
        return this;
    }

    public DateTimePicker setSeparatorSize(float size){
        separatorSize = size;
        mYear.setTextSize(separatorSize);
        mMonth.setTextSize(separatorSize);
        mDay.setTextSize(separatorSize);
        mHour.setTextSize(separatorSize);
        mMinute.setTextSize(separatorSize);
        return this;
    }

    public DateTimePicker setStartYear(int year){
        startYear = year;
        mYearPicker.setStartNumber(startYear);
        return this;
    }

    public DateTimePicker setEndYear(int year){
        endYear = year;
        mYearPicker.setEndNumber(endYear);
        return this;
    }

    public DateTimePicker setDateTimeSpacing(int spacing){
        dateTimeSpacing = spacing * mDensity;
        LinearLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.height = (int) dateTimeSpacing;
        view.setLayoutParams(params);
        return this;
    }

    public DateTimePicker setBackground(int color) {
        super.setBackgroundColor(color);
        mYearPicker.setBackground(color);
        mMonthPicker.setBackground(color);
        mDayOfMonthPicker.setBackground(color);
        mHourPicker.setBackground(color);
        mMinutePicker.setBackground(color);
        mSecondPicker.setBackground(color);
        return this;
    }
}
