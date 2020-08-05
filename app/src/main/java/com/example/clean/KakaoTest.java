package com.example.clean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
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

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class KakaoTest extends AppCompatActivity {
    //UI
    private ImageButton imageButton;
    TemplateParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kakaotest);

        //ui 찾기
        findViewByIdFunction();


        LinkObject link = LinkObject.newBuilder()
                .setWebUrl("https://developers.kakao.com")
                .setMobileWebUrl("https://developers.kakao.com")
                .build();
        params = TextTemplate.newBuilder("Text", link)
                .setButtonTitle("This is button")
                .build();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 기본 템플릿으로 카카오링크 보내기
                KakaoLinkService.getInstance()
                        .sendDefault(getApplicationContext(), params, new ResponseCallback<KakaoLinkResponse>() {
                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
                            }

                            @Override
                            public void onSuccess(KakaoLinkResponse result) {
                                Log.i("KAKAO_API", "카카오링크 공유 성공");

                                // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                                Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
                                Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());
                            }
                        });
            }
        });

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("MY KEY HASH:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }









    }




    //ui 찾기
    private void findViewByIdFunction() {
        imageButton = findViewById(R.id.imageButton);
    }






}
