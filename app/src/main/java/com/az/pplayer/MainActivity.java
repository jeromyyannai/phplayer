package com.az.pplayer;

import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.az.pplayer.Base.PinchView;
import com.az.pplayer.Constants.Url;
import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.Menu.LeftMenu;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.Storage.UserStorage;
import com.az.pplayer.Views.CategoryDataAdapter;
import com.az.pplayer.Views.VideoDataAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.az.pplayer.Menu.LeftMenu.SETTINGS_CHANGED;

public class MainActivity extends CommonActivity
        implements  PinchView.IOnTouchListener {

    PinchView pView;
    private ScaleGestureDetector mScaleGestureDetector;
    RecyclerView recyclerView;
    SwipyRefreshLayout mSwipyRefreshLayout;
    private CategoryItem requestUrl;
    List<VideoItem> Video;
    LeftMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Intent intent = getIntent();
        requestUrl = new Gson().fromJson(intent.getStringExtra("url"), CategoryItem.class);
        final String catUrl  = prepareUrl(requestUrl.Link);
        menu = new LeftMenu(this);
        menu.InsertCategory(requestUrl);
        menu.SetSelected(requestUrl.Link);
        setupPinch();

        Video = new ArrayList<>();
        if (DataHolder.Size(catUrl)==0) {
            LoadSite(DataHolder.Get(catUrl).FullUrl(),SwipyRefreshLayoutDirection.BOTH);
        } else {
            ShowVideos(catUrl);
        }
        mSwipyRefreshLayout = findViewById(R.id.swipyrefreshlayout);
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d("MainActivity", "Refresh triggered at "
                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                boolean result = false;
                if (direction == SwipyRefreshLayoutDirection.TOP) {
                    result = DataHolder.Get(catUrl).DownPageNumber();
                } else if (direction == SwipyRefreshLayoutDirection.BOTTOM) {
                    result = DataHolder.Get(catUrl).UpPageNumber();
                }

                if (result)
                    LoadSite(DataHolder.Get(catUrl).FullUrl(),direction);
                else
                    mSwipyRefreshLayout.setRefreshing(false);

            }
        });
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == SETTINGS_CHANGED) {
            UserStorage.Get().UpdateConfiguration();
            ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount( UserStorage.Get().getColumns());
            ((VideoDataAdapter)recyclerView.getAdapter()).updateTextSize( UserStorage.Get().getFontSize());

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    String prepareUrl(String url){


        if (!(url != null && !url.isEmpty())){
            url = "/video";
        }
        return Url.MainUrl +url;

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
        mScaleGestureDetector = new ScaleGestureDetector(this, new MainActivity.ScaleListener());
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
    private boolean updateGrid(MainActivity.gridUpdateFactor updateFactor){
        int currentCount =  ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
        if (updateFactor == MainActivity.gridUpdateFactor.in){
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



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            if (scaleGestureDetector.getScaleFactor() ==1)
                return;
            MainActivity.gridUpdateFactor factor = MainActivity.gridUpdateFactor.out;
            if (scaleGestureDetector.getScaleFactor()<1)
                factor = MainActivity.gridUpdateFactor.in;

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
