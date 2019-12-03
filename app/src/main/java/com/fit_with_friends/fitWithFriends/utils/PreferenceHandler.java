package com.fit_with_friends.fitWithFriends.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

/**
 * Common PrefrenceConnector class for storing preference values.
 *
 * @author smadan
 */
public class PreferenceHandler {

    private static final String PREF_NAME = "FIT_WITH_FRIEND";
    private static final int MODE = Context.MODE_PRIVATE;

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();

    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).apply();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        if (key != null && value != null) {
            if (getEditor(context) != null) {
                getEditor(context).putString(key, value).commit();
            }
        }
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static void writeStringSet(Context context, String key, Set<String> value) {
        getEditor(context).putStringSet(key, value).commit();
    }

    public static Set<String> readStringSet(Context context, String key, Set<String> defValue) {
        return getPreferences(context).getStringSet(key, defValue);
    }

    private static SharedPreferences getPreferences(Context context) {
        if (context != null && context.getSharedPreferences(PREF_NAME, MODE) != null) {
            return context.getSharedPreferences(PREF_NAME, MODE);
        } else {
            return null;
        }
    }

    private static Editor getEditor(Context context) {
        if (context != null && context.getSharedPreferences(PREF_NAME, MODE) != null) {
            return getPreferences(context).edit();
        } else {
            return null;
        }
    }

    public static void clearPreferences(Context context) {
        getEditor(context).clear().commit();
    }
}