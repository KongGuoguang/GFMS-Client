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
     * 将YYYY-MM-DD的字符串转成YYYYMMDD格式的整形
     */
    public static int getDateInt(String date){
        String[] array = date.split("-");
        StringBuilder builder = new StringBuilder();
        for (String str : array) {
            builder.append(str);
        }

        int result = 0;
        try {
            result = Integer.parseInt(builder.toString());
        }catch (Exception e){
            return result;
        }

        return result;
    }

}
