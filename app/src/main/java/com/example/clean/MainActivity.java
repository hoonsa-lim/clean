package com.example.clean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    //tab 관련
    public FragmentTabHost tabHost;
    private TabHost.TabSpec tabSpecOne, tabSpecTwo, tabSpecThree, tabSpecFour, tabSpecFive, tabSpecSix, tabSpecSeven, tabSpecEight, tabSpecNine, tabSpecTen, tabSpecEleven;
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

    //마지막 선택된 탭의 위치
    private String selectedLastTab;

    //날짜
    private String toDayDate;
    private String dayOfWeek1;
    private ArrayList<SpaceData> arrayList;

    //디비
    private MyDBHelper myDBHelper;
    private SQLiteDatabase sqLiteDatabase;

    //메뉴
    public static MenuItem mSearch;
    public static SearchView menuSearch;

    //알림
    private AlarmManager alarm_manager;
    private PendingIntent pendingIntent;

    //도움말
    private InitActionReceiver InitActionReceiver;
    private IntentFilter intentFilter;

    //도움말 이동에 관련된 SharedPreferences by 재훈
    SharedPreferences passTutorial;
    int tutorialState;

    @Override
    protected void onResume() {
        super.onResume();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        registerReceiver(InitActionReceiver, intentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ui 찾기
        findViewByIdFunction();

        //tab setting
        setTabFunction();

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

        CurrentTabFuntion(0);

        myDBHelper = new MyDBHelper(getApplicationContext(), "cleanDB");

        //다시 보지 않기 설정 안할 시 자동으로 도움말로 이동
        passTutorial = getSharedPreferences("change", MODE_PRIVATE);
        tutorialState = passTutorial.getInt("First", 0);
        if (tutorialState != 1) {
            Intent intent = new Intent(getApplicationContext(), TutorialGuideActivity.class);
            startActivity(intent);
        }

        InitActionReceiver = new InitActionReceiver();


    }

    public void CurrentTabFuntion(int i) {
        tabHost.setCurrentTab(i);
    }

    //메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        mSearch = menu.findItem(R.id.menuSearch);

        menuSearch = (SearchView) mSearch.getActionView();
        menuSearch.setSubmitButtonEnabled(true);
        menuSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadSpaceList(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuProfile:
                tabHost.setCurrentTab(7);
                break;
            case R.id.menuStatistics:
                tabHost.setCurrentTab(8);
                break;
            case R.id.menuSwitch:
                final View dialogView = View.inflate(getApplicationContext(), R.layout.dialog_menu1, null);

                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this, R.style.MyCustomDialogStyle);
                final AlertDialog ad = dialog.create();
                ad.setView(dialogView);

                final Switch switch1 = dialogView.findViewById(R.id.switch1);
                Button d1btnExit = dialogView.findViewById(R.id.d1btnExit);

                //튜토리얼 관련 SharedPreferences
                final SharedPreferences passTutorial = getApplicationContext().getSharedPreferences("change", MODE_PRIVATE);
                int tutorialState = passTutorial.getInt("First", 0);
                if (tutorialState == 1) {
                    switch1.setChecked(false);
                } else if (tutorialState == 0) {
                    switch1.setChecked(true);
                }

                switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (switch1.isChecked()) {
                            int intoTuto = 0;
                            SharedPreferences.Editor editor = passTutorial.edit();
                            editor.putInt("First", intoTuto);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "도움말 보기 설정", Toast.LENGTH_SHORT).show();
                        } else {
                            int intoMain = 1;
                            SharedPreferences.Editor editor = passTutorial.edit();
                            editor.putInt("First", intoMain);
                            editor.commit();
                            Toast.makeText(getApplicationContext(), "도움말 보기 해제", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                d1btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad.dismiss();
                    }
                });

                ad.show();
                break;
            case R.id.menuProfileAdd:
                tabHost.setCurrentTab(9);
                break;
            case R.id.menuProfileEdit:
                tabHost.setCurrentTab(10);
                break;
            case R.id.menuProfileDel:
                View dialogView2 = View.inflate(getApplicationContext(), R.layout.dialog_menu2, null);

                AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainActivity.this, R.style.MyCustomDialogStyle);
                final AlertDialog ad2 = dialog2.create();
                ad2.setView(dialogView2);

                final EditText d2edtNickName = dialogView2.findViewById(R.id.d2edtNickName);
                Button d2btnDelete = dialogView2.findViewById(R.id.d2btnDelete);
                Button d2btnExit = dialogView2.findViewById(R.id.d2btnExit);

                d2btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqLiteDatabase = myDBHelper.getWritableDatabase();
                        if (d2edtNickName.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            sqLiteDatabase.execSQL("DELETE FROM myTBL WHERE nickname = '"
                                    + d2edtNickName.getText().toString() + "';");
                            Toast.makeText(getApplicationContext(), "프로필이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                        sqLiteDatabase.close();
                        ad2.dismiss();
                    }
                });

                d2btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ad2.dismiss();
                    }
                });

                ad2.show();
                break;
            case R.id.menuAppInfo:
                Intent intent = new Intent(getApplicationContext(), AppInfomationActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

        //tab spec 3개 생성
        tabSpecOne = tabHost.newTabSpec("ONE").setIndicator("1");
        tabSpecTwo = tabHost.newTabSpec("TWO").setIndicator("2");
        tabSpecThree = tabHost.newTabSpec("THREE").setIndicator("3");
        tabSpecFour = tabHost.newTabSpec("FOUR").setIndicator("4");
        tabSpecFive = tabHost.newTabSpec("FIVE").setIndicator("5");
        tabSpecSix = tabHost.newTabSpec("SIX").setIndicator("6");
        tabSpecSeven = tabHost.newTabSpec("SEVEN").setIndicator("7");
        tabSpecEight = tabHost.newTabSpec("EIGHT").setIndicator("8");
        tabSpecNine = tabHost.newTabSpec("NINE").setIndicator("9");
        tabSpecTen = tabHost.newTabSpec("TEN").setIndicator("10");
        tabSpecEleven = tabHost.newTabSpec("ELEVEN").setIndicator("11");

        //tab spec을 tabhost에 저장함
        tabHost.addTab(tabSpecOne, fragmentSpaceList.getClass(), null);
        tabHost.addTab(tabSpecTwo, fragmentTodayList.getClass(), null);
        tabHost.addTab(tabSpecThree, fragmentCalender.getClass(), null);
        tabHost.addTab(tabSpecFour, fragmentEditSpace.getClass(), null);
        tabHost.addTab(tabSpecFive, fragmentTodoList.getClass(), null);
        tabHost.addTab(tabSpecSix, fragmentAddToDo_2.getClass(), null);
        tabHost.addTab(tabSpecSeven, fragmentAddToDo_3.getClass(), null);
        tabHost.addTab(tabSpecEight, FragmentMyProfile.class, null);
        tabHost.addTab(tabSpecNine, FragmentChart.class, null);
        tabHost.addTab(tabSpecTen, FragmentProfileAdd.class, null);
        tabHost.addTab(tabSpecEleven, FragmentProfileEdit.class, null);
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

    //뒤로가기 버튼 이벤트 2번 눌러서 종료
    //메인 tab이 아닌 fragment는 다른 페이지로 설정
    long first_time;
    long second_time;

    @Override
    public void onBackPressed() {
        try {
            switch (selectedLastTab) {
                case "ONE":
                case "TWO":
                case "THREE":
                    backPressedTwoFunction();
                    break;
                case "FOUR":
                case "FIVE":
                    tabHost.setCurrentTab(0);
                    break;
                case "SIX":
                case "SEVEN":
                    tabHost.setCurrentTab(4);
                    break;
                case "EIGHT":
                case "NINE":
                case "TEN":
                case "ELEVEN":
                case "TWELVE":
                    tabHost.setCurrentTab(0);
                    break;
                default:
                    break;
            }
        }catch (NullPointerException e){
            Log.d("MainActivity", "NullPointerException" + e.getMessage());
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

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(InitActionReceiver);
    }

    //data load
    public void loadSpaceList(String searchString) {
        try {
            MyDBHelper myDBHelper = new MyDBHelper(getApplicationContext(), "cleanDB");
            SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
//            myDBHelper.onUpgrade(sqLiteDatabase, 0,1);
            String query = "SELECT image, spaceName FROM toDoListTBL WHERE spaceName = '" + searchString + "' GROUP by spaceName;";
            Cursor cursor = sqLiteDatabase.rawQuery(query, null);
            cursor.moveToFirst();
            if (cursor.getCount() != 0) {
                SpaceData spaceData = new SpaceData(cursor.getBlob(0), cursor.getString(1));
                //메인으로 보내야함
                this.fragmentSpaceList.arrayList.clear();
                this.fragmentSpaceList.arrayList.add(spaceData);
                this.fragmentSpaceList.arrayList.add(new SpaceData(null, ""));
                this.fragmentSpaceList.gridViewAdapter.setArrayList(this.fragmentSpaceList.arrayList);
                this.fragmentSpaceList.gridViewAdapter.notifyDataSetChanged();
            } else {
                searchDialogFunction();
            }
            cursor = null;
            sqLiteDatabase = null;
        } catch (Exception e) {
            Log.d("MainActivity", "loadSpaceList : 예외 발생" + e.getMessage());
            searchDialogFunction();
        }

    }

    //검색했을 때 dialog
    public void searchDialogFunction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyCustomDialogStyle);
        builder.setNegativeButton("확인", null);
        builder.setMessage("알맞는 값이 없습니다.");
        builder.show();
    }
}
