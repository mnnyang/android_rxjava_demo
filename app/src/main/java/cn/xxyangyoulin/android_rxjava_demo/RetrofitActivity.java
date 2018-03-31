package cn.xxyangyoulin.android_rxjava_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.GsonBuilder;
import com.mnnyang.advance.R;

import cn.xxyangyoulin.android_rxjava_demo.interfaces.RetrofitService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        fun();
    }

    private void fun() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/")
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
//        retrofitService.

    }
}
