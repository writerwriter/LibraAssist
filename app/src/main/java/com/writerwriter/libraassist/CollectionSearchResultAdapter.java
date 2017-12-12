package com.writerwriter.libraassist;


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

import java.io.InputStream;
import java.util.List;

/**
 * Created by Larry on 2017/10/25.
 */

public class CollectionSearchResultAdapter extends RecyclerView.Adapter<CollectionSearchResultAdapter.ViewHolder> implements View.OnClickListener{

    private List<CollectionSearchResultUnit> collectionSearchResults;
    private OnItemClickListener mOnItemClickListener = null;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView;
        public TextView mTextView2;
        public ViewHolder(View itemView){
            super(itemView);

            mImageView = (ImageView)itemView.findViewById(R.id.bookcover);
            mTextView = (TextView)itemView.findViewById(R.id.book_name);
            mTextView2 = (TextView) itemView.findViewById(R.id.author);
        }

    }

    public CollectionSearchResultAdapter(List<CollectionSearchResultUnit> collectionSearchResults){
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
        holder.mTextView.setText(collectionSearchResultUnit.getName());
        new DownloadImageTask(holder.mImageView)
                .execute(collectionSearchResultUnit.getImg());

        holder.mTextView2.setText(collectionSearchResultUnit.getAuthor());
        holder.itemView.setTag(position);
    }

    public static interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View view) {
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }
    public void setmOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
