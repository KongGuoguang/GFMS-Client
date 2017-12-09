package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.OperationRecord;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-12-09
 * Time:11:17
 * Summary:
 */

public class SaveOperationRecordUseCase extends BaseUseCase<Boolean> {

    private List<OperationRecord> operationRecords;

    public SaveOperationRecordUseCase save(List<OperationRecord> operationRecords){
        this.operationRecords = operationRecords;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.saveOperationRecord(operationRecords);
    }
}
