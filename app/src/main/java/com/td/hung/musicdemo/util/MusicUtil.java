package com.td.hung.musicdemo.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.td.hung.musicdemo.entity.PlayList;
import com.td.hung.musicdemo.entity.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 15/10/2017.
 */

public class MusicUtil {

    private static MusicUtil musicUtil;

    public static MusicUtil instance(){
        if (musicUtil == null){
            musicUtil = new MusicUtil();
        }
        return musicUtil;
    }

    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (60*60));
        int minutes = (int)(milliseconds % (60*60)) / (60);
        int seconds = (int) ((milliseconds % (60*60)) % (60));
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public  static int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public static int progressToTimer(int progress, long totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public static ArrayList<Song> getSongList(Context mContext) {
        ArrayList<Song> allSong = new ArrayList<>();
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
                allSong.add(new Song(path, thisId, thisTitle, thisArtist, index));
                index++;
            }
            while (musicCursor.moveToNext());
        }
        return allSong;
    }

    public static List<PlayList> getMyPlayList(Context mContext){
        List<PlayList> lists = new ArrayList<>();
        String stringPLaylist = MusicPreference.newInstance(mContext).getString(MusicPreference.MY_PLAYLIST,"");
        Gson gson = new Gson();
        lists = gson.fromJson(stringPLaylist,new TypeToken<List<PlayList>>(){}.getType());
        return lists;
    }

}
