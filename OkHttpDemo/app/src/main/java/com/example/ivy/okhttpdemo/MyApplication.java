package com.example.ivy.okhttpdemo;

import android.app.Application;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by Ivy on 2016/7/10.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        initOkHttp();


    }

    private void initOkHttp() {
        OkHttpClient client = OkHttpUtils.getClientIntance();
        Log.d("App",client.toString());
    }
}
