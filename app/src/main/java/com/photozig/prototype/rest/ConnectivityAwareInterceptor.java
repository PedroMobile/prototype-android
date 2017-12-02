package com.photozig.prototype.rest;


import com.photozig.prototype.util.Utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ConnectivityAwareInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!Utils.isNetworkAvailable()) {
            throw new NoConnectivityException("No connectivity");
        }
        return chain.proceed(chain.request());
    }
}
