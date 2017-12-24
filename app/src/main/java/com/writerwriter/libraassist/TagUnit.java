package com.writerwriter.libraassist;

/**
 * Created by Larry on 2017/12/24.
 */

public class TagUnit {

    private String tag_text;
    private String tag_pages;

    public TagUnit() {
    }

    public TagUnit(String tag_text, String tag_pages) {
        this.tag_text = tag_text;
        this.tag_pages = tag_pages;
    }

    public String getTag_text() {
        return tag_text;
    }

    public void setTag_text(String tag_text) {
        this.tag_text = tag_text;
    }

    public String getTag_pages() {
        return tag_pages;
    }

    public void setTag_pages(String tag_pages) {
        this.tag_pages = tag_pages;
    }
}
