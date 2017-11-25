package com.chenjunquan.mingrinews.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.chenjunquan.mingrinews.R;

/**
 * Created by JunquanChen on 2017/11/23.
 */

public class CacheUtil {

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.app_name), Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void putBoolean(Context context, String key, Boolean value) {
        SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }
    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.app_name), Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(String.valueOf(R.string.app_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }
}
