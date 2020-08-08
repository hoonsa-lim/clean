package com.example.clean;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.PieModel;

public class FragmentChart extends Fragment {
    PieChart chart1;
    BarChart chart2;

    private MainActivity main3Activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        main3Activity = (MainActivity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        main3Activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup)inflater.inflate(R.layout.chart_fragment, container, false);
        main3Activity.setTitle("통계");

        chart1 = viewGroup.findViewById(R.id.f2tab1_chart_1);
        chart2 = viewGroup.findViewById(R.id.f2tab1_chart_2);

        setPieChart();
        setBarChart();

        return viewGroup;
    }

    // 파이 차트 설정
    private void setPieChart() {
        chart1.clearChart();

        chart1.addPieSlice(new PieModel("TYPE 1", 60, Color.parseColor("#FA7268")));
        chart1.addPieSlice(new PieModel("TYPE 2", 40, Color.parseColor("#A879FC")));

        chart1.startAnimation();
    }

    // 막대 차트 설정
    private void setBarChart() {

        chart2.clearChart();

        chart2.addBar(new BarModel("12", 10f, Color.parseColor("#A879FC")));
        chart2.addBar(new BarModel("13", 10f, Color.parseColor("#A879FC")));
        chart2.addBar(new BarModel("14", 10f, Color.parseColor("#A879FC")));
        chart2.addBar(new BarModel("15", 20f, Color.parseColor("#A879FC")));
        chart2.addBar(new BarModel("16", 10f, Color.parseColor("#A879FC")));
        chart2.addBar(new BarModel("17", 10f, Color.parseColor("#A879FC")));

        chart2.startAnimation();
    }
}
