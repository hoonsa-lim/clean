package com.example.clean;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TutorialVierPagerAdapter extends FragmentPagerAdapter {
    public TutorialVierPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //각 도움말 페이지 부르기
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return TutorialPageFirst.newInstance();
            case 1: return TutorialPage2.newInstance();
            case 2: return TutorialPage3.newInstance();
            case 3: return TutorialPage4.newInstance();
            case 4: return TutorialPageLast.newInstance();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }

}
