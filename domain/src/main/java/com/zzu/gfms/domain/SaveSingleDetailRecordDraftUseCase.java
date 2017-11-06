package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.DetailRecordDraft;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-11-06
 * Time:18:44
 * Summary:
 */

public class SaveSingleDetailRecordDraftUseCase extends BaseUseCase<Boolean> {

    private DetailRecordDraft detailRecordDraft;

    public SaveSingleDetailRecordDraftUseCase save(DetailRecordDraft detailRecordDraft){
        this.detailRecordDraft = detailRecordDraft;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.saveDetailRecordDraft(detailRecordDraft);
    }
}
