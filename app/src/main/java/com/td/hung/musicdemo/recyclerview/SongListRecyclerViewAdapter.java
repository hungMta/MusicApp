package com.td.hung.musicdemo.recyclerview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.entity.Song;

import java.util.List;

/**
 * Created by Hung Tran on 01/11/2017.
 */

public class SongListRecyclerViewAdapter extends RecyclerView.Adapter {

    public static int ITEM_SONG = 1;
    public static int ITEM_LOADMORE = 0;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private Context mContext;
    private List<Song> songList;
    private static OnItemSongClickListener onItemSongClickListener;
    private static OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    public SongListRecyclerViewAdapter(Context context, List<Song> list, RecyclerView recyclerView) {
        mContext = context;
        songList = list;
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)){
                        if (onLoadMoreListener != null){
                            onLoadMoreListener.onLoadMore();
                        }
                        isLoading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_SONG) {
            return new ItemSong(View.inflate(mContext, R.layout.item_song_recycler, null));
        } else {
            return new ItemLoadMore(View.inflate(mContext, R.layout.item_load_more, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemSong) {
            ((ItemSong) holder).txtStt.setText(position + 1 + "");
            ((ItemSong) holder).txtSongName.setText(songList.get(position).getTitle());
            ((ItemSong) holder).txtSongArtist.setText(songList.get(position).getArtist());
            ((ItemSong) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemSongClickListener.itemSongClicked(songList.get(position));
                }
            });
        }else {
            // load more
        }
    }

    @Override
    public int getItemCount() {
        return songList == null ? 0 : songList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return songList.get(position) == null ? ITEM_LOADMORE : ITEM_SONG;
    }

    public void setLoaded() {
        isLoading = false;
    }

    private static class ItemSong extends RecyclerView.ViewHolder {

        private TextView txtStt;
        private TextView txtSongName;
        private TextView txtSongArtist;
        private RelativeLayout relativeLayout;

        public ItemSong(View itemView) {
            super(itemView);

            txtStt = (TextView) itemView.findViewById(R.id.song_stt);
            txtSongName = (TextView) itemView.findViewById(R.id.song_title);
            txtSongArtist = (TextView) itemView.findViewById(R.id.song_artist);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.item_song);
        }
    }

    private static class ItemLoadMore extends RecyclerView.ViewHolder {

        public ItemLoadMore(View itemView) {
            super(itemView);
        }
    }

    public interface OnItemSongClickListener {
        void itemSongClicked(Song song);
    }

    public static void setOnItemSongClickListener(OnItemSongClickListener songClick) {
        onItemSongClickListener = songClick;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public static void setOnLoadMoreListener(OnLoadMoreListener listener){
        onLoadMoreListener = listener;
    }

}
