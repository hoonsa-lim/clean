package com.example.clean;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TutorialPageLast extends Fragment {
    ImageButton btnTutoClose;
    CheckBox cbEndTuto;
    View view;
    private ImageView tu5ImageView;
    public static TutorialPageLast newInstance(){
        TutorialPageLast tutorialPageLast=new TutorialPageLast();
        return tutorialPageLast;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tutorial_page_last,container,false);
        cbEndTuto=view.findViewById(R.id.cbEndTuto);
        btnTutoClose=view.findViewById(R.id.btnTutoClose);
        tu5ImageView=view.findViewById(R.id.tu5ImageView);

        Bitmap bitmapImag= BitmapFactory.decodeResource(view.getContext().getResources(),R.drawable.tu5);
        Bitmap bitmap=resizeBitmapImageFn(bitmapImag,800);
        tu5ImageView.setImageBitmap(bitmap);


        //튜토리얼 종료 버튼 클릭시 이벤트
        btnTutoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //만약 다시 보지 않기 버튼 클릭시 다음부터 도움말이 보이지 않음
                if(cbEndTuto.isChecked()){
                    int intoMain = 1;
                    SharedPreferences.Editor editor = TutorialGuideActivity.spPassTutorial.edit();
                    editor.putInt("First",intoMain);
                    editor.commit();
                }
                getActivity().finish();
            }
        });
        return view;
    }
    public Bitmap resizeBitmapImageFn(Bitmap bmpSource, int maxResolution){

        int iWidth = bmpSource.getWidth();      //비트맵이미지의 넓이

        int iHeight = bmpSource.getHeight();     //비트맵이미지의 높이

        int newWidth = iWidth ;

        int newHeight = iHeight ;

        float rate = 0.0f;



        //이미지의 가로 세로 비율에 맞게 조절

        if(iWidth > iHeight ){

            if(maxResolution < iWidth ){

                rate = maxResolution / (float) iWidth ;

                newHeight = (int) (iHeight * rate);

                newWidth = maxResolution;

            }

        }else{

            if(maxResolution < iHeight ){

                rate = maxResolution / (float) iHeight ;

                newWidth = (int) (iWidth * rate);

                newHeight = maxResolution;

            }

        }


        return Bitmap.createScaledBitmap(

                bmpSource, newWidth, newHeight, true);

    }
}
