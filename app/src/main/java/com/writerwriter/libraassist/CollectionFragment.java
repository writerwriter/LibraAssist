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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CollectionFragment extends Fragment {
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
                    /*HashMap<String, String> data = (HashMap<String, String>)dataSnapshot.getValue();
                    hotkey.addAll(data.values());
                    // 暫時顯示熱門關鍵字
                    Toast.makeText(getContext(), hotkey.toString(), Toast.LENGTH_SHORT).show();
                    Log.d(LOG_FLAG, "Got Hotkey List. Num :"+hotkey.size());*/
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
        return inflater.inflate(R.layout.fragment_collection, container, false);
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
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
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
        // 新增至清單內
        collectionSearchResultUnits.clear();
        collectionSearchResultUnits.addAll(searchResult.values());
        collectionSearchResults = getView().findViewById(R.id.colleciton_recyclerview);
        collectionSearchResults.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        collectionSearchResults.setLayoutManager(linearLayoutManager);
        CollectionSearchResultAdapter collectionSearchResultAdapter = new CollectionSearchResultAdapter(getContext(), collectionSearchResultUnits);
        collectionSearchResultAdapter.setHasStableIds(true);
        collectionSearchResultAdapter.notifyDataSetChanged();
        collectionSearchResults.setAdapter(collectionSearchResultAdapter);
        Log.d(LOG_FLAG, "Show BookList Num:"+searchResult.size());
        Xinpei_url = 999999;
        ntpu_url = 999999;
    }

}
