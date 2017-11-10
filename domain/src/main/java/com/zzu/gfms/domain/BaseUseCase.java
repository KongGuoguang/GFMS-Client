package com.zzu.gfms.domain;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Author:kongguoguang
 * Date:2017-10-25
 * Time:9:27
 * Summary:
 */

public abstract class BaseUseCase<T> {

    public abstract Observable<T> buildObservable();

    public void execute(Observer<T> observer){
        buildObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Disposable execute(Consumer<T> consumer){
        return buildObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    public void execute(){
        buildObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
