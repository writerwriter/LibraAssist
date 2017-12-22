package com.writerwriter.libraassist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class Sub3BorrowFragment extends Fragment{

    private FloatingActionButton add_tag_btn;

    public Sub3BorrowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sub3_borrow, container, false);
        add_tag_btn = (FloatingActionButton) v.findViewById(R.id.add_tag_button);
        add_tag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getContext())
                        .title("test")
                        .content("test2")
                        .positiveText("Add")
                        .negativeText("Cancel")
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                switch (which){
                                    case POSITIVE:
                                        Toast.makeText(getContext(),"test1",Toast.LENGTH_SHORT).show();
                                        break;
                                    case NEGATIVE:
                                        Toast.makeText(getContext(),"test2",Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        })
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Book Name", "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                Toast.makeText(getContext(),"get inputcallback!",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });


        return v;
    }

}
