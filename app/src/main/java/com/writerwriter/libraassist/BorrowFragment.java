package com.writerwriter.libraassist;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class BorrowFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabs;
    private Sub1BorrowFragment sub1BorrowFragment;
    private Sub2BorrowFragment sub2BorrowFragment;

    public BorrowFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_borrow, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        tabs = (TabLayout)getView().findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Borrow"));
        tabs.addTab(tabs.newTab().setText("Borrowed"));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                sub1BorrowFragment = (Sub1BorrowFragment)getFragmentManager().findFragmentByTag("sub1BorrowFragment");
                sub2BorrowFragment = (Sub2BorrowFragment)getFragmentManager().findFragmentByTag("sub2BorrowFragment");
                switch(tab.getPosition()){
                    case 0:
                        if(sub1BorrowFragment == null){
                            sub1BorrowFragment = new Sub1BorrowFragment();
                            ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.borrowSubPageContainer,sub1BorrowFragment,"sub1BorrowFragment");
                        }
                        break;
                    case 1:
                        if(sub2BorrowFragment == null){
                            sub2BorrowFragment = new Sub2BorrowFragment();
                            ft.setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.borrowSubPageContainer,sub2BorrowFragment,"sub2BorrowFragment");
                        }
                        break;
                }
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        Sub1BorrowFragment sub1BorrowFragment = new Sub1BorrowFragment();
        getFragmentManager().beginTransaction().replace(R.id.borrowSubPageContainer,sub1BorrowFragment,"sub1BorrowFragment").commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(sub1BorrowFragment != null){
            getFragmentManager().beginTransaction().remove(sub1BorrowFragment).commit();
        }
        if(sub2BorrowFragment != null){
            getFragmentManager().beginTransaction().remove(sub2BorrowFragment).commit();
        }
    }

}
