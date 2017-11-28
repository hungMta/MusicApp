package com.td.hung.musicdemo.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.td.hung.musicdemo.fragment.MainPlaylistFragment;
import com.td.hung.musicdemo.fragment.MainSongsFragment;

/**
 * Created by Hung Tran on 07/11/2017.
 */

public class MainMusicViewPagerAdapter extends FragmentStatePagerAdapter {

    public MainMusicViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = MainSongsFragment.newInstance();
                break;
            case 1:
                fragment = MainPlaylistFragment.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String pageTitle = "";
        switch (position) {
            case 0:
                pageTitle = "Songs";
                break;
            case 1:
                pageTitle = "Playlists";
                break;
        }
        return pageTitle;
    }
}
