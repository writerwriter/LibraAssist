package com.writerwriter.libraassist;

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


public class Sub1BorrowFragment extends Fragment {
    private RecyclerView borrowListView;
    private LinearLayoutManager linearLayoutManager;
    public Sub1BorrowFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sub1_borrow, container, false);

        BorrowBookAdapter adapter = new BorrowBookAdapter(AccountManager.Instance.borrowBookList,
                getContext());
        borrowListView = v.findViewById(R.id.borrow_book_recyclerView);
        borrowListView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        borrowListView.setLayoutManager(linearLayoutManager);
        borrowListView.setAdapter(adapter);

        return v;
    }
}
