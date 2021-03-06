package com.az.pplayer.Features.Downloads;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.az.pplayer.Base.PinchView;
import com.az.pplayer.CommonActivity;
import com.az.pplayer.Constants.Url;
import com.az.pplayer.Data.DataHolder;
import com.az.pplayer.DataSource.VideoLinksSource;
import com.az.pplayer.MainActivity;
import com.az.pplayer.Menu.LeftMenu;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.VideoItem;
import com.az.pplayer.R;
import com.az.pplayer.Services.DownloadRequest;
import com.az.pplayer.Services.DownloadService;
import com.az.pplayer.Storage.DataStorage;
import com.az.pplayer.Storage.UserStorage;
import com.az.pplayer.Views.VideoDataAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.tonyodev.fetch2.AbstractFetchListener;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.FetchListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DownloadsActivity  extends CommonActivity
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

            menu = new LeftMenu(this);
            menu.SetSelected("ic_downloads");
            setupPinch();

            Video = new ArrayList<>();
            ShowVideos();

            mSwipyRefreshLayout = findViewById(R.id.swipyrefreshlayout);

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




        String prepareUrl(String url){


            if (!(url != null && !url.isEmpty())){
                url = "/video";
            }
            return Url.MainUrl +url;

        }


        void ShowVideos(){

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setNestedScrollingEnabled(false);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), UserStorage.Get().getColumns());
            recyclerView.setLayoutManager(gridLayoutManager);


            DownloadsDataAadapter dataAdapter = new DownloadsDataAadapter(this,
                    DataStorage.Get().GetDownloadedVideoList(),
                    UserStorage.Get().getFontSize());
            recyclerView.setAdapter(dataAdapter);
        }
        private void setupPinch() {
            pView = findViewById(R.id.nsView);
            mScaleGestureDetector = new ScaleGestureDetector(this, new DownloadsActivity.ScaleListener());
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
        private boolean updateGrid(DownloadsActivity.gridUpdateFactor updateFactor){
            int currentCount =  ((GridLayoutManager)recyclerView.getLayoutManager()).getSpanCount();
            if (updateFactor == DownloadsActivity.gridUpdateFactor.in){
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
                DownloadsActivity.gridUpdateFactor factor = DownloadsActivity.gridUpdateFactor.out;
                if (scaleGestureDetector.getScaleFactor()<1)
                    factor = DownloadsActivity.gridUpdateFactor.in;

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
