package com.zzu.gfms.data;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.FlowCursor;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.zzu.gfms.data.dbflow.AppDatabase;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.ClothesType_Table;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DayRecord_Table;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.DetailRecordDraft;
import com.zzu.gfms.data.dbflow.DetailRecordDraft_Table;
import com.zzu.gfms.data.dbflow.DetailRecord_Table;
import com.zzu.gfms.data.dbflow.OperationRecord;
import com.zzu.gfms.data.dbflow.OperationRecord_Table;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.WorkType_Table;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.data.dbflow.Worker_Table;
import com.zzu.gfms.data.utils.ConvertState;

import java.util.ArrayList;
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
                Worker worker = SQLite.select()
                        .from(Worker.class)
                        .where(Worker_Table.userName.eq(userName))
                        .and(Worker_Table.passWord.eq(password))
                        .querySingle();
                e.onNext(worker);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> saveDayRecord(final DayRecord dayRecord){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                dayRecord.save();
                e.onNext(true);
                e.onComplete();
            }
        });
    }


    /**
     * 获取某个月份的工作日报记录
     * @param workerId 员工id
     * @param yearMonth 年份
     * @return 日报记录列表
     */
    public static Observable<List<DayRecord>> getDayRecords(final long workerId, final String yearMonth){

        return Observable.create(new ObservableOnSubscribe<List<DayRecord>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DayRecord>> e) throws Exception {

                List<DayRecord> dayRecords = SQLite.select()
                        .from(DayRecord.class)
                        .where(DayRecord_Table.workerID.eq(workerId))
                        .and(DayRecord_Table.day.like(yearMonth + "-%"))
                        .queryList();
                e.onNext(dayRecords);
                e.onComplete();
            }
        });
    }

    /**
     * 获取起止日期内的工作日报记录
     * @param workerId 员工id
     * @param startDate 起始年份
     * @return 日报记录列表
     */
    public static Observable<List<DayRecord>> getDayRecords(final long workerId, final String startDate,
                                                            final String endDate){
        return Observable.create(new ObservableOnSubscribe<List<DayRecord>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DayRecord>> e) throws Exception {

                List<DayRecord> dayRecords = SQLite.select()
                        .from(DayRecord.class)
                        .where(DayRecord_Table.workerID.eq(workerId))
                        .and(DayRecord_Table.day.between(startDate).and(endDate))
                        .orderBy(DayRecord_Table.day, false)
                        .queryList();
                e.onNext(dayRecords);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> saveDayRecord(final List<DayRecord> dayRecords){
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
                        })
                                .addAll(dayRecords)
                                .build());
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    public static Observable<List<DetailRecord>> getDetailRecords(final String dayRecordId){
        return Observable.create(new ObservableOnSubscribe<List<DetailRecord>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<DetailRecord>> e) throws Exception {
                List<DetailRecord> detailRecords = SQLite.select()
                        .from(DetailRecord.class)
                        .where(DetailRecord_Table.dayRecordID.eq(dayRecordId))
                        .queryList();
                e.onNext(detailRecords);
                e.onComplete();
            }
        });
    }

    /**
     * 获取起止日期内的工作详细记录
     * @param workerId 员工id
     * @param startDate 起始日期
     * @param endDate 结束日期
     * @param clothesTypeId 衣服类型
     * @param workTypeId 工作类型
     * @return 详细记录列表
     */
    public static Observable<List<DetailRecord>> getDetailRecords(final long workerId, final String startDate,
                                                                  final String endDate,
                                                                  final int clothesTypeId,
                                                                  final int workTypeId){

        return Observable.create(new ObservableOnSubscribe<List<DetailRecord>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DetailRecord>> e) throws Exception {

                List<DetailRecord> detailRecordList = new ArrayList<>();

                FlowCursor cursor = SQLite.select(DayRecord_Table.dayRecordID, DayRecord_Table.day)
                        .from(DayRecord.class)
                        .where(DayRecord_Table.workerID.eq(workerId))
                        .and(DayRecord_Table.day.between(startDate).and(endDate))
                        .and(DayRecord_Table.convertState.notEq(ConvertState.DAY_RECORD_MODIFY_HISTORY))
                        .query();

                if (cursor != null){
                    while (cursor.moveToNext()){
                        String dayRecordId = cursor.getStringOrDefault(0);

                        if (TextUtils.isEmpty(dayRecordId)) continue;

                        String day = cursor.getStringOrDefault(1);

                        List<DetailRecord> detailRecords;

                        if (clothesTypeId == 0 && workTypeId == 0){
                            detailRecords = SQLite.select()
                                    .from(DetailRecord.class)
                                    .where(DetailRecord_Table.dayRecordID.eq(dayRecordId))
                                    .queryList();
                        }else if (clothesTypeId != 0 && workTypeId != 0){
                            detailRecords = SQLite.select()
                                    .from(DetailRecord.class)
                                    .where(DetailRecord_Table.dayRecordID.eq(dayRecordId))
                                    .and(DetailRecord_Table.clothesID.eq(clothesTypeId))
                                    .and(DetailRecord_Table.workTypeID.eq(workTypeId))
                                    .queryList();
                        }else if (clothesTypeId != 0){
                            detailRecords = SQLite.select()
                                    .from(DetailRecord.class)
                                    .where(DetailRecord_Table.dayRecordID.eq(dayRecordId))
                                    .and(DetailRecord_Table.clothesID.eq(clothesTypeId))
                                    .queryList();
                        }else {
                            detailRecords = SQLite.select()
                                    .from(DetailRecord.class)
                                    .where(DetailRecord_Table.dayRecordID.eq(dayRecordId))
                                    .and(DetailRecord_Table.workTypeID.eq(workTypeId))
                                    .queryList();
                        }

                        if (detailRecords.size() > 0){
                            for (DetailRecord detailRecord : detailRecords){
                                detailRecord.setDay(day);
                            }

                            detailRecordList.addAll(detailRecords);
                        }
                    }
                }

                e.onNext(detailRecordList);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> saveDetailRecords(final List<DetailRecord> detailRecords){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                FlowManager.getDatabase(AppDatabase.class)
                        .executeTransaction(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<DetailRecord>() {
                                    @Override
                                    public void processModel(DetailRecord detailRecord, DatabaseWrapper wrapper) {
                                        detailRecord.save();
                                    }
                                }).addAll(detailRecords).build());
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    public static Observable<List<WorkType>> getWorkType(final int enterpriseID){
        return Observable.create(new ObservableOnSubscribe<List<WorkType>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<WorkType>> e) throws Exception {
                List<WorkType> workTypes = SQLite.select()
                        .from(WorkType.class)
                        .where(WorkType_Table.enterpriseID.eq(enterpriseID))
                        .queryList();
                e.onNext(workTypes);
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

    public static Observable<List<ClothesType>> getClothesType(final int enterpriseID){
        return Observable.create(new ObservableOnSubscribe<List<ClothesType>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<ClothesType>> e) throws Exception {
                List<ClothesType> clothesTypes = SQLite.select()
                        .from(ClothesType.class)
                        .where(ClothesType_Table.enterpriseID.eq(enterpriseID))
                        .queryList();
                e.onNext(clothesTypes);
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

    /**
     * 获取某天未提交的详细记录草稿
     * @param date
     * @return
     */
    public static Observable<List<DetailRecordDraft>> getDetailRecordDrafts(final long workerId, final String date){
        return Observable.create(new ObservableOnSubscribe<List<DetailRecordDraft>>() {
            @Override
            public void subscribe(ObservableEmitter<List<DetailRecordDraft>> e) throws Exception {
                List<DetailRecordDraft> detailRecordDrafts = SQLite.select()
                        .from(DetailRecordDraft.class)
                        .where(DetailRecordDraft_Table.workerId.eq(workerId))
                        .and(DetailRecordDraft_Table.date.eq(date))
                        .queryList();
                e.onNext(detailRecordDrafts);
                e.onComplete();
            }
        });
    }

    /**
     * 删除某天未提交的详细记录草稿
     * @param workerId
     * @param date
     * @return
     */
    public static Observable<Boolean> deleteDetailRecordDrafts(final long workerId, final String date){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                SQLite.delete()
                        .from(DetailRecordDraft.class)
                        .where(DetailRecordDraft_Table.workerId.eq(workerId))
                        .and(DetailRecordDraft_Table.date.eq(date))
                        .execute();
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> saveDetailRecordDraft(final DetailRecordDraft detailRecordDraft){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                long id = detailRecordDraft.insert();
                detailRecordDraft.setId(id);
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> updateDetailRecordDraft(final DetailRecordDraft detailRecordDraft){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                detailRecordDraft.update();
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> deleteDetailRecordDraft(final DetailRecordDraft detailRecordDraft){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                detailRecordDraft.delete();
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    public static Observable<Boolean> convertDayRecordState(final String dayRecordId, final String state){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                SQLite.update(DayRecord.class)
                        .set(DayRecord_Table.convertState.eq(state))
                        .where(DayRecord_Table.dayRecordID.eq(dayRecordId))
                        .execute();
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    /**
     * 保存修改申请操作记录到本地数据库
     * @param operationRecords
     * @return
     */
    public static Observable<Boolean> saveOperationRecord(final List<OperationRecord> operationRecords){
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                FlowManager.getDatabase(AppDatabase.class)
                        .executeTransaction(new ProcessModelTransaction.Builder<>(
                                new ProcessModelTransaction.ProcessModel<OperationRecord>() {
                                    @Override
                                    public void processModel(OperationRecord operationRecord, DatabaseWrapper wrapper) {
                                        operationRecord.save();
                                    }
                                })
                                .addAll(operationRecords)
                                .build());

                e.onNext(true);
                e.onComplete();
            }
        });
    }

    public static Observable<List<OperationRecord>> getOperationRecords(final long workerId, final String startDate, final String endDate, final String convertState){
        return Observable.create(new ObservableOnSubscribe<List<OperationRecord>>() {
            @Override
            public void subscribe(ObservableEmitter<List<OperationRecord>> e) throws Exception {

                List<OperationRecord> operationRecords;

                if (ConvertState.OPERATION_RECORD_ALL.equals(convertState)){
                    operationRecords = SQLite.select()
                            .from(OperationRecord.class)
                            .where(OperationRecord_Table.workerID.eq(workerId))
                            .and(OperationRecord_Table.day.between(startDate).and(endDate))
                            .queryList();
                }else {
                    operationRecords = SQLite.select()
                            .from(OperationRecord.class)
                            .where(OperationRecord_Table.workerID.eq(workerId))
                            .and(OperationRecord_Table.day.between(startDate).and(endDate))
                            .and(OperationRecord_Table.convertState.eq(convertState))
                            .queryList();
                }

                e.onNext(operationRecords);
                e.onComplete();
            }
        });
    }
}
