package com.td.hung.musicdemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.entity.PlayList;
import com.td.hung.musicdemo.entity.Song;
import com.td.hung.musicdemo.recyclerview.PlayListRecyclerViewAdapter;
import com.td.hung.musicdemo.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung Tran on 02/11/2017.
 */

public class PlayListDialog extends DialogFragment implements PlayListRecyclerViewAdapter.ItemPlaylistClickListener, NewPlayListDialog.CreateNewPlaylistListener {

    private static PlayListDialog playListDialog;
    private static Song song;
    private static Context mContext;
    private RecyclerView recyclerView;
    private PlayListRecyclerViewAdapter playListRecyclerViewAdapter;
    private FragmentTransaction fragmentTransaction;
    private static FragmentManager fragmentManager;
    List<PlayList> playLists = new ArrayList<>();

    public static PlayListDialog newInstance(Context context, Song song1 , FragmentManager fragmentManager1) {
        playListDialog = new PlayListDialog();
        mContext = context;
        fragmentManager = fragmentManager1;
        playListDialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle agrs = new Bundle();
        agrs.putSerializable("SONG", song1);
        song = song1;
        return playListDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.dialog_playlist, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.reycler_playlist);
        return view;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        PlayListRecyclerViewAdapter.setItemPlaylistClickListener(this);
        NewPlayListDialog.setCreateNewPlaylistListener(this);
    }

    private void initRecyclerView() {
        playListRecyclerViewAdapter = playListRecyclerViewAdapter.newInstance(mContext, song, getPlayList());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(playListRecyclerViewAdapter);
    }

    private List<PlayList> getPlayList() {
        playLists.add(null);
        if (MusicUtil.getMyPlayList(mContext) != null) {
            playLists.addAll(MusicUtil.getMyPlayList(mContext));
        }
        return playLists;
    }

    public void showDialog(FragmentTransaction ft, Fragment prev) {
        fragmentTransaction = ft;
//        fragment = prev;
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        show(ft, "dialog");
    }

    @Override
    public void createNewPlaylist() {
        NewPlayListDialog newPlayListDialog = NewPlayListDialog.newInstance(mContext);
        newPlayListDialog.showDialog(fragmentManager.beginTransaction(),fragmentManager.findFragmentByTag("newdialog"));
    }

    @Override
    public void itemPlaylistClicked(PlayList playList) {

    }

    @Override
    public void CreateNewClicked(String name) {
        PlayList playList = new PlayList();
        playList.setName(name);
        playLists.add(playList);
        playListRecyclerViewAdapter.notifyDataSetChanged();
    }
}
