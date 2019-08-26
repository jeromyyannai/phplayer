package com.az.pplayer.Storage.Db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.az.pplayer.Services.DownloadRequest;
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
