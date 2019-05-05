package com.az.pplayer.Views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.az.pplayer.MainActivity;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.CategoryStorageItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.Models.VideoUrl;
import com.az.pplayer.Models.VideoUrlBind;
import com.az.pplayer.R;
import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Services.DownloadService;
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
        NavigationView  nv = (NavigationView)findViewById(R.id.video_nav_view);
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
                        DownloadService.Get().Download(new DownloadRequest(VideoLinkHolder.GetDownloadUrl(mVideoUrl.Video),
                                mVideoUrl.Title,Tags,mVideoUrl.Image,mVideoUrl.Preview));
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
                String defaultUrl ="";

    try {
        Document doc = Jsoup.connect("https://pornhub.com" + _url).timeout(0).get();
        Element script = doc.select("#player").select("script").first();
        Elements categories = doc.select(".video-detailed-info .categoriesWrapper a");
        List<String> tagList = new ArrayList<>();
        for (int i=0;i<categories.size()-1;i++){
            if (categories.get(i).text() != null &&categories.get(i).text().length()>0 && categories.get(i).text().substring(0,1) != "+")
            tagList.add( categories.get(i).text());
        }
        Tags = tagList.toArray(new String[0]);
        String rawHtml = script.html().substring(0, 6553);
        String videoDataPart = rawHtml.substring(rawHtml.indexOf("mediaDefinitions")+18);
        String videoData = videoDataPart.substring(0,videoDataPart.indexOf("}]")+2);
        Type listType = new TypeToken<ArrayList<VideoUrlBind>>(){}.getType();
        List<VideoUrlBind> videoUrls = new Gson().fromJson(videoData,listType);
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

    } catch (IOException e) {
        e.printStackTrace();
    }

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