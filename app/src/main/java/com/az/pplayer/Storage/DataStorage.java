package com.az.pplayer.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.PhpPlayerApp;
import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Storage.Db.db;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.observers.BlockingMultiObserver;

public class DataStorage {
    private static  DataStorage ourInstance;
private com.az.pplayer.Storage.Db.db db;
    public DataStorage(Context appContext) {
        db = new db(appContext);


    }
    public void init(final Runnable action){
        db.getVideoItems(new Consumer<List<LocalVideoItem>>() {
            @Override
            public void accept(List<LocalVideoItem> localVideoItems) {
                VideoItems.setValue(localVideoItems);
                if (action != null)
                    action.run();
            }
        });
    }
    public static void Init(Runnable action){
        ourInstance.init(action);
    }
    public static DataStorage Get() {
        if (ourInstance == null)
            ourInstance = new DataStorage(PhpPlayerApp.getAppContext());
        return ourInstance;
    }
    private MutableLiveData<List<LocalVideoItem>> VideoItems = new MutableLiveData<>();

    public LiveData<List<LocalVideoItem>> GetDownloadedVideoList(){
    return  VideoItems;
}


    public List<LocalVideoItem> GetDownloadRequests(){

       return db.getUnfinishedDownloadRequests();

    }

    public void AddDownloadedVideo(LocalVideoItem item){
       db.updateVideoItem(item, new CompletableObserver() {
           @Override
           public void onSubscribe(Disposable d) {

           }

           @Override
           public void onComplete()
           {

           }

           @Override
           public void onError(Throwable e) {

           }
       },true);
    }

    public void RemoveDownloadedVideo(LocalVideoItem item){
        for (int i=0;i<VideoItems.getValue().size();i++){
            if (item.VideoPath.equals(VideoItems.getValue().get(i).VideoPath)){
                VideoItems.getValue().remove(i);
                break;
            }
        }
        db.deleteVideoItem(item);
    }


    public LocalVideoItem GetRequest(int id,String url){
        for (int i=0;i<VideoItems.getValue().size();i++){
            if (id ==VideoItems.getValue().get(i).Request.FetchId ||  id ==-VideoItems.getValue().get(i).Request.FetchId){
                return  VideoItems.getValue().get(i);
            }
        }
        String[] urlParts = url.split("/");
        if (urlParts.length>1){
            String videoId = urlParts[urlParts.length-2];
            for (int i=0;i<VideoItems.getValue().size();i++){
                if (VideoItems.getValue().get(i).Request.VideoId.equals(videoId)){
                    return  VideoItems.getValue().get(i);
                }
            }
        }
        return null;
    }

    public void RemoveDownloadRequest(int id){
        for (int i=0;i<VideoItems.getValue().size();i++){
            if (id ==VideoItems.getValue().get(i).Request.FetchId || id ==-VideoItems.getValue().get(i).Request.FetchId){
                VideoItems.getValue().get(i).IsDownloaded = true;
                VideoItems.getValue().get(i).Request.PercentCompleted = 100;
               db.deleteVideoItem(VideoItems.getValue().get(i));
               break;
            }
        }
    }

    public void CompleteDownloadRequest(int id) {
        for (int i=0;i<VideoItems.getValue().size();i++){
            if (id ==VideoItems.getValue().get(i).Request.FetchId || id ==-VideoItems.getValue().get(i).Request.FetchId){
                VideoItems.getValue().get(i).IsDownloaded = true;
                VideoItems.getValue().get(i).Request.PercentCompleted = 100;
                db.updateVideoItem(VideoItems.getValue().get(i),new BlockingMultiObserver(),false);
                break;
            }
        }
    }
}
