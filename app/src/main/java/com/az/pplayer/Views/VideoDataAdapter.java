package com.az.pplayer.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureOverlayView;
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
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.az.pplayer.Data.VideoLinkHolder;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Services.DownloadService;
import com.az.pplayer.Storage.UserStorage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VideoDataAdapter  extends RecyclerView.Adapter<VideoDataAdapter.ViewHolder> {
    private List<VideoItem> videoItems;
    private Context context;
    private int textSize;
    private  int screenWidth;

    public VideoDataAdapter(Context context, List<VideoItem> videoItems, int textSize) {
        this.context = context;
        this.videoItems = videoItems;
        this.textSize = textSize;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         screenWidth = displayMetrics.widthPixels;
    }

    @Override
    public VideoDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_item, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * gets the image url from adapter and passes to Glide API to load the image
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        int colCount = UserStorage.Get().getColumns();
        int cellWith = screenWidth/colCount;
        int cellHeight= cellWith*131/233;
        viewHolder.img.getLayoutParams().height = cellHeight;

        Glide.with(context).load(videoItems.get(i).Image)
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

        viewHolder.textView.setText(videoItems.get(i).Title);
        viewHolder.item = videoItems.get(i);
        viewHolder.textView.setTextSize(textSize);
    }

    public void setTextSizes(int textSize) {
        this.textSize += textSize;
        notifyDataSetChanged();
    }
    public int getTextSize(){
        return textSize;
    }
    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        ViewHolder v;
        public MyGestureListener(ViewHolder v)
        {
            this.v = v;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Intent intent = new Intent(v.view.getContext(),VideoPlayerActivity.class);
            intent.putExtra("url",new Gson().toJson(v.item));

            v.view.getContext().startActivity(intent);
            return super.onDoubleTap(e);
        }



        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            v.surfaceView.setVideoURI(Uri.parse(v.item.Preview));
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
        VideoItem item;
        VideoView surfaceView;
        View view;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageView);
            textView = view.findViewById(R.id.textView);
            surfaceView = view.findViewById(R.id.videoView);
            this.view = view;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            final GestureDetector mDetector = new GestureDetector(view.getContext(), new VideoDataAdapter.MyGestureListener(this));
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
                            .setTitle("Download this video")
                            .setMessage("Download " + item.Title + "?")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    DownloadService.Get().Download(item);

                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);
                    return false;
                }
            });
        }
    }
}