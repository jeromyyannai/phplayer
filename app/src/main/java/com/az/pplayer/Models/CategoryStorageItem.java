package com.az.pplayer.Models;

import java.util.Date;

public class CategoryStorageItem extends CategoryItem {
    public Date date;

    public CategoryStorageItem(String title, String link, String image, Date date) {
        super(title, link, image,"");
        this.date = date;
    }
    public CategoryStorageItem(CategoryItem category){
        super(category.Title, category.Link, category.Image,category.Id);
        this.date = new Date();
    }
}
