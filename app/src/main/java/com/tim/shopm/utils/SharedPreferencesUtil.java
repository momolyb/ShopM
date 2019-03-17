package com.tim.shopm.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {
    private static SharedPreferences sharedpreference;

    public static void initSetting(Context context) {
        sharedpreference = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
    }

    public static void put(String key, String value) {
        sharedpreference.edit().putString(key, value).apply();
    }

    public static String get(String key) {
        return sharedpreference.getString(key, "");
    }
}
