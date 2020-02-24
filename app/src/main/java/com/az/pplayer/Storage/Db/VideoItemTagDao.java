package com.az.pplayer.Storage.Db;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

@Entity
public class VideoItemTagDao {
    @PrimaryKey
    public int id_video_tag;
    public int fk_id_tag;
    public int fk_id_item;
    @Relation(parentColumn = "id_tag", entityColumn = "fk_id_tag", entity = TagDao.class)
    public TagDao tag;
}
