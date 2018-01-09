package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2018-01-09
 * Time:14:54
 * Summary:
 */

public class ModifyPwdUseCase extends BaseUseCase<Boolean> {

    private long workerId;

    private String oldPwd;

    private String newPwd;

    public ModifyPwdUseCase modify(long workerId, String oldPwd, String newPwd){
        this.workerId = workerId;
        this.oldPwd = oldPwd;
        this.newPwd = newPwd;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.modifyPwd(workerId, oldPwd, newPwd);
    }
}
