package com.example.clean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentSpaceList extends Fragment {
    private MainActivity mainActivity;

    //UI
    private GridView gridView;

    //list 출력
    private ArrayList<SpaceData> arrayList;
    public GridViewAdapter gridViewAdapter;

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
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.spacelist_fragment, container, false);

        //ui 찾기
        findViewByIdFunction(viewGroup);

        //test data
        arrayList = loadSpaceList();

        //adapter 적용
        gridViewAdapter = new GridViewAdapter(mainActivity);
        gridViewAdapter.setArrayList(arrayList);
        gridViewAdapter.notifyDataSetChanged();
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //맨 마지막의 + 버튼이 눌렸을 때의 이벤트
                if ((position + 1) == arrayList.size()) {
                    Intent intent = new Intent(mainActivity, AddSpaceAndToDoActivity.class);
                    intent.putExtra("spaceArray", arrayList);
                    startActivity(intent);
                } else {
                    SpaceData sd = arrayList.get(position);
                    Intent intent = new Intent(mainActivity, ToDoListActivity.class);
                    intent.putExtra("spaceData",sd);
                    startActivity(intent);
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

    //data load
    public ArrayList<SpaceData> loadSpaceList() {
        ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();
        MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
        SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
//        myDBHelper.onUpgrade(sqLiteDatabase, 0,1);

        String query = "select image, spaceName from toDoListTBL GROUP by spaceName;";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext() == true) {
            SpaceData spaceData = new SpaceData(cursor.getBlob(0), cursor.getString(1));
            arrayList.add(spaceData);
        }
        arrayList.add(new SpaceData(null, ""));
        cursor = null;
        sqLiteDatabase = null;
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

    @Override
    public void onResume() {
        super.onResume();
    }
}


