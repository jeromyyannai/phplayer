package com.az.pplayer.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.az.pplayer.DataSource.CategorySource;
import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.CategoryStorageItem;
import com.az.pplayer.PhpPlayerApp;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UserStorage {
    private static  UserStorage ourInstance;;

    public static UserStorage Get() {
        if (ourInstance == null)
            ourInstance = new UserStorage(PhpPlayerApp.getAppContext());
        return ourInstance;
    }
    SharedPreferences preferences;
    private UserStorage(Context context) {
        preferences = context.getSharedPreferences("phplayer", MODE_PRIVATE);
        resolution = preferences.getString("resolution","480");
       String rescentCatsstr = preferences.getString("rescentCats","");
        Type listType = new TypeToken<ArrayList<CategoryStorageItem>>(){}.getType();
        rescentCats = new Gson().fromJson(rescentCatsstr, listType);
        if (java.util.Arrays.asList(resolutions).indexOf(resolution) ==-1)
            resolution = "480";
    }

    private String resolution = "480";
    private String[] resolutions ={"240","480","720","1080"};
    private List<CategoryStorageItem> rescentCats;
    private int maxRescentcats = 7;
    public String getResolution(){
        return  resolution;
    }
    public  void  setResolution(String  res){
        if (java.util.Arrays.asList(resolutions).indexOf(resolution) >-1){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("resolution", res);
            editor.commit();
            resolution = res;
        }
    }

    public List<CategoryStorageItem> getRescentCats(){
        return rescentCats;
    }
    public void AddCategory(CategoryItem item){
        rescentCats.add(new CategoryStorageItem(item));
        Collections.sort(rescentCats, Collections.reverseOrder( new Comparator<CategoryStorageItem>() {
            @Override
            public int compare(CategoryStorageItem item1, CategoryStorageItem item2) {
                return item1.date.compareTo(item2.date);
            }
        }));
        while (rescentCats.size()>maxRescentcats)
            rescentCats.remove(rescentCats.size()-1);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("rescentCats", new Gson().toJson(rescentCats));
        editor.commit();
    }
}
