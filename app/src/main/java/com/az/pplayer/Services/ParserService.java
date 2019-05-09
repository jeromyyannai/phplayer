package com.az.pplayer.Services;

import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.Data.VideoLinkHolder;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.Models.VideoUrl;
import com.az.pplayer.Models.VideoUrlBind;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ParserService {
    public static DownloadRequest ParseVideoPage(VideoItem mVideoUrl) {

        String defaultUrl = "";

        try {
            Document doc = Jsoup.connect("https://pornhub.com" + mVideoUrl.Video).timeout(0).get();
            Element script = doc.select("#player").select("script").first();
            Elements categories = doc.select(".video-detailed-info .categoriesWrapper a");
            List<String> tagList = new ArrayList<>();
            for (int i = 0; i < categories.size() - 1; i++) {
                if (categories.get(i).text() != null && categories.get(i).text().length() > 0 && categories.get(i).text().substring(0, 1) != "+")
                    tagList.add(categories.get(i).text());
            }
            String[] Tags = tagList.toArray(new String[0]);
            String rawHtml = script.html().substring(0, 6553);
            String videoDataPart = rawHtml.substring(rawHtml.indexOf("mediaDefinitions") + 18);
            String videoData = videoDataPart.substring(0, videoDataPart.indexOf("}]") + 2);
            Type listType = new TypeToken<ArrayList<VideoUrlBind>>() {
            }.getType();
            List<VideoUrlBind> videoUrls = new Gson().fromJson(videoData, listType);
            //String[] urlParts = rawHtml.substring(rawHtml.indexOf("videoUrl")).split("videoUrl");

            List<VideoUrl> urls = new ArrayList<>();

            for (VideoUrlBind urlPart : videoUrls) {
                try {
//                String url = urlPart.substring(0, urlPart.indexOf(","))
//                        .replace("\":\"", "").replace("\"}", "").replace("\\", "").replace("]", "");
//                if (url.length() > 2) {
//                    VideoUrl _url = new VideoUrl(url,
//                            urlPart.substring(urlPart.indexOf("quality") + 10, urlPart.indexOf("quality") + 14).replace("\"", "")
//                    );
                    if (urlPart.videoUrl == "")
                        continue;
                    VideoUrl _url = new VideoUrl(urlPart.videoUrl, urlPart.quality.toString());
                    if (_url.Quality.equals("480"))
                        defaultUrl = _url.Link;
                    if (defaultUrl.length() == 0)
                        defaultUrl = _url.Link;
                    urls.add(_url);


                } catch (Exception ex) {

                }

            }
            VideoLinkHolder.Save(mVideoUrl.Video, urls);
            DataHolder.Save(mVideoUrl.Video, VideoLinksSource.ParseLinks(doc));
            return new DownloadRequest(VideoLinkHolder.GetDownloadUrl(mVideoUrl.Video),
                    mVideoUrl.Title,Tags,mVideoUrl.Image,mVideoUrl.Preview);
        }
     catch (
    IOException e) {
        e.printStackTrace();
        return null;
    }
    }
}
