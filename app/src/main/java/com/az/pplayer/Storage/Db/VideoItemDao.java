package com.az.pplayer.Storage.Db;



import com.az.pplayer.Services.DownloadRequest;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VideoItemDao {
    @PrimaryKey
    public int id;
    public  String VideoPath;
    public String PreviewPath;
    public String ImagePath;
    public String Name;
    //public  String[] Tags;
    public int fk_id_request;
    public DownloadRequest Request;
    public boolean IsDownloaded;
}
