package com.zzu.gfms.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.zzu.gfms.data.bean.DayAndDetailRecords;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.DetailRecordDraft;
import com.zzu.gfms.data.dbflow.OperationRecord;
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

    private static SharedPreferences preferences;

    public static void init(Context context){
        Retrofit2Util.createServerInterface(context);
        FlowManager.init(context);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void saveUserNameWithPassword(String userName, String password){
        preferences.edit()
                .putString("user_name", userName)
                .putString("password", password)
                .apply();
    }

    public static void clearPassword(){
        preferences.edit()
                .remove("password")
                .apply();
    }

    public static String getUserName(){
        return preferences.getString("user_name", "");
    }

    public static String getPassword(){
        return preferences.getString("password", "");
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

    public static Observable<Boolean> saveDayRecord(List<DayRecord> dayRecords){
        return LocalRepository.saveDayRecord(dayRecords);
    }

    /**
     *
     * @param workerId
     * @param yearMonth
     * @return
     */
    public static Observable<List<DayRecord>> getDayRecords(long workerId, String yearMonth){
        return Observable.concat(LocalRepository.getDayRecords(workerId, yearMonth),
                RemoteRepository.getDayRecordOfMonth(workerId, yearMonth));
    }

    public static Observable<List<DayRecord>> getDayRecords(long workerId, String startDate,
                                                            String endDate){
        return LocalRepository.getDayRecords(workerId, startDate,
                endDate);
    }


    public static Observable<Boolean> saveDetailRecords(List<DetailRecord> detailRecords){
        return LocalRepository.saveDetailRecords(detailRecords);
    }

    public static Observable<List<DetailRecord>> getDetailRecords(String dayRecordId){
        return Observable.concat(LocalRepository.getDetailRecords(dayRecordId), RemoteRepository.getDetailRecords(dayRecordId));
    }

    public static Observable<List<DetailRecord>> getDetailRecords(long workerId, String startDate,
                                                                  String endDate, int clothesTypeId,
                                                                  int workTypeId){
        return LocalRepository.getDetailRecords(workerId, startDate, endDate, clothesTypeId, workTypeId);
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

    public static Observable<Boolean> updateDetailRecordDraft(DetailRecordDraft detailRecordDraft){
        return LocalRepository.updateDetailRecordDraft(detailRecordDraft);
    }

    public static Observable<Boolean> deleteDetailRecordDrafts(long workerId, String date){
        return LocalRepository.deleteDetailRecordDrafts(workerId, date);
    }

    public static Observable<List<DetailRecordDraft>> getDetailRecordDrafts(long workerId, String date){
        return LocalRepository.getDetailRecordDrafts(workerId, date);
    }

    public static Observable<Boolean> submitModifyApplication(String dayRecordID, String modifyReason){
        return RemoteRepository.submitModifyApplication(dayRecordID, modifyReason);
    }

    public static Observable<Boolean> convertDayRecordState(String dayRecordId, String state){
        return LocalRepository.convertDayRecordState(dayRecordId, state);
    }

    public static Observable<Boolean> saveOperationRecord(List<OperationRecord> operationRecords){
        return LocalRepository.saveOperationRecord(operationRecords);
    }

    public static Observable<List<OperationRecord>> getOperationRecords(long workerId, String startDate, String endDate, String convertState){
        return Observable.concat(LocalRepository.getOperationRecords(workerId, startDate, endDate, convertState),
                RemoteRepository.getOperationRecords(workerId));
    }


}
