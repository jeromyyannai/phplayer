package com.az.pplayer.Views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.az.pplayer.Data.CategoryHolder;
import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.DataSource.CategorySource;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewActivity  extends AppCompatActivity {
    List<CategoryItem> Categories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Categories = new ArrayList<>();
        if (CategoryHolder.Size()==0) {
            LoadSite();
        } else {
            ShowCategories();
        }

    }

    void LoadSite(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                CategoryHolder.Save(CategorySource.GetCategories(""));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //imageView = (ImageView) findViewById(R.id.imageView);
                        ShowCategories();
                    }
                });
            }
        }).start();
    }

    void ShowCategories(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int columns = width / 200;
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), columns);
        recyclerView.setLayoutManager(gridLayoutManager);


        CategoryDataAdapter dataAdapter = new CategoryDataAdapter(getApplicationContext(), CategoryHolder.Get());
        recyclerView.setAdapter(dataAdapter);
    }
}
