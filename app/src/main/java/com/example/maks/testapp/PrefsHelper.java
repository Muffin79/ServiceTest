package com.example.maks.testapp;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefsHelper {

    private final String KEY_COUNTER = "key_counter";
    private final String KEY_LAST_START_TIME = "key_last_start_time";

    private SharedPreferences preferences;

    public PrefsHelper(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public int getCounter() {
        return preferences.getInt(KEY_COUNTER,0);
    }

    public long getLastStartTime() {
        return preferences.getLong(KEY_LAST_START_TIME,0);
    }

    public void saveLastStartTime(long lastStartTime) {
        preferences.edit().putLong(KEY_LAST_START_TIME,lastStartTime).commit();
    }

    public void saveCounter(int counter) {
        preferences.edit().putInt(KEY_COUNTER,counter).commit();
    }
}
