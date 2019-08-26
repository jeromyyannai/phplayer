package com.az.pplayer.Storage.Db;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
