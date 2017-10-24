package com.zzu.gfms.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.http.GFMSException;
import com.zzu.gfms.data.http.HttpReply;
import com.zzu.gfms.data.http.Retrofit2Util;

import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Author:kongguoguang
 * Date:2017-10-24
 * Time:17:04
 * Summary:
 */

public class RemoteRepository {

    private static Gson gson = new Gson();

    public static Observable<List<DayRecord>> getDayRecordOfMonth(long workerId){

        Calendar calendar = Calendar.getInstance();
        long milSecond = calendar.getTimeInMillis();

        return Retrofit2Util.getServerInterface()
                .getDayRecordOfMonth(String.valueOf(workerId), String.valueOf(milSecond))
                .flatMap(new Function<HttpReply, Observable<List<DayRecord>>>() {
                    @Override
                    public Observable<List<DayRecord>> apply(@NonNull final HttpReply httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<List<DayRecord>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<DayRecord>> e) throws Exception {

                                if (status == 0){
                                    List<DayRecord> dayRecords = gson.fromJson(httpReply.getData(),
                                            new TypeToken<List<DayRecord>>(){}.getType());
                                    e.onNext(dayRecords);
                                }else {
                                    e.onError(new GFMSException(status));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }

    public static Observable<List<DetailRecord>> getDetailRecordOfDay(long dayRecordId){
        return Retrofit2Util.getServerInterface()
                .getDetailRecordOfDay(String.valueOf(dayRecordId))
                .flatMap(new Function<HttpReply, Observable<List<DetailRecord>>>() {
                    @Override
                    public Observable<List<DetailRecord>> apply(@NonNull final HttpReply httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<List<DetailRecord>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<DetailRecord>> e) throws Exception {
                                if (status == 0){
                                    List<DetailRecord> detailRecords = gson.fromJson(httpReply.getData(),
                                            new TypeToken<List<DetailRecord>>(){}.getType());
                                    e.onNext(detailRecords);
                                }else {
                                    e.onError(new GFMSException(status));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }

    public static Observable<List<WorkType>> getAllWorkType(long dayRecordId){
        return Retrofit2Util.getServerInterface()
                .getAllWorkType()
                .flatMap(new Function<HttpReply, Observable<List<WorkType>>>() {
                    @Override
                    public Observable<List<WorkType>> apply(@NonNull final HttpReply httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<List<WorkType>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<WorkType>> e) throws Exception {
                                if (status == 0){
                                    List<WorkType> workTypes = gson.fromJson(httpReply.getData(),
                                            new TypeToken<List<WorkType>>(){}.getType());
                                    e.onNext(workTypes);
                                }else {
                                    e.onError(new GFMSException(status));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }

    public static Observable<List<ClothesType>> getAllClothesType(){
        return Retrofit2Util.getServerInterface()
                .getAllClothesType()
                .flatMap(new Function<HttpReply, Observable<List<ClothesType>>>() {
                    @Override
                    public Observable<List<ClothesType>> apply(@NonNull final HttpReply httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<List<ClothesType>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<ClothesType>> e) throws Exception {
                                if (status == 0){
                                    List<ClothesType> clothesTypes = gson.fromJson(httpReply.getData(),
                                            new TypeToken<List<ClothesType>>(){}.getType());
                                    e.onNext(clothesTypes);
                                }else {
                                    e.onError(new GFMSException(status));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }
}
