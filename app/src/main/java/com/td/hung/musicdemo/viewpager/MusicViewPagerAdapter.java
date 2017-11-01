package com.td.hung.musicdemo.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.td.hung.musicdemo.fragment.MusicListFragment;
import com.td.hung.musicdemo.fragment.MusicLyricsFragment;
import com.td.hung.musicdemo.fragment.MusicPlayFragment;

/**
 * Created by Hung Tran on 01/11/2017.
 */

public class MusicViewPagerAdapter extends FragmentStatePagerAdapter{

    public MusicViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0: fragment = MusicListFragment.newInstance(); break;
            case 1: fragment = MusicPlayFragment.newInstance(); break;
            case 2: fragment = MusicLyricsFragment.newInstance(); break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
