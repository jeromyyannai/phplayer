package com.az.pplayer.Views;

import android.content.Context;
import android.content.Intent;
import android.gesture.GestureOverlayView;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class VideoDataAdapter  extends RecyclerView.Adapter<VideoDataAdapter.ViewHolder> {
    private List<VideoItem> videoItems;
    private Context context;
    private int textSize;

    public VideoDataAdapter(Context context, List<VideoItem> videoItems, int textSize) {
        this.context = context;
        this.videoItems = videoItems;
        this.textSize = textSize;
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
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Glide.with(context).load(videoItems.get(i).Image).into(viewHolder.img);
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
            intent.putExtra("url", v.item.Video);
            v.view.getContext().startActivity(intent);
            return super.onDoubleTap(e);
        }



        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            v.surfaceView.setVideoURI(Uri.parse(v.item.Preview));
            v.surfaceView.start();
            v.img.setVisibility(View.GONE);
            v.surfaceView.setVisibility(View.VISIBLE);

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
        }
    }
}