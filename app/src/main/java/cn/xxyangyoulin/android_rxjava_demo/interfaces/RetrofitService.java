package cn.xxyangyoulin.android_rxjava_demo.interfaces;


import cn.xxyangyoulin.android_rxjava_demo.School;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit2.Call;

/**
 * Created by mnnyang on 2017/6/30.
 */

public interface RetrofitService {
    @GET("hah/hehe")
    Call<School> getSchool(@Query("q") String q, @Query("cc") int cc);
}
