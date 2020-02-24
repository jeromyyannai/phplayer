package com.az.pplayer.Menu;

import android.content.Context;
import android.graphics.Color;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.MenuItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.az.pplayer.Views.VideoDataAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LeftMenuDataAdapter extends RecyclerView.Adapter<LeftMenuDataAdapter.MenuViewHolder>  {

    private List<MenuItem> items;
    private Context context;
    IMenuItemClick clickHandler;
    public  LeftMenuDataAdapter(Context context,IMenuItemClick clickHandler){
        items = LeftMenuItems.Get().getItems();
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new LeftMenuDataAdapter.MenuViewHolder(view);
        } else if (viewType ==2){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
            return new LeftMenuDataAdapter.MenuViewHolder(view);

        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_menu_item, parent, false);
        return new LeftMenuDataAdapter.MenuViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        MenuItem item = items.get(position);
        if (item.Type == MenuItem.MenuType.Link)
            return  1;
        else if (item.Type == MenuItem.MenuType.SwitchOption)
            return 2;
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = items.get(position);
        holder.textView.setText(item.getName());
        holder.img.setImageResource(item.getDrawableId());
        holder.view.setTag(position);
        if (item.isSelected()){
            holder.textView.setTextColor(Color.WHITE);
        }

    }
    private int getDp(int size){

        final float scale = context.getResources().getDisplayMetrics().density;
        return  (int) (size * scale + 0.5f);
    }
    private int getSp(int size){

        final float scale = context.getResources().getDisplayMetrics().density;
        return  (int) (size * scale );
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void refresh(){
        notifyDataSetChanged();
    }
    public void InsertCategory(CategoryItem item){
            LeftMenuItems.Get().InsertCategory(item);
            refresh();
    }
    public void SetSelected(String id){
        LeftMenuItems.Get().SetSelected(id);
        refresh();
    }
    public class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView textView;
        LinearLayout menuLayout;
        RelativeLayout menuItemLayout;
        View view;

        public MenuViewHolder(final View view) {
            super(view);
            img = view.findViewById(R.id.ivNavigation);
            textView = view.findViewById(R.id.tvNavigationName);
            menuLayout = view.findViewById(R.id.menuLayout);
            this.view = view;
            this.menuItemLayout = view.findViewById(R.id.menuItemLayout);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickHandler.MenuItemClick(items.get((int)view.getTag()));
                }
            });

        }
    }
}
