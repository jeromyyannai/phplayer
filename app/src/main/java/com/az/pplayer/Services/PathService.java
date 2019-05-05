package com.az.pplayer.Services;

import com.az.pplayer.Storage.UserStorage;

import java.io.File;
import java.util.regex.Pattern;

public class PathService {
    public static String GetVideoLocalPath(String url){
        File file = new File (UserStorage.Get().getDownloadPath(), getFileName(url));
        return file.getPath();
    }

    public static String GetPreviewLocalPath(String url){
        File file = new File (UserStorage.Get().getDownloadDataPath(), getFileName(url));
        return file.getPath();
    }

    private static String getFileName(String url) {
        if (url == null || url == "")
            return null;
        String[] parts = url.split("/");
        return  parts[parts.length-1].split(Pattern.quote("?"))[0];
    }
    private static String getFileNameNoExt(String url) {
        if (url == null || url == "")
            return null;
        String[] parts = url.split("/");
        String fname=  parts[parts.length-1].split("/?")[0];
        return fname.substring(0,fname.lastIndexOf('.'));
    }
}
