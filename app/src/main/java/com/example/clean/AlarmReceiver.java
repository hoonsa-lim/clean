package com.example.clean;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private PendingIntent pendingIntent = null;
    private Context context;
    private String get_yout_string, get_yout_string1,get_yout_string2;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;
        // intent로부터 전달받은 string
        get_yout_string = intent.getExtras().getString("state");
        get_yout_string1 = intent.getExtras().getString("spaceName");
        get_yout_string2 = intent.getExtras().getString("todoName");

        //알림음
        intentRingtonPlay();
    }

    //알림음 intent
    private void intentRingtonPlay() {
        // RingtonePlayingService 서비스 intent 생성
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        // RingtonePlayinService로 extra string값 보내기
        service_intent.putExtra("state", get_yout_string);
        service_intent.putExtra("spaceName", get_yout_string1);
        service_intent.putExtra("todoName", get_yout_string2);
        // start the ringtone service

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            this.context.startForegroundService(service_intent);
        }else{
            this.context.startService(service_intent);
        }
    }
}
