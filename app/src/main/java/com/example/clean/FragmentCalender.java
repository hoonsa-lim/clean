package com.example.clean;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentCalender extends Fragment implements View.OnClickListener {
    private MainActivity mainActivity;

    //ui
    private ImageButton ibPrevious, ibNext;
    private TextView tvMonth;
    private GridView gvCalendar;
    private com.example.clean.CalendarAdapter calendarAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.calender_fragment, container, false);
        getActivity().setTitle("달력");
        //ui 찾기
        findViewByIdFunction(viewGroup);

        calendarAdapter = new com.example.clean.CalendarAdapter(mainActivity);
        gvCalendar.setAdapter(calendarAdapter);

        setTvYearMonth();

        ibPrevious.setOnClickListener(this);
        ibNext.setOnClickListener(this);


        return viewGroup;
    }


    //ui 찾기
    private void findViewByIdFunction(ViewGroup viewGroup) {
        ibPrevious = viewGroup.findViewById(R.id.ibPrevious);
        ibNext = viewGroup.findViewById(R.id.ibNext);
        tvMonth = viewGroup.findViewById(R.id.tvMonth);
        gvCalendar = viewGroup.findViewById(R.id.gvCalendar);

    }

    //main 반납 생명주기 마지막
    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    //상단에 년월 표시
    private void setTvYearMonth() {
        String yearMonth = String.valueOf(calendarAdapter.currentYear) + "년 " + String.valueOf(calendarAdapter.currentMonth + 1) + "월";
        tvMonth.setText(yearMonth);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibNext:
                calendarAdapter.setNextMonth();
                setTvYearMonth();
                calendarAdapter.notifyDataSetChanged();
                break;
            case R.id.ibPrevious:
                calendarAdapter.setPreviousMonth();
                setTvYearMonth();
                calendarAdapter.notifyDataSetChanged();
                break;
        }
    }


}


