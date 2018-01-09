package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2018-01-09
 * Time:14:50
 * Summary:注销用例
 */

public class LogoutUseCase extends BaseUseCase<Boolean> {

    private long workerId;

    public LogoutUseCase logout(long workerId){
        this.workerId = workerId;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.logout(workerId);
    }
}
