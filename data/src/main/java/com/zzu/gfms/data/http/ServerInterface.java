package com.zzu.gfms.data.http;

import com.zzu.gfms.data.dbflow.ClothesType;
import com.zzu.gfms.data.dbflow.DayRecord;
import com.zzu.gfms.data.dbflow.DetailRecord;
import com.zzu.gfms.data.dbflow.WorkType;

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

    @FormUrlEncoded
    @POST("user/login")
    Observable<HttpReply> login(@Field("userName") String userName,
                                              @Field("password") String password);
    //获取挑战值
    @GET("api/v1/challenge")
    Observable<HttpReply> getDayRecordOfMonth(@Query("workerId") String id,
                                              @Query("date") String date);

    //认证
    @GET("api/v1/auth")
    Observable<HttpReply> getDetailRecordOfDay(@Query("dayRecordId") String id);

    //获取工作类型
    @GET("worktype/getWorkTypesByWorkerID/{enterpriseID}")
    Observable<HttpReply> getWorkType(@Path("enterpriseID") int enterpriseID);

    //获取衣服类型
    @GET("clothestype/getAllClothesTypeByWorkerID/{enterpriseID}")
    Observable<HttpReply> getClothesType(@Path("enterpriseID") int enterpriseID);
}
