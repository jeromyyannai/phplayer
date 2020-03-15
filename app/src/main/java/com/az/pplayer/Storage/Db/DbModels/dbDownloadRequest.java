package com.az.pplayer.Storage.Db.DbModels;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "download_request")

public class dbDownloadRequest {
    @PrimaryKey(autoGenerate = true)
    public int id_request;
    public int Id;
    public int fk_id_item;
    public String Url;
    public String VideoDisplayName;
//    public String[] Tags;
    public String ImageUrl;
    public String PreviewUrl;
    public long Size;
    public int PercentCompleted;
    public String Video;
    public String VideoId;
}
