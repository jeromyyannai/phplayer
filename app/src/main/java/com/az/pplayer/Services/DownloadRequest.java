package com.az.pplayer.Services;

import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.Storage.Db.DbModels.dbDownloadRequest;

public class DownloadRequest {
    public String Video;
    public String VideoId;
    public String Url;
    public String VideoDisplayName;
    public String[] Tags;
    public String ImageUrl;
    public String PreviewUrl;
    public int Id;
    public int FetchId;
    public int PercentCompleted;
    public long Size;
    //public LocalVideoItem VideoItem;



    public DownloadRequest(dbDownloadRequest r) {
        Url = r.Url;
        VideoDisplayName = r.VideoDisplayName;
        //Tags = r.Tags;
        ImageUrl = r.ImageUrl;
        PreviewUrl = r.PreviewUrl;
        Id = r.Id;
        PercentCompleted = r.PercentCompleted;
        Size = r.Size;
        Video = r.Video;
        VideoId = r.VideoId;
    }

    public DownloadRequest() {
        Id =0;
    }

    public DownloadRequest(String url, String videoDisplayName, String[] tags, String imageUrl, String previewUrl, long size, String videoId, String video) {
        Id =0;
        Url = url;
        VideoDisplayName = videoDisplayName;
        Tags = tags;
        ImageUrl = imageUrl;
        PreviewUrl = previewUrl;
        Size = size;
        Video = video;
        VideoId = videoId;
    }
}
