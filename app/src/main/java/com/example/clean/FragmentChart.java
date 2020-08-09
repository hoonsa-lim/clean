package com.example.clean;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

public class FragmentChart extends Fragment {
    PieChart chart1;
    BarChart chart2;

    private MainActivity main3Activity;

    //db 값
    float countUnClear, countClear, countTotal;
    ArrayList<TodayListData> todayListDataTotal = new ArrayList<TodayListData>();
    ArrayList<TodayListData> todayListDataClear = new ArrayList<TodayListData>();

    //ui
    private TextView[] textViews = new TextView[3];

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main3Activity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        main3Activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.chart_fragment, container, false);
        main3Activity.setTitle("통계");

        //UI
        chart1 = viewGroup.findViewById(R.id.f2tab1_chart_1);
        chart2 = viewGroup.findViewById(R.id.f2tab1_chart_2);
        textViews[0] = viewGroup.findViewById(R.id.fragmentChart_tvToday1);
        textViews[1] = viewGroup.findViewById(R.id.fragmentChart_tvToday2);
        textViews[2] = viewGroup.findViewById(R.id.fragmentChart_tvToday3);

        //db로드
        loadDatabase();

        //값 세팅
        setPieChart();
        setBarChart();

        return viewGroup;
    }

    //db로드
    private void loadDatabase() {
        MyDBHelper myDBHelper = new MyDBHelper(main3Activity, "cleanDB");
        SQLiteDatabase sqLiteDatabase = myDBHelper.getReadableDatabase();
        Cursor cursor1, cursor2;
        try {
            //clear 하지 못한 총 개수
            String query_unClear = "SELECT count(t_clear) FROM todayListTBL WHERE t_clear = 0;";
            cursor1 = sqLiteDatabase.rawQuery(query_unClear, null);
            cursor1.moveToFirst();
            countUnClear = (float) cursor1.getInt(0);

            //clear 한 총 개수
            String query_Clear = "SELECT count(t_clear) FROM todayListTBL WHERE t_clear = 1;";
            cursor2 = sqLiteDatabase.rawQuery(query_Clear, null);
            cursor2.moveToFirst();
            countClear = (float) cursor2.getInt(0);

            //총 개수
            countTotal = countUnClear + countClear;
        } catch (Exception e) {
            Log.d("FragmentChart", "예외 발생 : unClear 개수, clear 개수" + e.getMessage());
        } finally {
            cursor1 = null;
            cursor2 = null;
        }

        try {
            //최근 5일 중, 날짜별 clear 못한 개수
            todayListDataTotal.clear();
            String top5UnClear = "SELECT t_today, count(t_clear) FROM todayListTBL GROUP by t_today ORDER by t_today ASC LIMIT 5;";
            cursor1 = sqLiteDatabase.rawQuery(top5UnClear, null);
            if (cursor1.getCount() <= 1) cursor1.moveToFirst();
            while (cursor1.moveToNext()) {
                TodayListData todayListData1 = new TodayListData(cursor1.getString(0), cursor1.getInt(1));
                todayListDataTotal.add(todayListData1);
            }

            //최근 5일 중, 날짜별 clear 한 개수
            todayListDataClear.clear();
            String top5Clear = "SELECT t_today, count(t_clear) FROM todayListTBL WHERE t_clear = 1 GROUP by t_today ORDER by t_today ASC LIMIT 5;";
            cursor2 = sqLiteDatabase.rawQuery(top5Clear, null);
            if (cursor2.getCount() <= 1) cursor1.moveToFirst();
            while (cursor2.moveToNext()) {
                TodayListData todayListData2 = new TodayListData(cursor2.getString(0), cursor2.getInt(1));
                todayListDataClear.add(todayListData2);
            }

        } catch (Exception e) {
            Log.d("FragmentChart", "예외 발생 : 최근 5일 중, 날짜별 clear 개수" + e.getMessage());
        } finally {
            cursor1 = null;
            cursor2 = null;
            sqLiteDatabase = null;

        }
    }

    // 파이 차트 설정
    private void setPieChart() {
        float ratioClear = Math.round((((countClear / countTotal) * 100.0f) * 1000) / 1000);
        float ratioUnClear = Math.round((((countUnClear / countTotal) * 100.0f) * 1000) / 1000);

        chart1.clearChart();
        chart1.setLegendTextSize(20.0f);
        chart1.addPieSlice(new PieModel("해결한 수", ratioClear, Color.parseColor("#FA7268")));
        chart1.addPieSlice(new PieModel("미해결 수", ratioUnClear, Color.parseColor("#A879FC")));
        chart1.startAnimation();

    }

    // 막대 차트 설정
    private void setBarChart() {
        try {
            chart2.clearChart();

            for (int i = 0; i < todayListDataTotal.size(); i++) {
                TodayListData todayListData1 = todayListDataTotal.get(i);
                TodayListData todayListData2 = todayListDataClear.get(i);
                textViews[i].setText(todayListData1.getT_today());
                chart2.addBar(new BarModel("", todayListData1.getT_clear(), Color.parseColor("#A879FC")));
                chart2.addBar(new BarModel("", todayListData2.getT_clear(), Color.parseColor("#FA7268")));
            }

            chart2.startAnimation();
        } catch (IndexOutOfBoundsException e) {
            Log.d("FragmentChart", "IndexOutOfBoundsException 예외 발생 : " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        main3Activity.menuSearch.setVisibility(View.INVISIBLE);
        main3Activity.mSearch.setVisible(false);
    }
}
