package com.az.pplayer.Features.Downloads;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Services.DownloadService;
import com.az.pplayer.Storage.UserStorage;
import com.az.pplayer.Views.VideoDataAdapter;
import com.az.pplayer.Views.VideoPlayerActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.tonyodev.fetch2.AbstractFetchListener;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.FetchListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class DownloadsDataAadapter extends RecyclerView.Adapter<DownloadsDataAadapter.ViewHolder> {
    private List<LocalVideoItem> videoItems;
    private Context context;
    private int textSize;
    private  int screenWidth;

    public DownloadsDataAadapter(Context context, List<LocalVideoItem> videoItems, int textSize) {
        this.context = context;
        this.videoItems = videoItems;
        this.textSize = textSize;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        DownloadService.Get().addFetchListener(fetchListener);

    }

    @Override
    public DownloadsDataAadapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.local_video_item, viewGroup, false);
        return new DownloadsDataAadapter.ViewHolder(view);
    }

    /**
     * gets the image url from adapter and passes to Glide API to load the image
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(final DownloadsDataAadapter.ViewHolder viewHolder, int i) {

        int colCount = UserStorage.Get().getColumns();
        int cellWith = screenWidth/colCount;
        int cellHeight= cellWith*131/233;
        viewHolder.img.getLayoutParams().height = cellHeight;

        Glide.with(context).load(videoItems.get(i).ImagePath)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        viewHolder.img.getLayoutParams().height= ViewGroup.LayoutParams.WRAP_CONTENT;
                        return false;
                    }
                })
                .into(viewHolder.img);

        viewHolder.textView.setText(videoItems.get(i).Name);
        viewHolder.item = videoItems.get(i);
        viewHolder.textView.setTextSize(textSize);
        if (!viewHolder.item.IsDownloaded && viewHolder.item.Request != null){

            viewHolder.opacityProgress.setProgress(viewHolder.item.Request.PercentCompleted);
            viewHolder.opacityProgress.setVisibility(View.VISIBLE);
        } else {
            viewHolder.opacityProgress.setVisibility(View.GONE);
        }
    }


    public int getTextSize(){
        return textSize;
    }
    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    private FetchListener fetchListener = new AbstractFetchListener() {


        @Override
        public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {
            UserStorage.Get().RemoveDownloadRequest(download.getId());
            super.onError(download, error, throwable);
        }

        @Override
        public void onCompleted(@NotNull Download download) {
            super.onCompleted(download);
            LocalVideoItem item = GetRequest(download.getId());
            if (item == null)
                return;
            item.IsDownloaded = true;
            item.Request.PercentCompleted = 100;
            notifyItemChanged(videoItems.indexOf(item));
        }

        @Override
        public void onProgress(@NotNull Download download, long etaInMilliSeconds, long downloadedBytesPerSecond) {
            super.onProgress(download, etaInMilliSeconds, downloadedBytesPerSecond);
            LocalVideoItem item = GetRequest(download.getId());
            if (item == null)
                return;

            if (download.getTotal() != 0) {
                item.Request.PercentCompleted = (int) (download.getDownloaded() * 100 / download.getTotal());
                notifyItemChanged(videoItems.indexOf(item));
            }

        }

        private LocalVideoItem GetRequest(int id){
            for (int i=0;i<videoItems.size();i++){
                if (videoItems.get(i).Request == null)
                    continue;
                if (id ==videoItems.get(i).Request.Id){
                    return  videoItems.get(i);
                }
            }
            return null;
        }

    };


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        DownloadsDataAadapter.ViewHolder v;
        public MyGestureListener(DownloadsDataAadapter.ViewHolder v)
        {
            this.v = v;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (!v.item.IsDownloaded)
                return  super.onDoubleTap(e);

            Intent intent = new Intent(v.view.getContext(), DVideoPlayerActivity.class);
            intent.putExtra("url",new Gson().toJson(v.item));

            v.view.getContext().startActivity(intent);
            return super.onDoubleTap(e);
        }




        @Override
        public void onLongPress(MotionEvent e) {


        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            v.surfaceView.setVideoURI(Uri.parse(v.item.PreviewPath));
            v.surfaceView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(0,0);

                }
            });
            v.img.setVisibility(View.GONE);
            v.surfaceView.setVisibility(View.VISIBLE);
            v.surfaceView.start();


            v.surfaceView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    v.img.setVisibility(View.VISIBLE);
                    v.surfaceView.setVisibility(View.GONE);
                }
            });
            return super.onSingleTapConfirmed(e);
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView textView;
        LocalVideoItem item;
        VideoView surfaceView;
        View view;
        ProgressBar opacityProgress;
        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageView);
            textView = view.findViewById(R.id.textView);
            surfaceView = view.findViewById(R.id.videoView);
            this.view = view;
            opacityProgress = view.findViewById(R.id.opacityFilter);

            final GestureDetector mDetector = new GestureDetector(view.getContext(), new DownloadsDataAadapter.MyGestureListener(this));
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mDetector.onTouchEvent(event);

                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    AlertDialog dialog =  new AlertDialog.Builder(view.getContext())
                                .setTitle("Remove video")
                                .setMessage("Remove " + item.Name + "?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        UserStorage.Get().RemoveDownloadedVideo(item);
                                        //videoItems.remove(v.item);
                                        notifyDataSetChanged();
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                                try {
                                                    new File(item.VideoPath).delete();
                                                    new File(item.ImagePath).delete();
                                                    new File(item.PreviewPath).delete();

                                                } catch (Exception ex) {

                                                }
                                            }
                                        }).run();

                                    }
                                })
                                .setNegativeButton(android.R.string.no, null).show();
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                    //super.onLongPress(e);
                    return false;
                }
            });
        }
    }
}