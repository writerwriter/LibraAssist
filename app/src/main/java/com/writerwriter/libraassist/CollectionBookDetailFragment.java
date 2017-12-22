package com.writerwriter.libraassist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CollectionBookDetailFragment extends Fragment {

    private CollectionSearchResultUnit collectionSearchResultUnit;

    ImageView cover;
    TextView title,author,ISBN,publisher,publish_year,remain,library;

    public CollectionBookDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_collection_book_detail, container, false);
        cover = v.findViewById(R.id.book_detail_cover);
        title = v.findViewById(R.id.book_detail_title);
        author = v.findViewById(R.id.book_detail_author);
        ISBN = v.findViewById(R.id.book_detail_ISBN);
        publisher = v.findViewById(R.id.book_detail_publisher);
        publish_year = v.findViewById(R.id.book_detail_publishYear);
        remain = v.findViewById(R.id.book_detail_remain);
        library = v.findViewById(R.id.book_detail_library);

        Picasso.with(getContext()).load(collectionSearchResultUnit.getImg()).into(cover);
        title.setText(collectionSearchResultUnit.getTitle());
        author.setText("作者:"+collectionSearchResultUnit.getAuthor());
        ISBN.setText("ISBN:"+collectionSearchResultUnit.getIsbn());
        publisher.setText("出版社:"+collectionSearchResultUnit.getPublisher());
        publish_year.setText("出版年份:"+collectionSearchResultUnit.getPublish_year());
        remain.setText("剩餘數量:"+collectionSearchResultUnit.getStorage());
        library.setText("館藏地:"+collectionSearchResultUnit.getLibrary());

        return v;
    }

    public void setCollectionSearchResultUnit(CollectionSearchResultUnit collectionSearchResultUnit){
        this.collectionSearchResultUnit = collectionSearchResultUnit;
    }


}
