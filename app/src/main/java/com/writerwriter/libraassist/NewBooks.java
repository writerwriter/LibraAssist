package com.writerwriter.libraassist;

/**
 * Created by Larry on 2017/10/11.
 */

public class NewBooks {
    private String Name,ImageURL;

    public NewBooks(String name, String imageURL) {
        Name = name;
        ImageURL = imageURL;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
