package com.az.pplayer;

import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PinchView.IOnTouchListener{
List<VideoItem> Video;
private String catUrl;
    PinchView pView;
    private ScaleGestureDetector mScaleGestureDetector;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
         catUrl = Url.MainUrl+ intent.getStringExtra("url");
        setupPinch();
        Video = new ArrayList<>();
        if (DataHolder.Size(catUrl)==0) {
            LoadSite();
        } else {
            ShowVideos();
        }

    }

    void LoadSite(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataHolder.Save(catUrl,VideoLinksSource.ParseLinks(catUrl));

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
          recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), UserStorage.Get().getColumns());
        recyclerView.setLayoutManager(gridLayoutManager);


        VideoDataAdapter dataAdapter = new VideoDataAdapter(getApplicationContext(), DataHolder.Get(catUrl),UserStorage.Get().getFontSize());
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
