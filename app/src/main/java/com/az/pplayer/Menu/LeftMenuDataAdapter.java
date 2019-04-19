package com.az.pplayer.Menu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.az.pplayer.Models.MenuItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.az.pplayer.Views.VideoDataAdapter;

import java.util.List;

public class LeftMenuDataAdapter extends RecyclerView.Adapter<LeftMenuDataAdapter.MenuViewHolder>  {

    private List<MenuItem> items;
    private Context context;
    public  LeftMenuDataAdapter(Context context){
        items = LeftMenuItems.Get();
        this.context = context;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new LeftMenuDataAdapter.MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = items.get(position);
        holder.textView.setText(item.getName());
        holder.img.setImageResource(item.getDrawableId());
        holder.view.setTag(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void refresh(){
        notifyDataSetChanged();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView textView;

        View view;

        public MenuViewHolder(final View view) {
            super(view);
            img = view.findViewById(R.id.ivNavigation);
            textView = view.findViewById(R.id.tvNavigationName);
            this.view = view;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((IMenuItemClick)context).MenuItemClick(items.get((int)view.getTag()));
                }
            });

        }
    }
}
