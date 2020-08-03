package com.example.clean;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class FragmentAddToDo_2 extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ToDoListActivity toDoListActivity;

    //UI
    private Spinner f2SpinnerRepetition, f2SpinnerAlarm;
    private LinearLayout f2Linear_repetition;
    private EditText f2EtToDoName;
    private TextView f2TvDate, f2TvTime, f2TvRepetition;
    private Button f2BtnBack, f2BtnSave;

    //spinner 관련
    private final int DATEPICKER = 1;
    private final int TIMEPICKER = 2;
    private final int REPETITION_CHECKBOX = 3;
    private ArrayList<String> arrayList_repetition;
    private ArrayList<String> arrayList_alarm;
    private LayoutInflater layoutInflater;
    private LinearLayout linearLayout;
    private CheckBox ckbMon, ckbTus, ckbWen, ckbTur, ckbFri, ckbSat, ckbSun;

    //activity 에서 넘겨준 공간 등록한 정보, db 저장을 위해 넘겨 받음
    private SpaceData spaceData;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toDoListActivity = (ToDoListActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        toDoListActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.add_to_do_fragment_2, container, false);

        //UI 찾기
        findViewByIdFunction(rootView);

        //layout inflater :
        layoutInflater = getLayoutInflater();

        //spaceData 받기
        Bundle bundle = getArguments();
        spaceData = bundle.getParcelable("spaceData");

        //spinner 세팅
        setRepetitionSpinner();//반복
        setAlarmSpinner();//알림

        //이벤트 등록
        f2TvDate.setOnClickListener(this);
        f2TvTime.setOnClickListener(this);
        f2BtnBack.setOnClickListener(this);
        f2BtnSave.setOnClickListener(this);
        f2SpinnerRepetition.setOnItemSelectedListener(this);
        f2SpinnerAlarm.setOnItemSelectedListener(this);

        return rootView;
    }

    //반복 spinner 세팅
    private void setRepetitionSpinner() {
        arrayList_alarm = new ArrayList<String>();
        arrayList_alarm.add(new String("반복 안함."));
        arrayList_alarm.add(new String("반복"));
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(toDoListActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_alarm);
        f2SpinnerRepetition.setAdapter(arrayAdapter);
    }

    //반복 spinner 세팅
    private void setAlarmSpinner() {
        arrayList_repetition = new ArrayList<String>();
        arrayList_repetition.add(new String("알림 안함."));
        arrayList_repetition.add(new String("알림"));
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(toDoListActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_repetition);
        f2SpinnerAlarm.setAdapter(arrayAdapter);
    }

    //UI 찾기
    private void findViewByIdFunction(View view) {
        f2TvDate = view.findViewById(R.id.f2TvDate);
        f2TvRepetition = view.findViewById(R.id.f2TvRepetition);
        f2EtToDoName = view.findViewById(R.id.f2EtToDoName);
        f2TvTime = view.findViewById(R.id.f2TvTime);
        f2SpinnerRepetition = view.findViewById(R.id.f2SpinnerRepetition);
        ckbMon = view.findViewById(R.id.ckbMon);
        ckbTus = view.findViewById(R.id.ckbTus);
        ckbWen = view.findViewById(R.id.ckbWen);
        ckbTur = view.findViewById(R.id.ckbTur);
        ckbFri = view.findViewById(R.id.ckbFri);
        ckbSat = view.findViewById(R.id.ckbSat);
        ckbSun = view.findViewById(R.id.ckbSun);
        f2SpinnerAlarm = view.findViewById(R.id.f2SpinnerAlarm);
        f2Linear_repetition = view.findViewById(R.id.f2Linear_repetition);
        f2BtnBack = view.findViewById(R.id.f2BtnBack);
        f2BtnSave = view.findViewById(R.id.f2BtnSave);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.f2TvDate:
                linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_date, null);
                dialogFunction(linearLayout, DATEPICKER);
                break;
            case R.id.f2TvTime:
                linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_time, null);
                dialogFunction(linearLayout, TIMEPICKER);
                break;
            case R.id.f2BtnBack:
                fragmentFinishFunction();
                break;
            case R.id.f2BtnSave:
                MyDBHelper myDBHelper = new MyDBHelper(toDoListActivity, "cleanDB");
                SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                byte[] image = spaceData.getImage();
                String spaceName = spaceData.getSpaceName();
                String toDoName = f2EtToDoName.getText().toString();
                String date = f2TvDate.getText().toString();
                String time = f2TvTime.getText().toString();
                int alarm = 0;
                if (f2SpinnerAlarm.getSelectedItem().toString().equals("알림")) {
                    alarm = 1;
                }
                String repetition = null;
                try {
                    repetition = f2TvRepetition.getText().toString().substring(8).trim();
                } catch (NullPointerException e) {
                    Log.d("FragmentAddToDo", e.getMessage());
                }

                if (image != null) {
                    SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement("insert into toDoListTBL " +
                            "values(?, '" + spaceName + "', '" + toDoName + "', '" + date + "', '"
                            + time + "',  '" + repetition + "', " + alarm + ",0);");
                    sqLiteStatement.bindBlob(1, image);
                    sqLiteStatement.execute();
                } else {
                    String query = "insert into toDoListTBL " +
                            "values(null, '" + spaceName + "', '" + toDoName + "', '" + date + "', '"
                            + time + "',  '" + repetition + "', " + alarm + ",0);";
                    sqLiteDatabase.execSQL(query);
                }
                Toast.makeText(toDoListActivity, "저장 됐습니다.", Toast.LENGTH_SHORT).show();
                toDoListActivity.finish();
                break;
            default:
                break;
        }
    }

    //dialog function
    private void dialogFunction(final View view, final int num) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(toDoListActivity);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (num) {
                    case DATEPICKER:
                        DatePicker datePicker = view.findViewById(R.id.datePicker);
                        f2TvDate.setText(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        break;
                    case TIMEPICKER:
                        TimePicker timePicker = view.findViewById(R.id.timePicker);
                        f2TvTime.setText(timePicker.getHour() + ":" + timePicker.getMinute());
                        break;
                    case REPETITION_CHECKBOX:
                        ckbMon = view.findViewById(R.id.ckbMon);
                        ckbTus = view.findViewById(R.id.ckbTus);
                        ckbWen = view.findViewById(R.id.ckbWen);
                        ckbTur = view.findViewById(R.id.ckbTur);
                        ckbFri = view.findViewById(R.id.ckbFri);
                        ckbSat = view.findViewById(R.id.ckbSat);
                        ckbSun = view.findViewById(R.id.ckbSun);
                        String strWeek = null;
                        if (ckbMon.isChecked()) {
                            strWeek = " 월";
                        }
                        if (ckbTus.isChecked()) {
                            if (strWeek != null) {
                                strWeek = strWeek + " 화";
                            } else {
                                strWeek = " 화";
                            }
                        }
                        if (ckbWen.isChecked()) {
                            if (strWeek != null) {
                                strWeek = strWeek + " 수";
                            } else {
                                strWeek = " 수";
                            }
                        }
                        if (ckbTur.isChecked()) {
                            if (strWeek != null) {
                                strWeek = strWeek + " 목";
                            } else {
                                strWeek = " 목";
                            }
                        }
                        if (ckbFri.isChecked()) {
                            if (strWeek != null) {
                                strWeek = strWeek + " 금";
                            } else {
                                strWeek = " 금";
                            }
                        }
                        if (ckbSat.isChecked()) {
                            if (strWeek != null) {
                                strWeek = strWeek + " 토";
                            } else {
                                strWeek = " 토";
                            }
                        }
                        if (ckbSun.isChecked()) {
                            if (strWeek != null) {
                                strWeek = strWeek + " 일";
                            } else {
                                strWeek = " 일";
                            }
                        }
                        f2TvRepetition.setText("반복 요일 : " + strWeek);
                        break;
                    default:
                        break;
                }
            }
        });
        alertDialog.setNegativeButton("취소", null);
        alertDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.f2SpinnerRepetition:
                String strRepetition = (String) f2SpinnerRepetition.getSelectedItem();
                if (strRepetition.equals("반복")) {
                    linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_repetition, null);
                    dialogFunction(linearLayout, REPETITION_CHECKBOX);
                    f2Linear_repetition.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f2SpinnerAlarm:
                String strAlarm = (String) f2SpinnerRepetition.getSelectedItem();
                if (strAlarm.equals("알림")) {

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //fragment 종료 함수
    public void fragmentFinishFunction(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(FragmentAddToDo_2.this).commit();
        fragmentManager.popBackStack();
    }
}

