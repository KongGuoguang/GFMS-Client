package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.OperationRecord;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-11-16
 * Time:9:41
 * Summary:
 */

public class GetOperationRecordUseCase extends BaseUseCase<List<OperationRecord>> {

    private long workerId;

    public GetOperationRecordUseCase get(long workerId){
        this.workerId = workerId;
        return this;
    }

    @Override
    public Observable<List<OperationRecord>> buildObservable() {
        return DataRepository.getOperationRecords(workerId);
    }
}
