package com.zzu.gfms.domain;

import com.blankj.utilcode.util.LogUtils;

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

    private Consumer<Throwable> throwableConsumer;

    private Consumer<T> defaultConsumer;

    public void execute(Observer<T> observer){
        buildObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public Disposable execute(Consumer<T> consumer){

        return buildObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, getThrowableConsumer());
    }

    public void execute(){
        buildObservable().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getDefaultConsumer(), getThrowableConsumer());
    }

    private Consumer<Throwable> getThrowableConsumer(){
        if (throwableConsumer == null){
            throwableConsumer = new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    LogUtils.file(throwable.toString());
                }
            };
        }

        return throwableConsumer;
    }

    private Consumer<T> getDefaultConsumer(){
        if (defaultConsumer == null){
            defaultConsumer = new Consumer<T>() {
                @Override
                public void accept(T t) throws Exception {

                }
            };
        }

        return defaultConsumer;
    }
}
