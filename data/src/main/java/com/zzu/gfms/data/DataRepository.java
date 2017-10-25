package com.zzu.gfms.data;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.data.http.Retrofit2Util;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-10-25
 * Time:10:07
 * Summary:
 */

public class DataRepository {

    public static void init(Context context){
        Retrofit2Util.createServerInterface(context);
        FlowManager.init(context);
    }

    public static Observable<Worker> getWorker(final String userName, final String password){
        return LocalRepository.getWorker(userName, password);
    }

    public static Observable<List<WorkType>> getWorkType(long workerId){
        return Observable.concat(LocalRepository.getWorkType(), RemoteRepository.getWorkType(workerId));
    }


}
