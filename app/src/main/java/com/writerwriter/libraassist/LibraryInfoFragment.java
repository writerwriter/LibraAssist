package com.writerwriter.libraassist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LibraryInfoFragment extends Fragment {

    private RecyclerView library_info_list;
    private LinearLayoutManager mLayoutManager;
    private List<Library_info> library_infos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library_info, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        library_infos.add(new Library_info(R.drawable.ic_library_img1,"台北大學圖書館",R.drawable.ic_arrow_right_black_24px));
        library_infos.add(new Library_info(R.drawable.ic_library_img1,"新北市立圖書館",R.drawable.ic_arrow_right_black_24px));
        library_infos.add(new Library_info(R.drawable.ic_library_img1,"台北市立圖書館",R.drawable.ic_arrow_right_black_24px));

        library_info_list = (RecyclerView)getView().findViewById(R.id.library_info_recyclerview);
        library_info_list.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        library_info_list.setLayoutManager(mLayoutManager);

        Library_info_list_adapter adapter = new Library_info_list_adapter(library_infos);
        library_info_list.setAdapter(adapter);

    }
}
