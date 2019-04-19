package com.az.pplayer.Models;

public class MenuItem {
    private boolean isSelected;
    private String name;
    private int drawableId;
public  String Id;
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
    public String Link;

    public MenuItem( String name, int drawableId, String id) {
        this.name = name;
        this.drawableId = drawableId;
        Id = id;
        Type = MenuType.Main;
    }

    public MenuItem(String name, int drawableId, String id, MenuType type) {
        this.name = name;
        this.drawableId = drawableId;
        Id = id;
        Type = type;
    }

    public enum MenuType{Main,Link}
}
