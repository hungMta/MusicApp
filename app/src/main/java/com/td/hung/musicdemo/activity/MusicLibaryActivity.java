package com.td.hung.musicdemo.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.td.hung.musicdemo.R;
import com.td.hung.musicdemo.adapter.SongAdapter;
import com.td.hung.musicdemo.entity.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MusicLibaryActivity extends AppCompatActivity implements SongAdapter.OnItemSonglistner {
    private static SongClickListener songCickListener;
    private ArrayList<Song> songList = new ArrayList<>();
    private ListView songView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songView = (ListView)findViewById(R.id.song_list);
        songList = (ArrayList<Song>) getIntent().getSerializableExtra(MusicMainActivity.KEY_LIST_SONG);
        SongAdapter.setOnItemSongListner(this);
        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdt = new SongAdapter(this, songList);
        songView.setAdapter(songAdt);
    }

    public void getSongList() {
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
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
                Uri uri = Uri.parse("file:///"+path);
                songList.add(new Song(path,thisId, thisTitle, thisArtist,index));
                index++;
            }
            while (musicCursor.moveToNext());
        }
    }

    @Override
    public void itemSongClicked(Song song) {
        onBackPressed();
        songCickListener.songClick(song);
//        if (isMyServiceRunning(MusicService.class)){
//            stopService(new Intent(this,MusicService.class));
//        }
//        Intent intent = new Intent(this, MusicService.class);
//        intent.putExtra("uri",song.getUri());
//        startService(intent);
    }

//    private boolean isMyServiceRunning(Class<?> serviceClass) {
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
//            if (serviceClass.getName().equals(service.service.getClassName())) {
//                return true;
//            }
//        }
//        return false;
//    }

    interface SongClickListener{
       void songClick(Song song);
   }

   public static void setSongClikcListener(SongClickListener listener){
       songCickListener = listener;
   }

}
