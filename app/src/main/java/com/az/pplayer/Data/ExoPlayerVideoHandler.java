package com.az.pplayer.Data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.az.pplayer.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class ExoPlayerVideoHandler implements Player.EventListener {

    private final boolean SHOULD_CACHE_VIDEO = false;

    private static ExoPlayerVideoHandler instance;

    private SimpleExoPlayer mPlayer;
    private Uri mUri;
    private boolean isPlayerPlaying;
    private DataSource.Factory mMediaDataSourceFactory;
    private View mBufferView, mPlayPauseView;

    public static ExoPlayerVideoHandler getInstance() {
        if (instance == null) {
            instance = new ExoPlayerVideoHandler();
        }
        return instance;
    }

    private ExoPlayerVideoHandler() {
    }

    /**
     * @param context       Application Context
     * @param uri           Video file uri
     * @param exoPlayerView
     * @param bufferView    progress bar, visible on video buffer
     * @param playPauseView container of play pause button
     */
    public void prepareExoPlayerForUri(Context context, Uri uri, SimpleExoPlayerView exoPlayerView, View bufferView, View playPauseView) {
        mBufferView = bufferView;
        mPlayPauseView = playPauseView;
        if (context != null && uri != null && exoPlayerView != null) {
            if (!uri.equals(mUri) || mPlayer == null) {
                mUri = uri;
                DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
                DefaultTrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
                mPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);

                mMediaDataSourceFactory = SHOULD_CACHE_VIDEO ? new ExoPlayerVideoHandler.CacheDataSourceFactory(context, 100 * 1024 * 1024, 100 * 1024 * 1024)
                        : new DefaultDataSourceFactory(context, Util.getUserAgent(context, context.getApplicationContext().getPackageName()), bandwidthMeter);

                MediaSource mediaSource = new ExtractorMediaSource(mUri, mMediaDataSourceFactory, new DefaultExtractorsFactory(), null, null);
                mPlayer.prepare(mediaSource);
            }

            exoPlayerView.requestFocus();
            exoPlayerView.setPlayer(mPlayer);

            mPlayer.clearVideoSurface();
            mPlayer.setVideoSurfaceView((SurfaceView) exoPlayerView.getVideoSurfaceView());
            mPlayer.addListener(this);
            mPlayer.setVolume(0f);
            long currentPosition = mPlayer.getCurrentPosition();
            mPlayer.seekTo(currentPosition + 1);
            if (currentPosition == 0)
                isPlayerPlaying = true;
        }
    }

    /**
     * Releases / Destroys the Video Player
     */
    public void releaseVideoPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
        }
        mPlayer = null;
    }

    /**
     * Make video pause and puts in background
     */
    public void goToBackground() {
        if (mPlayer != null) {
            isPlayerPlaying = mPlayer.getPlayWhenReady();
            mPlayer.setPlayWhenReady(false);
        }
    }

    /**
     * Resumes or Plays video
     */
    public void goToForeground() {
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(isPlayerPlaying);
        }
    }



    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        mBufferView.setVisibility(playbackState == Player.STATE_BUFFERING ? View.VISIBLE : View.GONE);
        // mPlayPauseView.setVisibility(playbackState == Player.STATE_BUFFERING ? View.GONE : View.VISIBLE);

        if (playbackState == Player.STATE_ENDED) {
            mPlayer.seekToDefaultPosition();
            mPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d("PLAYER", "onPlayerError " + error.getCause());
    }



    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    class CacheDataSourceFactory implements DataSource.Factory {
        private final Context mContext;
        private final DefaultDataSourceFactory mDefaultDataSourceFactory;
        private final long maxFileSize, maxCacheSize;

        CacheDataSourceFactory(Context context, long maxCacheSize, long maxFileSize) {
            super();
            this.mContext = context;
            this.maxCacheSize = maxCacheSize;
            this.maxFileSize = maxFileSize;
            String userAgent = Util.getUserAgent(context, context.getString(R.string.app_name));
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            mDefaultDataSourceFactory = new DefaultDataSourceFactory(this.mContext,
                    bandwidthMeter,
                    new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter));
        }

        @Override
        public DataSource createDataSource() {
            LeastRecentlyUsedCacheEvictor evictor = new LeastRecentlyUsedCacheEvictor(maxCacheSize);
            SimpleCache simpleCache = new SimpleCache(new File(mContext.getCacheDir(), "media"), evictor);
            return new CacheDataSource(simpleCache, mDefaultDataSourceFactory.createDataSource(),
                    new FileDataSource(), new CacheDataSink(simpleCache, maxFileSize),
                    CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
        }
    }
}
