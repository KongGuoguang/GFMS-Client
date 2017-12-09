package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.DayRecord;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-10-26
 * Time:11:08
 * Summary:
 */

public class SaveDayRecordUseCase extends BaseUseCase<Boolean> {

    private List<DayRecord> dayRecords;

    public SaveDayRecordUseCase save(List<DayRecord> dayRecords){
        this.dayRecords = dayRecords;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.saveDayRecord(dayRecords);
    }
}
