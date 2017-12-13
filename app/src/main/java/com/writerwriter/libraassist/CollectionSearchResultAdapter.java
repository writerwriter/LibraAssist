package com.writerwriter.libraassist;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Larry on 2017/10/25.
 */

public class CollectionSearchResultAdapter extends RecyclerView.Adapter<CollectionSearchResultAdapter.ViewHolder> implements View.OnClickListener{

    private List<CollectionSearchResultUnit> collectionSearchResults;
    private OnItemClickListener mOnItemClickListener = null;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView;
        public TextView mTextView2;
        public TextView mTextView3;
        public ViewHolder(View itemView){
            super(itemView);

            mImageView = (ImageView)itemView.findViewById(R.id.bookcover);
            mTextView = (TextView)itemView.findViewById(R.id.book_name);
            mTextView2 = (TextView) itemView.findViewById(R.id.author);
            mTextView3 = (TextView) itemView.findViewById(R.id.library);
        }

    }

    public CollectionSearchResultAdapter(Context context, List<CollectionSearchResultUnit> collectionSearchResults){
        this.context = context;
        this.collectionSearchResults = collectionSearchResults;
    }

    @Override
    public int getItemCount() {
        return collectionSearchResults.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View collection_result_view = LayoutInflater.from(context).inflate(R.layout.collection_search_result_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(collection_result_view);
        collection_result_view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CollectionSearchResultUnit collectionSearchResultUnit = collectionSearchResults.get(position);
        holder.mTextView.setText("書名:"+collectionSearchResultUnit.getName());

        Picasso.with(context).load(collectionSearchResultUnit.getImg()).into(holder.mImageView);

        holder.mTextView2.setText("作者:"+collectionSearchResultUnit.getAuthor());
        holder.mTextView3.setText("館藏地:"+collectionSearchResultUnit.getLibrary());
        holder.itemView.setTag(position);
    }

    public static interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
        // 暫時顯示詳細資訊
        Toast.makeText(view.getContext(),
                collectionSearchResults.get((int)view.getTag()).getDetail(),
                Toast.LENGTH_LONG).show();
    }
    public void setmOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
}
