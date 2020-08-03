package com.example.clean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
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

import java.util.ArrayList;

public class FragmentAddToDo extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private AddSpaceAndToDoActivity addSpaceAndToDoActivity;

    //UI
    private Spinner f1SpinnerRepetition, f1SpinnerAlarm;
    private LinearLayout f1Linear_repetition;
    private EditText f1EtToDoName;
    private TextView f1TvDate, f1TvTime, f1TvRepetition;
    private Button f1BtnBack, f1BtnSave;

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
        addSpaceAndToDoActivity = (AddSpaceAndToDoActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addSpaceAndToDoActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.add_to_do_fragment, container, false);

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
        f1TvDate.setOnClickListener(this);
        f1TvTime.setOnClickListener(this);
        f1BtnBack.setOnClickListener(this);
        f1BtnSave.setOnClickListener(this);
        f1SpinnerRepetition.setOnItemSelectedListener(this);
        f1SpinnerAlarm.setOnItemSelectedListener(this);

        return rootView;
    }

    //반복 spinner 세팅
    private void setRepetitionSpinner() {
        arrayList_alarm = new ArrayList<String>();
        arrayList_alarm.add(new String("반복 안함."));
        arrayList_alarm.add(new String("반복"));
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(addSpaceAndToDoActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_alarm);
        f1SpinnerRepetition.setAdapter(arrayAdapter);
    }

    //반복 spinner 세팅
    private void setAlarmSpinner() {
        arrayList_repetition = new ArrayList<String>();
        arrayList_repetition.add(new String("알림 안함."));
        arrayList_repetition.add(new String("알림"));
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(addSpaceAndToDoActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_repetition);
        f1SpinnerAlarm.setAdapter(arrayAdapter);
    }

    //UI 찾기
    private void findViewByIdFunction(View view) {
        f1TvDate = view.findViewById(R.id.f1TvDate);
        f1TvRepetition = view.findViewById(R.id.f1TvRepetition);
        f1EtToDoName = view.findViewById(R.id.f1EtToDoName);
        f1TvTime = view.findViewById(R.id.f1TvTime);
        f1SpinnerRepetition = view.findViewById(R.id.f1SpinnerRepetition);
        ckbMon = view.findViewById(R.id.ckbMon);
        ckbTus = view.findViewById(R.id.ckbTus);
        ckbWen = view.findViewById(R.id.ckbWen);
        ckbTur = view.findViewById(R.id.ckbTur);
        ckbFri = view.findViewById(R.id.ckbFri);
        ckbSat = view.findViewById(R.id.ckbSat);
        ckbSun = view.findViewById(R.id.ckbSun);
        f1SpinnerAlarm = view.findViewById(R.id.f1SpinnerAlarm);
        f1Linear_repetition = view.findViewById(R.id.f1Linear_repetition);
        f1BtnBack = view.findViewById(R.id.f1BtnBack);
        f1BtnSave = view.findViewById(R.id.f1BtnSave);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.f1TvDate:
                linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_date, null);
                dialogFunction(linearLayout, DATEPICKER);
                break;
            case R.id.f1TvTime:
                linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_time, null);
                dialogFunction(linearLayout, TIMEPICKER);
                break;
            case R.id.f1BtnBack:
                fragmentFinishFunction();
                break;
            case R.id.f1BtnSave:
                MyDBHelper myDBHelper = new MyDBHelper(addSpaceAndToDoActivity, "cleanDB");
                SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                byte[] image = spaceData.getImage();
                String spaceName = spaceData.getSpaceName();
                String toDoName = f1EtToDoName.getText().toString();
                String date = f1TvDate.getText().toString();
                String time = f1TvTime.getText().toString();
                int alarm = 0;
                if (f1SpinnerAlarm.getSelectedItem().toString().equals("알림")) {
                    alarm = 1;
                }
                String repetition = null;
                try {
                    repetition = f1TvRepetition.getText().toString().substring(8).trim();
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
                Toast.makeText(addSpaceAndToDoActivity, "저장 됐습니다.", Toast.LENGTH_SHORT).show();
                addSpaceAndToDoActivity.finish();
                break;
            default:
                break;
        }
    }

    //dialog function
    private void dialogFunction(final View view, final int num) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(addSpaceAndToDoActivity);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (num) {
                    case DATEPICKER:
                        DatePicker datePicker = view.findViewById(R.id.datePicker);
                        f1TvDate.setText(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        break;
                    case TIMEPICKER:
                        TimePicker timePicker = view.findViewById(R.id.timePicker);
                        f1TvTime.setText(timePicker.getHour() + ":" + timePicker.getMinute());
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
                        f1TvRepetition.setText("반복 요일 : " + strWeek);
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
            case R.id.f1SpinnerRepetition:
                String strRepetition = (String) f1SpinnerRepetition.getSelectedItem();
                if (strRepetition.equals("반복")) {
                    linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_repetition, null);
                    dialogFunction(linearLayout, REPETITION_CHECKBOX);
                    f1Linear_repetition.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f1SpinnerAlarm:
                String strAlarm = (String) f1SpinnerRepetition.getSelectedItem();
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
        fragmentManager.beginTransaction().remove(FragmentAddToDo.this).commit();
        fragmentManager.popBackStack();
        addSpaceAndToDoActivity.a1Linear_big.setVisibility(View.VISIBLE);
    }
}

