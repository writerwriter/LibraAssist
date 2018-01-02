package com.writerwriter.libraassist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Sub3BorrowFragment extends Fragment{
    public static Sub3BorrowFragment Instance;
    private static final String LOG_FLAG = "BookmarkFragment";
    private static final String BOOK_MARK_KEY = "book_mark";

    private static DatabaseReference bookmarkRef;
    private ChildEventListener bookmarkEventListener;

    private FloatingActionButton add_tag_btn;
    private List<TagUnit> list = new ArrayList<>();
    private TagAdapter adapter;

    public Sub3BorrowFragment() {
        Instance = this;

        bookmarkEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
            //Log.d(LOG_FLAG, "DataAdd    "+dataSnapshot.getKey()+" "+dataSnapshot.getValue());
            list.add(new TagUnit(dataSnapshot.getKey(), (String)dataSnapshot.getValue()));
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
            //Log.d(LOG_FLAG, "DataChange "+dataSnapshot.getKey()+" "+dataSnapshot.getValue());
            for (int i=0; i<list.size(); i++){
                if(list.get(i).getTag_text().equals(dataSnapshot.getKey())){
                    list.get(i).setTag_pages((String)dataSnapshot.getValue());
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            //Log.d(LOG_FLAG, "DataRemove "+dataSnapshot.getKey()+" "+dataSnapshot.getValue());
            for (int i=0; i<list.size(); i++){
                if(list.get(i).getTag_text().equals(dataSnapshot.getKey())){
                    list.remove(i);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_sub3_borrow, container, false);
        final View customDialogView = v.findViewById(R.id.custom_view);
        final EditText e1 = v.findViewById(R.id.enter_tag_name);
        final EditText e2 = v.findViewById(R.id.enter_tag_pages);
        customDialogView.setVisibility(View.GONE);
        final ListView listView = v.findViewById(R.id.tag_list_view);
        adapter = new TagAdapter(getContext(),list);
        listView.setAdapter(adapter);
        add_tag_btn = v.findViewById(R.id.add_tag_button);
        add_tag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogView.setVisibility(View.VISIBLE);
                new MaterialDialog.Builder(getContext())
                        .title("新增書籤")
                        .positiveText("Add")
                        .negativeText("Cancel")
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                switch (which){
                                    case POSITIVE:
                                        if (e2.getText().toString().equals("")){
                                            Toast.makeText(getContext(),"The page is empty.",Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        boolean check = true;
                                        for (int i=0; i<list.size(); i++){
                                            if(list.get(i).getTag_text().equals(e1.getText().toString())){
                                                Toast.makeText(getContext(),"Bookmark is already exist.",Toast.LENGTH_SHORT).show();
                                                check = false;
                                                break;
                                            }
                                        }
                                        if(check){
                                            UpdateBookmark(e1.getText().toString(), e2.getText().toString());
                                        }
                                        break;
                                    case NEGATIVE:
                                        break;
                                }
                            }
                        })/*
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Book Name", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                Toast.makeText(getContext(),"get inputcallback!",Toast.LENGTH_SHORT).show();
                            }
                        })*/
                        .customView(customDialogView,true)
                        .show();
            }
        });
        if (bookmarkRef == null){
            bookmarkRef = AccountManager.Instance.GetUserDatabaseRef(BOOK_MARK_KEY);
            if (bookmarkRef == null){
                return v;
            }
        }
        bookmarkRef.addChildEventListener(bookmarkEventListener);

        return v;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (bookmarkRef != null)
            bookmarkRef.removeEventListener(bookmarkEventListener);
    }

    public void UpdateBookmark(String key, String value){
        if (bookmarkRef != null){
            if (value.equals("")){
                bookmarkRef.child(key).removeValue();
            }
            else{
                Map<String, Object> data = new HashMap<>();
                data.put(key, value);
                bookmarkRef.updateChildren(data);
            }
        }
    }
}
