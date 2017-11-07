package com.td.hung.musicdemo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Hung Tran on 07/11/2017.
 */

public class NewPlayListDialog extends DialogFragment {
    private static NewPlayListDialog newPlayListDialog;
    private static Context mContext;

    public static NewPlayListDialog newInstance(Context context) {
        newPlayListDialog = new NewPlayListDialog();
        mContext = context;
        return newPlayListDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showDialog(FragmentTransaction ft, Fragment prev) {
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, "newdialog");
    }
}
