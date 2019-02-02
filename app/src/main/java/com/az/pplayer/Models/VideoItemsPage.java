package com.az.pplayer.Models;

import android.net.rtp.RtpStream;

import com.az.pplayer.Constants.Url;
import com.az.pplayer.Data.DataHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VideoItemsPage {
    public Map<Integer,List<VideoItem>> Items;
    private String uri;
    private int currentPageNumber;

    public VideoItemsPage(String uri){
        Items = new HashMap<>();
        currentPageNumber =0;
        this.uri = uri;
    }

    public boolean UpPageNumber(){
        currentPageNumber++;
        return true;
    }
    public boolean DownPageNumber()
    {
        if (currentPageNumber>0) {
            currentPageNumber--;
            return true;
        }
        return false;
    }

    public List<VideoItem> SetPageNumber(int key){
        currentPageNumber = key;
        List<VideoItem> items = Items.get(key);
        if (items == null)
            items = new ArrayList<>();
        return items;
    }

    public void Save(int key, List<VideoItem> items){
        Items.put(key,items);
    }

    public void Save( List<VideoItem> items){
        Items.put(currentPageNumber,items);

    }

    public List<VideoItem> CurrentVideo(){
        return Items.get(currentPageNumber);
    }

    public int getCurrentPageNumber(){
        return currentPageNumber;
    }

    public String FullUrl(){


        if (currentPageNumber<1)
            return  uri;
        return uri + (uri.indexOf('?') == -1 ? "?" : "&") + "page=" + currentPageNumber;
    }
}
