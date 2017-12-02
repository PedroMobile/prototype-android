package com.photozig.prototype.rest.services;

import com.photozig.prototype.rest.models.AppInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("/pz_challenge/assets.json")
    Call<AppInfo> appInfo();

}