package com.example.clean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

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

    //long click
    private Point p;
    private int longClick_position;

    private FragmentEditSpace fragmentEditSpace = new FragmentEditSpace();

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
        getActivity().setTitle("메인");

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
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("spaceDataFromSpaceList",sd);
                    mainActivity.fragmentTodoList.setArguments(bundle);
                    mainActivity.tabHost.setCurrentTab(4);
                }
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                if((position+1) != arrayList.size()){
                    longClick_position = position;
                    SpaceData sd = arrayList.get(position);
                    p = new Point();
                    p.x = (int) view.getX()+50;
                    p.y = (int) view.getY()+400;
                    showPopup(mainActivity, p, sd);
                }
                return true;
            }
        });



        return viewGroup;
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

    @Override
    public void onResume() {
        super.onResume();
        arrayList = loadSpaceList();
        gridViewAdapter.notifyDataSetChanged();
        mainActivity.main_widget_linear.setVisibility(View.VISIBLE);




    }


    // The method that displays the popup.
    private void showPopup(final Activity context, Point p, final SpaceData sd) {

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup_linear);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                mainActivity.fragmentEditSpace.setArguments(bundle);
                mainActivity.tabHost.setCurrentTab(3);
                mainActivity.main_widget_linear.setVisibility(View.GONE);
                popup.dismiss();
            }
        });
        popup_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
                alert.setMessage("정말 삭제 하시겠습니까?");
                alert.setNegativeButton("취소", null);
                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
                            SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                            String query = "DELETE FROM toDoListTBL WHERE spaceName = '"+sd.getSpaceName()+"';";
                            sqLiteDatabase.execSQL(query);
                            mainActivity.tabHost.setCurrentTab(1);
                            mainActivity.tabHost.setCurrentTab(0);
                            Toast.makeText(mainActivity, "삭제 됐습니다.", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Log.d("ToDoListActivity", e.getMessage());
                        }
                    }
                });
                alert.show();
                popup.dismiss();
            }
        });
    }
}


