package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.Worker;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

/**
 * Author:kongguoguang
 * Date:2017-10-25
 * Time:15:15
 * Summary:
 */

public class LoginUseCase extends BaseUseCase<Worker> {

    private String userName, password;

    public LoginUseCase login(String userName, String password){
        this.userName = userName;
        this.password = password;
        return this;
    }

    @Override
    public Observable<Worker> buildObservable() {
        return DataRepository.login(userName, password);
    }
}
