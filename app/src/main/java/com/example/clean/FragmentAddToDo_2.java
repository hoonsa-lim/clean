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

public class FragmentAddToDo_2 extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private MainActivity mainActivity;

    //UI
    private LinearLayout f2Linear_repetition;
    private Spinner f2SpinnerRepetition, f2SpinnerAlarm;
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

        //초기화
        f2EtToDoName.setText("");
        f2TvDate.setText("");
        f2TvTime.setText("");
        f2TvRepetition.setText("");
        f2SpinnerRepetition.setSelection(0);
        f2SpinnerAlarm.setSelection(0);

        mainActivity.main_widget_linear.setVisibility(View.GONE);
        mainActivity.menuSearch.setVisibility(View.INVISIBLE);
        mainActivity.mSearch.setVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.add_to_do_fragment, container, false);
        getActivity().setTitle("할일 추가");
        //UI 찾기
        findViewByIdFunction(rootView);

        //layout inflater :
        layoutInflater = getLayoutInflater();

        //spaceData 받기
        Bundle bundle = mainActivity.fragmentAddToDo_2.getArguments();
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

    //알람 spinner 세팅
    private void setRepetitionSpinner() {
        arrayList_alarm = new ArrayList<String>();
        arrayList_alarm.add(new String("반복 안함."));
        arrayList_alarm.add(new String("반복"));
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_alarm);
        f2SpinnerRepetition.setAdapter(arrayAdapter);
    }

    //반복 spinner 세팅
    private void setAlarmSpinner() {
        arrayList_repetition = new ArrayList<String>();
        arrayList_repetition.add(new String("알림 안함."));
        arrayList_repetition.add(new String("알림"));
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_spinner_dropdown_item, arrayList_repetition);
        f2SpinnerAlarm.setAdapter(arrayAdapter);
    }

    //UI 찾기
    private void findViewByIdFunction(View view) {
        f2TvDate = view.findViewById(R.id.f1TvDate);
        f2TvRepetition = view.findViewById(R.id.f1TvRepetition);
        f2EtToDoName = view.findViewById(R.id.f1EtToDoName);
        f2TvTime = view.findViewById(R.id.f1TvTime);
        f2SpinnerRepetition = view.findViewById(R.id.f1SpinnerRepetition);
        ckbMon = view.findViewById(R.id.ckbMon);
        ckbTus = view.findViewById(R.id.ckbTus);
        ckbWen = view.findViewById(R.id.ckbWen);
        ckbTur = view.findViewById(R.id.ckbTur);
        ckbFri = view.findViewById(R.id.ckbFri);
        ckbSat = view.findViewById(R.id.ckbSat);
        ckbSun = view.findViewById(R.id.ckbSun);
        f2SpinnerAlarm = view.findViewById(R.id.f1SpinnerAlarm);
        f2Linear_repetition = view.findViewById(R.id.f1Linear_repetition);
        f2BtnBack = view.findViewById(R.id.f1BtnBack);
        f2BtnSave = view.findViewById(R.id.f1BtnSave);

        f2EtToDoName.requestFocus();

        //키보드 보이게 하는 부분
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
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
                    String toDoName = f2EtToDoName.getText().toString();
                    String date = f2TvDate.getText().toString();
                    String time = f2TvTime.getText().toString();

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
                    if (f2SpinnerAlarm.getSelectedItem().toString().equals("알림")) {
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

                    repetition = f2TvRepetition.getText().toString();
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
                        if (image != null) {
                            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement("insert into toDoListTBL " +
                                    "values(?, '" + spaceName + "', '" + toDoName + "', '" + date + "', '"
                                    + time + "',  '" + arrayWeek[0] + "',   '" + arrayWeek[1] + "',  '" + arrayWeek[2] + "',  '" + arrayWeek[3] + "',  '" + arrayWeek[4] + "',  '" + arrayWeek[5] + "',  '" + arrayWeek[6] + "'," + alarm + ",0);");
                            sqLiteStatement.bindBlob(1, image);
                            sqLiteStatement.execute();
                        } else {
                            String query = "insert into toDoListTBL " +
                                    "values(null, '" + spaceName + "', '" + toDoName + "', '" + date + "', '"
                                    + time + "',  '" + arrayWeek[0] + "',  '" + arrayWeek[1] + "',  '" + arrayWeek[2] + "',  '" + arrayWeek[3] + "',  '" + arrayWeek[4] + "',  '" + arrayWeek[5] + "',  '" + arrayWeek[6] + "', " + alarm + ",0);";
                            sqLiteDatabase.execSQL(query);
                        }
                    } else {
                        //반복요일이 없는 것, 반복 안함!
                        String query = "insert into toDoListTBL " +
                                "values(null, '" + spaceName + "', '" + toDoName + "', '" + date + "', '"
                                + time + "',  '" + null + "',  '" + null + "',  '" + null + "',  '" + null + "',  '" + null + "',  '" + null + "',  '" + null + "', " + alarm + ",0);";
                        sqLiteDatabase.execSQL(query);
                    }
                    Toast.makeText(mainActivity, "저장 됐습니다.", Toast.LENGTH_SHORT).show();
                    fragmentFinishFunction();
                } catch (NullPointerException e) {
                    Log.d("FragmentAddToDo_2", "NullPointerException, 저장 버튼 시 예외 발생 : " + e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    Log.d("FragmentAddToDo_2", "ArrayIndexOutOfBoundsException, 저장 버튼 시 예외 발생 : " + e.getMessage());
                    Toast.makeText(mainActivity, "빈칸을 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //dialog function
    private void dialogFunction(final View view, final int num) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mainActivity, R.style.MyCustomDialogStyle);
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
                        f2TvDate.setText(datePicker.getYear() + "-" + strMonth + "-" + strDay);
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
            case R.id.f1SpinnerRepetition:
                String strRepetition = (String) f2SpinnerRepetition.getSelectedItem();
                if (strRepetition.equals("반복")) {
                    linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_repetition, null);
                    dialogFunction(linearLayout, REPETITION_CHECKBOX);
                    f2Linear_repetition.setVisibility(View.VISIBLE);
                } else {
                    f2TvRepetition.setText("");
                    f2Linear_repetition.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.f1SpinnerAlarm:
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
    public void fragmentFinishFunction() {
        mainActivity.tabHost.setCurrentTab(4);
    }


}

