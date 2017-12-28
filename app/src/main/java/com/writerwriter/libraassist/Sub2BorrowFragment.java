package com.writerwriter.libraassist;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.writerwriter.libraassist.BorrowBookAdapter;
import com.writerwriter.libraassist.BorrowBookUnit;
import com.writerwriter.libraassist.R;
import com.writerwriter.libraassist.*;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class Sub2BorrowFragment extends Fragment {
    private List<BorrowBookUnit> borrowBookList = new ArrayList<>();
    private RecyclerView borrowListView;
    private LinearLayoutManager linearLayoutManager;
    public Sub2BorrowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstafnceState) {
// Inflate the layout for this fragmepackage com.writerwriter.libraassist;
        View v = inflater.inflate(R.layout.fragment_sub2_borrow, container, false);

        //TODO:在這加入書籍借閱的資訊到list裡。

        borrowBookList.add(new BorrowBookUnit("a","a","a","a","a","a","a","a"));

        BorrowBookAdapter adapter = new BorrowBookAdapter(borrowBookList,getContext());
        borrowListView = (RecyclerView)v.findViewById(R.id.borrow_book_recyclerView2);
        borrowListView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        borrowListView.setLayoutManager(linearLayoutManager);
        borrowListView.setAdapter(adapter);

        return v;
    }
}
