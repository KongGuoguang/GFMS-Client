package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.DayRecord;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * Author:kongguoguang
 * Date:2017-11-06
 * Time:19:52
 * Summary:
 */

public class SaveSingleDayRecordsUseCase extends BaseUseCase<Boolean> {

    private DayRecord dayRecord;

    public SaveSingleDayRecordsUseCase save(DayRecord dayRecord){
        this.dayRecord = dayRecord;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.saveDayRecord(dayRecord);
    }

    @Override
    public void execute(Observer<Boolean> observer) {
        super.execute(observer);
    }
}
