package com.az.pplayer.Menu;

import android.app.Activity;
import android.content.Intent;


import com.az.pplayer.Data.CategoryHolder;
import com.az.pplayer.Features.Downloads.DownloadsActivity;
import com.az.pplayer.MainActivity;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.MenuItem;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;
import com.az.pplayer.Views.CategoryDataAdapter;
import com.az.pplayer.Views.CategoryViewActivity;
import com.az.pplayer.Views.SearchActivity;
import com.google.gson.Gson;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LeftMenu implements IMenuItemClick{
    private Activity context;
    private RecyclerView recyclerView;
    LeftMenuDataAdapter dataAdapter;


    public LeftMenu (Activity activity)
    {
        this.context = activity;
        recyclerView = (RecyclerView) context.findViewById(R.id.left_menu_container);

         dataAdapter = new LeftMenuDataAdapter(context.getApplicationContext(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(dataAdapter);
    }

    @Override
    public void MenuItemClick(MenuItem item) {
        SetSelected(item.Id);
        if (item.Type == MenuItem.MenuType.Link){
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("url",new Gson().toJson(new CategoryItem(item.getName(), item.Link,null)));

            context.startActivity(intent);
            return;
        }
        switch (item.Id){
            case "ic_categories":

               context.startActivity(new Intent(context, CategoryViewActivity.class));
                break;
            case "ic_search":
                 context.startActivity(new Intent(context, SearchActivity.class));
                break;
            case "ic_downloads":
                context.startActivity(new Intent(context, DownloadsActivity.class));
                break;

        }
    }

    public void InsertCategory(CategoryItem item){
        dataAdapter.InsertCategory(item);

    }
    public void SetSelected(String id){
        dataAdapter.SetSelected(id);

    }

}
