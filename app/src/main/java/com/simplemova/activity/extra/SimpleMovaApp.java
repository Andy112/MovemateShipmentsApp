package com.simplemova.activity.extra;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;

public class SimpleMovaApp extends Application {

    private static SimpleMovaApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
}