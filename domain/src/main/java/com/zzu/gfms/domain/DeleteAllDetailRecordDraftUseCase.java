package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-11-06
 * Time:18:58
 * Summary:
 */

public class DeleteAllDetailRecordDraftUseCase extends BaseUseCase<Boolean> {

    private long workerId;

    private String date;

    public DeleteAllDetailRecordDraftUseCase delete(long workerId, String date){
        this.workerId = workerId;
        this.date = date;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.deleteDetailRecordDrafts(workerId, date);
    }
}
