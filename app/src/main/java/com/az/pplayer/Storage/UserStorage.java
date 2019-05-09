package com.az.pplayer.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.CategoryStorageItem;
import com.az.pplayer.PhpPlayerApp;
import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.Services.DownloadRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.print.PrintHelper.ORIENTATION_LANDSCAPE;

public class UserStorage {
    private static  UserStorage ourInstance;;

    public static UserStorage Get() {
        if (ourInstance == null)
            ourInstance = new UserStorage(PhpPlayerApp.getAppContext());
        return ourInstance;
    }
    SharedPreferences preferences;
    private UserStorage(Context context) {
        preferences = context.getSharedPreferences("phplayer", MODE_PRIVATE);
        resolution = preferences.getString("resolution","480");
       String rescentCatsstr = preferences.getString("rescentCats","");
        Type listType = new TypeToken<ArrayList<CategoryStorageItem>>(){}.getType();
        rescentCats = new Gson().fromJson(rescentCatsstr, listType);

        String DownloadedItemsstr = preferences.getString("DownloadedItems","");
        Type listType2 = new TypeToken<ArrayList<LocalVideoItem>>(){}.getType();
        DownloadedItems = new Gson().fromJson(DownloadedItemsstr, listType2);

        String downloadRequestsstr = preferences.getString("downloadRequests","");
        Type listType3 = new TypeToken<ArrayList<DownloadRequest>>(){}.getType();
        downloadRequests = new Gson().fromJson(downloadRequestsstr, listType3);

        if (java.util.Arrays.asList(resolutions).indexOf(resolution) ==-1)
            resolution = "480";
        columns = Math.max(preferences.getInt("columns",3),1);
        fontSize = preferences.getInt("fontSize",10);
        if (fontSize ==0)
            fontSize = 10;
    }

    private String resolution = "480";
    private String[] resolutions ={"240","480","720","1080"};
    private List<CategoryStorageItem> rescentCats;
    private int maxRescentcats = 7;
    private int columns=3;
    private int fontSize = 10;
    public String getResolution(){
        return  resolution;
    }

    private List<LocalVideoItem> DownloadedItems;
    private List<DownloadRequest> downloadRequests;

    public String getDownloadPath(){
        String root = Environment.getExternalStorageDirectory().toString();
        //String root="";
        File myDir = new File(root + "/php_videos");
        myDir.mkdirs();
        return myDir.getPath();
    }
    public String getDownloadDataPath(){
        String root = Environment.getExternalStorageDirectory().toString();
        //String root=Context;
        File myDir = new File(root + "/php_videos/data");
        myDir.mkdirs();
        return myDir.getPath();
    }

    public  void  setResolution(String  res){
        if (java.util.Arrays.asList(resolutions).indexOf(resolution) >-1){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("resolution", res);
            editor.commit();
            resolution = res;
        }
    }

    public List<CategoryStorageItem> getRescentCats(){
        if (rescentCats == null)
            rescentCats = new ArrayList<>();
        return rescentCats;
    }
    public void AddCategory(CategoryItem item){
        if (rescentCats == null)
            rescentCats = new ArrayList<>();
        rescentCats.add(new CategoryStorageItem(item));
        Collections.sort(rescentCats, Collections.reverseOrder( new Comparator<CategoryStorageItem>() {
            @Override
            public int compare(CategoryStorageItem item1, CategoryStorageItem item2) {
                return item1.date.compareTo(item2.date);
            }
        }));
        while (rescentCats.size()>maxRescentcats)
            rescentCats.remove(rescentCats.size()-1);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("rescentCats", new Gson().toJson(rescentCats));
        editor.commit();
    }

    public List<LocalVideoItem> GetDownloadedVideoList(){
        if (DownloadedItems == null)
            DownloadedItems = new ArrayList<>();
        List<DownloadRequest> requests = GetDownloadRequests();
        for (LocalVideoItem item : DownloadedItems){
            if (item.Request != null) {
                DownloadRequest request = GetRequest(item.Request.Id);
                if (request != null)
                    item.IsDownloaded = false;

            }
        }
        return DownloadedItems;
    }
    public List<DownloadRequest> GetDownloadRequests(){
        if (downloadRequests == null)
            downloadRequests = new ArrayList<>();
        return downloadRequests;
    }

    public void AddDownloadedVideo(LocalVideoItem item){
        if (DownloadedItems == null)
            DownloadedItems = new ArrayList<>();
        DownloadedItems.add(item);


        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("DownloadedItems", new Gson().toJson(DownloadedItems));
        editor.apply();
    }

    public void AddDownloadRequest(DownloadRequest item){
        if (downloadRequests == null)
            downloadRequests = new ArrayList<>();
        downloadRequests.add(item);


        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("downloadRequests", new Gson().toJson(downloadRequests));
        editor.apply();
    }
    public void RemoveDownloadedVideo(LocalVideoItem item){
        for (int i=0;i<DownloadedItems.size();i++){
            if (item.VideoPath.equals(DownloadedItems.get(i).VideoPath)){
                DownloadedItems.remove(i);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("DownloadedItems", new Gson().toJson(DownloadedItems));
                editor.commit();
                return;
            }
        }
    }


    public DownloadRequest GetRequest(int id){
        for (int i=0;i<downloadRequests.size();i++){
            if (id ==downloadRequests.get(i).Id){
                return  downloadRequests.get(i);
            }
        }
        return null;
    }

    public void RemoveDownloadRequest(int id){
        for (int i=0;i<downloadRequests.size();i++){
            if (id ==downloadRequests.get(i).Id){
                downloadRequests.remove(i);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("downloadRequests", new Gson().toJson(downloadRequests));
                editor.apply();
                return;
            }
        }
    }

    public int getColumns() {
        if (PhpPlayerApp.getAppContext().getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
                return columns-2>0?columns-2:1;
        return columns>0?columns:1;
    }

    public void setColumns(int columns) {
        if (columns<1 ||columns>10)
            return;

        this.columns = columns;
        if (PhpPlayerApp.getAppContext().getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE)
            columns+=2;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("columns", columns);
        editor.commit();
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("fontSize", fontSize);
        editor.commit();
    }
}
