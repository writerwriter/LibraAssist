package com.writerwriter.libraassist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
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

import java.util.ArrayList;
import java.util.List;

public class Sub3BorrowFragment extends Fragment{

    private FloatingActionButton add_tag_btn;
    private List<TagUnit> list = new ArrayList<>();
    private TagAdapter adapter;
    public Sub3BorrowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_sub3_borrow, container, false);
        final View customDialogView = v.findViewById(R.id.custom_view);
        final EditText e1 = (EditText)v.findViewById(R.id.enter_tag_name);
        final EditText e2 = (EditText)v.findViewById(R.id.enter_tag_pages);
        customDialogView.setVisibility(View.GONE);
        ListView listView = (ListView) v.findViewById(R.id.tag_list_view);
        adapter = new TagAdapter(getContext(),list);
        listView.setAdapter(adapter);
        add_tag_btn = (FloatingActionButton) v.findViewById(R.id.add_tag_button);
        add_tag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogView.setVisibility(View.VISIBLE);
                new MaterialDialog.Builder(getContext())
                        .title("test")
                        .positiveText("Add")
                        .negativeText("Cancel")
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                switch (which){
                                    case POSITIVE:
                                        Toast.makeText(getContext(),"test1",Toast.LENGTH_SHORT).show();
                                        list.add(new TagUnit(e1.getText().toString(),e2.getText().toString()));
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case NEGATIVE:
                                        Toast.makeText(getContext(),"test2",Toast.LENGTH_SHORT).show();
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



        return v;
    }

}
