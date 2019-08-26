package com.az.pplayer.Storage.Db;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

public class VideoEntryDao {
    @Embedded
    VideoItemDao videoItem;
    @Relation(parentColumn = "id", entityColumn = "fk_id_item", entity = VideoItemTagDao.class)
    public List<VideoItemTagDao> tags;
}
