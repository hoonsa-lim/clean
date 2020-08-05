package com.example.clean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

public class MainActivity extends AppCompatActivity {

    //tab 관련
    public FragmentTabHost tabHost;
    private TabHost.TabSpec tabSpecOne, tabSpecTwo, tabSpecThree, tabSpecFour, tabSpecFive, tabSpecSix, tabSpecSeven;
    private ImageView imageView1, imageView2, imageView3;
    public LinearLayout main_widget_linear;
    public ImageButton main_ib1;
    public ImageButton main_ib2;
    public ImageButton main_ib3;

    //tab fragment
    public FragmentSpaceList fragmentSpaceList; //1번째 fragment 공간 리스트 그리드 뷰
    public FragmentTodayList fragmentTodayList; //2번째 fragment 오늘 할일
    public FragmentCalender fragmentCalender;   //3번째 fragment 달력

    public FragmentTodoList fragmentTodoList;   //1번째 fragment에서 공간을 눌렀을 때
    public FragmentEditSpace fragmentEditSpace; //1번째 fragment 에서 공간 명, 이미지 수정할 때
    public FragmentAddToDo_2 fragmentAddToDo_2; //1번째 fragment에서 공간을 눌렀을 때, 할일 추가 화면
    public FragmentAddToDo_3 fragmentAddToDo_3; //1번째 fragment에서 공간을 눌렀을 때, 할일 수정 화면

    //카카오 테스트
//    private TemplateParams params;
//    ImageButton imageButtonkakao; 카카오 테스트

    //마지막 선택된 탭의 위치
    private String selectedLastTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ui 찾기
        findViewByIdFunction();

        //tab setting
        setTabFunction();
//        imageButtonkakao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), KakaoTest.class);
//                startActivity(intent);
//            }
//        });
        //이미지 버튼 이벤트 fragment 변경 이벤트
        main_ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabHost.setCurrentTab(0);
            }
        });
        main_ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabHost.setCurrentTab(1);
            }
        });
        main_ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabHost.setCurrentTab(2);
            }
        });

        //tabhost 변경 이벤트
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                selectedLastTab = s;
            }
        });
    }

    //tab setting
    private void setTabFunction() {
        //tabhost와 custom framelayout 연결
        tabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.tabRealContent);

        //객체를 만들어서 넣는 식으로 하지 않으면 bundle 사용, fragment 객체에 접근하기 어려움
        fragmentSpaceList = new FragmentSpaceList();
        fragmentTodayList = new FragmentTodayList();
        fragmentCalender = new FragmentCalender();

        //tab이 아닌 fragment
        fragmentEditSpace = new FragmentEditSpace();
        fragmentTodoList = new FragmentTodoList();
        fragmentAddToDo_2 = new FragmentAddToDo_2();
        fragmentAddToDo_3 = new FragmentAddToDo_3();

        //디자인 작업할 때 이 문장으로 수정
//        //tab widget에 들어갈 icon 생성
//        imageView1 = new ImageView(this);
//        imageView2 = new ImageView(this);
//        imageView3 = new ImageView(this);
//        imageView1.setImageResource(R.drawable.tab_one);
//        imageView2.setImageResource(R.drawable.tab_two);
//        imageView3.setImageResource(R.drawable.tab_three);
//        tabSpecOne = tabHost.newTabSpec("ONE").setIndicator(imageView1);
//        tabSpecTwo = tabHost.newTabSpec("TWO").setIndicator(imageView2);
//        tabSpecThree = tabHost.newTabSpec("THREE").setIndicator(imageView3);

        //tab spec 3개 생성
        tabSpecOne = tabHost.newTabSpec("ONE").setIndicator("1");
        tabSpecTwo = tabHost.newTabSpec("TWO").setIndicator("2");
        tabSpecThree = tabHost.newTabSpec("THREE").setIndicator("3");
        tabSpecFour = tabHost.newTabSpec("FOUR").setIndicator("4");
        tabSpecFive = tabHost.newTabSpec("FIVE").setIndicator("5");
        tabSpecSix = tabHost.newTabSpec("SIX").setIndicator("6");
        tabSpecSeven = tabHost.newTabSpec("SEVEN").setIndicator("7");

        //tab spec을 tabhost에 저장함
        tabHost.addTab(tabSpecOne, fragmentSpaceList.getClass(), null);
        tabHost.addTab(tabSpecTwo, fragmentTodayList.getClass(), null);
        tabHost.addTab(tabSpecThree, fragmentCalender.getClass(), null);
        tabHost.addTab(tabSpecFour, fragmentEditSpace.getClass(), null);
        tabHost.addTab(tabSpecFive, fragmentTodoList.getClass(), null);
        tabHost.addTab(tabSpecSix, fragmentAddToDo_2.getClass(), null);
        tabHost.addTab(tabSpecSeven, fragmentAddToDo_3.getClass(), null);
        //시작 화면을 0번째 tab으로 설정함

        //view 메모리 로드를 위한
        tabHost.setCurrentTab(0);
    }

    //ui 찾기
    private void findViewByIdFunction() {
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        main_widget_linear = findViewById(R.id.main_widget_linear);
        main_ib1 = findViewById(R.id.main_ib1);
        main_ib2 = findViewById(R.id.main_ib2);
        main_ib3 = findViewById(R.id.main_ib3);
//        imageButtonkakao = findViewById(R.id.imageButtonkakao);
    }


//    public void kakao(){
//        LinkObject link = LinkObject.newBuilder()
//                .setWebUrl("https://developers.kakao.com")
//                .setMobileWebUrl("https://developers.kakao.com")
//                .build();
//        TemplateParams params = TextTemplate.newBuilder("Text", link)
//                .setButtonTitle("This is button")
//                .build();
//
//        // 기본 템플릿으로 카카오링크 보내기
//        KakaoLinkService.getInstance()
//                .sendDefault(getApplicationContext(), params, new ResponseCallback<KakaoLinkResponse>() {
//                    @Override
//                    public void onFailure(ErrorResult errorResult) {
//                        Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
//                    }
//
//                    @Override
//                    public void onSuccess(KakaoLinkResponse result) {
//                        Log.i("KAKAO_API", "카카오링크 공유 성공");
//
//                        // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
//                        Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
//                        Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());
//                    }
//                });
//    }

    //뒤로가기 버튼 이벤트 2번 눌러서 종료
    //메인 tab이 아닌 fragment는 다른 페이지로 설정
    long first_time;
    long second_time;
    @Override
    public void onBackPressed() {
        switch (selectedLastTab) {
            case "ONE":
            case "TWO":
            case "THREE":
                backPressedTwoFunction();
                break;
            case "FOUR":tabHost.setCurrentTab(0);
            case "FIVE":tabHost.setCurrentTab(0);
                break;
            case "SIX":tabHost.setCurrentTab(4);
            case "SEVEN":tabHost.setCurrentTab(4);
                break;
            default:
                break;
        }


    }

    //두번 눌렀을 때 종료되는 함수
    private void backPressedTwoFunction() {
        long second_time = System.currentTimeMillis();
        Toast.makeText(MainActivity.this, "한 번 더 누룰 시 종료 됩니다.", Toast.LENGTH_SHORT).show();
        if (second_time - first_time < 2000) {
            super.onBackPressed();
            finishAffinity();
        }
        first_time = System.currentTimeMillis();
    }

}
