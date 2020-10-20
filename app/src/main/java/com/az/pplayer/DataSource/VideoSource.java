package com.az.pplayer.DataSource;

import com.az.pplayer.Base.SSLHelper;
import com.az.pplayer.Models.VideoLinks;
import com.az.pplayer.Models.VideoUrl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoSource {
    public static VideoLinks ParseVideoLinks(String link){
        String defaultUrl ="";

        try {
            Document doc = SSLHelper.getDocUrl("https://pornhub.com"+link);
            Element script = doc.select("#player").select("script").first();

            String rawHtml = script.html().substring(0,6553);
            String[] urlParts = rawHtml.substring(rawHtml.indexOf("videoUrl")).split("videoUrl");
            List<VideoUrl> urls = new ArrayList<>();

            for (String urlPart : urlParts)
            {
                try {
                    String url = urlPart.substring(0, urlPart.indexOf(","))
                            .replace("\":\"","").replace("\"}","").replace("\\","").replace("]","");
                    if (url.length() > 2) {
                        VideoUrl _url =new VideoUrl(url,
                                urlPart.substring(urlPart.indexOf("quality")+10,urlPart.indexOf("quality")+14).replace("\"","")
                        );
                        if (_url.Quality =="480")
                            defaultUrl = _url.Link;
                        if (defaultUrl.length()==0)
                            defaultUrl = _url.Link;
                        urls.add(_url);

                    }
                } catch (Exception ex)
                {

                }
            }
            return new VideoLinks(urls);

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new VideoLinks();
    }
}
