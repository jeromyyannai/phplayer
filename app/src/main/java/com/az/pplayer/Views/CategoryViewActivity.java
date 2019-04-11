package com.az.pplayer.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.az.pplayer.Base.PinchView;
import com.az.pplayer.Data.CategoryHolder;
import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.DataSource.CategorySource;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewActivity  extends AppCompatActivity implements PinchView.IOnTouchListener,NavigationView.OnNavigationItemSelectedListener {
    List<CategoryItem> Categories;
    PinchView pView;
    private ScaleGestureDetector mScaleGestureDetector;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

        CategoryDataAdapter dataAdapter = new CategoryDataAdapter(getApplicationContext(), CategoryHolder.Get(),UserStorage.Get().getFontSize());
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search) {
            // Handle the camera action
        } else if (id == R.id.nav_favorites) {

        } else if (id == R.id.nav_categories) {

        } else if (id == R.id.nav_download) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
