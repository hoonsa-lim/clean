package com.example.clean;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InputProfileActivity extends AppCompatActivity {
    private EditText edtName;
    private RadioGroup rgGender;
    private RadioButton rdoMale;
    private RadioButton rdoFemale;
    private EditText edtAge;
    private Button btnPrev;
    private Button btnNext;

    private String nickName;
    private String gender;

    private MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_profile);
        setTitle("프로필 입력");

        edtName = findViewById(R.id.a2edtName);
        rgGender = findViewById(R.id.a2rgGender);
        rdoMale = findViewById(R.id.a2rdoMale);
        rdoFemale = findViewById(R.id.a2rdoFemale);
        edtAge = findViewById(R.id.a2edtAge);
        btnPrev = findViewById(R.id.a2btnPrev);
        btnNext = findViewById(R.id.a2btnNext);

        myDBHelper = new MyDBHelper(this,"cleanDB");

        Intent intent = getIntent();
        if(intent != null){
            nickName = intent.getStringExtra("nickName");
        }

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (rgGender.getCheckedRadioButtonId()){
                    case R.id.a2rdoMale:
                        gender = "남성";
                        break;
                    case R.id.a2rdoFemale:
                        gender = "여성";
                        break;
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtName.getText().toString().equals("")||edtAge.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "값을 제대로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    sqLiteDatabase = myDBHelper.getWritableDatabase();
                    sqLiteDatabase.execSQL("INSERT INTO myTBL VALUES ( '" + nickName + "' , '" + null + "' , " +
                            "'" + edtName.getText().toString() + "' , '" + gender + "' , '" + edtAge.getText().toString() + "');");
                    sqLiteDatabase.close();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NickNameActivity.class);
                startActivity(intent);
            }
        });
    }
}
