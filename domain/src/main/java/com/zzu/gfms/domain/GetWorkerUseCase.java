package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.Worker;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-10-25
 * Time:10:59
 * Summary:
 */

public class GetWorkerUseCase extends BaseUseCase<Worker> {

    private String userName;

    private String password;

    public GetWorkerUseCase(String user, String password){
        this.userName = user;
        this.password = password;
    }

    @Override
    public Observable<Worker> buildObservable() {
        return DataRepository.getWorker(userName, password);
    }
}
