package com.az.pplayer.Menu;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.az.pplayer.Data.CategoryHolder;
import com.az.pplayer.Models.MenuItem;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;
import com.az.pplayer.Views.CategoryDataAdapter;

public class LeftMenu implements IMenuItemClick{
    private Activity context;
    private RecyclerView recyclerView;
    public LeftMenu (Activity activity)
    {
        this.context = activity;
        recyclerView = (RecyclerView) context.findViewById(R.id.left_menu_container);

        LeftMenuDataAdapter dataAdapter = new LeftMenuDataAdapter(context.getApplicationContext(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(dataAdapter);
    }

    @Override
    public void MenuItemClick(MenuItem item) {
        switch (item.Id){
            case "ic_search":

                break;
        }
    }
}
