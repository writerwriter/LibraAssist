package com.writerwriter.libraassist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by Larry on 2017/12/12.
 */

public class CollectionSearchResultUnit {
    private String img;
    private String name;
    private String author;
    private String library;

    public CollectionSearchResultUnit() {
    }

    public CollectionSearchResultUnit(String img, String name, String author, String library) {
        this.img = img;
        this.name = name;
        this.author = author;
        this.library = library;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLibrary() {
        return library;
    }

    public void setLibrary(String library) {
        this.library = library;
    }


}
