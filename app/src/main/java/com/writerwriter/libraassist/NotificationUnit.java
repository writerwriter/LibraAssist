package com.writerwriter.libraassist;

/**
 * Created by Larry on 2017/12/31.
 */

public class NotificationUnit {
    private String title;
    private String left_days;
    private String location;

    public NotificationUnit(String title, String left_days, String location) {
        this.title = title;
        this.left_days = left_days;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLeft_days() {
        return left_days;
    }

    public void setLeft_days(String left_days) {
        this.left_days = left_days;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
