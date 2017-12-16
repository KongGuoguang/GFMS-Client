package com.zzu.gfms.utils;

import android.util.SparseArray;

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

    public static SparseArray<String> workNames = new SparseArray<>();

    public static void setWorkTypes(List<WorkType> workTypes) {
        ConstantUtil.workTypes = workTypes;

        if (workTypes != null && workTypes.size() > 0){
            for (WorkType workType : workTypes){
                workNames.put(workType.getWorkTypeID(), workType.getName());
            }
        }

    }

    public static String getWorkName(int workTypeID){
        return workNames.get(workTypeID);
    }


    public static List<ClothesType> clothesTypes;

    public static SparseArray<String> clothesNames = new SparseArray<>();

    public static void setClothesTypes(List<ClothesType> clothesTypes) {
        ConstantUtil.clothesTypes = clothesTypes;

        if (clothesTypes != null && clothesTypes.size() > 0){
            for (ClothesType clothesType : clothesTypes){
                clothesNames.put(clothesType.getClothesID(), clothesType.getName());
            }
        }
    }

    public static String getClothesName(int clothesID){
        return clothesNames.get(clothesID);
    }

    public static List<ClothesType> getChildClothesType(int parentID){
        List<ClothesType> clothesTypes = new ArrayList<>();
        for (ClothesType clothesType : ConstantUtil.clothesTypes){
            if (clothesType.getParentID() == parentID){
                clothesTypes.add(clothesType);
            }
        }
        return clothesTypes;
    }


}
