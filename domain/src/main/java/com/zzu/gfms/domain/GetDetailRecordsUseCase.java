package com.zzu.gfms.domain;

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

    public  GetDetailRecordsUseCase get(String dayRecordId){
        this.dayRecordId = dayRecordId;
        return this;
    }

    @Override
    public Observable<List<DetailRecord>> buildObservable() {
        return DataRepository.getDetailRecords(dayRecordId);
    }
}
