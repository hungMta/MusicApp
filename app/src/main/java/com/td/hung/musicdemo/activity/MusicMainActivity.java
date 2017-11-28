package com.td.hung.musicdemo.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.viewpager.MainMusicViewPagerAdapter;

import android.support.design.widget.TabLayout;

/**
 * Created by Hung Tran on 07/11/2017.
 */

public class MusicMainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView editText;
    private TabLayout tabLayout;
    private MainMusicViewPagerAdapter mainMusicViewPagerAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        mainMusicViewPagerAdapter = new MainMusicViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mainMusicViewPagerAdapter);
    }

}
