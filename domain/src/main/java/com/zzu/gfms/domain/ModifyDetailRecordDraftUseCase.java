package com.zzu.gfms.domain;

import android.provider.ContactsContract;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.DetailRecordDraft;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/11/12.
 */

public class ModifyDetailRecordDraftUseCase extends BaseUseCase<Boolean> {

    private DetailRecordDraft detailRecordDraft;

    public ModifyDetailRecordDraftUseCase modify(DetailRecordDraft detailRecordDraft){
        this.detailRecordDraft = detailRecordDraft;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.updateDetailRecordDraft(detailRecordDraft);
    }
}
