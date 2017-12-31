package com.writerwriter.libraassist;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Larry on 2017/12/31.
 */

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"fuck",Toast.LENGTH_SHORT).show();
        NotificationManager noMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_settings)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText("你借的書 : "+intent.getStringExtra("title")+" 即將在 "+intent.getIntExtra("time",1)+" 天後到期。");
        noMgr.notify(intent.getIntExtra("id",0),builder.build());
    }
}
