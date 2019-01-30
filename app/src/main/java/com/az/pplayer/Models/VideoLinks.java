package com.az.pplayer.Models;

import com.az.pplayer.Storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

public class VideoLinks {
    public List<VideoUrl> Links;
    public VideoLinks(){
        Links = new ArrayList<>();
    }

    public VideoLinks(List<VideoUrl> urls){
        Links = urls;
        if (Links == null)
            Links = new ArrayList<>();
    }
    public VideoUrl Default(){
        VideoUrl result;
        if (Links.size()==0)
            return null;
        String resolution = UserStorage.Get().getResolution();
        for (VideoUrl url : Links){
            if (url.Quality == resolution)
                return url;
        }
        return Links.get(Links.size()-1);
    }
}
