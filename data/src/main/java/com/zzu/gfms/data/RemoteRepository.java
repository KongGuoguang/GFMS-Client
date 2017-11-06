package com.zzu.gfms.data;

import com.google.gson.Gson;
import com.zzu.gfms.data.bean.DayAndDetailRecords;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.Worker;
import com.zzu.gfms.data.http.GFMSException;
import com.zzu.gfms.data.http.HttpReply;
import com.zzu.gfms.data.http.Retrofit2Util;

import java.text.DecimalFormat;
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

    public static Observable<Worker> login(String userName, String password){
        return Retrofit2Util.getServerInterface()
                .login(userName, password)
                .flatMap(new Function<HttpReply<Worker>, Observable<Worker>>() {
                    @Override
                    public Observable<Worker> apply(@NonNull final HttpReply<Worker> httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<Worker>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<Worker> e) throws Exception {
                                if (status == 0){
                                    Worker worker = httpReply.getData();
                                    e.onNext(worker);
                                }else {
                                    e.onError(new GFMSException(status, httpReply.getMessage()));
                                }

                                e.onComplete();
                            }
                        });
                    }
                });
    }

    public static Observable<List<DayRecord>> getDayRecordOfMonth(long workerId, int year, int month){

        return Retrofit2Util.getServerInterface()
                .getDayRecordOfMonth(String.valueOf(workerId), String.valueOf(year),
                        new DecimalFormat("00").format(month))
                .flatMap(new Function<HttpReply<List<DayRecord>>, Observable<List<DayRecord>>>() {
                    @Override
                    public Observable<List<DayRecord>> apply(@NonNull final HttpReply<List<DayRecord>> httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<List<DayRecord>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<DayRecord>> e) throws Exception {

                                if (status == 0){
                                    List<DayRecord> dayRecords = httpReply.getData();
                                    e.onNext(dayRecords);
                                }else {
                                    e.onError(new GFMSException(status, httpReply.getMessage()));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }

    public static Observable<List<DetailRecord>> getDetailRecords(String dayRecordId){
        return Retrofit2Util.getServerInterface()
                .getDetailRecordOfDay(dayRecordId)
                .flatMap(new Function<HttpReply<List<DetailRecord>>, Observable<List<DetailRecord>>>() {
                    @Override
                    public Observable<List<DetailRecord>> apply(@NonNull final HttpReply<List<DetailRecord>> httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<List<DetailRecord>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<DetailRecord>> e) throws Exception {
                                if (status == 0){
                                    List<DetailRecord> detailRecords = httpReply.getData();
                                    e.onNext(detailRecords);
                                }else {
                                    e.onError(new GFMSException(status, httpReply.getMessage()));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }

    public static Observable<List<WorkType>> getWorkType(long workerId){
        return Retrofit2Util.getServerInterface()
                .getWorkType(workerId)
                .flatMap(new Function<HttpReply<List<WorkType>>, Observable<List<WorkType>>>() {
                    @Override
                    public Observable<List<WorkType>> apply(@NonNull final HttpReply<List<WorkType>> httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<List<WorkType>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<WorkType>> e) throws Exception {
                                if (status == 0){
                                    List<WorkType> workTypes = httpReply.getData();
                                    e.onNext(workTypes);
                                }else {
                                    e.onError(new GFMSException(status, httpReply.getMessage()));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }

    static Observable<List<ClothesType>> getClothesType(long workerId){
        return Retrofit2Util.getServerInterface()
                .getClothesType(workerId)
                .flatMap(new Function<HttpReply<List<ClothesType>>, Observable<List<ClothesType>>>() {
                    @Override
                    public Observable<List<ClothesType>> apply(@NonNull final HttpReply<List<ClothesType>> httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<List<ClothesType>>() {
                            @Override
                            public void subscribe(@NonNull ObservableEmitter<List<ClothesType>> e) throws Exception {
                                if (status == 0){
                                    List<ClothesType> clothesTypes = httpReply.getData();
                                    e.onNext(clothesTypes);
                                }else {
                                    e.onError(new GFMSException(status, httpReply.getMessage()));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }

    static Observable<DayAndDetailRecords> submitDayRecord(DayAndDetailRecords dayAndDetailRecords){
        return Retrofit2Util.getServerInterface()
                .submitDayRecord(dayAndDetailRecords)
                .flatMap(new Function<HttpReply<DayAndDetailRecords>, Observable<DayAndDetailRecords>>() {
                    @Override
                    public Observable<DayAndDetailRecords> apply(final HttpReply<DayAndDetailRecords> httpReply) throws Exception {
                        final int status = httpReply.getStatus();
                        return Observable.create(new ObservableOnSubscribe<DayAndDetailRecords>() {
                            @Override
                            public void subscribe(ObservableEmitter<DayAndDetailRecords> e) throws Exception {
                                if (status == 0){
                                    DayAndDetailRecords dayAndDetailRecords = httpReply.getData();
                                    e.onNext(dayAndDetailRecords);
                                }else {
                                    e.onError(new GFMSException(status, httpReply.getMessage()));
                                }
                                e.onComplete();
                            }
                        });
                    }
                });
    }
}
