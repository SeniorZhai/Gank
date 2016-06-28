package com.seniorzhai.gank.network;

import android.support.annotation.NonNull;

import com.seniorzhai.gank.model.BenefitModel;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by zhai on 16/6/24.
 */
public interface IGankApi {
    @NonNull
    @GET("/api/search/query/listview/category/福利/count/20/page/{page}")
    Observable<BenefitModel> benefit(@Path("page") int page);
}
