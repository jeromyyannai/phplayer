package com.az.pplayer.Views;

import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.az.pplayer.Base.PinchView;
import com.az.pplayer.Data.CategoryHolder;
import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.DataSource.CategorySource;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.Menu.LeftMenu;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryViewActivity  extends AppCompatActivity implements PinchView.IOnTouchListener {
    List<CategoryItem> Categories;
    PinchView pView;
    private ScaleGestureDetector mScaleGestureDetector;
    RecyclerView recyclerView;
    LeftMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        menu = new LeftMenu(this);
        menu.SetSelected("ic_categories");
        setupPinch();
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
         recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), UserStorage.Get().getColumns());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        CategoryDataAdapter dataAdapter = new CategoryDataAdapter(this, CategoryHolder.Get(),UserStorage.Get().getFontSize());
        recyclerView.setAdapter(dataAdapter);
    }


    private void setupPinch() {
        pView = findViewById(R.id.nsView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
        pView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouchDetected(MotionEvent ev) {
        return  onTouchEvent(ev);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }
    private boolean updateGrid(gridUpdateFactor updateFactor){
        int currentCount =  ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
        if (updateFactor == gridUpdateFactor.in){
            if  (currentCount==10)
                return  false;
            ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(currentCount+1);
            ((CategoryDataAdapter)recyclerView.getAdapter()).setTextSizes(-2);
        } else {
            if  (currentCount==1)
                return  false;
            ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(currentCount-1);
            ((CategoryDataAdapter)recyclerView.getAdapter()).setTextSizes(+2);
        }
        UserStorage.Get().setColumns(((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount());
        UserStorage.Get().setFontSize(((CategoryDataAdapter)recyclerView.getAdapter()).getTextSize());
        return true;
    }




    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            if (scaleGestureDetector.getScaleFactor() ==1)
                return;
            gridUpdateFactor factor = gridUpdateFactor.out;
            if (scaleGestureDetector.getScaleFactor()<1)
                factor = gridUpdateFactor.in;

//            if (scaleGestureDetector.getScaleFactor()>  1){
//                mScaleFactor+=0.01;
//            }else {
//                mScaleFactor-=0.01;
//            }

            updateGrid(factor);
        }
    }

    enum gridUpdateFactor{
        in, out

    }
}
