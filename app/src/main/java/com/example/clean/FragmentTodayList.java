package com.example.clean;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
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

    private ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();
    public ListViewAdapter listViewAdapter;
    private ListView f6ListView;
    private TextView f6TvToday, f6Text;

    //날짜
    private String toDayDate;
    private String dayOfWeek1;

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
        listLoadFunction();

//        f6TvToday.
//
//        f6ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                SpaceData sd = arrayList.get(i);
//                sd.setClear(1);
//
//                if(checkBox.isChecked() == true){
//                    try {
//                        MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
//                        SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
//
//                        String query = "UPDATE toDoListTBL SET clear = 1 " +
//                                "where spaceName = '" + sd.getSpaceName() + "' " +
//                                "and toDoName = '" + sd.getToDoName() + "' " +
//                                "and date = '" + sd.getDate() + "' " +
//                                "and time = '" + sd.getTime() + "';";
//                        sqLiteDatabase.execSQL(query);
//                        linearLayout.animate().alpha(0f).translationX(1000f).setDuration(700).withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                    linearLayout.setTranslationX(0f);
//                                    linearLayout.setAlpha(1f);
//                            }
//                        });
//                    }catch(Exception e){
//                        Log.d("FragmentTodayList", e.getMessage());
//                    }
//                }
//                arrayList.remove(i);
//                f6ListView.invalidate();
//                listViewAdapter.notifyDataSetInvalidated();
//            }
//        });



        return viewGroup;
    }


    //ui 찾기
    private void findViewByIdFunction(ViewGroup viewGroup) {
        f6ListView = viewGroup.findViewById(R.id.f6ListView);
        f6TvToday = viewGroup.findViewById(R.id.f6TvToday);
        f6Text = viewGroup.findViewById(R.id.f6Text);
    }

    //목록 로드 함수
    public void listLoadFunction() {
        //시스템 날짜 받기
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = simpleDate.format(mDate);

        //요일받기
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfWeek1 = "";
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
            String query = "select * from toDoListTBL where date = '" + toDayDate + "' and clear = 0;";
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


