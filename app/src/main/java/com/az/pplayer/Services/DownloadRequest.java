package com.az.pplayer.Services;

public class DownloadRequest {
    public String Url;
    public String VideoDisplayName;
    public String[] Tags;
    public String ImageUrl;
    public String PreviewUrl;
    public LocalVideoItem VideoItem;

    public DownloadRequest(String url, String videoDisplayName, String[] tags, String imageUrl, String previewUrl) {
        Url = url;
        VideoDisplayName = videoDisplayName;
        Tags = tags;
        ImageUrl = imageUrl;
        PreviewUrl = previewUrl;
    }
}
