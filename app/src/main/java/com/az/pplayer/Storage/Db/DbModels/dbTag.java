package com.az.pplayer.Storage.Db.DbModels;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tag")
public class dbTag {
    @PrimaryKey(autoGenerate = true)
    public long id_tag;
    public String tag;
}
