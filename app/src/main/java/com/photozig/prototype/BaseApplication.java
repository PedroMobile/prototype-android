package com.photozig.prototype;

import android.app.Application;

/**
 * Created by macbook on 02/12/2017.
 */

public class BaseApplication extends Application {

    private static final String TAG = BaseApplication.class.getSimpleName();

    private static BaseApplication application;

    public static BaseApplication getApp() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        application = null;
    }

}
