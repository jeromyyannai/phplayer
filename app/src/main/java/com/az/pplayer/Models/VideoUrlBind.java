package com.az.pplayer.Models;

public class VideoUrlBind {
    public boolean defaultQuality;
    public String format;
    public Object quality;

    public VideoUrlBind(boolean defaultQuality, String format, Object quality, String videoUrl) {
        this.defaultQuality = defaultQuality;
        this.format = format;
        this.quality = quality;
        this.videoUrl = videoUrl;
    }

    public String videoUrl;
}
