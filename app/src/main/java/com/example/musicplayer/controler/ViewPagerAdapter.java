package com.example.musicplayer.controler;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.musicplayer.model.TabState;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List <String> mTabTitleList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm , List<String> tabTitleList) {
        super(fm);
        mTabTitleList = tabTitleList;
    }

    @Override
    public Fragment getItem(int position) {
        TabState tabState = TabState.valueOf(mTabTitleList.get(position));
        return TabFragment.newInstance(tabState , true);
    }

    @Override
    public int getCount() {
        return mTabTitleList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitleList.get(position);
    }
}