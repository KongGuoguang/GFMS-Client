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

public class GetDayRecordsUseCase extends BaseUseCase<List<DayRecord>> {

    private long workerId;

    private int year;

    private int month;

    private int startYear;

    private int startMonth;

    private int startDay;

    private int endYear;

    private int endMonth;

    private int endDay;

    private boolean isGetMonthDayRecords;

    public GetDayRecordsUseCase get(long workerId, int year, int month){
        isGetMonthDayRecords = true;
        this.workerId = workerId;
        this.year = year;
        this.month = month;
        return this;
    }

    public GetDayRecordsUseCase get(long workerId, int startYear, int startMonth, int startDay,
                                    int endYear, int endMonth, int endDay){
        isGetMonthDayRecords = false;
        this.workerId = workerId;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
        return this;
    }

    @Override
    public Observable<List<DayRecord>> buildObservable() {
        if (isGetMonthDayRecords)
            return DataRepository.getDayRecords(workerId, year, month);
        else
            return DataRepository.getDayRecords(workerId, startYear, startMonth, startDay,
        endYear, endMonth, endDay);
    }
}
