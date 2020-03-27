package com.az.pplayer.Models;

import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.Services.DownloadRequest;

public class VideoItem {
    public String Title;
    public String Video;
    public String Preview;
    public String VideoId;
    public String Image;

    public VideoItem(String title, String video, String preview, String videoId, String image) {
        Title = title;
        Video = video;
        Preview = preview;
        VideoId = videoId;
        Image = image;
    }

    public VideoItem(DownloadRequest r) {
        Title = r.VideoDisplayName;
        Video = r.Video;
        Preview = r.PreviewUrl;
        VideoId = r.VideoId;
        Image = r.ImageUrl;
    }

    public VideoItem(LocalVideoItem r) {
        Title = r.Name;
        Video = r.Request.Video;
        Preview = r.Request.PreviewUrl;
        VideoId = r.Request.VideoId;
        Image = r.Request.ImageUrl;
    }
}
