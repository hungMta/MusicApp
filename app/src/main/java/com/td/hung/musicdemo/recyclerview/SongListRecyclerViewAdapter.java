package com.td.hung.musicdemo.recyclerview;

import android.content.Context;
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

    private Context mContext;
    private List<Song> songList;
    private static OnItemSongClickListener onItemSongClickListener;

    public SongListRecyclerViewAdapter(Context context, List<Song> list) {
        mContext = context;
        songList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemSong(View.inflate(mContext, R.layout.item_song_recycler, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((ItemSong) holder).txtStt.setText(position + 1 + "");
        ((ItemSong) holder).txtSongName.setText(songList.get(position).getTitle());
        ((ItemSong) holder).txtSongArtist.setText(songList.get(position).getArtist());
        ((ItemSong) holder).relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSongClickListener.itemSongClicked(songList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return songList == null ? 0 : songList.size();
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

    public interface OnItemSongClickListener{
        void itemSongClicked(Song song);
    }

    public static void setOnItemSongClickListener(OnItemSongClickListener songClick){
        onItemSongClickListener = songClick;
    }

}
