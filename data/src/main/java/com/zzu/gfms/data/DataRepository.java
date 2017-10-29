package com.zzu.gfms.data;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.data.http.Retrofit2Util;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-10-25
 * Time:10:07
 * Summary:
 */

public class DataRepository {

    public static void init(Context context){
        Retrofit2Util.createServerInterface(context);
        FlowManager.init(context);
    }

    public static Observable<Worker> getWorker(String userName, String password){
        return LocalRepository.getWorker(userName, password);
    }

    public static Observable<List<WorkType>> getWorkType(int enterpriseID){
        return Observable.concat(LocalRepository.getWorkType(enterpriseID), RemoteRepository.getWorkType(enterpriseID));
    }

    public static Observable<Boolean> saveWorkType(List<WorkType> workTypes){
        return LocalRepository.saveWorkType(workTypes);
    }

    public static Observable<Boolean> saveDayRecords(List<DayRecord> dayRecords){
        return LocalRepository.saveDayRecords(dayRecords);
    }

    public static Observable<List<DayRecord>> getDayRecordOfMonth(long workerId){
        return LocalRepository.getDayRecordOfMonth(workerId);
    }

    public static Observable<List<ClothesType>> getClothesType(int enterpriseID){
        return Observable.concat(LocalRepository.getClothesType(enterpriseID), RemoteRepository.getClothesType(enterpriseID));
    }

    public static Observable<Boolean> saveClothesType(List<ClothesType> clothesTypes){
        return LocalRepository.saveClothesType(clothesTypes);
    }

}
