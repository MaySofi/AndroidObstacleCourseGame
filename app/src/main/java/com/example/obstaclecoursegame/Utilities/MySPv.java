package com.example.obstaclecoursegame.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class MySPv {
    private static final String DB_FILE = "DB_FILE";
    private static MySPv instance = null;
    private final SharedPreferences sharedPreferences;

    private MySPv(Context context) {
        sharedPreferences = context.getSharedPreferences(DB_FILE, Context.MODE_PRIVATE);
    }

    public static void init(Context context) {
        if(instance == null)
            instance = new MySPv(context);
    }

    public static MySPv getInstance() {
        return instance;
    }

    public String getString(String key, String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
