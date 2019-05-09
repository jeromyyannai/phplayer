package com.az.pplayer.Services;

import com.az.pplayer.Features.Downloads.LocalVideoItem;

public class DownloadRequest {
    public String Url;
    public String VideoDisplayName;
    public String[] Tags;
    public String ImageUrl;
    public String PreviewUrl;
    public int Id;
    public int PercentCompleted;
    //public LocalVideoItem VideoItem;

    public DownloadRequest(String url, String videoDisplayName, String[] tags, String imageUrl, String previewUrl) {
        Url = url;
        VideoDisplayName = videoDisplayName;
        Tags = tags;
        ImageUrl = imageUrl;
        PreviewUrl = previewUrl;
    }
}
