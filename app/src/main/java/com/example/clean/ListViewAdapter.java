package com.example.clean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
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
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import static androidx.annotation.Dimension.DP;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SpaceData> arrayList;
    private boolean flag_visible_checkBox = false; // false 면 checkbox invisible
    private LinearLayout par_big_linear;
    private boolean flag;

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

        view = layoutInflater.inflate(R.layout.listview_partition, null);

        //ui 찾기
        TextView par_toDoName = view.findViewById(R.id.par_toDoName);
        TextView par_time = view.findViewById(R.id.par_time);
        TextView par_alarm = view.findViewById(R.id.par_alarm);
        TextView par_tvRepetition = view.findViewById(R.id.par_tvRepetition);
        CheckBox par_checkBox = view.findViewById(R.id.par_checkBox);
        LinearLayout par_linear = view.findViewById(R.id.par_linear);
        LinearLayout par_linear_checkBox = view.findViewById(R.id.par_linear_checkBox);
        LinearLayout par_linear_text = view.findViewById(R.id.par_linear_text);
        par_big_linear = view.findViewById(R.id.par_big_linear);

        //listview adapter 2번째 프래그 먼트에서 사용할 때와 1번째 프래그먼트에서 사용할 때를 구분함
        final SpaceData spaceData = arrayList.get(i);
        if (flag_visible_checkBox == false) {

            //첫번째 fragment
            par_checkBox.setVisibility(View.INVISIBLE);
            if (spaceData.getSpaceName() == "") {
                par_toDoName.setText(new String(" + 할일 추가하기"));
                par_toDoName.setTextSize(20);
                par_toDoName.setPadding(20, 20, 20, 20);
                par_toDoName.setGravity(Gravity.CENTER);
                par_checkBox.setVisibility(View.GONE);
                par_linear.setVisibility(View.GONE);
                par_linear_checkBox.setVisibility(View.GONE);
                par_linear_text.setGravity(Gravity.CENTER);
                par_big_linear.setBackgroundColor(Color.WHITE);
            } else {
                setValuesFunction(spaceData, par_toDoName, par_time, par_alarm, par_tvRepetition);
            }
        } else {
            //두번째 fragment
            par_checkBox.setVisibility(View.VISIBLE);
            setValuesFunction(spaceData, par_toDoName, par_time, par_alarm, par_tvRepetition);

            //이벤트
//            par_checkBox.setOnClickListener(new View.OnClickListener() {
//                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//                @Override
//                public void onClick(View view) {
//                    par_big_linear.animate().alpha(0f).translationX(1000f).setDuration(700).withEndAction(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(flag == true){
//                                par_big_linear.setTranslationX(0f);
//                                par_big_linear.setAlpha(1f);
//                                Toast.makeText(context, flag + "asd", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//            });
        }
        return view;
    }

    private void setValuesFunction(SpaceData spaceData, TextView par_toDoName, TextView par_time, TextView par_alarm, TextView par_tvRepetition) {
        //값 적용
        par_toDoName.setText(spaceData.getToDoName());
        par_time.setText(spaceData.getTime());
        if (spaceData.getAlarm() == 0) {
            par_alarm.setText("알람 x");
        } else {
            par_alarm.setText("알람 o");
        }
        String repetition = "";
        String[] str = {spaceData.getMon(), spaceData.getTus(), spaceData.getWen(),
                spaceData.getTur(), spaceData.getFri(), spaceData.getSat(), spaceData.getSun()};
        int count = 0;
        for (int k = 0; k < str.length; k++) {
            if (!str[k].equals("null")) {
                repetition = repetition + " " + str[k];
                count++;
            }
        }
        if (count == 7) {
            par_tvRepetition.setText("매일");
        } else {
            par_tvRepetition.setText(repetition);
        }
    }
}
