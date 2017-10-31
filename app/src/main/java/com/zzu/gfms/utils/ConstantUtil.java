package com.zzu.gfms.utils;

import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.Worker;

import java.util.List;

/**
 * Author:kongguoguang
 * Date:2017-10-25
 * Time:10:28
 * Summary:
 */

public class ConstantUtil {

    public static Worker worker;

    public static List<WorkType> workTypes;

    public static List<ClothesType> clothesTypes;

    public static String getWorkTypeName(int workTypeID){
        String name = "";
        for (WorkType workType : workTypes){
            if (workType.getWorkTypeID() == workTypeID){
                name = workType.getName();
                break;
            }
        }
        return name;
    }

    public static String getClothesTypeName(int clothesTypeID){
        String name = "";
        for (ClothesType clothesType : clothesTypes){
            if (clothesType.getClothesID() == clothesTypeID){
                name = clothesType.getName();
                break;
            }
        }
        return name;
    }
}
