package com.zzu.gfms.domain;

import com.zzu.gfms.data.DataRepository;
import com.zzu.gfms.data.bean.DayAndDetailRecords;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:kongguoguang
 * Date:2017-11-06
 * Time:20:08
 * Summary:
 */

public class SubmitDayRecordUseCase extends BaseUseCase<DayAndDetailRecords> {

    private DayAndDetailRecords dayAndDetailRecords;

    public SubmitDayRecordUseCase commit(DayAndDetailRecords dayAndDetailRecords){
        this.dayAndDetailRecords = dayAndDetailRecords;
        return this;
    }

    @Override
    public Observable<DayAndDetailRecords> buildObservable() {
        return DataRepository.submitDayRecord(dayAndDetailRecords);
    }

    @Override
    public void execute(Observer<DayAndDetailRecords> observer) {
        buildObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<DayAndDetailRecords>() {
                    @Override
                    public void accept(DayAndDetailRecords dayAndDetailRecords) throws Exception {
                        DataRepository.saveDayRecord(dayAndDetailRecords.getDayRecord());
                        DataRepository.saveDetailRecords(dayAndDetailRecords.getDetailRecords());
                    }
                })
                .subscribe(observer);
    }
}
