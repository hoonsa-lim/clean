package com.example.clean;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FragmentProfileEdit extends Fragment {
    private Spinner spinner2;
    private ImageView f4ImageView;
    private Button f4btnImageView;
    private EditText f4edtName;
    private RadioGroup f4rgGender;
    private RadioButton f4rdoMale;
    private RadioButton f4rdoFemale;
    private EditText f4edtAge;
    private Button f4btnProfileEdit;
    private Button f4btnExit;

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

    private String gender;
    private String selectedPicture;
    private Uri selectedImageUri;
    private byte[] imgbytes;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main3Activity = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        main3Activity = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        main3Activity.menuSearch.setVisibility(View.INVISIBLE);
        main3Activity.mSearch.setVisible(false);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.profile_edit_fragment, container, false);
        main3Activity.setTitle("프로필 정보 수정");

        spinner2 = viewGroup.findViewById(R.id.f4spinner);
        f4ImageView = viewGroup.findViewById(R.id.f4ImageView);
        f4btnImageView = viewGroup.findViewById(R.id.f4btnImageView);
        f4edtName = viewGroup.findViewById(R.id.f4edtName);
        f4rgGender = viewGroup.findViewById(R.id.f4rgGender);
        f4rdoMale = viewGroup.findViewById(R.id.f4rdoMale);
        f4rdoFemale = viewGroup.findViewById(R.id.f4rdoFemale);
        f4edtAge = viewGroup.findViewById(R.id.f4edtAge);
        f4btnProfileEdit = viewGroup.findViewById(R.id.f4btnProfileEdit);
        f4btnExit = viewGroup.findViewById(R.id.f4btnExit);

        myDBHelper = new MyDBHelper(main3Activity,"cleanDB");

        findDBnickName();

        adapter = new ArrayAdapter<String>(
                main3Activity, android.R.layout.simple_list_item_1, arrayList
        );

        spinner2.setAdapter(adapter);

        f4rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (f4rgGender.getCheckedRadioButtonId()){
                    case R.id.f4rdoMale:
                        gender = "남성";
                        break;
                    case R.id.f4rdoFemale:
                        gender = "여성";
                        break;
                }
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedPicture = arrayList.get(position);

                Log.d("problem", selectedPicture);

                sqLiteDatabase = myDBHelper.getReadableDatabase();
                Cursor cursor;
                cursor = sqLiteDatabase.rawQuery("SELECT picture, name, gender, age FROM myTBL where nickname = '"+ selectedPicture +"';", null);

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
                    f4ImageView.setImageBitmap(imageBitmap);
                }

                imgbytes = strPicture;

                f4edtName.setText(strName);
                switch (strGender){
                    case "남성":
                        f4rdoMale.setChecked(true);
                        f4rdoFemale.setChecked(false);
                        break;
                    case "여성":
                        f4rdoMale.setChecked(false);
                        f4rdoFemale.setChecked(true);
                        break;
                }
                f4edtAge.setText(strAge);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(main3Activity, "닉네임을 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        f4btnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 101);
            }
        });


        f4btnProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqLiteDatabase = myDBHelper.getWritableDatabase();
                if (f4edtName.getText().toString() != "") {
                    SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement("UPDATE myTBL SET picture = ? , name = '"+f4edtName.getText().toString()+"' , gender = '"+ gender +"' , " +
                            "age = '"+ f4edtAge.getText().toString() +"' WHERE nickname = '"+ selectedPicture +"';");

                    sqLiteStatement.bindBlob(1, imgbytes);
                    sqLiteStatement.execute();
                }
                sqLiteDatabase.close();
                Toast.makeText(main3Activity, "프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        f4btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main3Activity.CurrentTabFuntion(0);
            }
        });

        f4ImageView.setBackground(new ShapeDrawable(new OvalShape()));
        f4ImageView.setClipToOutline(true);


        return viewGroup;
    }

    private void findDBnickName() {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            f4ImageView.setImageURI(selectedImageUri);

            Bitmap bm = null; //Bitmap 로드
            try {
                bm = MediaStore.Images.Media.getBitmap(main3Activity.getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
            imgbytes = byteArray.toByteArray();
        }
    }
}
