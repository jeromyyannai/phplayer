package com.az.pplayer.Menu;

import android.app.Activity;

import com.az.pplayer.MainActivity;
import com.az.pplayer.Views.CategoryViewActivity;

public  class LeftMenuNavigation {
    private  static LeftMenuNavigation instance;
    private String selectedItemId;
    public  static LeftMenuNavigation Get(){
        if (instance == null)
            instance = new LeftMenuNavigation();
        return instance;
    }
    public void SetSelected(Activity context,String link, String Name)
    {
        if (context instanceof CategoryViewActivity)
            selectedItemId = "ic_categories";


    }
}
