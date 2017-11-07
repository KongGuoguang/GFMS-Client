package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.DetailRecord;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-11-07
 * Time:11:13
 * Summary:
 */

public class SaveDetailRecordsUseCase extends BaseUseCase<Boolean> {

    private List<DetailRecord> detailRecords;

    public SaveDetailRecordsUseCase save(List<DetailRecord> detailRecords){
        this.detailRecords = detailRecords;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.saveDetailRecords(detailRecords);
    }
}
