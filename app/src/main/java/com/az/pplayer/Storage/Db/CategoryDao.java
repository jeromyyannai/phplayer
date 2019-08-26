package com.az.pplayer.Storage.Db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;
@Entity
public class CategoryDao {
    @PrimaryKey
    public int id;
    public String Title;
    public String Link;
    public String Image;
    public Date date;

}
