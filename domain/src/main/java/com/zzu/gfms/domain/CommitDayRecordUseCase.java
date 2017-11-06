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

public class CommitDayRecordUseCase extends BaseUseCase<DayAndDetailRecords> {

    private String dayRecord;

    private String detaiRecords;

    private int type;

    public CommitDayRecordUseCase commit(String dayRecord, String detaiRecords, int type){
        this.dayRecord = dayRecord;
        this.detaiRecords = detaiRecords;
        this.type = type;
        return this;
    }

    @Override
    public Observable<DayAndDetailRecords> buildObservable() {
        return DataRepository.commitDayRecord(dayRecord, detaiRecords, type);
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
