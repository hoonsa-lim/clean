package com.example.clean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clean.R;

import java.util.Calendar;
import java.util.jar.Attributes;

public class CalendarAdapter extends BaseAdapter {
    public Context mContext;
    public int layout;
    public com.example.clean.CalendarDAO[] items;
    public Calendar calendar;
    public LayoutInflater layoutInflater;
    private SQLiteDatabase db;

    public int firstDay;
    public int mStartDay;
    public int startDay;
    public int currentYear;
    public int currentMonth;
    public int lastDay;
    public int selectedPosition = -1;
    Integer[] markCheck = new Integer[49];

    public CalendarAdapter(Context mContext) {
        this.mContext = mContext;
        this.layout = R.layout.calendar_item;
        init();
    }

    public CalendarAdapter(Context mContext, Attributes attributes) {
        this.mContext = mContext;
        this.layout = R.layout.calendar_item;
        init();
    }

    //어댑터 시작시 달력 초기화 및 생성
    private void init() {
        items = new com.example.clean.CalendarDAO[49];
        calendar = Calendar.getInstance();
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        recalculate();
        resetDayNumbers();
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = layoutInflater.inflate(layout, viewGroup, false);
        }
        TextView tvCalendarDay = view.findViewById(R.id.tvCalendarDay);
        ImageView ivListResult = view.findViewById(R.id.ivListResult);

        final com.example.clean.CalendarDAO item = items[position];
        Log.d("Table3", String.valueOf(position));
        int columnIndex = position % 7;

        //일요일은 빨강, 토요일은 파랑으로 색 변경
        if (columnIndex == 0)
            tvCalendarDay.setTextColor(Color.RED);
        else if (columnIndex == 6)
            tvCalendarDay.setTextColor(Color.BLUE);
        else
            tvCalendarDay.setTextColor(Color.BLACK);

        //달력에 요일 표시 및 날짜 표시-DB연결 X.
        if (item.getDay() < 0) {
            ivListResult.setVisibility(View.INVISIBLE);
            switch (item.getDay()) {
                case -1:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("일");
                    break;
                case -2:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("월");
                    break;
                case -3:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("화");
                    break;
                case -4:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("수");
                    break;
                case -5:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("목");
                    break;
                case -6:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("금");
                    break;
                case -7:
                    tvCalendarDay.setVisibility(View.VISIBLE);
                    tvCalendarDay.setText("토");
                    break;

            }
        } else if (item.getDay() == 0) {
            tvCalendarDay.setVisibility(View.INVISIBLE);
            ivListResult.setVisibility(View.INVISIBLE);
        } else {

//            db = DBHelper.getInstance(mContext.getApplicationContext()).getWritableDatabase();
//            //오늘 날의 성공한 리스트 갯수
//            Cursor cursor = db.rawQuery("SELECT checkCount FROM cleaningTBL WHERE checkCount=" + 1 + " AND year=" + currentYear + " AND month=" + (currentMonth + 1) + " AND day=" + item.getDay() + ";", null);
//            //오늘의 리스트 총 갯수
//            Cursor cursor1 = db.rawQuery("SELECT checkCount FROM cleaningTBL WHERE year=" + currentYear + " AND month=" + (currentMonth + 1) + " AND day=" + item.getDay() + ";", null);
//            int size1 = cursor.getCount();
//            int size2 = cursor1.getCount();
//
            tvCalendarDay.setVisibility(View.VISIBLE);
            tvCalendarDay.setText(String.valueOf(item.getDay()));
//            //리스트가 있으면
//            if (size2 != 0) {
//                //성공 갯수가 리스트 전체 갯수와 같다.
//                if (size1 == size2) {
//                    ivListResult.setVisibility(View.VISIBLE);
//                    ivListResult.setImageResource(R.drawable.backbutton);
//                    ivListResult.setAlpha(100);
//                    markCheck[position] =1;
//                    //성공 갯수가 리스트 전체 갯수와 같다.
//                } else {
//                    ivListResult.setVisibility(View.VISIBLE);
//                    ivListResult.setImageResource(R.drawable.ic_launcher_background);
//                    ivListResult.setAlpha(100);
//                    markCheck[position] =2;
//                }
//                //리스트가 아예 없을 때
//            } else if (size2 == 0) {
//                ivListResult.setVisibility(View.INVISIBLE);
//                markCheck[position] =0;
//
//            }
            Log.d("MarkCheck",String.valueOf(markCheck));
            //오늘 day 텍스트 컬러 변경
            if ((Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == item.getDay())&&(Calendar.getInstance().get(Calendar.MONTH) == currentMonth)
                    &&(Calendar.getInstance().get(Calendar.YEAR) == currentYear)) {
                tvCalendarDay.setTextColor(Color.GREEN);
            }
            //날짜 클릭했을 때 이벤트
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (0){                     //markCheck[position]
                        case 0: toastDisplay("자료가 없습니다."); break;
                        case 1: toastDisplay("청소를 완료한 날!"); break;
                        case 2: toastDisplay("청소를 다 하지 못한 날..."); break;
                    }
                }
            });
