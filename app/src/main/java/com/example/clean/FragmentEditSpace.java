package com.example.clean;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentEditSpace extends Fragment implements View.OnClickListener {
    private MainActivity mainActivity;

    //ui
    private EditText a1EtSpaceName;
    private Button a1BtnCancel, a1BtnNext, a1BtnImageSetting;
    private ImageView a1IvSpaceImage;
    private FrameLayout a1FrameLayout;
    public LinearLayout a1Linear_big;

    private final int GALLERY = 101;//갤러리 상수
    private Uri a1SelectedImageUri;
    private byte[] imgbytes;
    private AlertDialog.Builder builder;

    private ArrayList<SpaceData> arrayList = new ArrayList<SpaceData>();
    private boolean flag_sameName = false;
    private SpaceData spaceData;
    private MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_add_space, container, false);
        getActivity().setTitle("공간 수정");

        //ui 찾기
        findViewByIdFunction(viewGroup);





        //이벤트
        a1BtnCancel.setOnClickListener(this);
        a1BtnNext.setOnClickListener(this);
        a1BtnImageSetting.setOnClickListener(this);
        return viewGroup;
    }

    //ui 찾기
    private void findViewByIdFunction(ViewGroup viewGroup) {
        a1IvSpaceImage = viewGroup.findViewById(R.id.a1IvSpaceImage);
        a1BtnCancel = viewGroup.findViewById(R.id.a1BtnCancel);
        a1BtnNext = viewGroup.findViewById(R.id.a1BtnNext);
        a1EtSpaceName = viewGroup.findViewById(R.id.a1EtSpaceName);
        a1BtnImageSetting = viewGroup.findViewById(R.id.a1BtnImageSetting);
        a1FrameLayout = viewGroup.findViewById(R.id.a1FrameLayout);
        a1Linear_big = viewGroup.findViewById(R.id.a1Linear_big);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.menuSearch.setVisibility(View.INVISIBLE);
        mainActivity.mSearch.setVisible(false);

        //번들 받기
        Bundle bundle = mainActivity.fragmentEditSpace.getArguments();
        spaceData = bundle.getParcelable("spaceData1");
        bundle = null;

        //값 세팅
        a1BtnNext.setText("저장");
        a1EtSpaceName.setText("");
        a1EtSpaceName.setText(spaceData.getSpaceName());

        //이미지 세팅
        byte[] data = spaceData.getImage();
        Bitmap imageBitmap = null;
        if (spaceData.getImage() != null) {
            imageBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            a1IvSpaceImage.setImageBitmap(imageBitmap);
        }else {
            a1IvSpaceImage.setImageResource(android.R.drawable.ic_menu_gallery);

        }
    }

    //main 반납 생명주기 마지막
    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.a1BtnNext:
                String str = a1EtSpaceName.getText().toString().trim();
                try {
                    if (str.equals("")) {
                        Toast.makeText(mainActivity, "공간명을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        flag_sameName = false;
                        myDBHelper = new MyDBHelper(mainActivity, "cleanDB");
                        sqLiteDatabase = myDBHelper.getReadableDatabase();
                        String query = "select spaceName from toDoListTBL group by spaceName;";
                        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
                        //같은 이름이 db에 있으면 flag = true;
                        while (cursor.moveToNext()) {
                            if (str.equals(cursor.getString(0))) {
                                flag_sameName = true;
                            }
                        }
                        cursor = null;
                        //저장하려는 이름이 db에 있는지 확인
                        if (flag_sameName == true) {
                            //원래 이름과 같을 경우에는 저장가능하게 설계
                            if(!str.equals(spaceData.getSpaceName())){
                                Toast.makeText(mainActivity, "같은 이름의 공간이 존재합니다.", Toast.LENGTH_SHORT).show();
                            }else{
                                //원래 이름과 같은데, 이미지 변경도 하지 않았다면 토스트 띄움
//                                if(){
//
//                                }
                                executeUpdateQuery(spaceData.getSpaceName(), str);
                            }
                        } else {
                            //같은 이름이 없을 때
                            executeUpdateQuery(spaceData.getSpaceName(), str);
                        }
                    }
                }catch (Exception e){
                    Log.d("FragmentEditSpace", "db 예외 발생 : " + e.getMessage());
                }

                break;
            case R.id.a1BtnCancel:
                fragmentFinishFunction();
                break;
            case R.id.a1BtnImageSetting:
                LayoutInflater layoutInflater = getLayoutInflater();
                LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.dialog_image_select, null);

                builder = new AlertDialog.Builder(mainActivity,R.style.MyCustomDialogStyle);
                final AlertDialog ad = builder.create();
                Button btnBasicImage = linearLayout.findViewById(R.id.btnBasicImage);
                Button btnGalleryImage = linearLayout.findViewById(R.id.btnGalleryImage);
                btnBasicImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        a1IvSpaceImage.setImageResource(android.R.drawable.ic_menu_gallery);
                        spaceData.setImage(null);
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

    //공간 수정 쿼리 실행 문
    private void executeUpdateQuery(String before, String after) throws Exception{
        sqLiteDatabase = myDBHelper.getWritableDatabase();
        if(spaceData.getImage() != null){
            String query1 = "update toDoListTBL set spaceName = '" + after + "', image = ? where spaceName = '" + before + "';";
            SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(query1);
            sqLiteStatement.bindBlob(1, spaceData.getImage());
            sqLiteStatement.execute();
            sqLiteStatement = null;
        }else{
            String query = "update toDoListTBL set spaceName = '" + after + "', image = null where spaceName = '" + before + "';";
            sqLiteDatabase.execSQL(query);
        }
        sqLiteDatabase = null;
        Toast.makeText(mainActivity, "저장 됐습니다.", Toast.LENGTH_SHORT).show();

        //공간 추가 fragment에 바로 적용 시키기 위해서 보냄
        Bundle bundle = new Bundle();
        bundle.putParcelable("editSpaceData", spaceData);
        mainActivity.fragmentSpaceList.setArguments(bundle);

        fragmentFinishFunction();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY && data != null && data.getData() != null) {
            a1SelectedImageUri = data.getData();
            a1IvSpaceImage.setImageURI(a1SelectedImageUri);

            Bitmap bm = null; //Bitmap 로드
            try {
                bm = MediaStore.Images.Media.getBitmap(mainActivity.getContentResolver(), a1SelectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 50, byteArray);
            imgbytes = byteArray.toByteArray();
            spaceData.setImage(imgbytes);
        }
    }
    //fragment 종료 함수
    public void fragmentFinishFunction() {
        mainActivity.tabHost.setCurrentTab(0);
        mainActivity.main_widget_linear.setVisibility(View.VISIBLE);
    }
}


