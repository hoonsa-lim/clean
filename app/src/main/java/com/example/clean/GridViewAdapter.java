package com.example.clean;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SpaceData> arrayList;

    public GridViewAdapter(Context context) {
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

        //view 재활용 안함, 재활용하니까 이미지 중복되어 출력됨
         LinearLayout  newView = (LinearLayout) layoutInflater.inflate(R.layout.gridview_partition, null);

        //ui 찾기
        ImageView ivSpace = newView.findViewById(R.id.gp_ivSpace);
        TextView tvSpaceName = newView.findViewById(R.id.gp_tvSpaceName);
        LinearLayout linear_gridView_partition = newView.findViewById(R.id.linear_gridView_partition);

        //데이터 view 에 적용
        SpaceData spaceData = arrayList.get(i);
        byte[] data = spaceData.getImage();
        Bitmap imageBitmap = null;
        if (spaceData.getImage() != null) {
            imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            ivSpace.setImageBitmap(imageBitmap);
        }else {
            if(spaceData.getSpaceName() == ""){
                ivSpace.setImageResource(R.drawable.samll_add);
                ivSpace.setBackgroundColor(00000000);
                tvSpaceName.setText("공간 추가 하기");
            }else{
                ivSpace.setImageResource(R.drawable.image_room_basic);
            }
        }
        tvSpaceName.setText(spaceData.getSpaceName());
        return newView;
    }
}
