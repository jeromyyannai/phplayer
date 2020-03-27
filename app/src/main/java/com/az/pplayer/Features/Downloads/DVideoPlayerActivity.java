package com.az.pplayer.Features.Downloads;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.az.pplayer.CommonActivity;
import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.Data.ExoPlayerVideoHandler;
import com.az.pplayer.Data.VideoLinkHolder;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.Models.VideoUrl;
import com.az.pplayer.Models.VideoUrlBind;
import com.az.pplayer.R;
import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Services.DownloadService;
import com.az.pplayer.Storage.UserStorage;
import com.az.pplayer.Views.SmallPlayerActivity;
import com.az.pplayer.Views.VideoDataAdapter;
import com.az.pplayer.Views.VideoPlayerActivity;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

public class DVideoPlayerActivity extends CommonActivity {
    private LocalVideoItem mVideoUrl;
    public static final String INTENT_EXTRA_VIDEO_URL = "VIDEO_URL";

    private SimpleExoPlayerView mSimpleExoPlayerView;
    private ImageButton mIbFullScreen;
    private  ImageButton mMenuButton;
    DrawerLayout mDrawerLayout;

    RecyclerView recyclerView;
    SwipyRefreshLayout mSwipyRefreshLayout;

    private boolean shouldDestroyVideo = true;

    private String[] Tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Intent intent = getIntent();


        mVideoUrl = new Gson().fromJson(intent.getStringExtra("url"), LocalVideoItem.class);
        mSwipyRefreshLayout = findViewById(R.id.swipyrefreshlayout);
        mSimpleExoPlayerView = findViewById(R.id.player_view);


        initializePlayer(mVideoUrl.VideoPath);
    }






    private void initializePlayer(final String mVideoUrl) {

        mSimpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        mIbFullScreen = (ImageButton) findViewById(R.id.exo_fullscreen_btn);
        ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUri(getApplicationContext(), Uri.parse(mVideoUrl), mSimpleExoPlayerView, findViewById(R.id.pBarBuffer), findViewById(R.id.container_play_pause));
        ExoPlayerVideoHandler.getInstance().goToForeground();
        SimpleExoPlayer player =(SimpleExoPlayer) mSimpleExoPlayerView.getPlayer();
        player.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        mIbFullScreen = findViewById(R.id.exo_fullscreen_btn);
        mIbFullScreen.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shouldDestroyVideo = false;
                        Intent intent = new Intent(DVideoPlayerActivity.this, SmallPlayerActivity.class);
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

