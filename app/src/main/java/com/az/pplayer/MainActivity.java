package com.az.pplayer;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.az.pplayer.Base.PinchView;
import com.az.pplayer.Constants.Url;
import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.Storage.UserStorage;
import com.az.pplayer.Views.CategoryDataAdapter;
import com.az.pplayer.Views.CategoryViewActivity;
import com.az.pplayer.Views.VideoDataAdapter;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PinchView.IOnTouchListener{
List<VideoItem> Video;
//private String catUrl;
    PinchView pView;
    private ScaleGestureDetector mScaleGestureDetector;
    RecyclerView recyclerView;
    SwipyRefreshLayout mSwipyRefreshLayout;
    private String requestUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        requestUrl = intent.getStringExtra("url");
        final String catUrl  = prepareUrl(requestUrl);

        setupPinch();

        Video = new ArrayList<>();
        if (DataHolder.Size(catUrl)==0) {
            LoadSite(DataHolder.Get(catUrl).FullUrl());
        } else {
            ShowVideos(catUrl);
        }
        mSwipyRefreshLayout = findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d("MainActivity", "Refresh triggered at "
                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                boolean result = false;
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    result = DataHolder.Get(catUrl).DownPageNumber();
                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    result = DataHolder.Get(catUrl).UpPageNumber();
                }

                if (result)
                    LoadSite(DataHolder.Get(catUrl).FullUrl());

                mSwipyRefreshLayout.setRefreshing(false);

            }
        });


    }

    String prepareUrl(String url){


        if (!(url != null && !url.isEmpty())){
            url = "/video";
        }
        return Url.MainUrl +url;

    }

    void LoadSite(final String  catUrl){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataHolder.Save(catUrl,VideoLinksSource.ParseLinks(catUrl));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //imageView = (ImageView) findViewById(R.id.imageView);
                       ShowVideos(catUrl);
                    }
                });
            }
        }).start();
    }

    void ShowVideos(String catUrl){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
          recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), UserStorage.Get().getColumns());
        recyclerView.setLayoutManager(gridLayoutManager);


        VideoDataAdapter dataAdapter = new VideoDataAdapter(getApplicationContext(),
                DataHolder.Get(catUrl).CurrentVideo(),
                UserStorage.Get().getFontSize());
        recyclerView.setAdapter(dataAdapter);
    }
    private void setupPinch() {
        pView = findViewById(R.id.nsView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new MainActivity.ScaleListener());
        pView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouchDetected(MotionEvent ev) {
        return  onTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }
    private boolean updateGrid(MainActivity.gridUpdateFactor updateFactor){
        int currentCount =  ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
        if (updateFactor == MainActivity.gridUpdateFactor.in){
            if  (currentCount==10)
                return  false;
            ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(currentCount+1);
            ((VideoDataAdapter)recyclerView.getAdapter()).setTextSizes(-2);
        } else {
            if  (currentCount==1)
                return  false;
            ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(currentCount-1);
            ((VideoDataAdapter)recyclerView.getAdapter()).setTextSizes(+2);
        }
        UserStorage.Get().setColumns(((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount());
        UserStorage.Get().setFontSize(((VideoDataAdapter)recyclerView.getAdapter()).getTextSize());
        return true;
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            if (scaleGestureDetector.getScaleFactor() ==1)
                return;
            MainActivity.gridUpdateFactor factor = MainActivity.gridUpdateFactor.out;
            if (scaleGestureDetector.getScaleFactor()<1)
                factor = MainActivity.gridUpdateFactor.in;

//            if (scaleGestureDetector.getScaleFactor()>  1){
//                mScaleFactor+=0.01;
//            }else {
//                mScaleFactor-=0.01;
//            }

            updateGrid(factor);
        }
    }

    enum gridUpdateFactor{
        in, out

    }
}
