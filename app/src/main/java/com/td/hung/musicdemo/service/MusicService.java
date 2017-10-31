package com.td.hung.musicdemo.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.td.hung.musicdemo.activity.MusicMainActivity;
import com.td.hung.musicdemo.entity.Song;
import com.td.hung.musicdemo.util.MusicPreference;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by PC on 15/10/2017.
 */

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, AudioManager.OnAudioFocusChangeListener {
    public static final String ACTION_PLAY = "com.example.action.PLAY";
    public static final String CURRENT_SONG = "CURRENT_SONG";
    public static final String CURRENT_SECCOND = "CURRENT_SECCOND";
    public static final String OPENAPP = "OPENAPP";
    public static final String CHANGETRACK = "CHANGETRACK";
    public static final String GET_DURATION = "GET_DURATION";
    public static final String GET_CURRENTPOSITITION = "GET_CURRENTPOSITITION";
    MediaPlayer mMediaPlayer = null;
    private final IBinder iBinder = new LocalBinder();
    private Song currentlySong;
    private ArrayList<Song> songList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        songList = (ArrayList<Song>) intent.getSerializableExtra(MusicMainActivity.KEY_LIST_SONG);
        initMediaPlayer();
        checkLastPlay();
        Toast.makeText(getApplicationContext(), "playing", Toast.LENGTH_SHORT);
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri uri = Uri.parse(intent.getStringExtra("uri"));
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {

            mMediaPlayer.setDataSource(getApplicationContext(), uri);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepareAsync();
            Toast.makeText(getApplicationContext(), "playing", Toast.LENGTH_SHORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
        MusicPreference.newInstance(getApplicationContext()).putLong(GET_DURATION, mMediaPlayer.getDuration());
        MusicPreference.newInstance(getApplicationContext()).putLong(GET_CURRENTPOSITITION, mMediaPlayer.getCurrentPosition());
        changeTrack(currentlySong);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();

    }

    public void startPlay(long songID) {
        if (mMediaPlayer != null) {
            currentlySong = getSongById(songID);
            if (currentlySong != null) {
                try {
                    MusicPreference.newInstance(getApplicationContext()).putString(CURRENT_SONG, currentlySong.toString());
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(currentlySong.getUri().toString()));
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Couldn't play this song", Toast.LENGTH_SHORT);
                    e.printStackTrace();
                }
            }
        }
    }

    public void play() {
        if (mMediaPlayer != null) {
            if (currentlySong != null) {
                mMediaPlayer.start();
            }
        }
    }

    public void pause() {
        if (mMediaPlayer != null)
            if (currentlySong != null && isPlaying()) {
                mMediaPlayer.pause();
            }
    }


    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        } else {
            return false;
        }
    }

    public void next() {
        if (mMediaPlayer != null) {
            Song nextSong;
            if (currentlySong.getIndex() < songList.size()) {
                nextSong = songList.get(currentlySong.getIndex() + 1);
            } else {
                nextSong = songList.get(0);
            }
            mMediaPlayer.reset();
            startPlay(nextSong.getID());
            changeTrack(currentlySong);
        }
    }

    public void previous() {
        if (mMediaPlayer != null) {
            Song nextSong;
            if (currentlySong.getIndex() > 0) {
                nextSong = songList.get(currentlySong.getIndex() - 1);
            } else {
                nextSong = songList.get(songList.size() - 1);
            }
            mMediaPlayer.reset();
            startPlay(nextSong.getID());
            changeTrack(currentlySong);
        }
    }

    private void initMediaPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
    }

    public void stop() {
        if (mMediaPlayer != null) {
            if (isPlaying()) {
                mMediaPlayer.stop();
                MusicPreference.newInstance(getApplicationContext()).putString(CURRENT_SONG, currentlySong.toString());
            }
        }
    }

    public Song getCurrentlySong() {
        return currentlySong;
    }

    public class LocalBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private Song getSongById(long id) {
        for (Song song : songList) {
            if (song.getID() == id)
                return song;
        }
        return null;
    }


    private void checkLastPlay() {
        String stringStrong = MusicPreference.newInstance(getApplicationContext()).getString(CURRENT_SONG, "");
        if (stringStrong != "") {
            Gson gson = new Gson();
            currentlySong = gson.fromJson(stringStrong, Song.class);
            broadcastInitService(currentlySong);
        }
    }

    // init service
    private void broadcastInitService(Song song) {
        Intent intent = new Intent(OPENAPP);
        intent.putExtra(CURRENT_SONG, song);
        sendBroadcast(intent);
    }

    //
    private void changeTrack(Song song) {
        Intent intent = new Intent(CHANGETRACK);
        intent.putExtra(CURRENT_SONG, song);
        sendBroadcast(intent);
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }
}
