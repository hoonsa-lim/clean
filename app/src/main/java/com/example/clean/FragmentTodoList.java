package com.example.clean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentTodoList extends Fragment {
    private MainActivity mainActivity;

    //UI
    private ListView f4ListView;

    private ListViewAdapter listViewAdapter;

    private ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();

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
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_todolist, container, false);
        //ui 찾기
        findViewByIdFunction(viewGroup);

        //bundle로 클릭한 공간 명 전달 받기
        Bundle bundle = mainActivity.fragmentTodoList.getArguments();
        SpaceData spaceData = bundle.getParcelable("spaceData");
        String spaceName = spaceData.getSpaceName();

        //db load
        MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
        SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
        String query = "select * from toDoListTBL where spaceName = '" + spaceName + "';";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext() == true) {
            SpaceData spaceData1 = new SpaceData(cursor.getBlob(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7),
                    cursor.getString(8), cursor.getString(9), cursor.getString(10),
                    cursor.getString(11), cursor.getInt(12), cursor.getInt(13));
            arrayList.add(spaceData1);
        }
        //adapter 설정
        listViewAdapter = new ListViewAdapter(mainActivity, true);
        listViewAdapter.setArrayList(arrayList);
        f4ListView.setAdapter(listViewAdapter);

        return viewGroup;
    }


    //ui 찾기
    private void findViewByIdFunction(ViewGroup viewGroup) {
        f4ListView = viewGroup.findViewById(R.id.a2ListView);
    }


}


