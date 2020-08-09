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
    private MainActivity mainActivity;

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
    private int flag_count;

    //activity 에서 넘겨준 공간 등록한 정보, db 저장을 위해 넘겨 받음
    private SpaceData spaceData;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.menuSearch.setVisibility(View.INVISIBLE);
        mainActivity.mSearch.setVisible(false);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.add_to_do_fragment, container, false);
        getActivity().setTitle("할일 수정");
        //UI 찾기
        findViewByIdFunction(rootView);

        //layout inflater :
        layoutInflater = getLayoutInflater();

        //spaceData 받기
        Bundle bundle = mainActivity.fragmentAddToDo_3.getArguments();
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
            //할일 이름, 날짜, 시간
            f3EtToDoName.setText(spaceData1.getToDoName());
            f3TvDate.setText(spaceData1.getDate());
            f3TvTime.setText(spaceData1.getTime());

            //반복 요일
            String repetition = "";
            String str[] = {spaceData1.getMon(), spaceData1.getTus(), spaceData1.getWen(),
                    spaceData1.getTur(), spaceData1.getFri(), spaceData1.getSat(), spaceData1.getSun()};
            for (int i = 0; i < str.length; i++) {
                if (!str[i].equals("null")) {
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

            //알림 여부
            if (spaceData1.getAlarm() == 0) {
                f3SpinnerAlarm.setSelection(0);
            } else {
                f3SpinnerAlarm.setSelection(1);
            }
        } catch (NullPointerException e) {
            Log.d("FragmentAddToDo_3", "NullPointer" + e.getMessage());
        }
    }

    //로드 db
    private SpaceData loadDataFunction(SpaceData sd) {
        MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
        SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();

        String todoName = sd.getToDoName();
        String date = sd.getDate();
        String time = sd.getTime();

        String query = "select toDoName, date, time, mon, tus, wen, tur, fri, sat, sun, alarm, clear from toDoListTBL where todoName = '" + todoName + "' and date = '" + date + "' and time = '" + time + "';";
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
        ArrayAdapter arrayAdapter1 = new ArrayAdapter<>(mainActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_alarm);
        Log.d("FragmentAddToDo", arrayAdapter1.toString());
        f3SpinnerAlarm.setAdapter(arrayAdapter1);
    }

    //알림 spinner 세팅
    private void setAlarmSpinner() {
        arrayList_repetition = new ArrayList<String>();
        arrayList_repetition.add(new String("반복 안함."));
        arrayList_repetition.add(new String("반복"));
        ArrayAdapter arrayAdapter1 = new ArrayAdapter<>(mainActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_repetition);
        Log.d("FragmentAddToDo", arrayAdapter1.toString());
        f3SpinnerRepetition.setAdapter(arrayAdapter1);
    }

    //UI 찾기
    private void findViewByIdFunction(View view) {
        f3TvDate = view.findViewById(R.id.f1TvDate);
        f3TvRepetition = view.findViewById(R.id.f1TvRepetition);
        f3EtToDoName = view.findViewById(R.id.f1EtToDoName);
        f3TvTime = view.findViewById(R.id.f1TvTime);
        f3SpinnerRepetition = view.findViewById(R.id.f1SpinnerRepetition);
        ckbMon = view.findViewById(R.id.ckbMon);
        ckbTus = view.findViewById(R.id.ckbTus);
        ckbWen = view.findViewById(R.id.ckbWen);
        ckbTur = view.findViewById(R.id.ckbTur);
        ckbFri = view.findViewById(R.id.ckbFri);
        ckbSat = view.findViewById(R.id.ckbSat);
        ckbSun = view.findViewById(R.id.ckbSun);
        f3SpinnerAlarm = view.findViewById(R.id.f1SpinnerAlarm);
        f3Linear_repetition = view.findViewById(R.id.f1Linear_repetition);
        f3BtnBack = view.findViewById(R.id.f1BtnBack);
        f3BtnEdit = view.findViewById(R.id.f1BtnSave);
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
                try {
                    MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
                    SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                    byte[] image = spaceData.getImage();
                    String spaceName = spaceData.getSpaceName();
                    String toDoName = f3EtToDoName.getText().toString();
                    String date = f3TvDate.getText().toString();
                    String time = f3TvTime.getText().toString();

                    //시간 00:00으로 자리수 맞추기
                    String[] array = time.split(":");

                    for (int i = 0; i < array.length; i++) {
                        if (array[i].length() < 2) {
                            array[i] = "0" + array[i];
                        }
                    }
                    time = array[0] + ":" + array[1];

                    //알람
                    int alarm = 0;
                    if (f3SpinnerAlarm.getSelectedItem().toString().equals("알림")) {
                        alarm = 1;
                    }

                    //반복여부
                    String mon = null;
                    String tus = null;
                    String wen = null;
                    String tur = null;
                    String fri = null;
                    String sat = null;
                    String sun = null;
                    String[] arrayWeek = {mon, tus, wen, tur, fri, sat, sun};
                    String repetition = null;

                    repetition = f3TvRepetition.getText().toString();
                    String[] array1 = repetition.split(":"); //"반복요일 : "를 버리기 위한
                    Log.d("FragmentAddToDo_2", array1.length + "");

                    String[] array2 = new String[7];
                    if (array1.length > 1) { // null 이면 1값이 나옴,구분된 요일이 1개라도 있다면
                        array2 = array1[1].trim().split(" ");//선택한 요일이 공백을 기준으로 구분됨
                        for (int i = 0; i < array2.length; i++) {//구분된 요일의 갯수보다 작을 때 반복
                            String[] week = {"월", "화", "수", "목", "금", "토", "일"};//선택한 요일과 비교하기 위한 배열
                            for (int j = 0; j < week.length; j++) {
                                if (array2[i].equals(week[j])) {
                                    arrayWeek[j] = week[j];
                                }
                            }
                        }

                        //쿼리문 실행 : 반복 요일 선택한 값이 있는 것
//                    String query = "update toDoListTBL " +
//                            "SET toDoName = '" +  + "', " +
//                            "date = '" +  + "', " +
//                            "time = '" +  + "', " +
//                            "mon = '" +  + "', " +
//                            "tus = '" +  + "', " +
//                            "wen = '" +  + "', " +
//                            "tur = '" +  + "', " +
//                            "fri = '" +  + "', " +
//                            "sat = '" +  + "', " +
//                            "sun = '" +  + "', " +
//                            "alarm = " +  +
//                            " WHERE spaceName = '" +  + "' " +
//                            "and  toDoName = '" +  + "' " +
//                        "and date = '" +  + "' " +
//                        "and  time = '" +  + "'; ";
//                    SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(query);
//                    sqLiteStatement.execute();
                    } else {
                        //쿼리문 : 반복요일이 없는 것, 반복 안함!


                    }
                    Toast.makeText(mainActivity, "저장 됐습니다.", Toast.LENGTH_SHORT).show();
                    fragmentFinishFunction();

                } catch (NullPointerException e) {
                    Log.d("FragmentAddToDo_3", "NullPointerException, 저장 버튼 시 예외 발생 : " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.d("FragmentAddToDo_3", "ArrayIndexOutOfBoundsException, 저장 버튼 시 예외 발생 : " + e.getMessage());
                    Toast.makeText(mainActivity, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //dialog function
    private void dialogFunction(final View view, final int num) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mainActivity);
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
                        if (month < 10) {
                            strMonth = "0" + month;
                        }else{
                            strMonth = String.valueOf(month);
                        }
                        String strDay = null;
                        int day = datePicker.getDayOfMonth();
                        if (day < 10) {
                            strDay = "0" + day;
                        }else{
                            strDay = String.valueOf(day);
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
            case R.id.f1SpinnerRepetition:
                String strRepetition = (String) f3SpinnerRepetition.getSelectedItem();
                if (strRepetition.equals("반복")) {
                    linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_repetition, null);
                    f3Linear_repetition.setVisibility(View.VISIBLE);
                    if (flag_count > 1) {
                        dialogFunction(linearLayout, REPETITION_CHECKBOX);
                    }
                    flag_count++;
                } else {
                    f3TvRepetition.setText("");
                    f3Linear_repetition.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.f1SpinnerAlarm:
                String strAlarm = (String) f3SpinnerAlarm.getSelectedItem();
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

    @Override
    public void onStop() {
        super.onStop();
        flag_count = 0;
    }

    //fragment 종료 함수
    public void fragmentFinishFunction() {
        mainActivity.tabHost.setCurrentTab(4);
    }
}

