package com.az.pplayer.Data;

import com.az.pplayer.Models.VideoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHolder {
    private static Map<String,List<VideoItem>> items;

    public static  List<VideoItem>Get(String key){
        if (items == null)
            items = new HashMap<>();
        if (!items.containsKey(key))
            return  new ArrayList<VideoItem>();
            return items.get(key);
    }
    public static void Save(String key,List<VideoItem> _items)
    {
        if (items == null)
            items = new HashMap<>();

            items.put(key,_items);

    }
    public static int Size(String key){
        if (items == null)
            return 0;
        if (!items.containsKey(key))
            return 0;
        return  items.get(key).size();
    }
}
