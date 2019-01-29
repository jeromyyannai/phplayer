package com.az.pplayer;

import android.support.annotation.MainThread;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.Views.VideoDataAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
List<VideoItem> Video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Video = new ArrayList<>();
        if (DataHolder.Size()==0) {
            LoadSite();
        } else {
            ShowVideos();
        }

    }

    void LoadSite(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("https://pornhub.com/").get();
                    Elements images = doc.select("img.rotating");
                    Video.clear();
                    for (Element el: images)
                    {
                        String imageUrl = el.attr("data-image");
                        if (imageUrl == "")
                            imageUrl = el.attr("data-src");
                        if (imageUrl == "")
                            imageUrl = el.attr("data-thumb_url");
                            Video.add(new VideoItem(el.attr("title"),
                                    el.parent().attr("href"),
                                    el.attr("data-mediabook"),
                                    el.attr("data-video-id"),
                                    imageUrl));
                    }
                    DataHolder.Save(Video);
                    Video.size();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //imageView = (ImageView) findViewById(R.id.imageView);
                       ShowVideos();
                    }
                });
            }
        }).start();
    }

    void ShowVideos(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int columns = width / 200;
        RecyclerView  recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
        recyclerView.setLayoutManager(gridLayoutManager);


        VideoDataAdapter dataAdapter = new VideoDataAdapter(getApplicationContext(), DataHolder.Get());
        recyclerView.setAdapter(dataAdapter);
    }
}
