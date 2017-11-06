package com.zzu.gfms.data.http;

import com.zzu.gfms.data.bean.DayAndDetailRecords;
import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.WorkType;
import com.zzu.gfms.data.dbflow.Worker;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

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
    @GET("dayrecord/getDayRecordByMonth/{workerId}-{year}-{month}")
    Observable<HttpReply<List<DayRecord>>> getDayRecordOfMonth(@Path("workerId") String workerId,
                                              @Path("year") String year,
                                              @Path("month") String month);

    //认证
    @GET("api/v1/auth")
    Observable<HttpReply<List<DetailRecord>>> getDetailRecordOfDay(@Query("dayRecordId") String id);

    //获取工作类型
    @GET("worktype/getWorkTypesByWorkerID/{workerId}")
    Observable<HttpReply<List<WorkType>>> getWorkType(@Path("workerId") long workerId);

    //获取衣服类型
    @GET("clothestype/getAllClothesTypeByWorkerID/{workerId}")
    Observable<HttpReply<List<ClothesType>>> getClothesType(@Path("workerId") long workerId);

    @POST("dayrecord/saveTmpDayRecord")
    Observable<HttpReply<DayAndDetailRecords>> submitDayRecord(@Body() DayAndDetailRecords dayAndDetailRecords);
}
