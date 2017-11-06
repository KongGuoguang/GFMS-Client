package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.DetailRecordDraft;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-11-06
 * Time:18:50
 * Summary:
 */

public class DeleteSingleDetailRecordDraftUseCase extends BaseUseCase<Boolean> {

    private DetailRecordDraft detailRecordDraft;

    public DeleteSingleDetailRecordDraftUseCase delete(DetailRecordDraft detailRecordDraft){
        this.detailRecordDraft = detailRecordDraft;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.deleteDetailRecordDraft(detailRecordDraft);
    }
}
