package com.td.hung.musicdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.td.hung.musicdemo.R;

/**
 * Created by Hung Tran on 07/11/2017.
 */

public class MainPlaylistFragment extends Fragment {

    private static MainPlaylistFragment mainPlaylistFragment;
    private Context mContext;

    public static MainPlaylistFragment newInstance() {
        if (mainPlaylistFragment == null) {
            mainPlaylistFragment = new MainPlaylistFragment();
        }
        return mainPlaylistFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_main_playlist, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
