package com.az.pplayer.Menu;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.az.pplayer.Data.CategoryHolder;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;
import com.az.pplayer.Views.CategoryDataAdapter;

public class LeftMenu {
    private Activity context;
    private RecyclerView recyclerView;
    public LeftMenu (Activity activity)
    {
        this.context = activity;
        recyclerView = (RecyclerView) context.findViewById(R.id.left_menu_container);

        LeftMenuDataAdapter dataAdapter = new LeftMenuDataAdapter(context.getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        recyclerView.setAdapter(dataAdapter);
    }
}
