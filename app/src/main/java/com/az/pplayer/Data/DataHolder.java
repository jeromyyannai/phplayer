package com.az.pplayer.Data;

import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.Models.VideoItemsPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHolder {
    private static Map<String,VideoItemsPage> items;
    public static  VideoItemsPage Get(String key){
        if (items == null)
            items = new HashMap<>();

        VideoItemsPage item= items.get(key);
        if (item == null) {
            item = new VideoItemsPage(key);
            items.put(key,item);
        }
        return item;


    }
    public static void Save(String key,List<VideoItem> videoItems)
    {
        if (items == null)
            items = new HashMap<>();
        VideoItemsPage itemsPage = items.get(key);
        if (itemsPage==null)
        {
            itemsPage = new VideoItemsPage(key);
        }
        itemsPage.Save(videoItems);
        items.put(key,itemsPage);


    }
    public static int Size(String key){
        if (items == null)
            return 0;
        if (!items.containsKey(key))
            return 0;
        return  items.get(key).CurrentVideo().size();
    }


}
