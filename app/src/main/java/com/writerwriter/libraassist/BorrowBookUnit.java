package com.writerwriter.libraassist;

import java.util.HashMap;

/**
 * Created by Larry on 2017/12/28.
 */

public class BorrowBookUnit {
    String book_name;
    String author;
    String location;
    String search_book_number;
    String borrow_time;
    //目前借閱才有
    String return_time;
    String waiting_people_number;
    String renew_count;

    public BorrowBookUnit() {
    }


    public BorrowBookUnit(HashMap<String, String> data) {
        if(data.containsKey("title")) this.book_name = data.get("title");
        if(data.containsKey("author")) this.author = data.get("author");
        if(data.containsKey("location")) this.location = data.get("location");
        if(data.containsKey("search_book_number")) this.search_book_number = data.get("search_book_number");
        if(data.containsKey("borrow_time")) this.borrow_time = data.get("borrow_time");
        if(data.containsKey("return_time")) this.return_time = data.get("return_time");
        if(data.containsKey("waiting_people_number")) this.waiting_people_number = data.get("waiting_people_number");
        if(data.containsKey("renew_count")) this.renew_count = data.get("renew_count");
    }

    public BorrowBookUnit(String book_name, String author, String location, String search_book_number, String borrow_time, String return_time, String waiting_people_number, String renew_count) {
        this.book_name = book_name;
        this.author = author;
        this.location = location;
        this.search_book_number = search_book_number;
        this.borrow_time = borrow_time;
        this.return_time = return_time;
        this.waiting_people_number = waiting_people_number;
        this.renew_count = renew_count;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSearch_book_number() {
        return search_book_number;
    }

    public void setSearch_book_number(String search_book_number) {
        this.search_book_number = search_book_number;
    }

    public String getBorrow_time() {
        return borrow_time;
    }

    public void setBorrow_time(String borrow_time) {
        this.borrow_time = borrow_time;
    }

    public String getReturn_time() {
        return return_time;
    }

    public void setReturn_time(String return_time) {
        this.return_time = return_time;
    }

    public String getWaiting_people_number() {
        return waiting_people_number;
    }

    public void setWaiting_people_number(String waiting_people_number) {
        this.waiting_people_number = waiting_people_number;
    }

    public String getRenew_count() {
        return renew_count;
    }

    public void setRenew_count(String renew_count) {
        this.renew_count = renew_count;
    }
}
