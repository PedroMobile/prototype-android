package com.photozig.prototype.rest;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.photozig.prototype.rest.services.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macbook on 02/12/2017.
 */

public class AppClient {

    public static final String BASE_URL =  "http://pbmedia.pepblast.com";

    private static AppClient mInstance;
    private final ApiService mApiService;

    public static synchronized AppClient getInstance() {
        if(mInstance == null)
            mInstance = new AppClient();
        return mInstance;
    }

    private AppClient() {

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

        Gson gson = new GsonBuilder()
                .create();

        final OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(logInterceptor)
                .addInterceptor(new ConnectivityAwareInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mApiService = retrofit.create(ApiService.class);
    }


    public static ApiService getApiService() {
        return getInstance().mApiService;
    }
}
