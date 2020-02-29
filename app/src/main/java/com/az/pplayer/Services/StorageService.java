package com.az.pplayer.Services;

import android.app.Activity;
import android.os.Environment;

import com.az.pplayer.Models.Storage;
import com.az.pplayer.PhpPlayerApp;
import com.az.pplayer.Views.SettingsActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.isExternalStorageEmulated;

public class StorageService {
    public static String internalStorage;
    public static List<Storage> GetAvailableStorages() {
        List<Storage> result = new ArrayList<>();
        internalStorage = Environment.getExternalStorageDirectory().getAbsolutePath();
        File[] storages = PhpPlayerApp.getAppContext().getExternalFilesDirs(null);

        for (File f: storages){
            Storage storage = new Storage();

                storage.Value =f.getAbsolutePath();
            storage.Key =  getStorageValue(f);
            result.add(storage);

        }
       return result;
    }

    private static String getStorageValue(File f)
    {
        if (f.getAbsolutePath().startsWith(internalStorage))
            return "Internal Storage";
        return f.getAbsolutePath().split("/")[2];

    }
}
