package com.example.clean;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

        try {
            Intent intent = getIntent();
            String spaceName = intent.getExtras().getString("spaceName");
            String todoName = intent.getExtras().getString("todoName");
            alarm_spaceName.setText(spaceName);
            alarm_toDoName.setText(todoName);
        }catch (NullPointerException e){
            Log.d("AlarmEndActivity", getIntent().toString() + "NullPointerException : "+e.getMessage());
        }
        alarm_btnAlarmEnd = findViewById(R.id.alarm_btnAlarmEnd);
        alarm_image = findViewById(R.id.alarm_image);
        alarm_nowTime = findViewById(R.id.alarm_nowTime);
        alarm_spaceName = findViewById(R.id.alarm_spaceName);
        alarm_toDoName = findViewById(R.id.alarm_toDoName);

        alarm_btnAlarmEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AlarmEndActivity.this, "Alarm 종료", Toast.LENGTH_SHORT).show();

                // 알람리시버 intent 생성
                final Intent my_intent = new Intent(getApplicationContext(), AlarmReceiver.class);

                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, my_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                // 알람매니저 취소
                AlarmManager alarm_manager = (AlarmManager) getApplicationContext().getSystemService(ALARM_SERVICE);
                alarm_manager.cancel(pendingIntent);

                // reveiver에 string 값 넘겨주기
                my_intent.putExtra("state", "alarm off");

                // 알람취소
                sendBroadcast(my_intent);

                finish();
            }
        });

    }
}
