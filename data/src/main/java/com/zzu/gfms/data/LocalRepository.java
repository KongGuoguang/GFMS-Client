package com.zzu.gfms.data;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.Select;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.zzu.gfms.data.dbflow.AppDatabase;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.ClothesType_Table;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DayRecord_Table;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.DetailRecord_Table;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.WorkType_Table;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.data.dbflow.Worker_Table;
import com.zzu.gfms.data.utils.CalendarUtil;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:14:49
 * Summary:
 */

public class LocalRepository {

    public static Observable<Worker> getWorker(final String userName, final String password){
        return Observable.create(new ObservableOnSubscribe<Worker>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Worker> e) throws Exception {
                Worker worker = new Select().from(Worker.class)
                        .where(Worker_Table.userName.eq(userName))
                        .and(Worker_Table.passWord.eq(password))
                        .querySingle();
                e.onNext(worker);
                e.onComplete();
            }
        });
    }


    public static Observable<List<DayRecord>> getDayRecordOfMonth(final long workerId){

        return Observable.create(new ObservableOnSubscribe<List<DayRecord>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DayRecord>> e) throws Exception {
                Calendar calendar = Calendar.getInstance();
                long start = CalendarUtil.getStartTimeInMillisOfMonth(calendar);
                long end = CalendarUtil.getEndTimeInMillisOfMonth(calendar);
                List<DayRecord> dayRecords = new Select().from(DayRecord.class)
                        .where(DayRecord_Table.workerID.eq(workerId))
                        .and(DayRecord_Table.day.between(start).and(end))
                        .queryList();
                e.onNext(dayRecords);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> saveDayRecords(final List<DayRecord> dayRecords){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                FlowManager.getDatabase(AppDatabase.class)
                        .executeTransaction(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<DayRecord>() {
                                    @Override
                                    public void processModel(DayRecord dayRecord, DatabaseWrapper wrapper) {
                                        dayRecord.save();
                                    }
                        }).addAll(dayRecords).build());
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    public static Observable<List<DetailRecord>> getDetailRecordOfDay(final long dayRecordId){
        return Observable.create(new ObservableOnSubscribe<List<DetailRecord>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DetailRecord>> e) throws Exception {
                List<DetailRecord> detailRecords = new Select().from(DetailRecord.class)
                        .where(DetailRecord_Table.dayRecordID.eq(dayRecordId))
                        .queryList();
                e.onNext(detailRecords);
                e.onComplete();
            }
        });
    }

    public static Observable<List<WorkType>> getWorkType(final int enterpriseID){
        return Observable.create(new ObservableOnSubscribe<List<WorkType>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<WorkType>> e) throws Exception {
                List<WorkType> workTypes = new Select()
                        .from(WorkType.class)
                        .where(WorkType_Table.enterpriseID.eq(enterpriseID))
                        .queryList();
                e.onNext(workTypes);
                e.onComplete();
            }
        });
    }

    public static Observable<List<ClothesType>> getClothesType(final int enterpriseID){
        return Observable.create(new ObservableOnSubscribe<List<ClothesType>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<ClothesType>> e) throws Exception {
                List<ClothesType> clothesTypes = new Select()
                        .from(ClothesType.class)
                        .where(ClothesType_Table.enterpriseID.eq(enterpriseID))
                        .queryList();
                e.onNext(clothesTypes);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> saveWorkType(final List<WorkType> workTypes){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                FlowManager.getDatabase(AppDatabase.class)
                        .executeTransaction(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<WorkType>() {
                                    @Override
                                    public void processModel(WorkType workType, DatabaseWrapper wrapper) {
                                        workType.save();
                                    }
                                }).addAll(workTypes).build());
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> saveClothesType(final List<ClothesType> clothesTypes){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                FlowManager.getDatabase(AppDatabase.class)
                        .executeTransaction(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<ClothesType>() {
                                    @Override
                                    public void processModel(ClothesType clothesType, DatabaseWrapper wrapper) {
                                        clothesType.save();
                                    }
                                }).addAll(clothesTypes).build());
                e.onNext(true);
                e.onComplete();
            }
        });
    }
}
