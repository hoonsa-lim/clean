package com.example.clean;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddSpaceAndToDoActivity extends AppCompatActivity implements View.OnClickListener {
    //UI
    private EditText a1EtSpaceName;
    private Button a1BtnImageSetting;
    private Button a1BtnCancel, a1BtnNext;
    private ImageView a1IvSpaceImage;
    private FrameLayout a1FrameLayout;
    public LinearLayout a1Linear_big;


    private final int GALLERY = 101;//갤러리 상수
    private Uri a1SelectedImageUri;
    private byte[] imgbytes;
    private AlertDialog.Builder builder;

    private ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();
    private boolean flag_sameName = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_space);
        setTitle("공간 추가");
        //UI 찾기
        findViewByIdFunction();

        Intent intent = getIntent();
        arrayList = intent.getParcelableArrayListExtra("spaceArray");

        a1BtnCancel.setOnClickListener(this);
        a1BtnNext.setOnClickListener(this);
        a1BtnImageSetting.setOnClickListener(this);
    }

    //UI 찾기
    private void findViewByIdFunction() {
        a1IvSpaceImage = findViewById(R.id.a1IvSpaceImage);
        a1BtnCancel = findViewById(R.id.a1BtnCancel);
        a1BtnNext = findViewById(R.id.a1BtnNext);
        a1EtSpaceName = findViewById(R.id.a1EtSpaceName);
        a1BtnImageSetting = findViewById(R.id.a1BtnImageSetting);
        a1FrameLayout = findViewById(R.id.a1FrameLayout);
        a1Linear_big = findViewById(R.id.a1Linear_big);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.a1BtnNext:
                Bundle bundle = new Bundle();
                String str = a1EtSpaceName.getText().toString().trim();

                if(str.equals("")){
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    flag_sameName = false;
                    MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext(), "cleanDB");
                    SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
                    String query = "select spaceName from toDoListTBL group by spaceName;";
                    Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                    while(cursor.moveToNext()){
                        if(str.equals(cursor.getString(0))){
                            flag_sameName = true;
                        }
                    }

                    if(flag_sameName == true){
                        Toast.makeText(getApplicationContext(), "같은 이름의 공간이 존재합니다. \n 다른 이름으로 만들어주세요.", Toast.LENGTH_SHORT).show();
                    }else{
                        bundle.putParcelable("spaceData", new SpaceData(imgbytes, str));
                        FragmentAddToDo fragmentAddToDo = new FragmentAddToDo();
                        fragmentAddToDo.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction().replace(R.id.a1FrameLayout, fragmentAddToDo).commit();
                        a1Linear_big.setVisibility(View.INVISIBLE);
                    }
                }

                break;
            case R.id.a1BtnCancel:
                finish();
                break;
            case R.id.a1BtnImageSetting:
                LayoutInflater layoutInflater = getLayoutInflater();
                LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_image_select, null);

                builder = new AlertDialog.Builder(this, R.style.MyCustomDialogStyle);
                final AlertDialog ad = builder.create();
                Button btnBasicImage = linearLayout.findViewById(R.id.btnBasicImage);
                Button btnGalleryImage = linearLayout.findViewById(R.id.btnGalleryImage);
                btnBasicImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        a1IvSpaceImage.setImageResource(android.R.drawable.ic_menu_gallery);
                        ad.dismiss();
                    }
                });
                btnGalleryImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent2 = new Intent(Intent.ACTION_PICK);
                        intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent2, GALLERY);
                        ad.dismiss();
                    }
                });
                ad.setView(linearLayout);
                ad.show();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY && data != null && data.getData() != null) {
            a1SelectedImageUri = data.getData();
            a1IvSpaceImage.setImageURI(a1SelectedImageUri);

            Bitmap bm = null; //Bitmap 로드
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), a1SelectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
            imgbytes = byteArray.toByteArray();
        }
    }
}
