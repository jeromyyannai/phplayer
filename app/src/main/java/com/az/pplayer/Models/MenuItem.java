package com.az.pplayer.Models;

public class MenuItem {
    private boolean isSelected;
    private String name;
    private int drawableId;
public  String Id;
public String Link;
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public MenuType Type;


    public MenuItem( String name, int drawableId, String id,boolean selected) {
        this.name = name;
        this.drawableId = drawableId;
        Id = id;
        Type = MenuType.Main;
        isSelected = selected;
    }

    public MenuItem(String name, Integer drawableId, String id, MenuType type, String link) {
        this.name = name;
        if (drawableId != null) {
            this.drawableId = drawableId;
        }
        Id = id;
        Type = type;
        Link = link;
    }

    public enum MenuType{Main,Link,SwitchOption}
}
