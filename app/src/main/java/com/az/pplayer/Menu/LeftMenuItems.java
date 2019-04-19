package com.az.pplayer.Menu;

import com.az.pplayer.Models.MenuItem;
import com.az.pplayer.R;

import java.util.ArrayList;
import java.util.List;

public class LeftMenuItems {
    public static List<MenuItem> Get(){
        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem("Search", R.drawable.ic_search,"ic_search"));
        items.add(new MenuItem("Categories", R.drawable.ic_video_library,"ic_categories"));
        items.add(new MenuItem("Favorites", R.drawable.ic_star,"ic_faworites"));
        items.add(new MenuItem("Downloads", R.drawable.ic_file_download,"ic_downloads"));

        return items;


    }
}

