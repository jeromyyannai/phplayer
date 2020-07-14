package com.az.pplayer.Menu;

import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.CategoryStorageItem;
import com.az.pplayer.Models.MenuItem;
import com.az.pplayer.R;
import com.az.pplayer.Storage.UserStorage;

import java.util.ArrayList;
import java.util.List;

public class LeftMenuItems {
    private   List<MenuItem> items;
    private  LeftMenuDataAdapter adapter;
    private  static LeftMenuItems instance;
    private  LeftMenuItems(){
        items = new ArrayList<>();
        items.add(new MenuItem("Search", R.drawable.ic_search, "ic_search",false));
        items.add(new MenuItem("Categories", R.drawable.ic_video_library, "ic_categories",true));
        items.add(new MenuItem("Favorites", R.drawable.ic_star, "ic_faworites",false));
        items.add(new MenuItem("Downloads", R.drawable.ic_file_download, "ic_downloads",false));
       List<CategoryStorageItem> cats =  UserStorage.Get().getRescentCats();
        for (CategoryItem item: cats){
            InsertCategory(new CategoryItem(item.Title,item.Link,item.Image, item.Id));
        }
    }

    public List<MenuItem> getItems() {
        return items;
    }



    public static LeftMenuItems Get(){
        if (instance == null) {
            instance = new LeftMenuItems();

        }
        return instance;

    }
    public  void SetSelected(String id){
        for (MenuItem i : items){
            if (i.Id.equals(id)){
              i.setSelected(true);
            }
            else {
                i.setSelected(false);
            }
        }
    }

    public List<MenuItem> GetItems(){
        return items;
    }
    public  void InsertCategory(CategoryItem item){
        MenuItem menuItem = new MenuItem(item.Title,null,item.Link, MenuItem.MenuType.Link,item.Link);
        int Index = 0;
        for (MenuItem i : items){
            if (i.Id.equals(menuItem.Id)){
                return;
            }
        }
        for (MenuItem i : items){
            if (i.Id == "ic_categories"){
                Index = items.indexOf(i);
                break;
            }
        }
        items.add(Index+1,menuItem);
    }
}

