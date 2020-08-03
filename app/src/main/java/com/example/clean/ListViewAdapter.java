package com.example.clean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SpaceData> arrayList;

    public ListViewAdapter(Context context) {
        this.context = context;
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

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null) {
            view = layoutInflater.inflate(R.layout.listview_partition, null);
        }

        TextView par_toDoName = view.findViewById(R.id.par_toDoName);
        TextView par_time_repetition = view.findViewById(R.id.par_time_repetition);
        CheckBox par_checkBox = view.findViewById(R.id.par_checkBox);

        SpaceData spaceData = arrayList.get(i);
        if (spaceData.getSpaceName() == "") {
            par_toDoName.setText(new String("할일 추가하기"));
            par_toDoName.setGravity(Gravity.CENTER);
            par_time_repetition.setText("클릭 하세요");
            par_time_repetition.setGravity(Gravity.CENTER);
            par_checkBox.setVisibility(View.GONE);
        } else {
            par_toDoName.setText(spaceData.getToDoName());
            par_time_repetition.setText(spaceData.getTime() + " " + spaceData.getRepetition());
        }

        return view;
    }
}
