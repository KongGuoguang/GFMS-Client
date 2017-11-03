package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.ClothesType;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-10-26
 * Time:15:37
 * Summary:
 */

public class GetClothesTypeUseCase extends BaseUseCase<List<ClothesType>> {

    private int enterpriseID;

    private long workerId;

    public GetClothesTypeUseCase get(long workerId, int enterpriseID){
        this.workerId = workerId;
        this.enterpriseID = enterpriseID;
        return this;
    }

    @Override
    public Observable<List<ClothesType>> buildObservable() {
        return DataRepository.getClothesType(workerId, enterpriseID);
    }
}
