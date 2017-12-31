package com.writerwriter.libraassist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Larry on 2017/12/31.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<NotificationUnit> notificationUnitList;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView left_days;
        public TextView location;

        public ViewHolder(View itemView){
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.notification_title);
            left_days = (TextView) itemView.findViewById(R.id.notification_left_days);
            location = (TextView) itemView.findViewById(R.id.notification_location);
        }
    }
    public NotificationAdapter(List<NotificationUnit> notificationUnits, Context context){
        this.notificationUnitList = notificationUnits;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return notificationUnitList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View notification_list_view = LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false);
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(notification_list_view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NotificationUnit notificationUnit = notificationUnitList.get(position);
        holder.title.setText(notificationUnit.getTitle());
        holder.left_days.setText(notificationUnit.getLeft_days());
        holder.location.setText(notificationUnit.getLocation());
        holder.itemView.setTag(position);
    }
}
