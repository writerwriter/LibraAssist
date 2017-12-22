package com.writerwriter.libraassist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;


public class MainFragment extends Fragment {
    private static final String LOG_FLAG = "---Main Fragment---";
    private static final String NEWBOOK_PATH = "new_book";

    private FeatureCoverFlow coverFlow;
    private NewBooksAdapter newBooksAdapter;
    private TextSwitcher mTitle;

    private DatabaseReference newbookRef;
    private ChildEventListener mNewbookEventListener;

    private static List<CollectionSearchResultUnit> newBooksList = new ArrayList<>();

    public MainFragment(){
        mNewbookEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                if (dataSnapshot.getKey().equals("search_result")){
                    HashMap<String, HashMap<String, String>> list = (HashMap<String, HashMap<String, String>>)dataSnapshot.getValue();
                    newBooksList.clear();
                    for (HashMap<String, String> data : list.values()) {
                        newBooksList.add(new CollectionSearchResultUnit(data));
                    }
                    coverFlow.setAdapter(newBooksAdapter);

                    newbookRef.removeEventListener(mNewbookEventListener);
                    Log.d(LOG_FLAG, "Got Newbook List. Num : "+newBooksAdapter.getCount());
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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mTitle == null) {
            mTitle = getView().findViewById(R.id.title);
            mTitle.setFactory(mFactory);
        }
        Animation in = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(getActivity(),R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);

        // 放一本假的書進去 不然會跳錯誤 (divide 0)
        if(newBooksList.isEmpty()) {
            newBooksList.add(new CollectionSearchResultUnit());
        }
        newBooksAdapter = new NewBooksAdapter(newBooksList, getActivity());
        newBooksAdapter.notifyDataSetChanged();

        coverFlow = getView().findViewById(R.id.coverFlow);
        coverFlow.setAdapter(newBooksAdapter);
        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                if(newBooksList.size() > position) {
                    if(newBooksList.get(position).getSearchState().equals("true")){
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        CollectionBookDetailFragment collectionBookDetailFragment = (CollectionBookDetailFragment)fragmentManager.findFragmentByTag("CollectionBookDetailFragment");

                        if(collectionBookDetailFragment == null){
                            collectionBookDetailFragment = new CollectionBookDetailFragment();
                            collectionBookDetailFragment.setCollectionSearchResultUnit(newBooksList.get(position));
                            fragmentTransaction.setCustomAnimations(R.anim.slide_frag_in_right, R.anim.slide_frag_out_right, R.anim.slide_frag_in_right, R.anim.slide_frag_out_right);
                            fragmentTransaction.add(R.id.load_fragment,collectionBookDetailFragment, "CollectionBookDetailFragment");
                            fragmentTransaction.addToBackStack("CollectionBookDetailFragment");
                            fragmentTransaction.commit();
                        }
                    }
                    //loading中
                    else{
                        Toast.makeText(getActivity(), newBooksList.get(position).getDetail(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(newBooksList.get(position).getTitle());
            }

            @Override
            public void onScrolling() {

            }
        });
        if (newBooksList.get(0).isNull()){
            newbookRef = FirebaseDatabase.getInstance().getReference(NEWBOOK_PATH);
            newbookRef.addChildEventListener(mNewbookEventListener);
        }
    }

    private ViewSwitcher.ViewFactory mFactory = new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            TextView txt = (TextView)inflater.inflate(R.layout.layout_title,null);
            return txt;
        }
    };

}
