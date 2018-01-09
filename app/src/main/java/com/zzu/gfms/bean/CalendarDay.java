package com.zzu.gfms.bean;

import com.zzu.gfms.data.dbflow.DayRecord;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:10:49
 * Summary:工作日历中的天
 */

public class CalendarDay {

    private int year;

    private int month;

    private int day;

    private boolean currentMonth;

    private boolean today;

    private boolean hasWorkRecord;

    private DayRecord dayRecord;

    public CalendarDay(int day, boolean currentMonth){
        this.day = day;
        this.currentMonth = currentMonth;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isHasWorkRecord() {
        return hasWorkRecord;
    }

    public void setHasWorkRecord(boolean hasWorkRecord) {
        this.hasWorkRecord = hasWorkRecord;
    }

    public boolean isCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(boolean currentMonth) {
        this.currentMonth = currentMonth;
    }

    public boolean isToday() {
        return today;
    }

    public void setToday(boolean today) {
        this.today = today;
    }

    public DayRecord getDayRecord() {
        return dayRecord;
    }

    public void setDayRecord(DayRecord dayRecord) {
        this.dayRecord = dayRecord;
    }

    @Override
    public String toString() {
        return "CalendarDay{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", currentMonth=" + currentMonth +
                ", today=" + today +
                ", hasWorkRecord=" + hasWorkRecord +
                ", dayRecord=" + dayRecord +
                '}';
    }
}
