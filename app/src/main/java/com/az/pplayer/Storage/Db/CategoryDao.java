package com.az.pplayer.Storage.Db;



import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CategoryDao {
    @PrimaryKey
    public int id;
    public String Title;
    public String Link;
    public String Image;
    public Date date;

}
