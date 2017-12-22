package com.writerwriter.libraassist;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.victor.loading.book.BookLoading;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Delayed;


public class CollectionFragment extends Fragment {
    BottomNavigationViewEx bottomNavigationViewEx = MainActivity.bottomNavigationView;

    Animation hide;
    Animation appear;

    private static final String LOG_FLAG = "---Collect Fragment---";

    private static final String SEARCH_KEY       = "search";
    private static final String SEARCHKEY_KEY    = "key";
    private static final String NTCLIB_URL_KEY   = "Xinpei_url"; // firebase在NTC搜到幾本書
    private static final String NTPULIB_URL_KEY  = "ntpu_url";   // firebase在NTPU收到幾本書
    private static final String RESULT_KEY       = "search_result";
    private static final String TEMP_NTCLIB_KEY  = "temp_result_Xinpei";
    private static final String TEMP_NTPULIB_KEY = "temp_search_ntpu";
    private static final String HOTKEY_PATH      = "hot_key";
    private static final int HOTKEY_SHOW_NUM = 10; // 要顯示幾筆HOTKEY

    private String searchTime = "";
    private int ntpu_url = 999999;
    private int Xinpei_url = 999999;

    private DatabaseReference searchRef;
    private DatabaseReference resultRef;
    private DatabaseReference ntcRef;
    private DatabaseReference ntpuRef;
    private DatabaseReference hotkeyRef;
    private ChildEventListener mSearchEventListener;
    private ChildEventListener mResultEventListener;
    private ChildEventListener mHotkeyEventListener;

    private static List<String> hotkey = new ArrayList<>();
    private HashMap<String, CollectionSearchResultUnit> searchResult = new HashMap<>();
    private List<CollectionSearchResultUnit> collectionSearchResultUnits = new ArrayList<>();
    private RecyclerView collectionSearchResults;
    private LinearLayoutManager linearLayoutManager;
    private String[] tokens;
    private List<TextView> textViews = new ArrayList<>();
    private TextView hotkey_title;
    private SearchView searchView;
    MenuItem item;
    private BookLoading bookLoadingAnimation;

    public CollectionFragment(){
        mSearchEventListener = new ChildEventListener() {
            @Override public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.getKey().equals(NTCLIB_URL_KEY)) {
                    Xinpei_url = Integer.parseInt((String)dataSnapshot.getValue());
                }
                else if (dataSnapshot.getKey().equals(NTPULIB_URL_KEY)) {
                    ntpu_url = Integer.parseInt((String)dataSnapshot.getValue());
                }
                if (searchResult.size() == Xinpei_url + ntpu_url) {
                    if (searchRef != null) searchRef.removeEventListener(mSearchEventListener);
                    ShowBookList();
                }
            }

