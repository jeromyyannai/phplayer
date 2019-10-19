package com.az.pplayer.Models;

public class VideoUrlBind {

    public String id;
    public String text;
    public String url;
    public float upgrade;
    public float active;


    public VideoUrlBind(String id, String text, String url, float upgrade, float active) {
        this.id = id;
        this.text = text;
        this.url = url;
        this.upgrade = upgrade;
        this.active = active;
    }
}
