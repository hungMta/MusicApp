package com.td.hung.musicdemo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.entity.Song;
import com.td.hung.musicdemo.recyclerview.SongListRecyclerViewAdapter;
import com.td.hung.musicdemo.util.MusicUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung Tran on 01/11/2017.
 */

public class MusicSongFragment extends Fragment implements SongListRecyclerViewAdapter.OnLoadMoreListener {
    private static MusicSongFragment musicListFragment;
    private Context mContext;
    private RecyclerView recyclerView;
    private SongListRecyclerViewAdapter songListRecyclerViewAdapter;
    private List<Song> allSong = new ArrayList<>();
    private List<Song> songList = new ArrayList<>();


    public static MusicSongFragment newInstance() {
        if (musicListFragment == null) {
            musicListFragment = new MusicSongFragment();
        }
        return musicListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_music_list, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.reycler_list);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecyclerView();
        SongListRecyclerViewAdapter.setOnLoadMoreListener(this);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (songList.size() == 0) {
            getSongList();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        songListRecyclerViewAdapter = new SongListRecyclerViewAdapter(mContext, songList, recyclerView);
        recyclerView.setAdapter(songListRecyclerViewAdapter);
    }

    private void getSongList() {
        allSong = MusicUtil.newInstance().getSongList(mContext);
        for (int i = 0 ; i < 30 ; i ++){
            if (i < allSong.size()){
                songList.add(allSong.get(i));
            }else {
                break;
            }
        }
    }

    private Handler mHander = new Handler();

    @Override
    public void onLoadMore() {
        final int songSize = songList.size();
        songList.add(null);
        songListRecyclerViewAdapter.notifyItemInserted(songList.size() - 1);
        mHander.postDelayed(new Runnable() {
            @Override
            public void run() {
                songList.remove(songList.size() - 1);
                songListRecyclerViewAdapter.notifyItemRemoved(songList.size());
                for (int i = songSize + 1; i < songSize + 30; i++) {
                    if (i < allSong.size()) {
                        songList.add(allSong.get(i));
                        songListRecyclerViewAdapter.notifyItemInserted(songList.size());
                    } else {
                        break;
                    }
                }
                songListRecyclerViewAdapter.setLoaded();
                songListRecyclerViewAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

}
