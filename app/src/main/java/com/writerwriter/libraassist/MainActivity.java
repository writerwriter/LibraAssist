package com.writerwriter.libraassist;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.Set;
import java.util.concurrent.Delayed;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationViewEx bottomNavigationView;

    //private NonSwipeableViewPager viewPager;

    public static AnimatedVectorDrawable mMenu;
    public static AnimatedVectorDrawable mBack;

    public static Toolbar toolbar;

    private AccountManager mAccountManager;

    MainFragment mainFragment;
    CollectionFragment collectionFragment;
    BorrowFragment borrowFragment;
    LibraryInfoFragment libraryInfoFragment;
    MenuItem preMenuItem;

    SettingsFragment settingsFragment;
    NotificationFragment notificationFragment;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fm = getSupportFragmentManager();
        fm.executePendingTransactions();
        //SettingsFragment settingsFragment = (SettingsFragment)getSupportFragmentManager().findFragmentByTag("settingsFragment");
        if(fm.getBackStackEntryCount()==0){
            toolbar.setNavigationIcon(mBack);
            mBack.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAccountManager = new AccountManager(MainActivity.this);
        mAccountManager.Init();

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("新書推薦");
        mMenu = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.ic_menu_animatable);
        mBack = (AnimatedVectorDrawable) getResources().getDrawable(R.drawable.ic_back_animatable);
        toolbar.setNavigationIcon(mMenu);
        setSupportActionBar(toolbar);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.load_fragment,new MainFragment(),"homeFragment").commit();

        /*toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("settingsFragment");
                        if (settingsFragment == null) {
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.setCustomAnimations(R.anim.slide_frag_in_top, R.anim.slide_frag_out_top, R.anim.slide_frag_in_top, R.anim.slide_frag_out_top);
                            fragmentTransaction.add(R.id.load_fragment, new SettingsFragment(), "settingsFragment");
                            fragmentTransaction.addToBackStack("settingsFragment");
                            fragmentTransaction.commit();
                            if(toolbar.getNavigationIcon() != getDrawable(R.drawable.ic_back_animatable)) {
                                toolbar.setNavigationIcon(mMenu);
                                mMenu.start();
                            }
                        } else {
                            fragmentManager.popBackStack();
                            toolbar.setNavigationIcon(mBack);
                            mBack.start();
                        }
                        break;
                }
                return true;
            }
        });*/

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toolbar.getNavigationIcon() == mMenu){
                    fragmentManager.popBackStack();
                    fragmentManager.executePendingTransactions();
                    if(fragmentManager.getBackStackEntryCount() == 0){
                        toolbar.setNavigationIcon(mBack);
                        mBack.start();
                    }
                }
                /*if(fragmentManager.findFragmentByTag("notificationFragment")!=null && fragmentManager.findFragmentByTag("settingsFragment")==null || fragmentManager.findFragmentByTag("notificationFragment")==null && fragmentManager.findFragmentByTag("settingsFragment")!=null){
                    toolbar.setNavigationIcon(mBack);
                    mBack.start();
                }*/
            }
        });

        //viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);

        bottomNavigationView = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bottomNavigationView.enableShiftingMode(false);


        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        invalidateOptionsMenu();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        //check existence to avoid recreating fragment
                        mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag("homeFragment");
                        collectionFragment = (CollectionFragment) getSupportFragmentManager().findFragmentByTag("collectionFragment");
                        borrowFragment = (BorrowFragment)getSupportFragmentManager().findFragmentByTag("borrowFragment");
                        libraryInfoFragment = (LibraryInfoFragment)getSupportFragmentManager().findFragmentByTag("libraryInfoFragment");
                        //if there is a settingsfragment exist, popbackstack first and then do the replace
                        SettingsFragment settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("settingsFragment");
                        if(settingsFragment!=null){
                            fragmentManager.popBackStack();
                            toolbar.setNavigationIcon(mBack);
                            mBack.start();
                        }
                        LibraryInfoDetailFragment libraryInfoDetailFragment = (LibraryInfoDetailFragment) getSupportFragmentManager().findFragmentByTag("LibraryInfoDetailFragment");
                        if(libraryInfoDetailFragment!=null){
                            fragmentManager.popBackStack();
                        }
                        CollectionBookDetailFragment collectionBookDetailFragment = (CollectionBookDetailFragment) getSupportFragmentManager().findFragmentByTag("CollectionBookDetailFragment");
                        if(collectionBookDetailFragment != null){
                            fragmentManager.popBackStack();
                        }
                        switch (item.getItemId()) {
                            case R.id.action_home:
                                if(mainFragment==null) {
                                    mainFragment = new MainFragment();
                                    fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.load_fragment, mainFragment, "homeFragment");
                                    toolbar.setTitle("新書推薦");
                                }
                                break;
                            case R.id.action_collection:
                                if(collectionFragment==null) {
                                    collectionFragment = new CollectionFragment();
                                    fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.load_fragment, collectionFragment, "collectionFragment");
                                    toolbar.setTitle("館藏查詢");
                                }
                                break;
                            case R.id.action_borrow:
                                if(borrowFragment==null) {
                                    borrowFragment = new BorrowFragment();
                                    fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.load_fragment, borrowFragment, "borrowFragment");
                                    toolbar.setTitle("借閱紀錄");
                                }
                                break;
                            case R.id.action_info:
                                if(libraryInfoFragment==null) {
                                    libraryInfoFragment = new LibraryInfoFragment();
                                    fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out).replace(R.id.load_fragment, libraryInfoFragment, "libraryInfoFragment");
                                    toolbar.setTitle("圖書館資訊");
                                }
                                break;
                        }
                        fragmentTransaction.commit();
                        return true;
                    }
                });


        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
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
    }*/
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch(bottomNavigationView.getSelectedItemId()){
            case R.id.action_collection:
                menu.findItem(R.id.search).setVisible(true);
                menu.findItem(R.id.action_notification).setVisible(true);
                menu.findItem(R.id.action_settings).setVisible(true);
                break;
            default:
                menu.findItem(R.id.search).setVisible(false);
                menu.findItem(R.id.action_notification).setVisible(true);
                menu.findItem(R.id.action_settings).setVisible(true);
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.action_settings:
                settingsFragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("settingsFragment");
                if (settingsFragment == null) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_frag_in_top, R.anim.slide_frag_out_top, R.anim.slide_frag_in_top, R.anim.slide_frag_out_top);
                    fragmentTransaction.add(R.id.load_fragment, new SettingsFragment(), "settingsFragment");
                    fragmentTransaction.addToBackStack("settingsFragment");
                    fragmentTransaction.commit();
                    if(fragmentManager.findFragmentByTag("notificationFragment")==null) {
                        toolbar.setNavigationIcon(mMenu);
                        mMenu.start();
                    }
                }
                else {
                    //if(fragmentManager.findFragmentByTag("settingsFragment") != fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1)) fragmentManager.popBackStack();
                    if(fragmentManager.getBackStackEntryCount() != 0) fragmentManager.popBackStack();
                    else fragmentManager.popBackStack("settingsFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    if(fragmentManager.findFragmentByTag("notificationFragment")==null){
                        toolbar.setNavigationIcon(mBack);
                        mBack.start();
                    }
                }
                break;
            case R.id.action_notification:
                notificationFragment = (NotificationFragment) getSupportFragmentManager().findFragmentByTag("notificationFragment");
                if(notificationFragment == null){
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.setCustomAnimations(R.anim.slide_frag_in_top, R.anim.slide_frag_out_top, R.anim.slide_frag_in_top, R.anim.slide_frag_out_top);
                    fragmentTransaction.add(R.id.load_fragment, new NotificationFragment(), "notificationFragment");
                    fragmentTransaction.addToBackStack("notificationFragment");
                    fragmentTransaction.commit();
                    if(fragmentManager.findFragmentByTag("settingsFragment")==null) {
                        toolbar.setNavigationIcon(mMenu);
                        mMenu.start();
                    }
                }
                else {
                    //if(fragmentManager.findFragmentByTag("notificationFragment")!= fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount()-1))fragmentManager.popBackStack();
                    if(fragmentManager.getBackStackEntryCount() != 0) fragmentManager.popBackStack();
                    else fragmentManager.popBackStack("notificationFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    if(fragmentManager.findFragmentByTag("settingsFragment")==null){
                        toolbar.setNavigationIcon(mBack);
                        mBack.start();
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == AccountManager.RC_SIGN_IN) {
            AccountManager.Instance.AuthenticateWithFirebase(data);
        }
    }

    public void toolbarAnimationStart(){
        toolbar.setNavigationIcon(mBack);
        mBack.start();
    }
    public void toolbarAnimationEnd(){
        toolbar.setNavigationIcon(mMenu);
        mMenu.start();
    }

}
