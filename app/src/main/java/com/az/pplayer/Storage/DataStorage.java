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
        db.getVideoItems(new Consumer<List<LocalVideoItem>>() {
            @Override
            public void accept(List<LocalVideoItem> localVideoItems) {
                VideoItems.setValue(localVideoItems);
            }
        });
        db.getDownloadRequests(new Consumer<List<DownloadRequest>>() {
            @Override
            public void accept(List<DownloadRequest> requests) throws Exception {
                DownloadedItems.setValue(requests);
            }
        });
    }

    public static DataStorage Get() {
        if (ourInstance == null)
            ourInstance = new DataStorage(PhpPlayerApp.getAppContext());
        return ourInstance;
    }
    private MutableLiveData<List<LocalVideoItem>> VideoItems = new MutableLiveData<>();
    private MutableLiveData<List<DownloadRequest>> DownloadedItems = new MutableLiveData<>();

    public LiveData<List<LocalVideoItem>> GetDownloadedVideoList(){
    return  VideoItems;
}


    public List<DownloadRequest> GetDownloadRequests(){

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
       });
    }

    public void RemoveDownloadedVideo(LocalVideoItem item){
        for (int i=0;i<VideoItems.getValue().size();i++){
            if (item.VideoPath.equals(VideoItems.getValue().get(i).VideoPath)){
                DownloadedItems.getValue().remove(i);
                break;
            }
        }
        db.deleteVideoItem(item);
    }


    public DownloadRequest GetRequest(int id){
        for (int i=0;i<DownloadedItems.getValue().size();i++){
            if (id ==DownloadedItems.getValue().get(i).FetchId){
                return  DownloadedItems.getValue().get(i);
            }
        }
        return null;
    }

    public void RemoveDownloadRequest(int id){
        for (int i=0;i<DownloadedItems.getValue().size();i++){
            if (id ==DownloadedItems.getValue().get(i).FetchId){
                DownloadedItems.getValue().remove(i);
               db.deleteDownloadRequest(id);
            }
        }
    }

}
