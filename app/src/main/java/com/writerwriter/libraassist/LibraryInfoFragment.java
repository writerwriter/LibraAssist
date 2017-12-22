package com.writerwriter.libraassist;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.graphics.drawable.AnimatedVectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LibraryInfoFragment extends Fragment {

    private RecyclerView library_info_list;
    private LinearLayoutManager mLayoutManager;
    private List<Library_info> library_infos = new ArrayList<>();
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_library_info, container, false);

        library_infos.clear();

        library_infos.add(new Library_info(R.drawable.ic_library_img1,"台北大學圖書館",R.drawable.ic_arrow_right_black_24px));
        library_infos.add(new Library_info(R.drawable.ic_library_img1,"新北市立圖書館",R.drawable.ic_arrow_right_black_24px));
        library_infos.add(new Library_info(R.drawable.ic_library_img1,"台北市立圖書館",R.drawable.ic_arrow_right_black_24px));

        library_info_list = (RecyclerView)v.findViewById(R.id.library_info_recyclerview);
        library_info_list.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        library_info_list.setLayoutManager(mLayoutManager);

        Library_info_list_adapter adapter = new Library_info_list_adapter(library_infos);
        library_info_list.setAdapter(adapter);
        adapter.setmOnItemClickListener(new Library_info_list_adapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LibraryInfoDetailFragment libraryInfoDetailFragment = (LibraryInfoDetailFragment)getFragmentManager().findFragmentByTag("LibraryInfoDetailFragment");
                if(libraryInfoDetailFragment == null) {
                    fragmentTransaction.setCustomAnimations(R.anim.slide_frag_in_right, R.anim.slide_frag_out_right, R.anim.slide_frag_in_right, R.anim.slide_frag_out_right);
                    fragmentTransaction.add(R.id.load_fragment,new LibraryInfoDetailFragment(), "LibraryInfoDetailFragment");
                    fragmentTransaction.addToBackStack("LibraryInfoDetailFragment");
                    fragmentTransaction.commit();
                }
                else{
                    fragmentManager.popBackStack();
                }

            }
        });

        // Inflate the layout for this fragment
        return v;
    }

}
