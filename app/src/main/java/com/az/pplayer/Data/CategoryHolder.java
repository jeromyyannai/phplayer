package com.az.pplayer.Data;

import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.VideoItem;

import java.util.ArrayList;
import java.util.List;

public class CategoryHolder {
    private static List<CategoryItem> items;
    public static  List<CategoryItem>Get(){
        if (items == null)
            items = new ArrayList<CategoryItem>();
        return items;
    }
    public static void Save(List<CategoryItem> _items)
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
