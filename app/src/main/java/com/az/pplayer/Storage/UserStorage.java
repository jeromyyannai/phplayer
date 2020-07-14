package com.az.pplayer.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.az.pplayer.Models.CategoryItem;
import com.az.pplayer.Models.CategoryStorageItem;
import com.az.pplayer.PhpPlayerApp;
import com.az.pplayer.Features.Downloads.LocalVideoItem;
import com.az.pplayer.Services.DownloadRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class UserStorage {
    private static  UserStorage ourInstance;;

    public static UserStorage Get() {
        if (ourInstance == null)
            ourInstance = new UserStorage(PhpPlayerApp.getAppContext());
        return ourInstance;
    }
    SharedPreferences preferences;
    private UserStorage(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String rescentCatsstr = preferences.getString("rescentCats","");
        Type listType = new TypeToken<ArrayList<CategoryStorageItem>>(){}.getType();
        rescentCats = new Gson().fromJson(rescentCatsstr, listType);
        UpdateConfiguration();

    }

    public void UpdateConfiguration() {
       resolution = preferences.getString("video_resolution","480");

        if (java.util.Arrays.asList(resolutions).indexOf(resolution) ==-1)
            resolution = "480";
        columns = Math.max(Integer.parseInt(preferences.getString("columns_count","3")),1);
        fontSize = Integer.parseInt(preferences.getString("font_size","10"));
        if (fontSize ==0)
            fontSize = 10;
        PasswordProtection = preferences.getBoolean("pass_enabled",true);
        downloadPath = preferences.getString("storage",Environment.getExternalStorageDirectory().getAbsolutePath());
        SearchMode = preferences.getString("search_mode","most_rescent");
        downloadResolution = preferences.getString("download_resolution","720");
        HashedPassword = preferences.getString("signature","9f6992966d4c363ea0162a56cb45fe5");
    }

    private String resolution = "480";
    private String downloadResolution="720";
    private String[] resolutions ={"240","480","720","1080"};
    private List<CategoryStorageItem> rescentCats;
    private int maxRescentcats = 7;
    private int columns=3;
    private int fontSize = 10;
    private boolean PasswordProtection = true;
    private String downloadPath;
    public String SearchMode;
    public String getResolution(){
        return  resolution;
    }


    private  String HashedPassword;

    public String getDownloadPath(){
        File myDir = new File(downloadPath + "/.php_videos");
        myDir.mkdirs();
        return myDir.getPath();
    }
    public String getDownloadDataPath(){
        File myDir = new File(downloadPath + "/.php_videos/data");
        myDir.mkdirs();
        return myDir.getPath();
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
        if (rescentCats == null)
            rescentCats = new ArrayList<>();
        return rescentCats;
    }
    public void AddCategory(CategoryItem item){
        if (rescentCats == null)
            rescentCats = new ArrayList<>();
        if (!rescentCatsContains(item)) {
            rescentCats.add(new CategoryStorageItem(item));
            Collections.sort(rescentCats, Collections.reverseOrder(new Comparator<CategoryStorageItem>() {
                @Override
                public int compare(CategoryStorageItem item1, CategoryStorageItem item2) {
                    return item1.date.compareTo(item2.date);
                }
            }));
            while (rescentCats.size() > maxRescentcats)
                rescentCats.remove(rescentCats.size() - 1);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("rescentCats", new Gson().toJson(rescentCats));
            editor.commit();
        }
    }

    private boolean rescentCatsContains(CategoryItem item) {
        for (int i=0; i<rescentCats.size();i++){
            if (rescentCats.get(i).Title.equals(item.Title))
                return true;
        }
        return false;
    }

    public int getColumns() {
        if (PhpPlayerApp.getAppContext().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
                return columns-2>0?columns-2:1;
        return columns>0?columns:1;
    }

    public void setColumns(int columns) {
        if (columns<1 ||columns>10)
            return;

        this.columns = columns;
        if (PhpPlayerApp.getAppContext().getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT)
            columns+=2;
        preferences.edit().putString("columns_count",new Integer(columns).toString()).apply();

    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
       preferences.edit().putString("font_size",new Integer(fontSize).toString()).apply();

    }

    public boolean getPasswordProtected() {
        return PasswordProtection;
    }

    public String getSearchOrder(){
        switch (SearchMode){
            case "most_rescent":
                return "&o=mr";
            case "most_viewed":
                return "&o=mv";
            case "top_rated":
                return "&o=tr";
                default:
                    return "";

        }

    }

    public String getDownloadResolution() {
       return downloadResolution;
    }

    public boolean CheckPassword(String pwd){
       return md5(pwd).equals(HashedPassword);

    }
    public  void HashPassword(String pwd){
        String str=  md5(pwd);
        preferences.edit().putString("signature",str).apply();


    }
    private String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    private  CategoryItem CurrectCategory;
    public void SetCurrentCategory(CategoryItem item) {
        CurrectCategory = item;
    }
    public CategoryItem getCurrectCategory(){
        return CurrectCategory;
    }
}
