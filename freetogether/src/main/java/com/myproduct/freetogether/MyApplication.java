package com.myproduct.freetogether;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

import yiqihi.mobile.com.commonlib.ImagePipelineConfigFactory;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(getApplicationContext(), ImagePipelineConfigFactory.getOkHttpImagePipelineConfig(getApplicationContext()));
    }
}
