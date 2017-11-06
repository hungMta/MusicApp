package com.td.hung.musicdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.entity.Song;

/**
 * Created by Hung Tran on 02/11/2017.
 */

public class PlayListDialog extends DialogFragment {

    private static PlayListDialog playListDialog;
    private static Song song;
    private static Context mContext;

    public static PlayListDialog newInstance(Context context, Song song1) {
        playListDialog = new PlayListDialog();
        mContext = context;
        playListDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        song = song1;
        return playListDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.dialog_playlist, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showDialog(FragmentTransaction ft, Fragment prev) {
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, "dialog");
    }

}
