package com.writerwriter.libraassist;

import android.app.SearchManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CollectionFragment extends Fragment {
    public static final String SEARCH_DATABASE_KEY = "search/key";

    public static DatabaseReference ref;

    private List<CollectionSearchResultUnit> collectionSearchResultUnits = new ArrayList<>();
    private RecyclerView collectionSearchResults;
    private LinearLayoutManager linearLayoutManager;

    public CollectionFragment(){

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

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Map<String, Object> users = new HashMap<String, Object>();
                users.put(SEARCH_DATABASE_KEY, query);
                AccountManager.ref.updateChildren(users);

                //放入搜尋結果(封面url,書名,作者,圖書館
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("http://whatpixel.com/images/2016/09/machine-learning-for-dummies.jpg","fdsafew","bdsafsda","cdsafsd"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResultUnits.add(new CollectionSearchResultUnit("https://dataissexy.files.wordpress.com/2014/06/51tc7h5-i7l-_sx342_.jpg","a","b","c"));
                collectionSearchResults = (RecyclerView)getView().findViewById(R.id.colleciton_recyclerview);
                collectionSearchResults.setHasFixedSize(true);

                linearLayoutManager = new LinearLayoutManager(getActivity());
                collectionSearchResults.setLayoutManager(linearLayoutManager);
                CollectionSearchResultAdapter collectionSearchResultAdapter = new CollectionSearchResultAdapter(collectionSearchResultUnits);
                collectionSearchResults.setAdapter(collectionSearchResultAdapter);
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
    }
}
