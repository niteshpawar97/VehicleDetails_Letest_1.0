package com.niketit.vehicleinfo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

//SharedPrefs class helps to store the Shared Preferences data
public class SharedPrefs {

    private SharedPreferences pref;
    private Editor editor;

    public SharedPrefs(Context context) {
        int PRIVATE_MODE = 0;
        String APP_PREFERENCES = "AppPrefs";
        pref = context.getSharedPreferences(APP_PREFERENCES, PRIVATE_MODE);
    }

    public void putBooleanInPrefs(String key, Boolean value) {
        editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public Boolean getBooleanFromPrefs(String key) {
        return pref.getBoolean(key, false);
    }

    public void putStringInPrefs(String key, String value) {
        editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringFromPrefs(String key) {
        return pref.getString(key, "");
    }


    public void putIntInPrefs(String key, int value) {
        editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public int getIntFromPrefs(String key) {
        return pref.getInt(key, -1);
    }

    public void deleteStringKey(String key) {
        editor = pref.edit();
        editor.remove(key);
        editor.apply();
    }
}