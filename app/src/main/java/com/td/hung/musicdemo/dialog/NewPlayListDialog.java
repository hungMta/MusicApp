package com.td.hung.musicdemo.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.td.hung.musicdemo.R;

/**
 * Created by Hung Tran on 07/11/2017.
 */

public class NewPlayListDialog extends DialogFragment {
    private static NewPlayListDialog newPlayListDialog;
    private static Context mContext;
    private TextView txtOk;
    private TextView txtCancel;
    private EditText edtName;
    private static CreateNewPlaylistListener createNewPlaylistListener;

    public static NewPlayListDialog newInstance(Context context) {
        newPlayListDialog = new NewPlayListDialog();
        mContext = context;
        return newPlayListDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.new_playlist_dialog, container, false);
        txtOk = (TextView) view.findViewById(R.id.txt_ok);
        txtCancel = (TextView) view.findViewById(R.id.txt_cancel);
        edtName = (EditText) view.findViewById(R.id.edt_name_playlist);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createNewPlaylistListener != null){
                    createNewPlaylistListener.CreateNewClicked(edtName.getText().toString());
                    dismiss();
                }
            }
        });
    }

    public void showDialog(FragmentTransaction ft, Fragment prev) {
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, "newdialog");
    }

    public interface CreateNewPlaylistListener{
        void CreateNewClicked(String name);
    }

    public static void setCreateNewPlaylistListener(CreateNewPlaylistListener listener){
        createNewPlaylistListener = listener;
    }
}
