package com.td.hung.musicdemo.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

    private static ItemPlaylistClickListener itemPlaylistClickListener;

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
            return new AddPlayList(View.inflate(mContext, R.layout.item_playlist_recyclerview, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemPlayList){
            ((ItemPlayList) holder).itemPlaylistLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemPlaylistClickListener != null){
                        itemPlaylistClickListener.itemPlaylistClicked(myPlayList.get(position));
                    }
                }
            });
        }else {
            ((AddPlayList) holder).createPlaylistLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemPlaylistClickListener != null){
                        itemPlaylistClickListener.createNewPlaylist();
                    }
                }
            });
        }
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
        private LinearLayout itemPlaylistLayout;
        public ItemPlayList(View itemView) {
            super(itemView);
            itemPlaylistLayout = (LinearLayout) itemView.findViewById(R.id.item_playlist_layout);

        }
    }

    private static class AddPlayList extends RecyclerView.ViewHolder {
        private LinearLayout createPlaylistLayout;
        public AddPlayList(View itemView) {
            super(itemView);
            createPlaylistLayout = (LinearLayout) itemView.findViewById(R.id.create_playlist_layout);
        }
    }


    public interface ItemPlaylistClickListener{
        void createNewPlaylist();
        void itemPlaylistClicked(PlayList playList);
    }

    public static void setItemPlaylistClickListener(ItemPlaylistClickListener listener){
        itemPlaylistClickListener = listener;
    }
}
