package com.writerwriter.libraassist;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private NonSwipeableViewPager viewPager;

    MainFragment mainFragment;
    CollectionFragment collectionFragment;
    BorrowFragment borrowFragment;
    MenuItem preMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);

        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        viewPager.setCurrentItem(item.getOrder());
                        return true;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels){

            }
            @Override
            public void onPageSelected(int position){
                if(preMenuItem != null){
                    preMenuItem.setChecked(false);
                }
                else{
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "OnPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                preMenuItem = bottomNavigationView.getMenu().getItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state){

            }
        });
        setupViewPager(viewPager);
    }
    private void setupViewPager(NonSwipeableViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mainFragment = new MainFragment();
        collectionFragment = new CollectionFragment();
        borrowFragment = new BorrowFragment();
        adapter.addFragment(mainFragment);
        adapter.addFragment(collectionFragment);
        adapter.addFragment(borrowFragment);
        viewPager.setAdapter(adapter);
    }
}
