package com.az.pplayer.Storage.Db;

import com.az.pplayer.Storage.Db.DbModels.dbCategory;
import com.az.pplayer.Storage.Db.DbModels.dbDownloadRequest;
import com.az.pplayer.Storage.Db.DbModels.dbTag;
import com.az.pplayer.Storage.Db.DbModels.dbTagVideoItem;
import com.az.pplayer.Storage.Db.DbModels.dbVideoEntry;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItem;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItemTag;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItemTagRef;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;
import io.reactivex.Flowable;

@Dao
public interface VideoItemDao {
    @Query("SELECT * FROM category")
    Flowable<List<dbCategory>> getCategories();

    @Query("SELECT * FROM download_request")
    Flowable<List<dbDownloadRequest>> getDownloadRequests();
    @Query("SELECT * FROM download_request")
    List<dbDownloadRequest> getUnfinishedDownloadRequests();
    @Query("SELECT * FROM video_item")
    Flowable<List<dbVideoItem>> getVideoItem();

    @Transaction
    @Query("SELECT * FROM video_item")
    Flowable<List<dbVideoItemTag>> getFullVideoItem();

    @Transaction
    @Query("SELECT * FROM video_item Where id_item =:id")
    dbVideoItemTag getVideoItemTags(long id);

    @Query("SELECT * FROM tag")
    List<dbTag> getTags();
    @Query("SELECT * FROM tag WHERE id_tag =:id")
    List<dbTagVideoItem> getTagVideoItems(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDownloadRequest(dbDownloadRequest entity);

    @Update
    void updateDownloadRequest(dbDownloadRequest entity);

    @Query("DELETE FROM download_request WHERE id_request = :id")
    void deleteDownloadRequest(long id);
@Delete
void deleteDownloadRequest(dbDownloadRequest request);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertVideoItem(dbVideoItem entity);

    @Update
    void updateVideoItem(dbVideoItem entity);

    @Delete
    void deleteVideoItem(dbVideoItem entity);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTag(dbTag tag);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItemTag(dbVideoItemTagRef tag);
    @Update
    void updateItemTag(dbVideoItemTagRef tag);
    @Query("SELECT * FROM video_item Where VideoPath =:videoPath")
    dbVideoItem findVideoByPath(String videoPath);
}
