package com.az.pplayer.Services;

import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.Storage.Db.DbModels.dbDownloadRequest;
import com.az.pplayer.Storage.Db.DbModels.dbVideoItem;

public class DownloadRequest {
    public String Video;
    public String VideoId;
    public String Url;
    public String VideoDisplayName;
    public String[] Tags;
    public String ImageUrl;
    public String PreviewUrl;
    public int FetchId;
    public int PercentCompleted;
    public long Size;
    //public LocalVideoItem VideoItem;





    public DownloadRequest() {

    }

    public DownloadRequest(String url, String videoDisplayName, String[] tags, String imageUrl, String previewUrl, long size, String videoId, String video) {

        Url = url;
        VideoDisplayName = videoDisplayName;
        Tags = tags;
        ImageUrl = imageUrl;
        PreviewUrl = previewUrl;
        Size = size;
        Video = video;
        VideoId = videoId;
    }

    public DownloadRequest(dbVideoItem r) {
        Url = r.Url;
        VideoDisplayName = r.VideoDisplayName;
        //Tags = r.Tags;
        ImageUrl = r.ImageUrl;
        PreviewUrl = r.PreviewUrl;

        PercentCompleted = r.PercentCompleted;
        Size = r.Size;
        Video = r.Video;
        VideoId = r.VideoId;
        FetchId = r.FetchId;
    }
}
