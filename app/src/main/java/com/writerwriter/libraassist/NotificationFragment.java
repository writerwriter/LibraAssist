package com.writerwriter.libraassist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class NotificationFragment extends Fragment {
    private List<BorrowBookUnit> allbooklist;
    private List<NotificationUnit> notificationUnitList = new ArrayList<>();
    private RecyclerView notificationListView;
    private LinearLayoutManager linearLayoutManager;
    private int notification_days = 20;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);

        allbooklist = AccountManager.Instance.borrowBookList;
        for(int i=0;i<allbooklist.size();i++) {
            BorrowBookUnit borrowBookUnit = allbooklist.get(i);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
            Date cur = new Date(System.currentTimeMillis());
            Date return_date = cur; //initial
            if(borrowBookUnit.getReturn_time().matches("[0-9]+/[0-9]+/[0-9]+")){
                try {
                    return_date = dateFormat1.parse(borrowBookUnit.getReturn_time());
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
            else if(borrowBookUnit.getReturn_time().matches("[0-9]+-[0-9]+-[0-9]+")){
                try{
                    return_date = dateFormat2.parse(borrowBookUnit.getReturn_time());
                }catch (ParseException e){
                    e.printStackTrace();
                }
            }
            long left_time_millis = return_date.getTime() - cur.getTime();
            int left_time_days =(int)(left_time_millis / 1000 / 60 / 60 / 24);
            Toast.makeText(getContext(),""+left_time_millis,Toast.LENGTH_SHORT).show();
            if(left_time_days > 0 && left_time_days <= notification_days){
                notificationUnitList.add(new NotificationUnit(borrowBookUnit.getBook_name(), ""+left_time_days,borrowBookUnit.getLocation()));

                Intent intent = new Intent();
                intent.setClass(getActivity(),AlarmReceiver.class);
                intent.putExtra("title",borrowBookUnit.getBook_name());
                intent.putExtra("time",left_time_days);
                intent.putExtra("id",i);
                PendingIntent pending = PendingIntent.getBroadcast(getContext(),i,intent,0);
                AlarmManager alarm = (AlarmManager) getContext().getSystemService(getContext().ALARM_SERVICE);
                alarm.set(AlarmManager.RTC_WAKEUP, cur.getTime()+30*1000,pending);
            }
        }
        NotificationAdapter adapter = new NotificationAdapter(notificationUnitList,getContext());
        notificationListView = (RecyclerView) v.findViewById(R.id.notification_recyclerview);
        notificationListView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        notificationListView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(notificationListView.getContext(),linearLayoutManager.getOrientation());
        notificationListView.addItemDecoration(dividerItemDecoration);
        notificationListView.setAdapter(adapter);

        return v;
    }

    public void setNotification_days(int notification_days) {
        this.notification_days = notification_days;
    }
}
