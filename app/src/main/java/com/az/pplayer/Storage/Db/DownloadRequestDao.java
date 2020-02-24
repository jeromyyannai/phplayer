package com.az.pplayer.Storage.Db;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class DownloadRequestDao {
    @PrimaryKey
    public int id_request;
    public int Id;
    public String Url;
    public String VideoDisplayName;
    public String[] Tags;
    public String ImageUrl;
    public String PreviewUrl;
    public long Size;
    public int PercentCompleted;
}
