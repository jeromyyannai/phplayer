package com.az.pplayer.DataSource;

import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.Models.VideoLinks;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoLinksSource {

    public static List<VideoItem> ParseLinks(String link){
        List<VideoItem> Video = new ArrayList<>();
        try {

            Document doc = Jsoup.connect(link).get();
            Elements images = doc.select("img.rotating");

            for (Element el: images)
            {
                String imageUrl = el.attr("data-image");
                if (imageUrl.length()>0)
                    continue;
                imageUrl = el.attr("data-src");
                if (imageUrl == "")
                    imageUrl = el.attr("data-thumb_url");
                Video.add(new VideoItem(el.attr("title"),
                        el.parent().attr("href"),
                        el.attr("data-mediabook"),
                        el.attr("data-video-id"),
                        imageUrl));
            }


        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Video;
    }
}
