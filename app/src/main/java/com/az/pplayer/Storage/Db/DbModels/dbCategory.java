package com.az.pplayer.Storage.Db.DbModels;



import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class dbCategory {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String Title;
    public String Link;
    public String Image;
    public Date date;

}
