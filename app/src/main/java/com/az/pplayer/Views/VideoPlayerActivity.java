package com.az.pplayer.Views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import android.os.Bundle;
import android.os.Handler;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.Data.ExoPlayerVideoHandler;
import com.az.pplayer.Data.VideoLinkHolder;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.MainActivity;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.CategoryStorageItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.Models.VideoUrl;
import com.az.pplayer.Models.VideoUrlBind;
import com.az.pplayer.R;
import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Services.DownloadService;
import com.az.pplayer.Services.ParserService;
import com.az.pplayer.Storage.UserStorage;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VideoPlayerActivity extends AppCompatActivity {
    private VideoItem mVideoUrl;
    public static final String INTENT_EXTRA_VIDEO_URL = "VIDEO_URL";

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private ImageButton mIbFullScreen;
    private  ImageButton mMenuButton;
    DrawerLayout mDrawerLayout;

    RecyclerView recyclerView;
    SwipyRefreshLayout mSwipyRefreshLayout;
    VisibleView visibleView;
    DownloadRequest request;

    private boolean shouldDestroyVideo = true;

    private String[] Tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Intent intent = getIntent();


        mVideoUrl = new Gson().fromJson(intent.getStringExtra("url"), VideoItem.class);
        mSwipyRefreshLayout = findViewById(R.id.swipyrefreshlayout);
        mSimpleExoPlayerView = findViewById(R.id.player_view);
        setupDrawer();

        LoadVideo();
    }

    void setupDrawer(){
        NavigationView nv = (NavigationView)findViewById(R.id.video_nav_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.nav_related:
                        visibleView = VisibleView.related;
                        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
                        mSimpleExoPlayerView.setVisibility(View.GONE);
                        mSwipyRefreshLayout.setVisibility(View.VISIBLE);
                        break;
                    case R.id.nav_download:
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
//                        }
                        if (request != null)
                        DownloadService.Get().Download(new LocalVideoItem(request));
                        break;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.video_drawer_layout);
                drawer.closeDrawer(Gravity.LEFT);
                return false;

            }
        });
    }



    void LoadVideo(){
        String videoUrl = VideoLinkHolder.GetDefaultUrl(mVideoUrl.Video);
        if (videoUrl==null)
            LoadSite(mVideoUrl.Video);
        else
            initializePlayer(videoUrl);
    }

    void LoadSite(final String _url){

        new Thread(new Runnable() {
            @Override
            public void run() {
         request = ParserService.ParseVideoPage(mVideoUrl);
                    final String finalDefaultUrl = VideoLinkHolder.GetDefaultUrl(mVideoUrl.Video);
                if (finalDefaultUrl != null && finalDefaultUrl.length()>0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //imageView = (ImageView) findViewById(R.id.imageView);
                            initializePlayer(finalDefaultUrl);
                            //mSwipyRefreshLayout.setRefreshing(false);
                            //imageView = (ImageView) findViewById(R.id.imageView);
                            ShowVideos(mVideoUrl.Video);
                        }
                    });
                }
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


        VideoDataAdapter dataAdapter = new VideoDataAdapter(this,
                DataHolder.Get(catUrl).CurrentVideo(),
                UserStorage.Get().getFontSize());
        recyclerView.setAdapter(dataAdapter);
    }


    private void initializePlayer(final String mVideoUrl) {
        mSimpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        mIbFullScreen = (ImageButton) findViewById(R.id.exo_fullscreen_btn);

        ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUri(getApplicationContext(), Uri.parse(mVideoUrl), mSimpleExoPlayerView, findViewById(R.id.pBarBuffer), findViewById(R.id.container_play_pause));
        ExoPlayerVideoHandler.getInstance().goToForeground();
        mIbFullScreen = findViewById(R.id.exo_fullscreen_btn);
        mIbFullScreen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shouldDestroyVideo = false;
                        Intent intent = new Intent(VideoPlayerActivity.this, SmallPlayerActivity.class);
                        intent.putExtra(VideoPlayerActivity.INTENT_EXTRA_VIDEO_URL, mVideoUrl);
                        startActivity(intent);

                    }
                });
        mMenuButton = findViewById(R.id.menu_btn);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.video_drawer_layout);
                drawer.openDrawer(Gravity.LEFT);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
       // LoadVideo();
    }

    @Override
    public void onBackPressed() {
        shouldDestroyVideo = false;
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
       // ExoPlayerVideoHandler.getInstance().goToBackground();
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shouldDestroyVideo) {
           // ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
        ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
    }

    // Get the x and y position after the button is draw on screen
// (It's important to note that we can't get the position in the onCreate(),
// because at that stage most probably the view isn't drawn yet, so it will return (0, 0))


    // The method that displays the popup.

}
enum VisibleView{
    video,
    related
}