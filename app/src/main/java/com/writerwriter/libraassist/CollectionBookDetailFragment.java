package com.writerwriter.libraassist;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class CollectionBookDetailFragment extends Fragment {

    private CollectionSearchResultUnit collectionSearchResultUnit;

    ImageView cover;
    TextView title,author,ISBN,publisher,publish_year,remain,library;
    WebView web;
    Button btn;

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
        web = v.findViewById(R.id.collection_detail_borrow_book_web_url);
        btn = v.findViewById(R.id.borrow_button);

        Picasso.with(getContext()).load(collectionSearchResultUnit.getImg()).into(cover);
        title.setText(collectionSearchResultUnit.getTitle());
        author.setText("作者 : "+collectionSearchResultUnit.getAuthor());
        ISBN.setText("ISBN : "+collectionSearchResultUnit.getIsbn());
        publisher.setText("出版社 : "+collectionSearchResultUnit.getPublisher());
        publish_year.setText("出版年份 : "+collectionSearchResultUnit.getPublish_year());
        remain.setText("剩餘數量 : "+collectionSearchResultUnit.getStorage());
        switch (collectionSearchResultUnit.getLibrary()){
            case "ntc_lib":
                library.setText("館藏地 : 新北市立圖書館");
                break;
            case "ntpu_lib":
                library.setText("館藏地 : 台北大學圖書館");
                break;
            case "tc_lib":
                library.setText("館藏地 : 台北市立圖書館");
                break;
        }
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient());
        web.loadUrl(collectionSearchResultUnit.getLink());
        web.setBackgroundColor(Color.WHITE);
        web.setVisibility(View.GONE);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setDisplayZoomControls(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(web.getVisibility() == View.GONE){
                    btn.setText("返回");
                    web.setVisibility(View.VISIBLE);
                }
                else if(web.getVisibility() == View.VISIBLE){
                    btn.setText("預借書籍");
                    web.setVisibility(View.GONE);
                }
            }
        });

        return v;
    }

    public void setCollectionSearchResultUnit(CollectionSearchResultUnit collectionSearchResultUnit){
        this.collectionSearchResultUnit = collectionSearchResultUnit;
    }


}
