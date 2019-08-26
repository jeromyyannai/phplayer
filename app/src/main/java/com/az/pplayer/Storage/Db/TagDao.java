package com.az.pplayer.Storage.Db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class TagDao {
    @PrimaryKey
    public int id_tag;
    public String tag;
}
