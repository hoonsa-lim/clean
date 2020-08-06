package com.example.clean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter_todayList extends BaseAdapter {
    private Context context;
    private ArrayList<TodayListData> arrayList;
    private boolean flag_visible_checkBox = false; // false 면 checkbox invisible
    private LinearLayout par_big_linear;
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

        view = layoutInflater.inflate(R.layout.listview_partition, null);

        //ui 찾기
        TextView par_toDoName = view.findViewById(R.id.par_toDoName);
        TextView par_time = view.findViewById(R.id.par_time);
        TextView par_alarm = view.findViewById(R.id.par_alarm);
        TextView par_tvRepetition = view.findViewById(R.id.par_tvRepetition);
        final CheckBox par_checkBox = view.findViewById(R.id.par_checkBox);
        LinearLayout par_linear = view.findViewById(R.id.par_linear);
        LinearLayout par_linear_checkBox = view.findViewById(R.id.par_linear_checkBox);
        LinearLayout par_linear_text = view.findViewById(R.id.par_linear_text);
        par_big_linear = view.findViewById(R.id.par_big_linear);

        //listview adapter 2번째 프래그 먼트에서 사용할 때와 1번째 프래그먼트에서 사용할 때를 구분함
        final TodayListData todayListData = arrayList.get(i);
        setValuesFunction(todayListData, par_toDoName, par_time, par_alarm, par_tvRepetition);
        par_tvRepetition.setVisibility(View.GONE);

        return view;
    }

    private void setValuesFunction(TodayListData todayListData, TextView par_toDoName, TextView par_time, TextView par_alarm, TextView par_tvRepetition) {
        //값 적용
        par_toDoName.setText(todayListData.getT_toDoName());
        par_time.setText(todayListData.getT_time());
        if (todayListData.getT_alarm() == 0) {
            par_alarm.setText("알람 x");
        } else {
            par_alarm.setText("알람 o");
        }
    }
}
