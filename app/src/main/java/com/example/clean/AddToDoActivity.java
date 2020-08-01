package com.example.clean;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class AddToDoActivity extends AppCompatActivity implements View.OnClickListener {
    //UI
    private Spinner spinnerSpace, spinnerRepetition, spinnerAlarm;
    private LinearLayout linear_dayOfWeek;
    private EditText etToDoName;
    private TextView tvDate, tvTime;
    private CheckBox ckbMon, ckbTus, ckbWen, ckbTur, ckbFri, ckbSat, ckbSun;

    //spinner 관련
    private ArrayAdapter arrayAdapter;
    private final int DATEPICKER = 1;
    private final int TIMEPICKER = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        //UI 찾기
        findViewByIdFunction();

        //공간 spinner 세팅
        setSpaceSpinner();

        //이벤트 등록
        tvDate.setOnClickListener(this);
        tvTime.setOnClickListener(this);
    }

    //공간 spinner 세팅
    private void setSpaceSpinner() {
        //test 값
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(new String("공간을 선택해주세요."));
        arrayList.add(new String("방1"));
        arrayList.add(new String("방2"));
        arrayList.add(new String("방3"));
        arrayList.add(new String("거실"));
        arrayList.add(new String("침실"));

        arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);
        spinnerSpace.setAdapter(arrayAdapter);

    }

    //UI 찾기
    private void findViewByIdFunction() {
        spinnerSpace = findViewById(R.id.spinnerSpace);
        tvDate = findViewById(R.id.tvDate);
        etToDoName = findViewById(R.id.etToDoName);
        tvTime = findViewById(R.id.tvTime);
        spinnerRepetition = findViewById(R.id.spinnerRepetition);
        linear_dayOfWeek = findViewById(R.id.linear_dayOfWeek);
        ckbMon = findViewById(R.id.ckbMon);
        ckbTus = findViewById(R.id.ckbTus);
        ckbWen = findViewById(R.id.ckbWen);
        ckbTur = findViewById(R.id.ckbTur);
        ckbFri = findViewById(R.id.ckbFri);
        ckbSat = findViewById(R.id.ckbSat);
        ckbSun = findViewById(R.id.ckbSun);
        spinnerAlarm = findViewById(R.id.spinnerAlarm);
    }

    @Override
    public void onClick(View view) {
        LinearLayout linearLayout;
        LayoutInflater layoutInflater = getLayoutInflater();

        switch (view.getId()) {
            case R.id.tvDate:

                linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_date, null);
                dialogFunction(linearLayout, DATEPICKER);
                break;
            case R.id.tvTime:

                linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_time, null);
                dialogFunction(linearLayout, TIMEPICKER);
                break;
            default:
                break;
        }
    }

    //dialog function
    private void dialogFunction(final View view, final int num) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (num) {
                    case DATEPICKER:
                        DatePicker datePicker = view.findViewById(R.id.datePicker);
                        tvDate.setText(String.valueOf(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth()));
                        break;
                    case TIMEPICKER:
                        TimePicker timePicker = view.findViewById(R.id.timePicker);
                        tvTime.setText(String.valueOf(timePicker.getHour() + ":" + timePicker.getMinute()));
                        break;
                    default:
                        break;
                }
            }
        });
        alertDialog.setNegativeButton("취소", null);
        alertDialog.show();
    }
}
