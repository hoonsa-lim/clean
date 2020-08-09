package com.example.clean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TutorialPageFirst extends Fragment {
    View view;
    public static TutorialPageFirst newInstance(){
        TutorialPageFirst tutorialPageFirst=new TutorialPageFirst();
        return tutorialPageFirst;
    }
    ImageView tu1ImageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tutorial_page_first,container,false);
        tu1ImageView=view.findViewById(R.id.tu1ImageView);

        Bitmap bitmapImag= BitmapFactory.decodeResource(view.getContext().getResources(),R.drawable.tu1);
        Bitmap bitmap=resizeBitmapImageFn(bitmapImag,800);
        tu1ImageView.setImageBitmap(bitmap);

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
