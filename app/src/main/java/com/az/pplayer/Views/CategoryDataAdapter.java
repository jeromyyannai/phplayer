package com.az.pplayer.Views;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.az.pplayer.MainActivity;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryDataAdapter extends RecyclerView.Adapter<CategoryDataAdapter.ViewHolder> {
    private List<CategoryItem> categories;
    private Context context;
    private int textSize;

    public CategoryDataAdapter(Context context, List<CategoryItem> categories, int textSize) {
        this.context = context;
        this.categories = categories;
        this.textSize = textSize;
    }

    @Override
    public CategoryDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.video_item, viewGroup, false);
        return new CategoryDataAdapter.ViewHolder(view);
    }

    /**
     * gets the image url from adapter and passes to Glide API to load the image
     *
     * @param viewHolder
     * @param i
     */
    @Override
    public void onBindViewHolder(CategoryDataAdapter.ViewHolder viewHolder, int i) {
        Glide.with(context).load(categories.get(i).Image).into(viewHolder.img);
        viewHolder.textView.setText(categories.get(i).Title);
        viewHolder.item = categories.get(i);
        viewHolder.textView.setTextSize(textSize);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        CategoryDataAdapter.ViewHolder v;
        public MyGestureListener(CategoryDataAdapter.ViewHolder v)
        {
            this.v = v;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }



        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Intent intent = new Intent(v.view.getContext(),MainActivity.class);
            intent.putExtra("url", v.item.Link);
            v.view.getContext().startActivity(intent);
            return super.onSingleTapConfirmed(e);
        }
    }

    public void setTextSizes(int textSize) {
        this.textSize += textSize;
        notifyDataSetChanged();
    }
    public int getTextSize(){
        return textSize;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView textView;
        CategoryItem item;
        View view;

        public ViewHolder(View view) {
            super(view);
            img = view.findViewById(R.id.imageView);
            textView = view.findViewById(R.id.textView);
            this.view = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            final GestureDetector mDetector = new GestureDetector(view.getContext(), new CategoryDataAdapter.MyGestureListener(this));
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mDetector.onTouchEvent(event);

                }
            });
        }
    }
}