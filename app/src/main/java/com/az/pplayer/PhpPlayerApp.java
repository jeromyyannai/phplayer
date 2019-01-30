package com.az.pplayer;

import android.app.Application;
import android.content.Context;

public class PhpPlayerApp extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        PhpPlayerApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return PhpPlayerApp.context;
    }
}
