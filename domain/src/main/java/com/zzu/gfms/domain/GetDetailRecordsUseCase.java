package com.zzu.gfms.domain;

import android.text.TextUtils;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.DetailRecord;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-11-07
 * Time:10:23
 * Summary:
 */

public class GetDetailRecordsUseCase extends BaseUseCase<List<DetailRecord>> {

    private String dayRecordId;

    private long workerId;

    private String startDate;

    private String endDate;

    private int clothesTypeId;

    private int workTypeId;

    public  GetDetailRecordsUseCase get(String dayRecordId){
        this.dayRecordId = dayRecordId;
        return this;
    }

    public GetDetailRecordsUseCase get(long workerId, String startDate,
                                       String endDate, int clothesTypeId,
                                       int workTypeId){
        this.workerId = workerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.clothesTypeId = clothesTypeId;
        this.workTypeId = workTypeId;
        return this;
    }

    @Override
    public Observable<List<DetailRecord>> buildObservable() {

        if (!TextUtils.isEmpty(dayRecordId))
            return DataRepository.getDetailRecords(dayRecordId);

        if (workerId > 0){
            return DataRepository.getDetailRecords(workerId, startDate,endDate, clothesTypeId, workTypeId);
        }

        return null;
    }
}
