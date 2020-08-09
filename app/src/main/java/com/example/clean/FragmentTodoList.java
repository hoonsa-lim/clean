package com.example.clean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentTodoList extends Fragment {
    private MainActivity mainActivity;

    //UI
    public ListView a2ListView;
    public static LinearLayout a2Linear;
    private FrameLayout a2FrameLayout;
    private ImageView a2IvImage;
    private TextView a2TvSpaceName;

    //
    private FragmentAddToDo_2 fragmentAddToDo_2 = new FragmentAddToDo_2();
    private FragmentAddToDo_3 fragmentAddToDo_3 = new FragmentAddToDo_3();

    private ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();
    private SpaceData spaceData;
    public ListViewAdapter listViewAdapter;

    //팝업메뉴
    private Point p;

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
        getActivity().setTitle("관리 공간");
        //ui 찾기
        findViewByIdFunction(viewGroup);

        //list load
        listLoadFunction();

        a2ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // + 할일추가 버튼 눌렸을 때의 이벤트
                if ((position + 1) == arrayList.size()) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("spaceData", spaceData);
                    mainActivity.fragmentAddToDo_2.setArguments(bundle);
                    mainActivity.tabHost.setCurrentTab(5);
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
                p.x = (int) view.getX() + 650;
                p.y = (int) view.getY() + 1200;
                showPopup(mainActivity, p, sd, position);
                return true;
            }
        });

        return viewGroup;
    }


    //ui 찾기
    private void findViewByIdFunction(ViewGroup viewGroup) {
        a2ListView = viewGroup.findViewById(R.id.a2ListView);
        a2FrameLayout = viewGroup.findViewById(R.id.a2FrameLayout);
        a2Linear = viewGroup.findViewById(R.id.a2Linear);
        a2IvImage = viewGroup.findViewById(R.id.a2IvImage);
        a2TvSpaceName = viewGroup.findViewById(R.id.a2TvSpaceName);
    }

    //목록 로드 함수
    public void listLoadFunction() {
        mainActivity.main_widget_linear.setVisibility(View.GONE);

        //bundle로 클릭한 공간 명, 이미지 전달 받기
        Bundle bundle = mainActivity.fragmentTodoList.getArguments();
        spaceData = bundle.getParcelable("spaceDataFromSpaceList");

        String spaceName = spaceData.getSpaceName();
        byte[] data = spaceData.getImage();
        if (spaceData.getImage() != null) {
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            a2IvImage.setImageBitmap(imageBitmap);
        } else {
            a2IvImage.setImageResource(R.drawable.image_room_basic);
        }
        a2TvSpaceName.setText(spaceData.getSpaceName());

        //db load
        arrayList.clear();
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
        arrayList.add(new SpaceData(null, ""));

        //adapter 설정
        listViewAdapter = new ListViewAdapter(mainActivity, false);
        listViewAdapter.setArrayList(arrayList);
        a2ListView.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetInvalidated();
    }

    @Override
    public void onResume() {
        super.onResume();
        a2Linear.setVisibility(View.VISIBLE);
        mainActivity.menuSearch.setVisibility(View.INVISIBLE);
        mainActivity.mSearch.setVisible(false);
    }

    // The method that displays the popup.
    private void showPopup(final Activity context, Point p, final SpaceData sd, final int position) {

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
        ImageButton popup_btnEdit = (ImageButton) layout.findViewById(R.id.popup_btnEdit);
        ImageButton popup_btnDelete = (ImageButton) layout.findViewById(R.id.popup_btnDelete);
        popup_btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("spaceData1", sd);
                mainActivity.fragmentAddToDo_3.setArguments(bundle);
                mainActivity.tabHost.setCurrentTab(6);
                popup.dismiss();
            }
        });
        popup_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity, R.style.MyCustomDialogStyle);
                alert.setMessage("정말로 삭제 하시겠습니까?");
                alert.setNegativeButton("취소", null);
                alert.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            MyDBHelper myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
                            SQLiteDatabase sqLiteDatabase = myDBHelper.getWritableDatabase();
                            String query = "delete from toDoListTBL " +
                                    "where spaceName = '" + sd.getSpaceName() + "' " +
                                    "and toDoName = '" + sd.getToDoName() + "' " +
                                    "and date = '" + sd.getDate() + "' " +
                                    "and time = '" + sd.getTime() + "' ;";
                            sqLiteDatabase.execSQL(query);
                            arrayList.remove(position);
                            Toast.makeText(mainActivity, "삭제 됐습니다.", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Log.d("ToDoListActivity", e.getMessage());
                        }
                        listViewAdapter.notifyDataSetInvalidated();
                    }
                });
                alert.show();
                popup.dismiss();
            }
        });
    }
}


