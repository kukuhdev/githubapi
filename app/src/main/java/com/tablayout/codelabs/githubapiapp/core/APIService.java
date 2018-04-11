package com.tablayout.codelabs.githubapiapp.core;

import com.tablayout.codelabs.githubapiapp.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIService {

    @GET("search/users?q=kukuhdev")
    Call<User> getResultInfo();

    @GET
    Call<ResponseBody> getResultAsJSON(@Url String url);
}
