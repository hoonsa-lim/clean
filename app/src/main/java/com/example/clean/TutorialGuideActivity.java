package com.example.clean;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

public class TutorialGuideActivity extends AppCompatActivity {


    ViewPager vpTutorial;

    FragmentPagerAdapter fragmentPagerAdapter;

    PageIndicatorView pageIndicatorView;

    public static SharedPreferences spPassTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_guide);
        setTitle("Tutorial");
        getSupportActionBar().hide();

        vpTutorial = findViewById(R.id.vpTutorial);
        pageIndicatorView = findViewById(R.id.pageIndicaotrView);


        fragmentPagerAdapter = new TutorialVierPagerAdapter(getSupportFragmentManager());
        vpTutorial.setAdapter(fragmentPagerAdapter);

        pageIndicatorView.setViewPager(vpTutorial);
        pageIndicatorView.setCount(5);
        pageIndicatorView.setSelection(1);
        pageIndicatorView.setAnimationType(AnimationType.THIN_WORM);


        spPassTutorial = getSharedPreferences("change",MODE_PRIVATE);

    }
}
