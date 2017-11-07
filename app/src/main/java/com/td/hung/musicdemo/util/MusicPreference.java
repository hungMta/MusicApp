package com.td.hung.musicdemo.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by PC on 16/10/2017.
 */

public class MusicPreference {
    private static final String NAME_CACHE = "MusicPreference";
    public static final String MY_PLAYLIST = "MY_PLAYLIST";
    private static Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static MusicPreference preferentUtil;

    public static MusicPreference newInstance(Context context){
        mContext = context;
        if (preferentUtil == null){
            preferentUtil = new MusicPreference();
        }
        return preferentUtil;
    }

    private MusicPreference(){
        sharedPreferences = mContext.getSharedPreferences(NAME_CACHE,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putString(String key, String value) {

        editor.putString(key, value).commit();
    }


    public void putLong(String key, long value) {

        editor.putLong(key, value).commit();
    }


    public long getLong(String key, long defValue) {

        return sharedPreferences.getLong(key, defValue);
    }

    public String getString(String key, String defValue) {

        return sharedPreferences.getString(key, defValue);
    }

    public void putInt(String key, int value) {

        editor.putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {

        return sharedPreferences.getInt(key ,defValue);
    }

    public void putFloat(String key, float value) {

        editor.putFloat(key, value).commit();
    }

    public float getFloat(String key, float defValue) {

        return sharedPreferences.getFloat(key, defValue);
    }

    public void putBoolean(String key, boolean value) {

        editor.putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {

        return sharedPreferences.getBoolean(key, defValue);
    }

    public void clearByName(String key) {

        editor.remove(key).commit();
    }

    public void clearAll() {

        editor.clear().apply();
    }
}
