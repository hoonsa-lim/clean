package com.example.clean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private TabHost.TabSpec tabSpecOne, tabSpecTwo, tabSpecThree, tabSpecFour, tabSpecFive, tabSpecSix, tabSpecSeven,tabSpecEight,tabSpecNine,tabSpecTen,tabSpecEleven;
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

    //날짜
    private String toDayDate;
    private String dayOfWeek1;
    private ArrayList<SpaceData> arrayList;

    //디비
    private MyDBHelper myDBHelper = null;
    private SQLiteDatabase sqLiteDatabase;

    //메뉴
    private MenuItem mSearch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitleColor(Color.parseColor("#061F5C"));
        //ui 찾기
        findViewByIdFunction();

        //오늘할일 및 달력을 위한 table을 위해 db에 값 넣기
        getToDayInformation();

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

    //메뉴
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        mSearch = menu.findItem(R.id.menuSearch);

        SearchView menuSearch = (SearchView)mSearch.getActionView();
        menuSearch.setSubmitButtonEnabled(true);
        menuSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuProfile:
                tabHost.setCurrentTab(7);
                break;
            case R.id.menuStatistics:
                tabHost.setCurrentTab(8);
                break;
            case R.id.menuSwitch:
                View dialogView = View.inflate(getApplicationContext(), R.layout.dialog_menu1, null);

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setView(dialogView);
                dialogView.setBackgroundColor(Color.parseColor("#A879FC"));

                Switch switch1 = dialogView.findViewById(R.id.switch1);
                Button d1btnExit = dialogView.findViewById(R.id.d1btnExit);

                d1btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                dialog.show();
                break;
            case R.id.menuProfileAdd:
                tabHost.setCurrentTab(9);
                break;
            case R.id.menuProfileEdit:
                tabHost.setCurrentTab(10);
                break;
            case R.id.menuProfileDel:
                View dialogView2 = View.inflate(getApplicationContext(), R.layout.dialog_menu2, null);

                AlertDialog.Builder dialog2 = new AlertDialog.Builder(MainActivity.this);
                dialog2.setView(dialogView2);
                dialogView2.setBackgroundColor(Color.parseColor("#A879FC"));

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
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                d2btnExit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

                dialog2.show();
                break;
            case R.id.menuAppInfo:
                Intent intent = new Intent(getApplicationContext(), AppInfomationActivity.class);
                startActivity(intent);
                break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }


    //today 정보 받아서 db에 저장
    private void getToDayInformation() {
        String query2 = null;

        //시스템 날짜 받기
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
        toDayDate = simpleDate.format(mDate).trim();
        Log.d("MainActivity", toDayDate);

        //요일받기
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        dayOfWeek1 = "";
        switch (dayOfWeek) {
            case 1:
                dayOfWeek1 = "일";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '"+ toDayDate + "') where sun = '"+ dayOfWeek1 +"' and clear = 0;";
                break;
            case 2:
                dayOfWeek1 = "월";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '"+ toDayDate + "') where mon = '"+ dayOfWeek1 +"' and clear = 0;";
                break;
            case 3:
                dayOfWeek1 = "화";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '"+ toDayDate + "') where tus = '"+ dayOfWeek1 +"' and clear = 0;";
                break;
            case 4:
                dayOfWeek1 = "수";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '"+ toDayDate + "') where wen = '"+ dayOfWeek1 +"' and clear = 0;";
                break;
            case 5:
                dayOfWeek1 = "목";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '"+ toDayDate + "') where tur = '"+ dayOfWeek1 +"' and clear = 0;";
                break;
            case 6:
                dayOfWeek1 = "금";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '"+ toDayDate + "') where fri = '"+ dayOfWeek1 +"' and clear = 0;";
                break;
            case 7:
                dayOfWeek1 = "토";
                query2 = "SELECT * FROM (SELECT * FROM toDoListTBL WHERE date < '"+ toDayDate + "') where sat = '"+ dayOfWeek1 +"' and clear = 0;";
                break;
            default:
                break;
        }

        //쿼리문

        Cursor cursor;
        //오늘 날짜중 clear가 0인 값
        try {
            myDBHelper = new MyDBHelper(getApplicationContext(), "cleanDB");
            sqLiteDatabase = myDBHelper.getWritableDatabase();
//            myDBHelper.onUpgrade(sqLiteDatabase, 0 , 1);
            String query = "select * from toDoListTBL where date = '" + toDayDate + "' and clear = 0";
            cursor = sqLiteDatabase.rawQuery(query, null);

            //toDayListTBL에 먼저 저장, 나중에 저장 되는 값은 중복되지 않도록 하기 위함
            while (cursor.moveToNext() == true) {
                String str0 = cursor.getString(1) + cursor.getString(2) + cursor.getString(3) + cursor.getString(4);
                String str1 = cursor.getString(1);
                String str2 = cursor.getString(2);
                String str3 = cursor.getString(3);
                String str4 = cursor.getString(4);
                int str12 = cursor.getInt(12);
                int str13 = cursor.getInt(13);

                String query1 = "insert or ignore into toDayListTBL values( " +
                        " '"+ str0 +"',  " +
                        " '"+ toDayDate +"',  " +
                        " '"+ str1 +"',  " +
                        " '"+ str2 +"',  " +
                        " '"+ str3 +"',  " +
                        " '"+ dayOfWeek1 +"',  " +
                        " '"+ str4 +"',  " +
                        " " + str12 + ",  " +
                        " " + str13 + ",  " +
                        " "+ null +"); ";
                sqLiteDatabase.execSQL(query1);
            }
        } catch (Exception e) {
            Log.d("MainActivity", "Exception 발생 : 시작 날짜가 오늘인 data 중 clear가 0인 값" + e.getMessage());
        }

        //오늘날짜 요일을 기준으로, clear가 0이고, 오늘날짜보다 시작날짜가 이른, 값을 로드 후 toDayListTBL에 insert
        try {
            sqLiteDatabase = myDBHelper.getWritableDatabase();
            cursor = sqLiteDatabase.rawQuery(query2, null);
            cursor.moveToFirst();
            while (cursor.moveToNext() == true) {
                String str0 = cursor.getString(1) + cursor.getString(2) + cursor.getString(3) + cursor.getString(4);
                String str1 = cursor.getString(1);
                String str2 = cursor.getString(2);
                String str3 = cursor.getString(3);
                String str4 = cursor.getString(4);
                int str12 = cursor.getInt(12);
                int str13 = cursor.getInt(13);

                String query1 = "insert or ignore into toDayListTBL values( " +
                        " '"+ str0 +"',  " +
                        " '"+ toDayDate +"',  " +
                        " '"+ str1 +"',  " +
                        " '"+ str2 +"',  " +
                        " '"+ str3 +"',  " +
                        " '"+ dayOfWeek1 +"',  " +
                        " '"+ str4 +"',  " +
                        " " + str12 + ",  " +
                        " " + str13 + ",  " +
                        " "+ null +"); ";
                sqLiteDatabase.execSQL(query1);
            }
        } catch (Exception e) {
            Log.d("MainActivity", "Exception 발생 : " + e.getMessage());
        }finally {
            cursor = null;
            sqLiteDatabase = null;
        }
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
        tabSpecEight = tabHost.newTabSpec("EIGHT").setIndicator("8");
        tabSpecNine= tabHost.newTabSpec("NINE").setIndicator("9");
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
            case "FOUR":
                tabHost.setCurrentTab(0);
            case "FIVE":
                tabHost.setCurrentTab(0);
                break;
            case "SIX":
                tabHost.setCurrentTab(4);
            case "SEVEN":
                tabHost.setCurrentTab(4);
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
