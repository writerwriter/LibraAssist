package com.writerwriter.libraassist;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Larry on 2017/10/25.
 */

public class Library_info_list_adapter extends RecyclerView.Adapter<Library_info_list_adapter.ViewHolder>{

    private List<Library_info> library_infos;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView;
        public ImageView mIconView;
        public ViewHolder(View itemView){
            super(itemView);

            mImageView = (ImageView)itemView.findViewById(R.id.library_img);
            mTextView = (TextView)itemView.findViewById(R.id.library_name);
            mIconView = (ImageView)itemView.findViewById(R.id.library_icon);
        }

    }

    public Library_info_list_adapter(List<Library_info> library_infos){
        this.library_infos = library_infos;
    }

    @Override
    public int getItemCount() {
        return library_infos.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View library_list_view = LayoutInflater.from(context).inflate(R.layout.library_info_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(library_list_view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Library_info library_info = library_infos.get(position);
        holder.mTextView.setText(library_info.getName());
        holder.mImageView.setImageResource(library_info.getImg());
        holder.mIconView.setImageResource(library_info.getIcon());
    }
}
