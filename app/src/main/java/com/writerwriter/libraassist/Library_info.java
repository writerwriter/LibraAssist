package com.writerwriter.libraassist;

import android.graphics.drawable.Drawable;

/**
 * Created by Larry on 2017/10/25.
 */

public class Library_info {
    private int img;
    private String name;
    private int icon;

    public Library_info() {
    }

    public Library_info(int img, String name, int icon) {
        this.img = img;
        this.name = name;
        this.icon = icon;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
