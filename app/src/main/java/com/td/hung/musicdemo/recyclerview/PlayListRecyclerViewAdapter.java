package com.td.hung.musicdemo.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.entity.PlayList;
import com.td.hung.musicdemo.entity.Song;

import java.util.List;

/**
 * Created by Hung Tran on 06/11/2017.
 */

public class PlayListRecyclerViewAdapter extends RecyclerView.Adapter {

    private static final int TYPE_ADD = 0;
    private static final int TYPE_ITEM = 1;

    private static PlayListRecyclerViewAdapter playListRecyclerViewAdapter;
    private static Context mContext;

    private static Song songToAdd;
    private static List<PlayList> myPlayList;

    public static PlayListRecyclerViewAdapter newInstance(Context context, Song song, List<PlayList> playLists) {
        playListRecyclerViewAdapter = new PlayListRecyclerViewAdapter();
        songToAdd = song;
        mContext = context;
        myPlayList = playLists;
        return playListRecyclerViewAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ADD) {
            return new AddPlayList(View.inflate(mContext, R.layout.item_add_playlist_header, null));
        } else {
            return new AddPlayList(View.inflate(mContext, R.layout.item_add_playlist_header, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return myPlayList == null ? 0 : myPlayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_ADD;
        } else {
            return TYPE_ITEM;
        }
    }

    private static class  ItemPlayList extends RecyclerView.ViewHolder {

        public ItemPlayList(View itemView) {
            super(itemView);
        }
    }

    private static class AddPlayList extends RecyclerView.ViewHolder {

        public AddPlayList(View itemView) {
            super(itemView);
        }
    }


}
