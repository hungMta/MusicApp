package com.td.hung.musicdemo.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hung Tran on 01/11/2017.
 */

public class MusicListFragment extends Fragment {
    private static MusicListFragment musicListFragment;
    private Context mContext;
    private RecyclerView recyclerView;
    private SongListRecyclerViewAdapter songListRecyclerViewAdapter;
    private List<Song> songList = new ArrayList<>();

    public static MusicListFragment newInstance() {
        if (musicListFragment == null) {
            musicListFragment = new MusicListFragment();
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
        songListRecyclerViewAdapter = new SongListRecyclerViewAdapter(mContext, songList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(songListRecyclerViewAdapter);
    }

    public void getSongList() {
        ContentResolver musicResolver = mContext.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            musicCursor.getNotificationUri();
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int data = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            //add songs to list
            int index = 0;
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String path = musicCursor.getString(data);
                Uri uri = Uri.parse("file:///" + path);
                songList.add(new Song(path, thisId, thisTitle, thisArtist, index));
                index++;
            }
            while (musicCursor.moveToNext());
        }
    }
}
