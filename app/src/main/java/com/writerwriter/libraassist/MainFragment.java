package com.writerwriter.libraassist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;


public class MainFragment extends Fragment {

    private FeatureCoverFlow coverFlow;
    private NewBooksAdapter newBooksAdapter;
    private List<NewBooks> newBooksList = new ArrayList<>();
    private TextSwitcher mTitle;

    public MainFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        mTitle = (TextSwitcher)getView().findViewById(R.id.title);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                TextView txt = (TextView)inflater.inflate(R.layout.layout_title,null);
                return txt;
            }
        });
        Animation in = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);
        newBooksAdapter = new NewBooksAdapter(newBooksList,getActivity());
        coverFlow = (FeatureCoverFlow)getView().findViewById(R.id.coverFlow);
        coverFlow.setAdapter(newBooksAdapter);

        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(newBooksList.get(position).getName());
            }

            @Override
            public void onScrolling() {

            }
        });

    }

    private void initData() {
        newBooksList.add(new NewBooks("machine learning","https://www.analyticsvidhya.com/wp-content/uploads/2015/10/2.png"));
        newBooksList.add(new NewBooks("machine learning","https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg"));
        newBooksList.add(new NewBooks("machine learning","http://whatpixel.com/images/2016/09/machine-learning-for-dummies.jpg"));
    }

}
