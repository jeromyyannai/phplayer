package com.az.pplayer;

import android.app.Application;
import android.content.Context;

import com.az.pplayer.Services.DownloadService;
import com.az.pplayer.Storage.DataStorage;

import androidx.multidex.MultiDexApplication;

public class PhpPlayerApp extends MultiDexApplication {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        PhpPlayerApp.context = getApplicationContext();
        DataStorage.Get().init(new Runnable() {
            @Override
            public void run() {
                DownloadService.Get();
            }
        });

    }

    public static Context getAppContext() {
        return PhpPlayerApp.context;
    }
}