//            cursor.close();
//            cursor1.close();
        }

        return view;
    }

    private void toastDisplay(String s) {
        Toast.makeText(mContext,s,Toast.LENGTH_SHORT).show();
    }

    //요일에 따른 달력 시작 확인
    private int getFirstDayOfWeek() {
        int startDay = Calendar.getInstance().getFirstDayOfWeek();
        switch (startDay) {
            case Calendar.SATURDAY:
                return Time.SATURDAY;
            case Calendar.MONDAY:
                return Time.MONDAY;
            case Calendar.SUNDAY:
                return Time.SUNDAY;
        }
        return 0;
    }

    //달력 날짜 세팅
    private void resetDayNumbers() {
        int dayNum = 0;
        for (int i = 0; i < 49; i++) {
            if (i < 7) {
                dayNum = -(i + 1);
            } else {
                dayNum = ((i - 7) + 1) - firstDay;

                if ((dayNum < 1) || dayNum > lastDay) {
                    dayNum = 0;
                }
            }
            items[i] = new com.example.clean.CalendarDAO(dayNum);
        }
    }

    //달의 시작 날짜 구하기
    private int getFirstDay(int dayOfWeek) {
        int firstDay = 0;
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                firstDay = 0;
                break;
            case Calendar.MONDAY:
                firstDay = 1;
                break;
            case Calendar.TUESDAY:
                firstDay = 2;
                break;
            case Calendar.WEDNESDAY:
                firstDay = 3;
                break;
            case Calendar.THURSDAY:
                firstDay = 4;
                break;
            case Calendar.FRIDAY:
                firstDay = 5;
                break;
            case Calendar.SATURDAY:
                firstDay = 6;
                break;
        }
        return firstDay;
    }

    //매달의 날짜 구하기
    private int getMonthLastDay(int currentYear, int currentMonth) {
        switch (currentMonth + 1) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;

            case 4:
            case 6:
            case 9:
            case 11:
                return 30;

            default:
                if ((currentYear % 4 == 0) && (currentYear % 100 != 0) || currentYear % 400 == 0) {
                    return 29;
                } else {
                    return 28;
                }
        }
    }

    //달력 생성
    private void recalculate() {
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        firstDay = getFirstDay(dayOfWeek);
        mStartDay = calendar.getFirstDayOfWeek();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        lastDay = getMonthLastDay(currentYear, currentMonth);
        startDay = getFirstDayOfWeek();
    }

    //이전달로 이동
    public void setPreviousMonth() {
        calendar.add(Calendar.MONTH, -1);
        recalculate();
        resetDayNumbers();
        selectedPosition = -1;
    }

    //다음달로 이동
    public void setNextMonth() {
        calendar.add(Calendar.MONTH, 1);
        recalculate();
        resetDayNumbers();
        selectedPosition = -1;
    }
}
