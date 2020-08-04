package com.example.clean;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

public class FragmentAddToDo_3 extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ToDoListActivity toDoListActivity;

    //UI
    private Spinner f3SpinnerRepetition, f3SpinnerAlarm;
    private LinearLayout f3Linear_repetition;
    private EditText f3EtToDoName;
    private TextView f3TvDate, f3TvTime, f3TvRepetition;
    private Button f3BtnBack, f3BtnEdit;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.add_to_do_fragment_3, container, false);

        //UI 찾기
        findViewByIdFunction(rootView);

        //layout inflater :
        layoutInflater = getLayoutInflater();

        //spaceData 받기
        Bundle bundle = getArguments();
        spaceData = bundle.getParcelable("spaceData1");

        //spinner 세팅
        setRepetitionSpinner();//반복
        setAlarmSpinner();//알림

        //db로드
        SpaceData spaceData1 = loadDataFunction(spaceData);

        //값 세팅
        setDBValues(spaceData1);

        //이벤트 등록
        f3TvDate.setOnClickListener(this);
        f3TvTime.setOnClickListener(this);
        f3BtnBack.setOnClickListener(this);
//        f3BtnSave.setOnClickListener(this);
        f3SpinnerRepetition.setOnItemSelectedListener(this);
        f3SpinnerAlarm.setOnItemSelectedListener(this);

        return rootView;
    }

    //값 세팅
    private void setDBValues(SpaceData spaceData1) {
        try {
            f3EtToDoName.setText(spaceData1.getToDoName());
            f3TvDate.setText(spaceData1.getDate());
            f3TvTime.setText(spaceData1.getTime());
            String repetition = "";
            String str[] = {spaceData1.getMon(), spaceData1.getTus(), spaceData1.getWen(),
                    spaceData1.getTur(), spaceData1.getFri(), spaceData1.getSat(), spaceData1.getSun()};
            for (int i = 0; i < str.length; i++) {
                if (str[i].equals("")) {
                    repetition = repetition + " " + str[i];
                }
            }

            if (repetition.trim().equals("")) {
                f3SpinnerRepetition.setSelection(0);
            } else {
                f3SpinnerRepetition.setSelection(1);
                f3TvRepetition.setVisibility(View.VISIBLE);
                f3Linear_repetition.setVisibility(View.VISIBLE);
                f3TvRepetition.setText("반복 요일 : " + repetition);
            }

            if (spaceData1.getAlarm() == 0) {
                f3SpinnerAlarm.setSelection(0);
            } else {
                f3SpinnerAlarm.setSelection(1);
            }
        }catch (NullPointerException e){
            Log.d("FragmentAddToDo_3", "NullPointer" + e.getMessage());
        }
    }

    //로드 db
    private SpaceData loadDataFunction(SpaceData sd) {
        MyDBHelper myDBHelper = new MyDBHelper(toDoListActivity, "cleanDB");
        SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();

        String todoName = sd.getToDoName();
        String date = sd.getDate();
        String time = sd.getTime();

        String query = "select toDoName, date, time, mon, tus, wen, tur, fri, sat, sun, alarm, clear from toDoListTBL where todoName = '"+ todoName +"' and date = '" + date + "' and time = '" + time +"';";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        cursor.moveToFirst();
        SpaceData spaceData1 = new SpaceData(cursor.getString(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7),
                cursor.getString(8), cursor.getString(9), cursor.getInt(10),
                cursor.getInt(11));
        cursor = null;
        sqLiteDatabase = null;
        return spaceData1;
    }

    //반복 spinner 세팅
    private void setRepetitionSpinner() {
        arrayList_alarm = new ArrayList<String>();
        arrayList_alarm.add(new String("알림 안함."));
        arrayList_alarm.add(new String("알림"));
        ArrayAdapter arrayAdapter1 = new ArrayAdapter<>(toDoListActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_alarm);
        Log.d("FragmentAddToDo", arrayAdapter1.toString());
        f3SpinnerAlarm.setAdapter(arrayAdapter1);
        f3SpinnerAlarm.setEnabled(false);
    }

    //알림 spinner 세팅
    private void setAlarmSpinner() {
        arrayList_repetition = new ArrayList<String>();
        arrayList_repetition.add(new String("반복 안함."));
        arrayList_repetition.add(new String("반복"));
        ArrayAdapter arrayAdapter1 = new ArrayAdapter<>(toDoListActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_repetition);
        Log.d("FragmentAddToDo", arrayAdapter1.toString());
        f3SpinnerRepetition.setAdapter(arrayAdapter1);
        f3SpinnerRepetition.setEnabled(false);
    }

    //UI 찾기
    private void findViewByIdFunction(View view) {
        f3TvDate = view.findViewById(R.id.f3TvDate);
        f3TvRepetition = view.findViewById(R.id.f3TvRepetition);
        f3EtToDoName = view.findViewById(R.id.f3EtToDoName);
        f3TvTime = view.findViewById(R.id.f3TvTime);
        f3SpinnerRepetition = view.findViewById(R.id.f3SpinnerRepetition);
        ckbMon = view.findViewById(R.id.ckbMon);
        ckbTus = view.findViewById(R.id.ckbTus);
        ckbWen = view.findViewById(R.id.ckbWen);
        ckbTur = view.findViewById(R.id.ckbTur);
        ckbFri = view.findViewById(R.id.ckbFri);
        ckbSat = view.findViewById(R.id.ckbSat);
        ckbSun = view.findViewById(R.id.ckbSun);
        f3SpinnerAlarm = view.findViewById(R.id.f3SpinnerAlarm);
        f3Linear_repetition = view.findViewById(R.id.f3Linear_repetition);
        f3BtnBack = view.findViewById(R.id.f3BtnBack);
        f3BtnEdit = view.findViewById(R.id.f3BtnEdit);

        f3EtToDoName.requestFocus();

        //키보드 보이게 하는 부분
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.f3TvDate:
                linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_date, null);
                dialogFunction(linearLayout, DATEPICKER);
                break;
            case R.id.f3TvTime:
                linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_time, null);
                dialogFunction(linearLayout, TIMEPICKER);
                break;
            case R.id.f3BtnBack:
                fragmentFinishFunction();
                break;
            case R.id.f3BtnEdit:
                MyDBHelper myDBHelper = new MyDBHelper(toDoListActivity, "cleanDB");
                SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                byte[] image = spaceData.getImage();
                String spaceName = spaceData.getSpaceName();
                String toDoName = f3EtToDoName.getText().toString();
                String date = f3TvDate.getText().toString();
                String time = f3TvTime.getText().toString();
                int alarm = 0;
                if (f3SpinnerAlarm.getSelectedItem().toString().equals("알림")) {
                    alarm = 1;
                }
                String repetition = null;
                try {
                    repetition = f3TvRepetition.getText().toString().substring(8).trim();
                } catch (NullPointerException e) {
                    Log.d("FragmentAddToDo", e.getMessage());
                }

                if (image != null) {
                    SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement("insert into toDoListTBL " +
                            "values(?, '" + spaceName + "', '" + toDoName + "', '" + date + "', '"
                            + time + "',  '" + repetition + "', " + alarm + ",0);");
                    sqLiteStatement.bindBlob(1, image);
                    sqLiteStatement.execute();
                    sqLiteStatement = null;
                } else {
                    String query = "insert into toDoListTBL " +
                            "values(null, '" + spaceName + "', '" + toDoName + "', '" + date + "', '"
                            + time + "',  '" + repetition + "', " + alarm + ",0);";
                    sqLiteDatabase.execSQL(query);
                    sqLiteDatabase = null;
                }
                Toast.makeText(toDoListActivity, "저장 됐습니다.", Toast.LENGTH_SHORT).show();
                toDoListActivity.finish();
                toDoListActivity.listLoadFunction();
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
                        String strMonth = null;
                        int month = (datePicker.getMonth() + 1);
                        if(month < 10){
                            strMonth = "0"+month;
                        }
                        String strDay = null;
                        int day = datePicker.getDayOfMonth();
                        if(day < 10){
                            strDay = "0"+day;
                        }

                        f3TvDate.setText(datePicker.getYear() + "-" + strMonth + "-" + strDay);
                        break;
                    case TIMEPICKER:
                        TimePicker timePicker = view.findViewById(R.id.timePicker);
                        f3TvTime.setText(timePicker.getHour() + ":" + timePicker.getMinute());
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
                        f3TvRepetition.setText("반복 요일 : " + strWeek);
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
            case R.id.f3SpinnerRepetition:
                String strRepetition = (String) f3SpinnerRepetition.getSelectedItem();
                if (strRepetition.equals("반복")) {
                    linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_repetition, null);
//                    dialogFunction(linearLayout, REPETITION_CHECKBOX);
                    f3Linear_repetition.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.f3SpinnerAlarm:
                String strAlarm = (String) f3SpinnerRepetition.getSelectedItem();
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
    public void fragmentFinishFunction() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().remove(FragmentAddToDo_3.this).commit();
        fragmentManager.popBackStack();
    }
}

