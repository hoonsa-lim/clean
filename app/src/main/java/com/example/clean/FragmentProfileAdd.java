package com.example.clean;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class FragmentProfileAdd extends Fragment {
    private EditText f3edtNickName;
    private ImageView f3ImageView;
    private Button f3btnImageView;
    private EditText f3edtName;
    private RadioGroup f3rgGender;
    private RadioButton f3rdoMale;
    private RadioButton f3rdoFemale;
    private EditText f3edtAge;
    private Button f3btnProfileAdd;
    private Button f3btnExit;

    private MainActivity main3Activity;

    private Uri selectedImageUri;
    private String gender;

    private MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.profile_add_fragment, container, false);
        main3Activity.setTitle("프로필 정보 추가");

        myDBHelper = new MyDBHelper(main3Activity,"cleanDB");

        f3edtNickName = viewGroup.findViewById(R.id.f3edtNickName);
        f3ImageView = viewGroup.findViewById(R.id.f3ImageView);
        f3btnImageView = viewGroup.findViewById(R.id.f3btnImageView);
        f3edtName = viewGroup.findViewById(R.id.f3edtName);
        f3rgGender = viewGroup.findViewById(R.id.f3rgGender);
        f3rdoMale = viewGroup.findViewById(R.id.f3rdoMale);
        f3rdoFemale = viewGroup.findViewById(R.id.f3rdoFemale);
        f3edtAge = viewGroup.findViewById(R.id.f3edtAge);
        f3btnProfileAdd = viewGroup.findViewById(R.id.f3btnProfileAdd);
        f3btnExit = viewGroup.findViewById(R.id.f3btnExit);


        f3btnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 101);
            }
        });

        f3rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (f3rgGender.getCheckedRadioButtonId()){
                    case R.id.f3rdoMale:
                        gender = "남성";
                        break;
                    case R.id.f3rdoFemale:
                        gender = "여성";
                        break;
                }
            }
        });


        f3btnProfileAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imgbytes != null) {
                    sqLiteDatabase = myDBHelper.getWritableDatabase();
                    SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement("INSERT INTO myTBL VALUES ('" + f3edtNickName.getText().toString() + "' , " +
                            "? , '" + f3edtName.getText().toString() + "' , " +
                            "'" + gender + "' , '" + f3edtAge.getText().toString() + "');");
                    sqLiteStatement.bindBlob(1, imgbytes);
                    sqLiteStatement.execute();

                    sqLiteDatabase.close();

                    Toast.makeText(main3Activity, "프로필이 추가되었습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(main3Activity, "이미지를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        f3btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main3Activity.CurrentTabFuntion(0);
            }
        });

        f3ImageView.setBackground(new ShapeDrawable(new OvalShape()));
        f3ImageView.setClipToOutline(true);

        return viewGroup;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101 && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            f3ImageView.setImageURI(selectedImageUri);

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
