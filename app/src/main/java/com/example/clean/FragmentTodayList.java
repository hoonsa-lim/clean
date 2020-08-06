package com.example.clean;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.CheckBox;
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
    private ImageButton f6IbNonClear, f6IbClear;
    private final int NON_CLEAR = 0;
    private final int CLEAR = 1;
    private LinearLayout linearLayout_getview;
    private boolean flag_page = false;

    //날짜
    private String toDayDate;
    private String dayOfWeek1;
    private long first_time;

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

        //list load
        listLoadFunction(NON_CLEAR);

        //값 세팅
        setUIValues();

        //listview click 이벤트
        f6ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                CheckBox checkBox = view.findViewById(R.id.par_checkBox);
                if(checkBox.isChecked() == true){
                    linearLayout_getview = (LinearLayout) view;
                    linearLayout_getview.animate().translationX(400f).alpha(1f).scaleX(1f).scaleY(1f).setDuration(1000).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            linearLayout_getview.setTranslationX(0f);
                            linearLayout_getview.setAlpha(0.1f);
                            linearLayout_getview.setScaleX(0.5f);
                            linearLayout_getview.setScaleY(0.5f);

                            TodayListData todayListData = arrayList.get(i);
                            MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
                            SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                            String query = null;
                            if(flag_page == false){
                                query = "update todayListTBL set t_clear = 1 where pk_fullName = '"+ todayListData.getPk_fullName()+"';";
                            }else{
                                query = "update todayListTBL set t_clear = 0 where pk_fullName = '"+ todayListData.getPk_fullName()+"';";
                            }
                            sqLiteDatabase.execSQL(query);

                            arrayList.remove(i);
                            f6ListView.invalidate();
                            listViewAdapter.notifyDataSetInvalidated();
                        }
                    }).start();
                }
            }
        });
        f6IbNonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listLoadFunction(NON_CLEAR);
                listViewAdapter.notifyDataSetChanged();
            }
        });
        f6IbClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listLoadFunction(CLEAR);
                listViewAdapter.notifyDataSetChanged();
            }
        });

        return viewGroup;
    }

    //값 세팅
    private void setUIValues() {
        toDayDate = toDayDate + " (" + dayOfWeek1 + ")";
        f6TvDate.setText(toDayDate);
    }

    //ui 찾기
    private void findViewByIdFunction(ViewGroup viewGroup) {
        f6ListView = viewGroup.findViewById(R.id.f6ListView);
        f6TvDate = viewGroup.findViewById(R.id.f6TvDate);
        f6IbNonClear = viewGroup.findViewById(R.id.f6IbNonClear);
        f6IbClear = viewGroup.findViewById(R.id.f6IbClear);
    }

    //목록 로드 함수
    public void listLoadFunction(int clearOrNonClear) {
        if(clearOrNonClear == 0){
            flag_page = false;
        }else{
            flag_page = true;
        }

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
            MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
            SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
            String query = "select * from todayListTBL where t_today = '" + toDayDate + "' and t_clear = "+ clearOrNonClear +";";
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            while (cursor.moveToNext() == true) {
                TodayListData todayListData = new TodayListData(cursor.getString(0),cursor.getString(1),
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
        } catch (Exception e) {
            Log.d("FragmentTodayList", e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        listViewAdapter.notifyDataSetInvalidated();
    }

    //main 반납 생명주기 마지막
    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}


