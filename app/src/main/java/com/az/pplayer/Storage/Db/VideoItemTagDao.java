package com.az.pplayer.Storage.Db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

@Entity
public class VideoItemTagDao {
    @PrimaryKey
    public int id_video_tag;
    public int fk_id_tag;
    public int fk_id_item;
    @Relation(parentColumn = "id_tag", entityColumn = "fk_id_tag", entity = TagDao.class)
    public TagDao tag;
}
