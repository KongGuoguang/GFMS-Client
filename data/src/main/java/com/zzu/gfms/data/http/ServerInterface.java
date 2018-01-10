package com.zzu.gfms.data.http;

import com.zzu.gfms.data.bean.DayAndDetailRecords;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.OperationRecord;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.Worker;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Author:kongguoguang
 * Date:2017-04-28
 * Time:16:39
 * Summary:服务器接口
 */

public interface ServerInterface {

    //登录
    @FormUrlEncoded
    @POST("user/login")
    Observable<HttpReply<Worker>> login(@Field("userName") String userName,
                                        @Field("password") String password);
    //获取一个月的日报记录
    @GET("dayrecord/getDayRecordByMonth/{workerId}-{year-month}")
    Observable<HttpReply<List<DayRecord>>> getDayRecord(@Path("workerId") String workerId,
                                                        @Path("year-month") String yearMonth);

    //获取某一天的详细记录
    @GET("dayrecord/getDayRecordDetail/{dayRecordId}")
    Observable<HttpReply<List<DetailRecord>>> getDetailRecords(@Path("dayRecordId") String dayRecordId);

    //获取统计详细记录
    @FormUrlEncoded
    @POST("api/showStaticsDetailRecord")
    Observable<HttpReply<List<DetailRecord>>> getDetailRecords(@Field("userWorkerID") long workerId,
                                                                  @Field("starttime") String startTime,
                                                                  @Field("endtime") String endTime,
                                                                  @Field("clothesID") int clothesID,
                                                                  @Field("workTypeID") int workTypeID,
                                                                  @Field("version") int version);

    //获取工作类型
    @GET("worktype/getWorkTypesByWorkerID/{workerId}")
    Observable<HttpReply<List<WorkType>>> getWorkType(@Path("workerId") long workerId);

    //获取衣服类型
    @GET("clothestype/getAllClothesTypeByWorkerID/{workerId}")
    Observable<HttpReply<List<ClothesType>>> getClothesType(@Path("workerId") long workerId);

    @POST("dayrecord/saveTmpDayRecord")
    Observable<HttpReply<DayAndDetailRecords>> submitDayRecord(@Body() DayAndDetailRecords dayAndDetailRecords);

    //申请修改
    @FormUrlEncoded
    @POST("dayrecord/applyModify")
    Observable<HttpReply> submitModifyApplication(@Field("dayRecordID") String dayRecordID,
                                                  @Field("modifyReason") String modifyReason);

    //获取修改审核记录
    @FormUrlEncoded
    @POST("api/showAllApplyRecord")
    Observable<HttpReply<List<OperationRecord>>> getOperationRecord(@Field("userWorkerID") long workerId,
                                                                    @Field("starttime") String startDate,
                                                                    @Field("endtime") String endDate,
                                                                    @Field("version") int version,
                                                                    @Field("applyType") String convertState);
    //退出
    @FormUrlEncoded
    @POST("api/logout")
    Observable<HttpReply> logout(@Field("userWorkerID") long workerId,@Field("version") int version);

    //退出
    @FormUrlEncoded
    @POST("api/modifyPassword")
    Observable<HttpReply> modifyPassword(@Field("workerID") long workerId,
                                         @Field("oldPassword") String oldPassword,
                                         @Field("newPassword") String newPassword,
                                         @Field("version") int version);

}
