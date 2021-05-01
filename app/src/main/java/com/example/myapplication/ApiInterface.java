package com.example.myapplication;

import com.example.myapplication.Model.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("everything")
    Call<Headlines> getHeadlines(
            @Query("q") String country,
            @Query("apiKey") String apiKey
    );
}
