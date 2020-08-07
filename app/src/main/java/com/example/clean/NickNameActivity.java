package com.example.clean;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class NickNameActivity extends AppCompatActivity {
    private EditText edtNickName;
    private Button btnPrev;
    private Button btnNext;

    private MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private String cursorData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        edtNickName = findViewById(R.id.a1edtNickName);
        btnPrev = findViewById(R.id.a1btnPrev);
        btnNext = findViewById(R.id.a1btnNext);

        try{

        }catch(Exception e){
            Log.d("NickNameActivity", "예외 발생 : "+ e.getMessage());
        }
        myDBHelper = new MyDBHelper(this,"cleanDB");
        sqLiteDatabase = myDBHelper.getReadableDatabase();
//        myDBHelper.onUpgrade(sqLiteDatabase, 0,1);
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM myTBL;", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            cursorData = cursor.getString(cursor.getColumnIndex("nickname"));
            Log.d("problem",cursorData);
        }

        if (cursorData == null) {
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(edtNickName.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), InputProfileActivity.class);
                        intent.putExtra("nickName", edtNickName.getText().toString());
                        startActivity(intent);
                    }
                }
            });
        } else if (cursorData != null) {
            //만약 닉네임 존재시 바로 메인으로.
            Toast.makeText(getApplicationContext(), cursorData + "님, 환영합니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        cursor.close();


        btnPrev.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
                finishAndRemoveTask();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }


}
