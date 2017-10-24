package com.zzu.gfms.bean;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:10:49
 * Summary:
 */

public class Day {

    private int day;

    private boolean currentMonth;

    private boolean today;

    private boolean hasWorkRecord;

    public Day(int day, boolean currentMonth){
        this.day = day;
        this.currentMonth = currentMonth;
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
}
