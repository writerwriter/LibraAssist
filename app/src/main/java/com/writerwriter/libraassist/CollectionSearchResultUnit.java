package com.writerwriter.libraassist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Larry on 2017/12/12.
 */

public class CollectionSearchResultUnit {
    public String isbn;
    public String title;
    public String author;
    public String img;
    public String publish_year;
    public String publisher;
    public String location;
    public String storage;
    public String link;
    public String searchState;

    public CollectionSearchResultUnit() {
    }

    public CollectionSearchResultUnit(HashMap<String, Object> data) {
        SetInfo(data);
    }

    public void SetInfo(HashMap<String, Object> data) {
        isbn = (String)data.get("isbn");
        title = (String)data.get("title");
        author = (String)data.get("author");
        img = (String)data.get("img");
        publish_year = (String)data.get("publish_year");
        publisher = (String)data.get("publisher");
        link = (String)data.get("link");
        location = (String)data.get("location");
        storage = (String)data.get("storage");
        searchState = (String)data.get("searchState");
    }

    public String getImg() {
        return img;
    }

    public String getName() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDetail() {
        if (searchState.equals("true"))
            return   "書名         : "+title+"\n"
                    +"作者         : "+author+"\n"
                    +"ISBN         : "+isbn+"\n"
                    +"出版社     : "+publisher+"\n"
                    +"出版年     : "+publish_year+"\n"
                    +"館藏位置 : "+location+"\n"
                    +"可借數量 : "+storage;
        else if (searchState.equals("false") || searchState.equals("Pending!"))
            return "搜尋詳細資訊中...";
        else
            return "Error!!! State : "+searchState;
    }
}