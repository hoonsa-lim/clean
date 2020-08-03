package com.example.clean;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {
    //UI
    private ListView a2ListView;
    private LinearLayout a2Linear;
    private FragmentAddToDo_2 fragmentAddToDo_2= new FragmentAddToDo_2();

    private ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();
    private SpaceData spaceData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        //ui 찾기
        findViewByIdFunction();

        //bundle로 클릭한 공간 명 전달 받기
        Intent intent = getIntent();
        spaceData = intent.getParcelableExtra("spaceData");
        String spaceName = spaceData.getSpaceName();

        //db load
        MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext(), "cleanDB");
        SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
        String query = "select * from toDoListTBL where spaceName = '" + spaceName + "';";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext() == true) {
            SpaceData spaceData1 = new SpaceData(cursor.getBlob(0), cursor.getString(1),
                    cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getInt(6), cursor.getInt(7));
            arrayList.add(spaceData1);
        }
        arrayList.add(new SpaceData(null, ""));
        //adapter 설정
        ListViewAdapter listViewAdapter = new ListViewAdapter(getApplicationContext());
        listViewAdapter.setArrayList(arrayList);
        a2ListView.setAdapter(listViewAdapter);


        a2ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //맨 마지막의 + 버튼이 눌렸을 때의 이벤트
                if ((position + 1) == arrayList.size()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("spaceData",spaceData);
                    fragmentAddToDo_2.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction().replace(R.id.a2Linear, fragmentAddToDo_2).commit();
                    a2ListView.setVisibility(View.INVISIBLE);
                } else {
                    //일반 리스트 클릭 이벤트
//                    getSupportFragmentManager().beginTransaction().replace(R.id.a2Linear, fragmentAddToDo_2).commit();
//                    a2ListView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //ui 찾기
    private void findViewByIdFunction() {
        a2ListView = findViewById(R.id.a2ListView);
        a2Linear = findViewById(R.id.a2Linear);
    }
}
