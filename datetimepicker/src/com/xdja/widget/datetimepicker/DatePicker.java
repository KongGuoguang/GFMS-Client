package com.xdja.widget.datetimepicker;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DatePicker extends LinearLayout implements NumberPicker.OnValueChangeListener {

    private NumberPicker mYearPicker;
    private NumberPicker mMonthPicker;
    private NumberPicker mDayOfMonthPicker;

    private TextView mYear;
    private TextView mMonth;
    private TextView mDay;

    private Calendar mCalendar;

    private OnDateChangedListener mOnDateChangedListener;

    private LayoutInflater mLayoutInflater;

    private int color;

    private int background;

    private float numberSize;

    private float separatorSize;

    private int startYear;

    private int endYear;

    private int numberSpacing;

    private float mDensity;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
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
        background = a.getColor(R.styleable.DateTimePicker_dtp_background, Color.WHITE);
        a.recycle();
    }

    private void init() {

        mLayoutInflater.inflate(R.layout.date_picker_layout, this, true);

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

        mYear = (TextView) findViewById(R.id.year_text);
        mYear.setTextColor(color);
        mYear.setTextSize(TypedValue.COMPLEX_UNIT_PX, separatorSize);

        mMonth = (TextView) findViewById(R.id.month_text);
        mMonth.setTextColor(color);
        mMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX, separatorSize);

        mDay = (TextView) findViewById(R.id.day_text);
        mDay.setTextColor(color);
        mDay.setTextSize(TypedValue.COMPLEX_UNIT_PX, separatorSize);

        mYearPicker.setOnValueChangeListener(this);
        mMonthPicker.setOnValueChangeListener(this);
        mDayOfMonthPicker.setOnValueChangeListener(this);

        mCalendar = Calendar.getInstance();
        setDate(mCalendar.getTime());
        setBackground(background);
    }

    public DatePicker setDate(Date date) {
        mCalendar.setTime(date);
        mDayOfMonthPicker.setEndNumber(mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        mYearPicker.setCurrentNumber(mCalendar.get(Calendar.YEAR));
        mMonthPicker.setCurrentNumber(mCalendar.get(Calendar.MONTH) + 1);
        mDayOfMonthPicker.setCurrentNumber(mCalendar.get(Calendar.DAY_OF_MONTH));
        return this;
    }

    @Override
    public void onValueChange(final NumberPicker picker, final int oldVal, final int newVal) {

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

        notifyDateChanged();
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
         * @param month The month that was set (0-11) for compatibility
         *                    with {@link java.util.Calendar}.
         * @param day  The day of the month that was set.
         */
        void onDateChanged(DatePicker view, int year, int month, int day);
    }

    public DatePicker setOnDateChangedListener(OnDateChangedListener l) {
        mOnDateChangedListener = l;
        return this;
    }

    private void notifyDateChanged() {
        if (mOnDateChangedListener != null) {
            mOnDateChangedListener.onDateChanged(this, getYear(), getMonth(), getDay());
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

//    public DatePicker setSoundEffect(Sound sound) {
//        mYearPicker.setSoundEffect(sound);
//        mMonthPicker.setSoundEffect(sound);
//        mDayOfMonthPicker.setSoundEffect(sound);
//        return this;
//    }
//
//    @Override
//    public void setSoundEffectsEnabled(boolean soundEffectsEnabled) {
//        super.setSoundEffectsEnabled(soundEffectsEnabled);
//        mYearPicker.setSoundEffectsEnabled(soundEffectsEnabled);
//        mMonthPicker.setSoundEffectsEnabled(soundEffectsEnabled);
//        mDayOfMonthPicker.setSoundEffectsEnabled(soundEffectsEnabled);
//    }

//    public DatePicker setRowNumber(int rowNumber) {
//        mYearPicker.setRowNumber(rowNumber);
//        mMonthPicker.setRowNumber(rowNumber);
//        mDayOfMonthPicker.setRowNumber(rowNumber);
//        return this;
//    }

    public DatePicker setColor(int color) {
        this.color = color;
        mYearPicker.setTextColor(color);
        mMonthPicker.setTextColor(color);
        mDayOfMonthPicker.setTextColor(color);
        mYear.setTextColor(color);
        mMonth.setTextColor(color);
        mDay.setTextColor(color);
        return this;
    }

    public DatePicker setNumberSize(float size) {
        numberSize = size * mDensity;
        mYearPicker.setTextSize(numberSize);
        mMonthPicker.setTextSize(numberSize);
        mDayOfMonthPicker.setTextSize(numberSize);
        return this;
    }

    public DatePicker setNumberSpacing(int spacing) {
        numberSpacing = (int) (spacing * mDensity);
        mYearPicker.setVerticalSpacing(numberSpacing);
        mMonthPicker.setVerticalSpacing(numberSpacing);
        mDayOfMonthPicker.setVerticalSpacing(numberSpacing);
        return this;
    }

    public DatePicker setSeparatorSize(float size){
        separatorSize = size;
        mYear.setTextSize(separatorSize);
        mMonth.setTextSize(separatorSize);
        mDay.setTextSize(separatorSize);
        return this;
    }

    public DatePicker setStartYear(int year){
        startYear = year;
        mYearPicker.setStartNumber(startYear);
        return this;
    }

    public DatePicker setEndYear(int year){
        endYear = year;
        mYearPicker.setEndNumber(endYear);
        return this;
    }


    public DatePicker setBackground(int color) {
        super.setBackgroundColor(color);
        mYearPicker.setBackground(color);
        mMonthPicker.setBackground(color);
        mDayOfMonthPicker.setBackground(color);
        return this;
    }



}
