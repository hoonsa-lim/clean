package com.example.clean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
    //tab 관련
    public FragmentTabHost tabHost;
    private TabHost.TabSpec tabSpecOne, tabSpecTwo, tabSpecThree,tabSpecFour;
    private ImageView imageView1, imageView2, imageView3;
    public LinearLayout main_widget_linear;
    public ImageButton main_ib1;
    public ImageButton main_ib2;
    public ImageButton main_ib3;

    public FragmentSpaceList fragmentSpaceList;
    public FragmentTodayList fragmentTodayList;
    public FragmentCalender fragmentCalender;
    public FragmentTodoList fragmentTodoList;
    public FragmentEditSpace fragmentEditSpace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ui 찾기
        findViewByIdFunction();

        //tab setting
        setTabFunction();

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
    }

    //tab setting
    private void setTabFunction() {
    //tabhost와 custom framelayout 연결
        tabHost.setup(getApplicationContext(), getSupportFragmentManager(), R.id.tabRealContent);

        //객체를 만들어서 넣는 식으로 하지 않으면 bundle 사용, fragment 객체에 접근하기 어려움
        fragmentSpaceList = new FragmentSpaceList();
        fragmentTodayList = new FragmentTodayList();
        fragmentCalender = new FragmentCalender();
        fragmentEditSpace = new FragmentEditSpace();

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

        //tab spec을 tabhost에 저장함
        tabHost.addTab(tabSpecOne, fragmentSpaceList.getClass(), null);
        tabHost.addTab(tabSpecTwo, fragmentTodayList.getClass(), null);
        tabHost.addTab(tabSpecThree, fragmentCalender.getClass(), null);
        tabHost.addTab(tabSpecFour, fragmentEditSpace.getClass(), null);
        //시작 화면을 0번째 tab으로 설정함
        tabHost.setCurrentTab(0);
    }

    //ui 찾기
    private void findViewByIdFunction() {
        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        main_widget_linear = findViewById(R.id.main_widget_linear);
        main_ib1 = findViewById(R.id.main_ib1);
        main_ib2 = findViewById(R.id.main_ib2);
        main_ib3 = findViewById(R.id.main_ib3);
    }
}
