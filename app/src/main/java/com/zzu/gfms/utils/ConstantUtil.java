package com.zzu.gfms.utils;

import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.Worker;

import java.util.ArrayList;
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

    public static List<ClothesType> allClothesTypes;

    public static String getWorkName(int workTypeID){
        String name = "";
        for (WorkType workType : workTypes){
            if (workType.getWorkTypeID() == workTypeID){
                name = workType.getName();
                break;
            }
        }
        return name;
    }

    public static List<ClothesType> getChildClothesType(int parentID){
        List<ClothesType> clothesTypes = new ArrayList<>();
        for (ClothesType clothesType : allClothesTypes){
            if (clothesType.getParentID() == parentID){
                clothesTypes.add(clothesType);
            }
        }
        return clothesTypes;
    }

    public static String getClothesName(int clothesID){
        String name = "";
        for (ClothesType clothesType : allClothesTypes){
            if (clothesType.getClothesID() == clothesID){
                name = clothesType.getName();
                break;
            }
        }
        return name;
    }
}
