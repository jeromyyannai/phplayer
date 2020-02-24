package com.az.pplayer.Storage.Db;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TagDao {
    @PrimaryKey
    public int id_tag;
    public String tag;
}
