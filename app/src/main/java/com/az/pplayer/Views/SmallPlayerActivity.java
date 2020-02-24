package com.az.pplayer.Views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;

import com.az.pplayer.Data.ExoPlayerVideoHandler;
import com.az.pplayer.R;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SmallPlayerActivity extends AppCompatActivity {
    public static final String INTENT_EXTRA_VIDEO_URL = "VIDEO_URL";
    private ImageButton mIbFullScreen;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private boolean shouldDestroyVideo = true;


    private String mVideoUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        mVideoUrl = getIntent().getStringExtra(INTENT_EXTRA_VIDEO_URL);
//        Fragment fragment = new VideoPlayerFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putString(VideoPlayerFragment.BUNDLE_KEY_VIDEO_URL, mVideoUrl);
//        fragment.setArguments(bundle);
//
//        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
        mIbFullScreen = findViewById(R.id.exo_fullscreen_btn);

    }


    private void initializePlayer() {
        mSimpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        mIbFullScreen = (ImageButton) findViewById(R.id.exo_fullscreen_btn);

        ExoPlayerVideoHandler.getInstance().prepareExoPlayerForUri(getApplicationContext(), Uri.parse(mVideoUrl), mSimpleExoPlayerView, findViewById(R.id.pBarBuffer), findViewById(R.id.container_play_pause));
        ExoPlayerVideoHandler.getInstance().goToForeground();
        mIbFullScreen = findViewById(R.id.exo_fullscreen_btn);
        mIbFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SmallPlayerActivity.this, VideoPlayerActivity.class);
                intent.putExtra(VideoPlayerActivity.INTENT_EXTRA_VIDEO_URL, mVideoUrl);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        initializePlayer();
    }

    @Override
    public void onBackPressed() {
        shouldDestroyVideo = false;
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ExoPlayerVideoHandler.getInstance().goToBackground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (shouldDestroyVideo) {
            ExoPlayerVideoHandler.getInstance().releaseVideoPlayer();
        }
    }
}
