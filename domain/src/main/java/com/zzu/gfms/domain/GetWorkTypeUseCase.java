package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.WorkType;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-10-25
 * Time:9:37
 * Summary:
 */

public class GetWorkTypeUseCase extends BaseUseCase<List<WorkType>>{

    private long workerId;

    @Override
    public Observable<List<WorkType>> buildObservable() {
        return DataRepository.getWorkType(workerId);
    }
}
