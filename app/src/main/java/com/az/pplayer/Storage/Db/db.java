package com.az.pplayer.Storage.Db;

import android.content.Context;

import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Storage.Db.DbMappers.Mapper;
import com.az.pplayer.Storage.Db.DbModels.dbBuilder;
import com.az.pplayer.Storage.Db.DbModels.dbDownloadRequest;
import com.az.pplayer.Storage.Db.DbModels.dbTag;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItem;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItemTag;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItemTagRef;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class db {
    private phPlayerDb _db;
    public  List<DownloadRequest> downloadRequests;
public  List<LocalVideoItem> localVideoItems;
    public db(Context context) {

        _db = new dbBuilder(context).getDatabase();

    }



public void getVideoItems(Consumer<List<LocalVideoItem>> action){
    _db.videoItemDao().getFullVideoItem()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<dbVideoItemTag>>() {
                @Override
                public void accept(List<dbVideoItemTag> videoItems) throws Exception {
                    try {
                        action.accept(Mapper.Map(videoItems));
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            });
}

public List<LocalVideoItem> getUnfinishedDownloadRequests(){
        List<dbVideoItem> result =  _db.videoItemDao().getUnfinishedDownloadRequests();
        if (result == null)
            return new ArrayList<>();
        return  Mapper.MapRequest(result);
}
public void updateVideoItem(LocalVideoItem item, CompletableObserver action,boolean updateTags){
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isNewItem = false;
                dbVideoItem dbitem = Mapper.Map(item);
                dbVideoItem existedItem = _db.videoItemDao().findVideoByPath(dbitem.VideoId);
                if (existedItem != null) {
                    dbitem.id_item = existedItem.id_item;
                }
                if (dbitem.id_item ==0){
                    isNewItem = true;
                    dbitem.id_item = (int)_db.videoItemDao().insertVideoItem(dbitem);
                    List<dbTag> tags = _db.videoItemDao().getTags();
                    insertTags(dbitem,item.Tags,tags);

                } else{
                    _db.videoItemDao().updateVideoItem(dbitem);
                }

                if (updateTags && !isNewItem){
                    List<dbTag> tags = _db.videoItemDao().getTags();
                    insertTags(dbitem,item.Tags,tags);

                }
            }
        }).start();
   }
    public  void deleteVideoItem(LocalVideoItem item){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    _db.videoItemDao().deleteVideoItem(Mapper.Map(item));
                }
            }).start();
    }

    private void insertTags(dbVideoItem dbitem, String[] tags, List<dbTag> dbTags) {
        //check if tags exists in the db, otherwice insert
        //update video_item_tags table
        dbVideoItemTag itemTag = _db.videoItemDao().getVideoItemTags(dbitem.id_item);
        for (int i=0;i<tags.length;i++){
            if (tags[i] == null)
                continue;
            dbTag dbTag = findTag(dbTags,tags[i]);

            if (itemTag != null && tagExists(itemTag.Tags,dbTag))
                continue;
            if (dbTag == null){
                dbTag = new dbTag();
                dbTag.tag = tags[i];
                dbTag.id_tag = (int)_db.videoItemDao().insertTag(dbTag);
            }
            _db.videoItemDao().insertItemTag(new dbVideoItemTagRef(dbTag.id_tag,dbitem.id_item));
        }
    }

    private boolean tagExists(List<dbTag> itemTags, dbTag tag) {
        if (tag == null)
            return false;
        for (dbTag _dbTag: itemTags){
            if (_dbTag.id_tag == tag.id_tag)
                return true;
        }
        return false;
    }

    private dbTag findTag(List<dbTag> dbTags, String tag) {
        for (dbTag _dbTag: dbTags){
            if (_dbTag == null )
                continue;
            if (_dbTag.tag.equals(tag))
                return _dbTag;
        }
        return null;
    }


}
