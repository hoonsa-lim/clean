package com.example.clean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SpaceData> arrayList;
    private boolean flag_visible_checkBox = false; // false 면 checkbox invisible

    public ListViewAdapter(Context context, boolean flag_visible_checkBox) {
        this.context = context;
        this.flag_visible_checkBox = flag_visible_checkBox;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<SpaceData> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<SpaceData> arrayList) {
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

        //재활용
        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_partition, null);
        }

        //ui 찾기
        TextView par_toDoName = view.findViewById(R.id.par_toDoName);
        TextView par_time = view.findViewById(R.id.par_time);
        TextView par_alarm = view.findViewById(R.id.par_alarm);
        TextView par_tvRepetition = view.findViewById(R.id.par_tvRepetition);
        CheckBox par_checkBox = view.findViewById(R.id.par_checkBox);
        LinearLayout par_linear = view.findViewById(R.id.par_linear);
        LinearLayout par_linear_checkBox = view.findViewById(R.id.par_linear_checkBox);
        LinearLayout par_big_linear = view.findViewById(R.id.par_big_linear);

        //값 적용
        final SpaceData spaceData = arrayList.get(i);
        if (spaceData.getSpaceName() == "") {
            par_toDoName.setText(new String(" + 할일 추가하기"));
            par_toDoName.setTextSize(20);
            par_toDoName.setPadding(20,20,20,20);
            par_toDoName.setGravity(Gravity.CENTER);
            par_checkBox.setVisibility(View.GONE);
            par_linear.setVisibility(View.GONE);
            par_linear_checkBox.setVisibility(View.GONE);
            par_big_linear.setBackgroundColor(Color.rgb(162, 255, 255));
        } else {
            par_toDoName.setText(spaceData.getToDoName());
            par_time.setText(spaceData.getTime());
            if(spaceData.getAlarm() == 0){
                par_alarm.setText("알람 x");
            }else{
                par_alarm.setText("알람 o");
            }
            String repetition = "";
            String[] str = {spaceData.getMon(), spaceData.getTus() ,spaceData.getWen() ,
                    spaceData.getTur() ,spaceData.getFri() ,spaceData.getSat() ,spaceData.getSun()};
            int count = 0;
            for(int k = 0; k < str.length; k++){
                if(!str[k].equals("null")){
                    repetition = repetition + " " + str[k];
                    count++;
                }
            }
            if(count == 7){
                par_tvRepetition.setText("매일");
            }else{
                par_tvRepetition.setText(repetition);
            }

            if(flag_visible_checkBox == false){
                par_checkBox.setVisibility(View.INVISIBLE);
            }else{
                par_checkBox.setVisibility(View.VISIBLE);
            }

        }

        //이벤트
        par_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                try {
                    MyDBHelper myDBHelper = new MyDBHelper(context, "cleanDB");
                    SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                    String query = null;
                    if (b == true) {
                        query = "UPDATE toDoListTBL SET clear = 1 " +
                                "WHERE spaceName = '" + spaceData.getSpaceName() + "' " +
                                "AND  toDoName = '" + spaceData.getToDoName() + "' " +
                                "AND date = '" + spaceData.getDate() + "' " +
                                "AND time = '" + spaceData.getTime() + "';";
                    } else {
                        query = "UPDATE toDoListTBL SET clear = 0 " +
                                "WHERE spaceName = '" + spaceData.getSpaceName() + "' " +
                                "AND  toDoName = '" + spaceData.getToDoName() + "' " +
                                "AND date = '" + spaceData.getDate() + "' " +
                                "AND time = '" + spaceData.getTime() + "';";
                    }
                    sqLiteDatabase.execSQL(query);
                }catch (Exception e){
                    Log.d("ListViewAdapter", e.getMessage());
                }
            }
        });
        return view;
    }
}
