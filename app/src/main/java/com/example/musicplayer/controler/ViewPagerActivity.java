package com.example.musicplayer.controler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.musicplayer.R;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity {


    private androidx.viewpager.widget.ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

    //    checkStoragePermission();
        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tab_layout);


        setUpViewPager();
        mTabLayout.setupWithViewPager(mViewPager);

            }




    private void setUpViewPager() {


        List<String> tabTitleList = new ArrayList<>();
        tabTitleList.add("Music");
        tabTitleList.add("Album");
        tabTitleList.add("Singer");
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager() ,tabTitleList);
        mViewPager.setAdapter(viewPagerAdapter);
    }


}
