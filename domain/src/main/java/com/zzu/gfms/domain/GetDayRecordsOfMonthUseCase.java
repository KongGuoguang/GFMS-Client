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

    private int year;

    private int month;

    public GetDayRecordsOfMonthUseCase get(long workerId, int year, int month){
        this.workerId = workerId;
        this.year = year;
        this.month = month;
        return this;
    }

    @Override
    public Observable<List<DayRecord>> buildObservable() {
        return DataRepository.getDayRecordsOfMonth(workerId, year, month);
    }
}
