package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:kongguoguang
 * Date:2017-11-09
 * Time:15:33
 * Summary:
 */

public class SubmitModifyApplicationUseCase extends BaseUseCase<Boolean> {

    private String dayRecordID, modifyReason;

    public SubmitModifyApplicationUseCase submit(String dayRecordID, String modifyReason){
        this.dayRecordID = dayRecordID;
        this.modifyReason = modifyReason;
        return this;
    }

    @Override
    public Observable<Boolean> buildObservable() {
        return DataRepository.submitModifyApplication(dayRecordID, modifyReason);
    }

    @Override
    public void execute(Observer<Boolean> observer) {
        buildObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean b) throws Exception {

                    }
                })
                .subscribe(observer);
    }
}
