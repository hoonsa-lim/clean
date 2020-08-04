package com.example.clean;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentTodayList extends Fragment {
    private MainActivity mainActivity;

    private ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();
    private ListViewAdapter listViewAdapter;
    private ListView f6ListView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.todaylist_fragment, container, false);

        //ui 찾기
        findViewByIdFunction(viewGroup);
        listLoadFunction();

        return viewGroup;
    }


    //ui 찾기
    private void findViewByIdFunction(ViewGroup viewGroup) {
        f6ListView = viewGroup.findViewById(R.id.f6ListView);

    }

    //목록 로드 함수
    public void listLoadFunction() {
        //시스템 날짜 받기
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDate.format(mDate);

        //요일받기
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek1 = "";
        switch (dayOfWeek) {
            case 1:dayOfWeek1 = "일";
                break;
            case 2:dayOfWeek1 = "월";
                break;
            case 3:dayOfWeek1 = "화";
                break;
            case 4:dayOfWeek1 = "수";
                break;
            case 5:dayOfWeek1 = "목";
                break;
            case 6:dayOfWeek1 = "금";
                break;
            case 7:dayOfWeek1 = "토";
                break;
            default:
                break;
        }

        //db load
        try {
            arrayList.clear();
            MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
            SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
            String query = "select * from toDoListTBL where date = '" + date + "';";
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            while (cursor.moveToNext() == true) {
                SpaceData spaceData1 = new SpaceData(cursor.getBlob(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7),
                        cursor.getString(8), cursor.getString(9), cursor.getString(10),
                        cursor.getString(11), cursor.getInt(12), cursor.getInt(13));
                arrayList.add(spaceData1);
            }

//            String query1 = "select * from toDoListTBL where date = '" + date + "';";
//            Cursor cursor1 = sqLiteDatabase.rawQuery(query, null);
//            while (cursor.moveToNext() == true) {
//                SpaceData spaceData1 = new SpaceData(cursor.getBlob(0), cursor.getString(1),
//                        cursor.getString(2), cursor.getString(3), cursor.getString(4),
//                        cursor.getString(5), cursor.getString(6), cursor.getString(7),
//                        cursor.getString(8), cursor.getString(9), cursor.getString(10),
//                        cursor.getString(11), cursor.getInt(12), cursor.getInt(13));
//                arrayList.add(spaceData1);
//            }
            arrayList.add(new SpaceData(null, ""));

            //adapter 설정
            listViewAdapter = new ListViewAdapter(mainActivity, true);
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
        listViewAdapter.notifyDataSetChanged();
    }

    //main 반납 생명주기 마지막
    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }
}


