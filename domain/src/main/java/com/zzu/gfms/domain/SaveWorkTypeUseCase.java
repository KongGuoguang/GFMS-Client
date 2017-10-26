package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.WorkType;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-10-26
 * Time:15:25
 * Summary:
 */

public class SaveWorkTypeUseCase extends BaseUseCase<Boolean> {

    private List<WorkType> workTypes;

    public SaveWorkTypeUseCase(List<WorkType> workTypes){
        this.workTypes = workTypes;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.saveWorkType(workTypes);
    }
}
