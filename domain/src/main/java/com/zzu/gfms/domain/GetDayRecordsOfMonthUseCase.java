package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.DayRecord;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-10-26
 * Time:11:23
 * Summary:
 */

public class GetDayRecordsOfMonthUseCase extends BaseUseCase<List<DayRecord>> {

    private long workerId;

    public GetDayRecordsOfMonthUseCase get(long workerId){
        this.workerId = workerId;
        return this;
    }

    @Override
    public Observable<List<DayRecord>> buildObservable() {
        return DataRepository.getDayRecordOfMonth(workerId);
    }
}
