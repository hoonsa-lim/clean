package com.example.clean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ToDoListActivity extends AppCompatActivity {
    //UI
    public ListView a2ListView;
    private FrameLayout a2Linear;
    private FragmentAddToDo_2 fragmentAddToDo_2= new FragmentAddToDo_2();
    private FragmentAddToDo_3 fragmentAddToDo_3= new FragmentAddToDo_3();

    private ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();
    private SpaceData spaceData;
    public ListViewAdapter listViewAdapter;

    private Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        //ui 찾기
        findViewByIdFunction();

        //list load
        listLoadFunction();

        a2ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //맨 마지막의 + 버튼이 눌렸을 때의 이벤트
                if ((position + 1) == arrayList.size()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("spaceData",spaceData);
                    fragmentAddToDo_2.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction().replace(R.id.a2Linear, fragmentAddToDo_2).commit();
                } else if(position < arrayList.size()){






                }
            }
        });

        a2ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                SpaceData sd = arrayList.get(position);
                sd.setSpaceName(spaceData.getSpaceName());
                sd.setImage(spaceData.getImage());
                p = new Point();
                p.x = (int) view.getX()+600;
                p.y = (int) view.getY()+230;
                showPopup(ToDoListActivity.this, p, sd);
                return true;
            }
        });
    }

    //목록 로드 함수
    public void listLoadFunction() {
        //bundle로 클릭한 공간 명 전달 받기
        Intent intent = getIntent();
        spaceData = intent.getParcelableExtra("spaceData");
        String spaceName = spaceData.getSpaceName();

        //db load
        arrayList.clear();
        MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext(), "cleanDB");
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
        arrayList.add(new SpaceData(null, ""));

        //adapter 설정
        listViewAdapter = new ListViewAdapter(getApplicationContext(), false);
        listViewAdapter.setArrayList(arrayList);
        a2ListView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetInvalidated();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //ui 찾기
    private void findViewByIdFunction() {
        a2ListView = findViewById(R.id.a2ListView);
        a2Linear = findViewById(R.id.a2Linear);
    }

    // The method that displays the popup.
    private void showPopup(final Activity context, Point p, final SpaceData sd) {

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_linear);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = 30;
        int OFFSET_Y = 30;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);

        // Getting a reference to Close button, and close the popup when clicked.
        Button popup_btnEdit = (Button) layout.findViewById(R.id.popup_btnEdit);
        Button popup_btnDelete = (Button) layout.findViewById(R.id.popup_btnDelete);
        popup_btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("spaceData1",sd);
                fragmentAddToDo_3.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.a2Linear, fragmentAddToDo_3).commit();
                popup.dismiss();
            }
        });
        popup_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ToDoListActivity.this);
                alert.setMessage("정말 삭제 하시겠습니까?");
                alert.setNegativeButton("취소", null);
                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            MyDBHelper myDBHelper = new MyDBHelper(ToDoListActivity.this, "cleanDB");
                            SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                            String query = "delete from toDoListTBL " +
                                    "where spaceName = '"+sd.getSpaceName()+"' " +
                                    "and toDoName = '"+sd.getToDoName()+"' " +
                                    "and date = '"+sd.getDate()+"' " +
                                    "and time = '"+sd.getTime()+"' ;";
                             sqLiteDatabase.execSQL(query);
                            Toast.makeText(getApplicationContext(), sd.getSpaceName() +"\n"+ sd.getToDoName() +"\n"+sd.getDate()+"\n"+ sd.getTime() + "삭제 됐습니다.", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Log.d("ToDoListActivity", e.getMessage());
                        }
                        listViewAdapter.notifyDataSetInvalidated();
                        a2ListView.invalidate();
                    }
                });
                alert.show();
                popup.dismiss();
            }
        });
    }




}
