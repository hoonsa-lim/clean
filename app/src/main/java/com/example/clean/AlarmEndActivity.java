package com.example.clean;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmEndActivity extends AppCompatActivity {
    private Button alarm_btnAlarmEnd;
    private ImageView alarm_image;
    private TextView alarm_nowTime;
    private TextView alarm_spaceName;
    private TextView alarm_toDoName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //title bar 없애기
        getSupportActionBar().hide();
        setContentView(R.layout.activity_alarm_end);

        //ui 찾기
        findViewByIdFunction();

        //현재 시간 세팅
        setCurrentTime();

        try {
            Intent intent = getIntent();
            String spaceName = intent.getExtras().getString("spaceName");
            String todoName = intent.getExtras().getString("todoName");
            alarm_spaceName.setText(spaceName);
            alarm_toDoName.setText(todoName);
        } catch (NullPointerException e) {
            Log.d("AlarmEndActivity", getIntent().toString() + "NullPointerException : " + e.getMessage());
        }

        //알람 종료 버튼 이벤트
        alarm_btnAlarmEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AlarmEndActivity.this, "Alarm 종료", Toast.LENGTH_SHORT).show();

                // 알람리시버 intent 생성
                final Intent my_intent = new Intent(getApplicationContext(), AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, my_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                // reveiver에서 알람관리하는 activity에 전달할 string 값
                my_intent.putExtra("state", "alarm off");
                // 알람음 취소를 위한 receiver
                sendBroadcast(my_intent);

                // 알람매니저 취소
                AlarmManager alarm_manager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                alarm_manager.cancel(pendingIntent);

                //activity 종료
                finish();
            }
        });

    }

    //현재 시간 세팅
    private void setCurrentTime() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Calendar calendar = Calendar.getInstance(); // 칼렌다 변수
                            int hour = calendar.get(Calendar.HOUR_OF_DAY); // 시
                            int minute = calendar.get(Calendar.MINUTE); // 분
                            int second = calendar.get(Calendar.SECOND); // 초
                            alarm_nowTime.setText(hour + ":" + minute + ":" + second );
                        }
                    });
                    try {
                        Thread.sleep(1000); // 1000 ms = 1초
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

    //ui 찾기
    private void findViewByIdFunction() {
        alarm_btnAlarmEnd = findViewById(R.id.alarm_btnAlarmEnd);
        alarm_image = findViewById(R.id.alarm_image);
        alarm_nowTime = findViewById(R.id.alarm_nowTime);
        alarm_spaceName = findViewById(R.id.alarm_spaceName);
        alarm_toDoName = findViewById(R.id.alarm_toDoName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //noti 종료
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }
}
