package com.az.pplayer.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import com.az.pplayer.Models.VideoUrl;
import com.az.pplayer.R;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class VideoPlayerLoader extends AppCompatActivity {
    public static final String INTENT_EXTRA_VIDEO_URL = "VIDEO_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player_loader);
        Intent intent = getIntent();
        String message = intent.getStringExtra("url");
        LoadSite(message);

    }


    void LoadSite(final String _url){

        new Thread(new Runnable() {
            @Override
            public void run() {
                String defaultUrl ="";

                try {
                    Document doc = Jsoup.connect("https://pornhub.com"+_url).get();
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


                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if (defaultUrl.length()>0) {
                    final String finalDefaultUrl = defaultUrl;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //imageView = (ImageView) findViewById(R.id.imageView);
                            Intent intent = new Intent(VideoPlayerLoader.this,VideoPlayerActivity.class);

                            intent.putExtra(VideoPlayerLoader.INTENT_EXTRA_VIDEO_URL, finalDefaultUrl);
                            startActivity(intent);

                        }
                    });
                }
            }
        }).start();
    }

}
