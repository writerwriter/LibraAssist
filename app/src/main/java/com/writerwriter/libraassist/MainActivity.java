package com.writerwriter.libraassist;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private NonSwipeableViewPager viewPager;

    private AnimatedVectorDrawable mMenu;
    private AnimatedVectorDrawable mBack;

    Toolbar toolbar;

    MainFragment mainFragment;
    CollectionFragment collectionFragment;
    BorrowFragment borrowFragment;
    LibraryInfoFragment libraryInfoFragment;
    MenuItem preMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        toolbar.setTitle("Home");
        mMenu = (AnimatedVectorDrawable)getResources().getDrawable(R.drawable.ic_menu_animatable);
        mBack = (AnimatedVectorDrawable)getResources().getDrawable(R.drawable.ic_back_animatable);
        toolbar.setNavigationIcon(mMenu);
        setSupportActionBar(toolbar);

        final FragmentManager fragmentManager = getSupportFragmentManager();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){
                    case R.id.action_settings:
                        SettingsFragment settingsFragment = (SettingsFragment)getSupportFragmentManager().findFragmentByTag("settingsFragment");
                        if(settingsFragment == null) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.slide_frag_in_top,R.anim.slide_frag_out_top,R.anim.slide_frag_in_top,R.anim.slide_frag_out_top);
                            fragmentTransaction.add(R.id.load_fragment,new SettingsFragment(), "settingsFragment");
                            fragmentTransaction.addToBackStack("settingsFragment");
                            fragmentTransaction.commit();
                            toolbar.setNavigationIcon(mMenu);
                            mMenu.start();
                        }
                        else{
                            fragmentManager.popBackStack();
                            toolbar.setNavigationIcon(mBack);
                            mBack.start();
                        }
                        break;
                }
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SettingsFragment settingsFragment = (SettingsFragment)getSupportFragmentManager().findFragmentByTag("settingsFragment");
                if(settingsFragment != null){
                    fragmentManager.popBackStack();
                    toolbar.setNavigationIcon(mBack);
                    mBack.start();
                }
            }
        });

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
        libraryInfoFragment = new LibraryInfoFragment();
        adapter.addFragment(mainFragment);
        adapter.addFragment(collectionFragment);
        adapter.addFragment(borrowFragment);
        adapter.addFragment(libraryInfoFragment);
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
