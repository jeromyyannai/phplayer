package com.az.pplayer.Storage.Db;



import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class VideoEntryDao {
    @Embedded
    VideoItemDao videoItem;
    @Relation(parentColumn = "id", entityColumn = "fk_id_item", entity = VideoItemTagDao.class)
    public List<VideoItemTagDao> tags;
}
