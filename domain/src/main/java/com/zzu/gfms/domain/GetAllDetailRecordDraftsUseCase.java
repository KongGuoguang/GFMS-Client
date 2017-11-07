package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.DetailRecordDraft;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-11-06
 * Time:19:01
 * Summary:
 */

public class GetAllDetailRecordDraftsUseCase extends BaseUseCase<List<DetailRecordDraft>> {

    private long workerId;

    private String date;

    public GetAllDetailRecordDraftsUseCase get(long workerId, String date){
        this.workerId = workerId;
        this.date = date;
        return this;
    }

    @Override
    public Observable<List<DetailRecordDraft>> buildObservable() {
        return DataRepository.getDetailRecordDrafts(workerId, date);
    }
}
