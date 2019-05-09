package com.az.pplayer.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.az.pplayer.Base.PinchView;
import com.az.pplayer.Constants.Url;
import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.Menu.LeftMenu;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;
import com.google.gson.Gson;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements  PinchView.IOnTouchListener, MaterialSearchBar.OnSearchActionListener, SwipyRefreshLayout.OnRefreshListener {

    PinchView pView;
    private ScaleGestureDetector mScaleGestureDetector;
    RecyclerView recyclerView;
    SwipyRefreshLayout mSwipyRefreshLayout;
    private CategoryItem requestUrl;
    List<VideoItem> Video;
    LeftMenu menu;
    MaterialSearchBar searchBar;

    private String searchPattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //video/search?search=big+toys&o=mr
        searchBar = findViewById(R.id.searchBar);
        searchBar.setOnSearchActionListener(this);
      searchBar.disableSearch();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = new LeftMenu(this);
        menu.SetSelected("ic_search");

        setupPinch();

        Video = new ArrayList<>();

        search();

        mSwipyRefreshLayout = findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        searchBar.setPlaceHolder(text.toString());
        searchBar.disableSearch();
       searchPattern = text.toString();
       search();

    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }

    void search(){
        if (searchPattern !=null){

            if (DataHolder.Size(prepareUrl())==0) {
                LoadSite(DataHolder.Get(prepareUrl()).FullUrl(),SwipyRefreshLayoutDirection.BOTH);
            } else {
                ShowVideos(prepareUrl());
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }




    String prepareUrl(){
        if (searchPattern==null || searchPattern=="")
            return Url.MainUrl;
        return Url.MainUrl +"/video/search?search="+searchPattern.replace(' ', '+')+"&o=mr";



    }

    void LoadSite(final String  catUrl, final SwipyRefreshLayoutDirection direction){
        new Thread(new Runnable() {
            @Override
            public void run() {
                DataHolder.Save(catUrl,VideoLinksSource.ParseLinks(catUrl));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipyRefreshLayout.setRefreshing(false);
                        //imageView = (ImageView) findViewById(R.id.imageView);
                        if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                            // pView.scrollTo(0, pView.getBottom());

                            pView.post(new Runnable() {
                                public void run() {
                                    pView.fullScroll(pView.FOCUS_UP);
                                }
                            });
                        }
                        else if (direction == SwipyRefreshLayoutDirection.TOP) {
                            // pView.scrollTo(pView.getBottom(), );
                            pView.post(new Runnable() {
                                public void run() {
                                    pView.fullScroll(pView.FOCUS_DOWN);
                                }
                            });
                        }
                        ShowVideos(catUrl);

                    }
                });
            }
        }).start();
    }

    void ShowVideos(String catUrl){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), UserStorage.Get().getColumns());
        recyclerView.setLayoutManager(gridLayoutManager);


        VideoDataAdapter dataAdapter = new VideoDataAdapter(this,
                DataHolder.Get(catUrl).CurrentVideo(),
                UserStorage.Get().getFontSize());
        recyclerView.setAdapter(dataAdapter);
    }
    private void setupPinch() {
        pView = findViewById(R.id.nsView);
        mScaleGestureDetector = new ScaleGestureDetector(this, new SearchActivity.ScaleListener());
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
    private boolean updateGrid(SearchActivity.gridUpdateFactor updateFactor){
        int currentCount =  ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
        if (updateFactor == SearchActivity.gridUpdateFactor.in){
            if  (currentCount==10)
                return  false;
            ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(currentCount+1);
            ((VideoDataAdapter)recyclerView.getAdapter()).setTextSizes(-2);
        } else {
            if  (currentCount==1)
                return  false;
            ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(currentCount-1);
            ((VideoDataAdapter)recyclerView.getAdapter()).setTextSizes(+2);
        }
        UserStorage.Get().setColumns(((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount());
        UserStorage.Get().setFontSize(((VideoDataAdapter)recyclerView.getAdapter()).getTextSize());
        return true;
    }

    @Override
    public void onRefresh(SwipyRefreshLayoutDirection direction) {
        Log.d("MainActivity", "Refresh triggered at "
                + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
        boolean result = false;
        if (direction == SwipyRefreshLayoutDirection.TOP) {
            result = DataHolder.Get(prepareUrl()).DownPageNumber();
        } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
            result = DataHolder.Get(prepareUrl()).UpPageNumber();
        }

        if (result) {
            LoadSite(DataHolder.Get(prepareUrl()).FullUrl(),direction);
        }
        else
            mSwipyRefreshLayout.setRefreshing(false);
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            if (scaleGestureDetector.getScaleFactor() ==1)
                return;
            SearchActivity.gridUpdateFactor factor = SearchActivity.gridUpdateFactor.out;
            if (scaleGestureDetector.getScaleFactor()<1)
                factor = SearchActivity.gridUpdateFactor.in;

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
