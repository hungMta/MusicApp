package com.td.hung.musicdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.entity.Song;

import java.util.ArrayList;

/**
 * Created by PC on 15/10/2017.
 */

public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songs;
    private LayoutInflater songInf;
    private static OnItemSonglistner onItemSonglistner;
    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //map to song layout
        RelativeLayout songLay = (RelativeLayout) songInf.inflate(R.layout.song, parent, false);
        songLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSonglistner.itemSongClicked(songs.get(position));
            }
        });
        //get title and artist views
        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        TextView stt = (TextView)songLay.findViewById(R.id.song_stt);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings
        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        stt.setText(""+position + 1);

        //set position as tag
        songLay.setTag(position);
        return songLay;
    }

   public interface OnItemSonglistner{
        void itemSongClicked(Song song);
   }
    public static void setOnItemSongListner(OnItemSonglistner listner){
        onItemSonglistner = listner;
    }
}
