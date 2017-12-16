package com.zzu.gfms.data.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-10-12
 * Time:11:30
 * Summary:
 */

public class DateUtil {

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

        try {
            return Integer.parseInt(builder.toString());
        }catch (Exception e){
            return 0;
        }

    }

}
