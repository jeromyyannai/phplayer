package com.az.pplayer.Storage.Db;

import com.az.pplayer.Storage.Db.DbMappers.DateConverter;
import com.az.pplayer.Storage.Db.DbModels.dbCategory;
import com.az.pplayer.Storage.Db.DbModels.dbDownloadRequest;
import com.az.pplayer.Storage.Db.DbModels.dbTag;
import com.az.pplayer.Storage.Db.DbModels.dbTagVideoItem;
import com.az.pplayer.Storage.Db.DbModels.dbVideoEntry;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItem;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItemTag;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItemTagRef;

import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@androidx.room.Database(entities = {
        dbCategory.class,
        dbDownloadRequest.class,
        dbTag.class,
        //dbTagVideoItem.class,
        dbVideoItem.class,
      //  dbVideoItemTag.class,
        dbVideoItemTagRef.class
}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class phPlayerDb extends RoomDatabase {


    public abstract VideoItemDao videoItemDao();


}
