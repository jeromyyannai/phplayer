package com.az.pplayer.Data;

import com.az.pplayer.Models.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    private static List<VideoItem> items;
    public static  List<VideoItem>Get(){
        if (items == null)
            items = new ArrayList<VideoItem>();
            return items;
    }
    public static void Save(List<VideoItem> _items)
    {
        if (_items == null)
            items = new ArrayList<>();
        items = _items;
    }
    public static int Size(){
        if (items == null)
            return 0;
        return  items.size();
    }
}