            @Override public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
        mResultEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                HashMap<String, String> data = (HashMap<String, String>)dataSnapshot.getValue();
                String link = data.get("link");
                if (searchResult.containsKey(link)) {
                    searchResult.get(link).SetInfo(data);
                    Log.d(LOG_FLAG, "詳細資料 "+searchResult.get(link).getLink()+" "+link+" "+searchResult.get(link).getTitle());
                }
                else {
                    CollectionSearchResultUnit info = new CollectionSearchResultUnit(data);
                    searchResult.put(link, info);
                    Log.d(LOG_FLAG, "新增目錄 "+searchResult.get(link).getLink()+" "+link+" "+searchResult.get(link).getTitle());
                    if (searchResult.size() == Xinpei_url + ntpu_url) {
                        ShowBookList();
                    }
                }
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
        mHotkeyEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.getKey().equals("result")){
                    HashMap<String, String> data = (HashMap<String, String>)dataSnapshot.getValue();
                    hotkey.addAll(data.values());
                    // 暫時顯示熱門關鍵字
                    Toast.makeText(getContext(), hotkey.toString(), Toast.LENGTH_SHORT).show();

                    for(int i=0;i<hotkey.size();i++){
                        textViews.get(i).setText(hotkey.get(i));
                        textViews.get(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MenuItemCompat.expandActionView(item);
                                searchView.setQuery(((TextView)view).getText().toString(),false);
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.showSoftInput(view, 0);
                                }
                            }
                        });
                    }

                    Log.d(LOG_FLAG, "Got Hotkey List. Num :"+hotkey.size());
                    hotkeyRef.removeEventListener(mHotkeyEventListener);
                }
            }
            @Override public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_collection, container, false);
        collectionSearchResults = v.findViewById(R.id.colleciton_recyclerview);

        hotkey_title = (TextView)v.findViewById(R.id.hotkey_title);

        textViews.add((TextView) v.findViewById(R.id.hotkey_0));
        textViews.add((TextView) v.findViewById(R.id.hotkey_1));
        textViews.add((TextView) v.findViewById(R.id.hotkey_2));
        textViews.add((TextView) v.findViewById(R.id.hotkey_3));
        textViews.add((TextView) v.findViewById(R.id.hotkey_4));
        textViews.add((TextView) v.findViewById(R.id.hotkey_5));
        textViews.add((TextView) v.findViewById(R.id.hotkey_6));
        textViews.add((TextView) v.findViewById(R.id.hotkey_7));
        textViews.add((TextView) v.findViewById(R.id.hotkey_8));
        textViews.add((TextView) v.findViewById(R.id.hotkey_9));
        textViews.add((TextView) v.findViewById(R.id.hotkey_10));
        textViews.add((TextView) v.findViewById(R.id.hotkey_11));

        bookLoadingAnimation = (BookLoading)getActivity().findViewById(R.id.book_loading_animation);
        bookLoadingAnimation.setVisibility(View.GONE);

        for(int i=0;i<hotkey.size();i++){
            textViews.get(i).setText(hotkey.get(i));
            textViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MenuItemCompat.expandActionView(item);
                    searchView.setQuery(((TextView)view).getText().toString(),false);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.showSoftInput(view, 0);
                    }
                }
            });
        }

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();

                bookLoadingAnimation.setVisibility(View.VISIBLE);
                bookLoadingAnimation.start();


                hotkey_title.setVisibility(View.GONE);
                for(int i =0;i<textViews.size();i++){
                    textViews.get(i).setVisibility(View.GONE);
                }

                if (query.equals("")){
                    return true;
                }
                if (searchRef != null) {
                    searchResult.clear();
                    collectionSearchResults.setAdapter(null);
                }

                searchTime = new Date().toString();
                if (searchRef != null) searchRef.removeEventListener(mSearchEventListener);
                if (resultRef != null) resultRef.removeEventListener(mResultEventListener);
                if (ntcRef    != null) ntcRef.removeEventListener(mResultEventListener);
                if (ntpuRef   != null) ntpuRef.removeEventListener(mResultEventListener);
                AccountManager.Instance.GetUserDatabaseRef(SEARCH_KEY).removeValue();
                searchRef = AccountManager.Instance.GetUserDatabaseRef(SEARCH_KEY+"/"+searchTime);
                resultRef = AccountManager.Instance.GetUserDatabaseRef(SEARCH_KEY+"/"+searchTime+"/"+RESULT_KEY);
                ntcRef    = AccountManager.Instance.GetUserDatabaseRef(SEARCH_KEY+"/"+searchTime+"/"+TEMP_NTCLIB_KEY);
                ntpuRef   = AccountManager.Instance.GetUserDatabaseRef(SEARCH_KEY+"/"+searchTime+"/"+TEMP_NTPULIB_KEY);
                searchRef.addChildEventListener(mSearchEventListener);
                resultRef.addChildEventListener(mResultEventListener);
                ntcRef.addChildEventListener(mResultEventListener);
                ntpuRef.addChildEventListener(mResultEventListener);

                Xinpei_url = 999999;
                ntpu_url = 999999;
                Map<String, Object> users = new HashMap<>();
                users.put(SEARCHKEY_KEY, query);
                searchRef.updateChildren(users);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        });
        if (hotkey.size() == 0){
            hotkeyRef = FirebaseDatabase.getInstance().getReference(HOTKEY_PATH);
            hotkeyRef.addChildEventListener(mHotkeyEventListener);
        }
    }

    public void ShowBookList() {
        bookLoadingAnimation.stop();
        bookLoadingAnimation.setVisibility(View.GONE);
        // 新增至清單內
        collectionSearchResultUnits.clear();
        collectionSearchResultUnits.addAll(searchResult.values());
        collectionSearchResults.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        collectionSearchResults.setLayoutManager(linearLayoutManager);
        CollectionSearchResultAdapter collectionSearchResultAdapter = new CollectionSearchResultAdapter(getContext(), collectionSearchResultUnits);
        collectionSearchResultAdapter.setHasStableIds(true);
        collectionSearchResultAdapter.notifyDataSetChanged();
        collectionSearchResults.setAdapter(collectionSearchResultAdapter);

        /*hide = AnimationUtils.loadAnimation(getContext(),R.anim.slide_out_bottom);
        appear = AnimationUtils.loadAnimation(getContext(),R.anim.slide_in_top);
        collectionSearchResults.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    bottomNavigationViewEx.startAnimation(hide);
                    hide.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            bottomNavigationViewEx.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
                else if(dy<0){
                    bottomNavigationViewEx.startAnimation(appear);
                    appear.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            bottomNavigationViewEx.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            }
        });*/

        Log.d(LOG_FLAG, "Show BookList Num:"+searchResult.size());
        Xinpei_url = 999999;
        ntpu_url = 999999;
    }

}
