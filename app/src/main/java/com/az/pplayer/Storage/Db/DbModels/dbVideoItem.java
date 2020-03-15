package com.az.pplayer.Storage.Db.DbModels;



import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.Services.DownloadRequest;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "video_item")
public class dbVideoItem {
    @PrimaryKey(autoGenerate = true)
    public long id_item;
    public  String VideoPath;
    public String PreviewPath;
    public String ImagePath;
    public String Name;
    //public  String[] Tags;

    public boolean IsDownloaded;
    public String Url;
    public String VideoDisplayName;
    //    public String[] Tags;
    public String ImageUrl;
    public String PreviewUrl;
    public long Size;
    public int PercentCompleted;
    public String Video;
    public String VideoId;
    public int FetchId;

    public dbVideoItem(LocalVideoItem item) {
        id_item = item.Id;
        VideoPath = item.VideoPath;
        PreviewPath = item.PreviewPath;
        ImagePath = item.ImagePath;
        Name = item.Name;
        IsDownloaded = item.IsDownloaded;
        Url = item.Request.Url;
        ImageUrl = item.Request.ImageUrl;
        PreviewUrl = item.Request.PreviewUrl;
        Size = item.Request.Size;
        PercentCompleted= item.Request.PercentCompleted;
        Video = item.Request.Video;
        VideoId = item.Request.VideoId;

    }

    public dbVideoItem() {
    }
}
