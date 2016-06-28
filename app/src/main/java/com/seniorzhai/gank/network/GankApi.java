package com.seniorzhai.gank.network;

import com.seniorzhai.gank.model.BenefitModel;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by zhai on 16/6/24.
 */
public class GankApi {

    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    private IGankApi mApi;

    public GankApi() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        if (false) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addNetworkInterceptor(loggingInterceptor);
        }
        Retrofit retrofit = new Retrofit.Builder()
                .client(okBuilder.build())
                .baseUrl("http://gank.io")
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
        mApi = retrofit.create(IGankApi.class);
    }

    public Observable<BenefitModel> getBenefit(int page) {
        return mApi.benefit(page);
    }
}
