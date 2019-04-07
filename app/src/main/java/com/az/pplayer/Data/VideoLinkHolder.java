package com.az.pplayer.Data;

import com.az.pplayer.Models.VideoItemsPage;
import com.az.pplayer.Models.VideoUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoLinkHolder {
    private static Map<String, List<VideoUrl>> items;
public static  List<VideoUrl>  Get(String key){
    if (items == null)
        items = new HashMap<>();
    return  items.get(key);
}
public static void Save(String key,  List<VideoUrl>  videoUrl){
    if (items == null)
        items = new HashMap<>();
    items.put(key,videoUrl);
}
public static String GetDefaultUrl(String key){
    List<VideoUrl> videoItems = Get(key);
    if (videoItems == null)
        return null;
    String defaultVideoLink=null;
    for (VideoUrl item : videoItems) {
        if (item.Quality == "480")
            defaultVideoLink = item.Link;
        break;
    }
    if (defaultVideoLink ==null && videoItems.size()>0)
        defaultVideoLink = videoItems.get(0).Link;
    return defaultVideoLink;


    }
}

