package com.example.clean;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AppInfomationActivity extends AppCompatActivity {
    private TextView tvView1;
    private TextView tvView2;
    private Button btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_infomation);
        setTitle("앱 정보");

        btnExit = findViewById(R.id.a4btnExit);
        tvView1 = findViewById(R.id.tvView1);
        tvView2 = findViewById(R.id.tvView2);

        tvView1.setMovementMethod(new ScrollingMovementMethod());
        tvView2.setMovementMethod(new ScrollingMovementMethod());

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
