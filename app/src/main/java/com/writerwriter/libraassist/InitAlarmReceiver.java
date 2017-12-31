package com.writerwriter.libraassist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class InitAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        List<BorrowBookUnit> allbooklist;
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
            Toast.makeText(context,""+left_time_millis,Toast.LENGTH_SHORT).show();
            if(left_time_days > 0 && left_time_days <= 20){
                Intent intent2 = new Intent();
                intent2.setClass(context,AlarmReceiver.class);
                intent2.putExtra("title",borrowBookUnit.getBook_name());
                intent2.putExtra("time",left_time_days);
                PendingIntent pending = PendingIntent.getBroadcast(context,i,intent2,0);
                AlarmManager alarm = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
                alarm.set(AlarmManager.RTC_WAKEUP, cur.getTime()+60*1000,pending);
            }
        }

    }
}
