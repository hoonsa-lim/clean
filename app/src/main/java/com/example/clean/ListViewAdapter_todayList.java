package com.example.clean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter_todayList extends BaseAdapter {
    private Context context;
    private ArrayList<TodayListData> arrayList;
    private boolean flag_visible_checkBox = false; // false 면 checkbox invisible
    private FrameLayout par_big_linear;
    private boolean flag;

    public ListViewAdapter_todayList(Context context, boolean flag_visible_checkBox) {
        this.context = context;
        this.flag_visible_checkBox = flag_visible_checkBox;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<TodayListData> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<TodayListData> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.listview_partition_todaylist, null);

        //ui 찾기
        TextView par_toDoName = view.findViewById(R.id.par_toDoName_today);
        TextView par_time = view.findViewById(R.id.par_time_today);
        TextView par_alarm = view.findViewById(R.id.par_alarm_today);
        TextView par_tvRepetition = view.findViewById(R.id.par_tvRepetition_today);
        LinearLayout par_linear = view.findViewById(R.id.par_linear_today);
        LinearLayout par_linear_text = view.findViewById(R.id.par_linear_text_today);
        LinearLayout linear_todaylist_selector = view.findViewById(R.id.linear_todaylist_selector);
        par_big_linear = view.findViewById(R.id.par_big_linear_today);

        TodayListData todayListData = arrayList.get(i);

        //값 적용
        par_toDoName.setText(todayListData.getT_toDoName());
        par_time.setText(todayListData.getT_time());
        if (todayListData.getT_alarm() == 0) {
            par_alarm.setText("알람 x");
        } else {
            par_alarm.setText("알람 o");
        }

        //취소선
        if(todayListData.getT_clear() != 0){
            par_big_linear.setAlpha(0.3f);
            par_big_linear.setBackgroundColor(Color.GRAY);
            par_toDoName.setPaintFlags(par_toDoName.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            par_time.setPaintFlags(par_time.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            par_alarm.setPaintFlags(par_alarm.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        }

        par_tvRepetition.setVisibility(View.GONE);
        return view;
    }
}
