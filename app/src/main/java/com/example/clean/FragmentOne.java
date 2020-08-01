package com.example.clean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class FragmentOne extends Fragment {
    private MainActivity mainActivity;

    //UI
    private GridView gridView;

    //list 출력
    private ArrayList<SpaceData> arrayList;
    private GridViewAdapter gridViewAdapter;

    //intent code
    private final int ADD_TODO_ACTIVITY = 105;
    private static final int RESULT_ADD_TODO_ACTIVITY = 1051;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.one_fragment, container, false);

        //ui 찾기
        findViewByIdFunction(viewGroup);

        //test data
        arrayList = testDataFunction();


        gridViewAdapter = new GridViewAdapter(mainActivity);
        gridViewAdapter.setArrayList(arrayList);
        //adapter 적용
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //맨 마지막의 + 버튼이 눌렸을 때의 이벤트
                if ((position + 1) == arrayList.size()) {
                    //alert
//                    alertFunction();

                    Intent intent = new Intent(mainActivity, AddToDoActivity.class);
                    startActivityForResult(intent, ADD_TODO_ACTIVITY);
                }
            }
        });
        return viewGroup;
    }

    //alert
    private void alertFunction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setMessage("test");
        builder.setPositiveButton("생성", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                arrayList.add(arrayList.size() - 1, new SpaceData(null, "공간test"));
                gridViewAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("취소", null);
        builder.show();
    }

    //test data
    private ArrayList<SpaceData> testDataFunction() {
        ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();
        SpaceData spaceData = new SpaceData(null, "거실");
        SpaceData spaceData1 = new SpaceData(null, "방");
        SpaceData spaceData2 = new SpaceData(null, "주방1");
        SpaceData spaceData3 = new SpaceData(null, "주방2");
        SpaceData spaceData4 = new SpaceData(null, "주방3");
        SpaceData spaceData5 = new SpaceData(null, "주방4");
        SpaceData spaceData6 = new SpaceData(null, "주방5");
        SpaceData spaceData7 = new SpaceData(null, "주방6");
        SpaceData spaceData8 = new SpaceData(null, "주방7");
        SpaceData spaceData9 = new SpaceData(null, "주방8");
        SpaceData spaceData10 = new SpaceData(null, "");

        arrayList.add(spaceData);
        arrayList.add(spaceData1);
        arrayList.add(spaceData2);
        arrayList.add(spaceData3);
        arrayList.add(spaceData4);
        arrayList.add(spaceData5);
        arrayList.add(spaceData6);
        arrayList.add(spaceData7);
        arrayList.add(spaceData8);
        arrayList.add(spaceData9);
        arrayList.add(spaceData10);


        return arrayList;
    }


    //ui 찾기
    private void findViewByIdFunction(ViewGroup viewGroup) {
        gridView = viewGroup.findViewById(R.id.gridView);


    }



    //intent 돌려받는 값
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_ADD_TODO_ACTIVITY:
                break;

            default:
                break;
        }
    }
}


