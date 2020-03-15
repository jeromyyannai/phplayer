package com.az.pplayer.Storage.Db.DbModels;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

@Entity(tableName = "video_item_tag",primaryKeys = {"id_tag", "id_item"})
public class dbVideoItemTagRef {
    public long id_tag;
    public long id_item;

    public dbVideoItemTagRef(long fk_id_tag, long fk_id_item) {
        this.id_tag = fk_id_tag;
        this.id_item = fk_id_item;
    }

    public dbVideoItemTagRef() {
    }
}
