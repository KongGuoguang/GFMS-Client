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

    private String yearMonth;

    private String startDate;

    private String endDate;


    private boolean isGetMonthDayRecords;

    public GetDayRecordsUseCase get(long workerId, String yearMonth){
        isGetMonthDayRecords = true;
        this.workerId = workerId;
        this.yearMonth = yearMonth;
        return this;
    }

    public GetDayRecordsUseCase get(long workerId, String startDate, String endDate){
        isGetMonthDayRecords = false;
        this.workerId = workerId;
        this.startDate = startDate;
        this.endDate = endDate;
        return this;
    }

    @Override
    public Observable<List<DayRecord>> buildObservable() {
        if (isGetMonthDayRecords)
            return DataRepository.getDayRecords(workerId, yearMonth);
        else
            return DataRepository.getDayRecords(workerId, startDate, endDate);
    }
}
