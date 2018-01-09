package com.zzu.gfms.utils;

import com.zzu.gfms.bean.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:10:52
 * Summary:
 */

public class CalendarUtil {

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getMonth(Calendar calendar) {
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前是几号
     *
     * @return
     */
    public static int getDayOfMonth(Calendar calendar){return calendar.get(Calendar.DAY_OF_MONTH);}

    /**
     * 根据传入的年份和月份，判断当前月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if (isLeap(year)) {
                    return 29;
                } else {
                    return 28;
                }
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
        }
        return -1;
    }

    /**
     * 根据传入的年份和月份，判断上一个有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfLastMonth(int year, int month) {
        int lastDaysOfMonth = 0;
        if (month == 1) {
            lastDaysOfMonth = getDaysOfMonth(year - 1, 12);
        } else {
            lastDaysOfMonth = getDaysOfMonth(year, month - 1);
        }
        return lastDaysOfMonth;
    }

    /**
     * 判断是否为闰年
     *
     * @param year
     * @return
     */
    public static boolean isLeap(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }

    /**
     * 获取上个月的最后几天
     * @return
     */
    private static List<Integer> getLastFewDaysOfLastMonth(int daysOfLastMonth, int dayOfWeek){
        List<Integer> lastFewDaysOfLastMonth = new ArrayList<>();
        switch (dayOfWeek){
            case Calendar.MONDAY:
                break;
            case Calendar.TUESDAY:
                lastFewDaysOfLastMonth.add(daysOfLastMonth);
                break;
            case Calendar.WEDNESDAY:
                lastFewDaysOfLastMonth.add(daysOfLastMonth-1);
                lastFewDaysOfLastMonth.add(daysOfLastMonth);
                break;
            case Calendar.THURSDAY:
                lastFewDaysOfLastMonth.add(daysOfLastMonth-2);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-1);
                lastFewDaysOfLastMonth.add(daysOfLastMonth);
                break;
            case Calendar.FRIDAY:
                lastFewDaysOfLastMonth.add(daysOfLastMonth-3);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-2);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-1);
                lastFewDaysOfLastMonth.add(daysOfLastMonth);
                break;
            case Calendar.SATURDAY:
                lastFewDaysOfLastMonth.add(daysOfLastMonth-4);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-3);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-2);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-1);
                lastFewDaysOfLastMonth.add(daysOfLastMonth);
                break;
            case Calendar.SUNDAY:
                lastFewDaysOfLastMonth.add(daysOfLastMonth-5);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-4);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-3);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-2);
                lastFewDaysOfLastMonth.add(daysOfLastMonth-1);
                lastFewDaysOfLastMonth.add(daysOfLastMonth);
                break;
        }

        return lastFewDaysOfLastMonth;
    }

    public static List<CalendarDay> getAllDays(Calendar calendar){

        boolean isCurrentMonth = getMonth(Calendar.getInstance()) == getMonth(calendar);

        List<CalendarDay> allCalendarDays = new ArrayList<>();
        int year = getYear(calendar);
        int month = getMonth(calendar);
        int dayOfMonth = getDayOfMonth(calendar);
        calendar.set(year, month-1, 1);//将日期设置为本月一号
        int daysOfMonth = getDaysOfMonth(year, month);
        int daysOfLastMonth = getDaysOfLastMonth(year, month);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);//获取本月一号是周几

        //添加上个月最后几天
        List<Integer> lastFewDaysOfLastMonth = getLastFewDaysOfLastMonth(daysOfLastMonth, dayOfWeek);
        for (Integer integer : lastFewDaysOfLastMonth){
            allCalendarDays.add(new CalendarDay(integer, false));
        }

        //添加本月
        for (int i = 1; i <= daysOfMonth; i++){
            CalendarDay calendarDay = new CalendarDay(i, true);
            calendarDay.setYear(year);
            calendarDay.setMonth(month);
            if (isCurrentMonth && i == dayOfMonth){
                calendarDay.setToday(true);
            }
            allCalendarDays.add(calendarDay);
        }

        int size = allCalendarDays.size();

        //用下个月开始几天补全42天
        for (int i = 1; i <= 42 - size; i++){
            allCalendarDays.add(new CalendarDay(i, false));
        }

        return allCalendarDays;

    }

    public static int getFirstDayOfMonth(Calendar calendar){
        return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
    }

    public static int getLastDayOfMonth(Calendar calendar){
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * /**
     * 将年月日转成YYYY-MM-DD格式的字符串
     * @param year
     * @param month
     * @param day
     * @return
     */

    public static String formatDate(int year, int month, int day){
        StringBuilder builder = new StringBuilder();
        builder.append(year).append("-");

        if (month < 10){
            builder.append(0);
        }
        builder.append(month).append("-");

        if (day < 10){
            builder.append(0);
        }
        builder.append(day);

        return builder.toString();
    }

    /**
     * /**
     * 将年月日转成YYYY-MM格式的字符串
     * @param year
     * @param month
     * @return
     */

    public static String formatDate(int year, int month){
        StringBuilder builder = new StringBuilder();
        builder.append(year).append("-");

        if (month < 10){
            builder.append(0);
        }
        builder.append(month);

        return builder.toString();
    }

    /**
     * 将年月日转成YYYYMMDD格式的整形
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static int getDateInt(int year, int month, int day){
        StringBuilder builder = new StringBuilder();
        builder.append(year);

        if (month < 10){
            builder.append(0);
        }
        builder.append(month);

        if (day < 10){
            builder.append(0);
        }
        builder.append(day);

        return Integer.parseInt(builder.toString());

    }

}
