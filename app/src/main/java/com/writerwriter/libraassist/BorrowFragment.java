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
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

public class BorrowFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabs;
    private Sub1BorrowFragment sub1BorrowFragment;
    private Sub2BorrowFragment sub2BorrowFragment;
    private Sub3BorrowFragment sub3BorrowFragment;//書籤

    public BorrowFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_borrow, container, false);
        tabs = v.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("借閱中"));
        tabs.addTab(tabs.newTab().setText("借閱歷史"));
        tabs.addTab(tabs.newTab().setText("書籤"));
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                sub1BorrowFragment = (Sub1BorrowFragment)getFragmentManager().findFragmentByTag("sub1BorrowFragment");
                sub2BorrowFragment = (Sub2BorrowFragment)getFragmentManager().findFragmentByTag("sub2BorrowFragment");
                sub3BorrowFragment = (Sub3BorrowFragment)getFragmentManager().findFragmentByTag("sub3BorrowFragment");
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
                    case 2:
                        if(sub3BorrowFragment == null){
                            sub3BorrowFragment = new Sub3BorrowFragment();
                            ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.borrowSubPageContainer,sub3BorrowFragment,"sub3BorrowFragment");
                        }
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

        if (AccountManager.Instance.GetGoogleAccountName() == null) {
            Toast.makeText(getContext(), "請先登入才能使用借閱紀錄功能", Toast.LENGTH_SHORT).show();
        }
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
        if(sub3BorrowFragment != null){
            getFragmentManager().beginTransaction().remove(sub3BorrowFragment).commit();
        }
    }

}
