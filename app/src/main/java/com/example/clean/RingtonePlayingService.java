package com.example.clean;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

//알림음 재생 및 종료를 관리, 또한 알림 notification 관리함
public class RingtonePlayingService extends Service {
    MediaPlayer mediaPlayer;
    int startId;//1이면 재생, 0이면 종료
    boolean isRunning; //media player 재생 상태

    private PendingIntent pendingIntent = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //전달 받은 값
        String getState = intent.getExtras().getString("state");
        String spaceName = intent.getExtras().getString("spaceName");
        String todoName = intent.getExtras().getString("todoName");

        //notification 눌렀을 때 나오는 화면
        Intent intentAlarm = new Intent(getApplicationContext(), AlarmEndActivity.class);
        intentAlarm.putExtra("spaceName", spaceName);
        intentAlarm.putExtra("todoName", todoName);
        pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                intentAlarm,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //state가 on일 때만 notification 발생
        if (!getState.equals("alarm off")) {
            if (Build.VERSION.SDK_INT >= 26) {
                String CHANNEL_ID = "default";
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                        "Clean App",
                        NotificationManager.IMPORTANCE_DEFAULT);

                ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setContentTitle("알람시작 : " + spaceName + "/" + todoName)
                        .setContentText("알람을 종료시키려면 '여기'를 클릭하세요.")
                        .setSmallIcon(R.mipmap.ic_launcher_bluecow_foreground)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentIntent(pendingIntent)
                        .build();

                startForeground(1, notification);
            }
        }else{
            stopForeground(true);
        }

        //아이디의 상태가 여기서 결정됨
        assert getState != null;
        switch (getState) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        // 알람음 재생 X , 알람음 시작 클릭
        if (!this.isRunning && startId == 1) {

            mediaPlayer = MediaPlayer.create(this, R.raw.earth_bound_slynk);
            mediaPlayer.start();

            this.isRunning = true;
            this.startId = 0;
        }

        // 알람음 재생 O , 알람음 종료 버튼 클릭
        else if (this.isRunning && startId == 0) {

            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();

            this.isRunning = false;
            this.startId = 0;
        }

        // 알람음 재생 X , 알람음 종료 버튼 클릭
        else if (!this.isRunning && startId == 0) {

            this.isRunning = false;
            this.startId = 0;

        }

        // 알람음 재생 O , 알람음 시작 버튼 클릭
        else if (this.isRunning && startId == 1) {

            this.isRunning = true;
            this.startId = 1;
        } else {
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestory() 실행", "서비스 파괴");
    }
}
