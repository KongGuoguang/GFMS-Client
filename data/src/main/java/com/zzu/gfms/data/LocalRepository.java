package com.zzu.gfms.data;

import com.raizlabs.android.dbflow.sql.language.Select;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DayRecord_Table;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.DetailRecord_Table;
import com.zzu.gfms.data.dbflow.WorkType;
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

    public static Observable<List<WorkType>> getAllWorkType(){
        return Observable.create(new ObservableOnSubscribe<List<WorkType>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<WorkType>> e) throws Exception {
                List<WorkType> workTypes = new Select().from(WorkType.class).queryList();
                e.onNext(workTypes);
                e.onComplete();
            }
        });
    }

    public static Observable<List<ClothesType>> getAllClothesType(){
        return Observable.create(new ObservableOnSubscribe<List<ClothesType>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<ClothesType>> e) throws Exception {
                List<ClothesType> clothesTypes = new Select().from(ClothesType.class).queryList();
                e.onNext(clothesTypes);
                e.onComplete();
            }
        });
    }

}
