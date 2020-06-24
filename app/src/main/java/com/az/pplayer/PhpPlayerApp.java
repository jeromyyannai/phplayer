package com.az.pplayer;

import android.app.Application;
import android.content.Context;

import com.az.pplayer.Services.DownloadService;

public class PhpPlayerApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        PhpPlayerApp.context = getApplicationContext();
        DownloadService.Get();
    }

    public static Context getAppContext() {
        return PhpPlayerApp.context;
    }
}
