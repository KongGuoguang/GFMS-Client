package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.dbflow.ClothesType;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author:kongguoguang
 * Date:2017-10-26
 * Time:15:50
 * Summary:
 */

public class SaveClothesTypeUseCase extends BaseUseCase<Boolean> {
    private List<ClothesType> clothesTypes;

    public SaveClothesTypeUseCase(List<ClothesType> clothesTypes){
        this.clothesTypes = clothesTypes;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.saveClothesType(clothesTypes);
    }
}
