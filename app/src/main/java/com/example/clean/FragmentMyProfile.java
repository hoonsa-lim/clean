package com.example.clean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class FragmentMyProfile extends Fragment {
    private Spinner spinner;
    private ImageView f1ImageView;
    private TextView f1tvName;
    private TextView f1tvGender;
    private TextView f1tvAge;

    private MainActivity main3Activity;

    private ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayAdapter<String> adapter;

    private MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;
    private String strNickName = "";
    private byte[] strPicture;
    private String strName = "";
    private String strGender = "";
    private String strAge = "";

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main3Activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        main3Activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.myprofile_fragment, container, false);
        main3Activity.setTitle("내 프로필");

        spinner = viewGroup.findViewById(R.id.f1spinner);
        f1ImageView = viewGroup.findViewById(R.id.f1ImageView);
        f1tvName = viewGroup.findViewById(R.id.f1tvName);
        f1tvGender = viewGroup.findViewById(R.id.f1tvGender);
        f1tvAge = viewGroup.findViewById(R.id.f1tvAge);

        myDBHelper = new MyDBHelper(main3Activity, "cleanDB");

        findDBnickName();

        adapter = new ArrayAdapter<String>(
                main3Activity, android.R.layout.simple_list_item_1, arrayList
        );

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedPicture = arrayList.get(position);

                sqLiteDatabase = myDBHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqLiteDatabase.rawQuery("SELECT picture, name, gender, age FROM myTBL where nickname = '" + selectedPicture + "';", null);

                while (cursor.moveToNext()) {
                    strPicture = cursor.getBlob(0);
                    strName = cursor.getString(1);
                    strGender = cursor.getString(2);
                    strAge = cursor.getString(3);
                }
                cursor.close();
                sqLiteDatabase.close();


                Bitmap imageBitmap = null;
                if (strPicture != null) {
                    imageBitmap = BitmapFactory.decodeByteArray(strPicture, 0, strPicture.length);
                    f1ImageView.setImageBitmap(imageBitmap);
                }
                f1tvName.setText(strName);
                f1tvGender.setText(strGender);
                f1tvAge.setText(strAge);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(main3Activity, "닉네임을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        f1ImageView.setBackground(new ShapeDrawable(new OvalShape()));
        f1ImageView.setClipToOutline(true);

        return viewGroup;
    }

    private void findDBnickName() {
        try {
            sqLiteDatabase = myDBHelper.getReadableDatabase();
            Cursor cursor;
            cursor = sqLiteDatabase.rawQuery("SELECT nickname FROM myTBL;", null);

            arrayList.clear();
            while (cursor.moveToNext()) {
                strNickName = cursor.getString(0);
                arrayList.add(strNickName);
            }

            cursor.close();
            sqLiteDatabase.close();
        } catch (Exception e) {
            Log.d("FragmentMyProfile", "findDBnickName(), 예외 발생" + e.getMessage());
        }
    }
}
