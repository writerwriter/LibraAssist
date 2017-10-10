package com.writerwriter.libraassist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Larry on 2017/10/11.
 */

public class NewBooksAdapter extends BaseAdapter {
    private List<NewBooks> newBooksList;
    private Context mContext;

    public NewBooksAdapter(List<NewBooks> newBooksList, Context mContext) {
        this.newBooksList = newBooksList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return newBooksList.size();
    }

    @Override
    public Object getItem(int i) {
        return newBooksList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if(rowView == null){
            rowView = LayoutInflater.from(mContext).inflate(R.layout.layout_item,null);

            TextView name = (TextView)rowView.findViewById(R.id.label);
            ImageView image = (ImageView)rowView.findViewById(R.id.image);

            //set data
            Picasso.with(mContext).load(newBooksList.get(i).getImageURL()).into(image);
            name.setText(newBooksList.get(i).getName());
        }
        return rowView;
    }
}
