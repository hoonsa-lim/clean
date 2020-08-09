package com.example.clean;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentTodayList extends Fragment {
    private MainActivity mainActivity;

    private ArrayList<TodayListData> arrayList = new ArrayList<TodayListData>();
    public ListViewAdapter_todayList listViewAdapter;
    private ListView f6ListView;
    private TextView f6TvDate;
    private FrameLayout linearLayout_getview;

    //날짜
    private String toDayDate;
    private String dayOfWeek1;
    private long first_time;

    //
    private View view_listview;
    private TextView todaylist_tvCount;

    //db
    private MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.todaylist_fragment, container, false);
        getActivity().setTitle("오늘의 할일");

        //ui 찾기
        findViewByIdFunction(viewGroup);

        //todaylist table에 값 추가
        getToDayInformation();

        //list load
        listLoadFunction();

        //값 세팅
        setUIValues();

        //listview click 이벤트
        f6ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, final int position, long l) {
                //다이얼로그 설정
                AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity, R.style.MyCustomDialogStyle);
                LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View v = layoutInflater.inflate(R.layout.dialog_listview, null);

                ImageButton dial_todaySuccess = v.findViewById(R.id.dial_todaySuccess);
                ImageButton dial_kakaoTalk = v.findViewById(R.id.dial_kakaoTalk);

                dial_todaySuccess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //다이얼로그 설정
                        AlertDialog.Builder alert2 = new AlertDialog.Builder(mainActivity, R.style.MyCustomDialogStyle);
                        LayoutInflater layoutInflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View v2 = layoutInflater.inflate(R.layout.dialog_listview_clear, null);
                        alert2.setNegativeButton("아니오", null);
                        alert2.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                linearLayout_getview = (FrameLayout) view;
                                linearLayout_getview.animate().alpha(1f).setDuration(500).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        linearLayout_getview.setAlpha(0.1f);

                                        TodayListData todayListData = arrayList.get(position);
                                        MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
                                        SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                                        String query = null;

                                        if (arrayList.get(position).getT_clear() == 0) {
                                            query = "update todayListTBL set t_clear = 1 where pk_fullName = '" + todayListData.getPk_fullName() + "';";
                                        } else if (arrayList.get(position).getT_clear() == 1) {
                                            query = "update todayListTBL set t_clear = 0 where pk_fullName = '" + todayListData.getPk_fullName() + "';";
                                        }
                                        sqLiteDatabase.execSQL(query);

                                        listLoadFunction();
                                        setUIValues();
                                        f6ListView.invalidate();
                                        listViewAdapter.notifyDataSetInvalidated();
                                    }
                                }).start();
                            }
                        });

                        //text 설정
                        TextView e1 = v2.findViewById(R.id.dial_editText1);
                        EditText e2 = v2.findViewById(R.id.dial_editText2);
                        e1.setText(arrayList.get(position).getT_spaceName());
                        e2.setText(arrayList.get(position).getT_toDoName());

                        alert2.setView(v2);
                        alert2.show();
                    }
                });


                dial_kakaoTalk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });


                alert.setView(v);
                alert.show();
            }
        });
        return viewGroup;
    }

    //남은 할일 수 세팅
    private void setUIValues() {
        int nonClearCount = 0;
        int clearCount = 0;
        for(int i = 0; i < arrayList.size(); i++){
            TodayListData todayListData = arrayList.get(i);
            if(todayListData.getT_clear() == 0){
                nonClearCount++;
            }else{
                clearCount++;
            }
        }
        todaylist_tvCount.setText(clearCount+"/"+arrayList.size());
    }

    //ui 찾기
    private void findViewByIdFunction(ViewGroup viewGroup) {
        f6ListView = viewGroup.findViewById(R.id.f6ListView);
        f6TvDate = viewGroup.findViewById(R.id.f6TvDate);
        todaylist_tvCount = viewGroup.findViewById(R.id.todaylist_tvCount);

    }

    //목록 로드 함수
    public void listLoadFunction() {

        //시스템 날짜 받기
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = simpleDate.format(mDate).trim();

        //요일받기
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfWeek1 = "";
        switch (dayOfWeek) {
            case 1:
                dayOfWeek1 = "일";
                break;
            case 2:
                dayOfWeek1 = "월";
                break;
            case 3:
                dayOfWeek1 = "화";
                break;
            case 4:
                dayOfWeek1 = "수";
                break;
            case 5:
                dayOfWeek1 = "목";
                break;
            case 6:
                dayOfWeek1 = "금";
                break;
            case 7:
                dayOfWeek1 = "토";
                break;
            default:
                break;
        }

        //db load
        try {
            arrayList.clear();
            myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
            sqLiteDatabase = myDBHelper.getReadableDatabase();
            String query = "select * from todayListTBL where t_today = '" + toDayDate + "' ORDER by t_clear ASC;";
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            while (cursor.moveToNext() == true) {
                TodayListData todayListData = new TodayListData(cursor.getString(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getInt(7),
                        cursor.getInt(8), cursor.getBlob(9));
                arrayList.add(todayListData);
            }

            //adapter 설정
            listViewAdapter = new ListViewAdapter_todayList(mainActivity, true);
            listViewAdapter.setArrayList(arrayList);
            f6ListView.setAdapter(listViewAdapter);
            listViewAdapter.notifyDataSetInvalidated();

            //날짜 세팅
            toDayDate = toDayDate + " (" + dayOfWeek1 + ")";
            f6TvDate.setText(toDayDate);
        } catch (Exception e) {
            Log.d("FragmentTodayList", e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {

            //list load
            listLoadFunction();

            //값 세팅
            setUIValues();
            listViewAdapter.notifyDataSetInvalidated();
            mainActivity.menuSearch.setVisibility(View.INVISIBLE);
            mainActivity.mSearch.setVisible(false);
        }catch (NullPointerException e){
            Log.d("FragmentTodayList", "NullPointerException" + e.getMessage());
        }
    }

    //main 반납 생명주기 마지막
    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    //today 정보 받아서 db에 저장
    private void getToDayInformation() {
        String query2 = null;

        //시스템 날짜 받기
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = simpleDate.format(mDate).trim();
        Log.d("MainActivity", toDayDate);

        //요일받기
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfWeek1 = "";
        switch (dayOfWeek) {
            case 1:
                dayOfWeek1 = "일";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '" + toDayDate + "') where sun = '" + dayOfWeek1 + "' and clear = 0;";
                break;
            case 2:
                dayOfWeek1 = "월";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '" + toDayDate + "') where mon = '" + dayOfWeek1 + "' and clear = 0;";
                break;
            case 3:
                dayOfWeek1 = "화";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '" + toDayDate + "') where tus = '" + dayOfWeek1 + "' and clear = 0;";
                break;
            case 4:
                dayOfWeek1 = "수";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '" + toDayDate + "') where wen = '" + dayOfWeek1 + "' and clear = 0;";
                break;
            case 5:
                dayOfWeek1 = "목";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '" + toDayDate + "') where tur = '" + dayOfWeek1 + "' and clear = 0;";
                break;
            case 6:
                dayOfWeek1 = "금";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '" + toDayDate + "') where fri = '" + dayOfWeek1 + "' and clear = 0;";
                break;
            case 7:
                dayOfWeek1 = "토";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '" + toDayDate + "') where sat = '" + dayOfWeek1 + "' and clear = 0;";
                break;
            default:
                break;
        }

        //쿼리문

        Cursor cursor;
        //오늘 날짜중 clear가 0인 값
        try {
            myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
            sqLiteDatabase = myDBHelper.getWritableDatabase();
//            myDBHelper.onUpgrade(sqLiteDatabase, 0 , 1);
            String query = "select * from toDoListTBL where date = '" + toDayDate + "' and clear = 0";
            cursor = sqLiteDatabase.rawQuery(query, null);

            //toDayListTBL에 먼저 저장, 나중에 저장 되는 값은 중복되지 않도록 하기 위함
            while (cursor.moveToNext() == true) {
                String str0 = cursor.getString(1) + cursor.getString(2) + cursor.getString(3) + cursor.getString(4);
                String str1 = cursor.getString(1);
                String str2 = cursor.getString(2);
                String str3 = cursor.getString(3);
                String str4 = cursor.getString(4);
                int str12 = cursor.getInt(12);
                int str13 = cursor.getInt(13);

                String query1 = "insert or ignore into toDayListTBL values( " +
                        " '" + str0 + "',  " +
                        " '" + toDayDate + "',  " +
                        " '" + str1 + "',  " +
                        " '" + str2 + "',  " +
                        " '" + str3 + "',  " +
                        " '" + dayOfWeek1 + "',  " +
                        " '" + str4 + "',  " +
                        " " + str12 + ",  " +
                        " " + str13 + ",  " +
                        " " + null + "); ";
                sqLiteDatabase.execSQL(query1);
            }
        } catch (Exception e) {
            Log.d("MainActivity", "Exception 발생 : 시작 날짜가 오늘인 data 중 clear가 0인 값" + e.getMessage());
        }

        //오늘날짜 요일을 기준으로, clear가 0이고, 오늘날짜보다 시작날짜가 이른, 값을 로드 후 toDayListTBL에 insert
        try {
            sqLiteDatabase = myDBHelper.getWritableDatabase();
            cursor = sqLiteDatabase.rawQuery(query2, null);
            cursor.moveToFirst();
            Log.d("MainActivity", dayOfWeek1 + "     " + query2);
            while (cursor.moveToNext() == true) {
                String str0 = cursor.getString(1) + cursor.getString(2) + cursor.getString(3) + cursor.getString(4);
                String str1 = cursor.getString(1);
                String str2 = cursor.getString(2);
                String str3 = cursor.getString(3);
                String str4 = cursor.getString(4);
                int str12 = cursor.getInt(12);
                int str13 = cursor.getInt(13);

                String query1 = "insert or ignore into toDayListTBL values( " +
                        " '" + str0 + "',  " +
                        " '" + toDayDate + "',  " +
                        " '" + str1 + "',  " +
                        " '" + str2 + "',  " +
                        " '" + str3 + "',  " +
                        " '" + dayOfWeek1 + "',  " +
                        " '" + str4 + "',  " +
                        " " + str12 + ",  " +
                        " " + str13 + ",  " +
                        " " + null + "); ";
                sqLiteDatabase.execSQL(query1);
            }
        } catch (Exception e) {
            Log.d("MainActivity", "Exception 발생 : " + e.getMessage());
        } finally {
            cursor = null;
            sqLiteDatabase = null;
        }
    }


}


