package com.zzu.gfms.data;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.zzu.gfms.data.bean.DayAndDetailRecords;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.DetailRecordDraft;
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

    public static Observable<Worker> login(String userName, String password){
        return RemoteRepository.login(userName, password);
    }

    public static Observable<Worker> getWorker(String userName, String password){
        return LocalRepository.getWorker(userName, password);
    }

    public static Observable<List<WorkType>> getWorkType(long workerId, int enterpriseID){
        return Observable.concat(LocalRepository.getWorkType(enterpriseID), RemoteRepository.getWorkType(workerId));
    }

    public static Observable<Boolean> saveWorkType(List<WorkType> workTypes){
        return LocalRepository.saveWorkType(workTypes);
    }

    public static Observable<Boolean> saveDayRecord(DayRecord dayRecord){
        return LocalRepository.saveDayRecord(dayRecord);
    }

    public static Observable<DayAndDetailRecords> submitDayRecord(DayAndDetailRecords dayAndDetailRecords){
        return RemoteRepository.submitDayRecord(dayAndDetailRecords);
    }

    public static Observable<Boolean> saveDayRecordsOfMonth(List<DayRecord> dayRecords){
        return LocalRepository.saveDayRecordsOfMonth(dayRecords);
    }

    /**
     *
     * @param workerId
     * @return
     */
    public static Observable<List<DayRecord>> getDayRecordsOfMonth(long workerId, int year, int month){
        return Observable.concat(LocalRepository.getDayRecordsOfMonth(workerId, year, month),
                RemoteRepository.getDayRecordOfMonth(workerId, year, month));
    }

    public static Observable<Boolean> saveDetailRecords(List<DetailRecord> detailRecords){
        return LocalRepository.saveDetailRecords(detailRecords);
    }

    public static Observable<List<DetailRecord>> getDetailRecords(String dayRecordId){
        return Observable.concat(LocalRepository.getDetailRecords(dayRecordId), RemoteRepository.getDetailRecords(dayRecordId));
    }

    public static Observable<List<ClothesType>> getClothesType(long workerId, int enterpriseID){
        return Observable.concat(LocalRepository.getClothesType(enterpriseID), RemoteRepository.getClothesType(workerId));
    }

    public static Observable<Boolean> saveClothesType(List<ClothesType> clothesTypes){
        return LocalRepository.saveClothesType(clothesTypes);
    }

    public static Observable<Boolean> saveDetailRecordDraft(DetailRecordDraft detailRecordDraft){
        return LocalRepository.saveDetailRecordDraft(detailRecordDraft);
    }

    public static Observable<Boolean> deleteDetailRecordDraft(DetailRecordDraft detailRecordDraft){
        return LocalRepository.deleteDetailRecordDraft(detailRecordDraft);
    }

    public static Observable<Boolean> deleteDetailRecordDrafts(long workerId, String date){
        return LocalRepository.deleteDetailRecordDrafts(workerId, date);
    }

    public static Observable<List<DetailRecordDraft>> getDetailRecordDrafts(long workerId, String date){
        return LocalRepository.getDetailRecordDrafts(workerId, date);
    }
}
