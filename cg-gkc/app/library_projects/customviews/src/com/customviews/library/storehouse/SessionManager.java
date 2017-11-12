/*********************************************************************
  SOURNEXT CONFIDENTIAL
  Copyright (C) SOURNEXT Corporation
 *********************************************************************/
package com.customviews.library.storehouse;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

/**
 * @author Md. Rashadul Alam
 *
 *This class is for handling session.
 */
public class SessionManager {

    /**
     * Set string value into the shared preference.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param value the string value that will be stored into the shared
     * preference against the string key.
     */
    public static void setStringSetting(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Get string value from shared preference.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @return string the string value that is stored into the shared
     * preference against the string key.
     */
    public static String getStringSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, null);

    }

    /**
     * Get string value from shared preference if not found return
     * given default value.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param defaultValue the default string value that is returned if
     * any value is not stored into the shared preference.
     * @return string the string value that is stored into the shared
     * preference against the string key.
     */
    public static String getStringSetting(Context context, String key, String defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultValue);

    }

    /**
     * Set boolean value into the shared preference.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param value the boolean value that will be stored into the shared
     * preference against the string key.
     */
    public static void setBooleanSetting(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    /**
     * Get boolean value from shared preference if not found return
     * given default value.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param defaultValue the default boolean value that is returned if
     * any value is not stored into the shared preference.
     * @return boolean the boolean value that is stored into the shared
     * preference against the string key.
     */
    public static boolean getBooleanSetting(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defaultValue);

    }

    /**
     * Set integer value into the shared preference.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param value the integer value that will be stored into the shared
     * preference against the string key.
     */
    public static void setIntegerSetting(Context context, String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    /**
     * Get integer value from shared preference if not found return
     * given default value.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param defaultValue the default integer value that is returned if
     * any value is not stored into the shared preference.
     * @return integer the string value that is stored into the shared
     * preference against the string key.
     */
    public static int getIntegerSetting(Context context, String key, int defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultValue);

    }

    /**
     * Set long value into the shared preference.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param value the long value that will be stored into the shared
     * preference against the string key.
     */
    public static void setLongSetting(Context context, String key, long value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    /**
     * Get long value from shared preference if not found return
     * given default value.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param defaultValue the default long value that is returned if
     * any value is not stored into the shared preference.
     * @return long the long value that is stored into the shared
     * preference against the string key.
     */
    public static long getLongSetting(Context context, String key, long defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(key, defaultValue);
    }

    /**
     * Set float value into the shared preference.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param value the float value that will be stored into the shared
     * preference against the string key.
     */
    public static void setFloatSetting(Context context, String key, float value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * Get float value from shared preference if not found return
     * given default value.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param defaultValue the default float value that is returned if
     * any value is not stored into the shared preference.
     * @return float the float value that is stored into the shared
     * preference against the string key.
     */
    public static float getFloatSetting(Context context, String key, float defValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getFloat(key, defValue);
    }

    /**
     * Set HashMap value into the shared preference.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @param value the HashMap value that will be stored into the shared
     * preference against the string key.
     */
    public static void setHashMapSetting(Context context, String key, HashMap value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    /**
     * Get HashMap value from shared preference if not found return
     * given default value.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of shared preference.
     * @return HashMap the HashMap value that is stored into the shared
     * preference against the string key.
     */
    public static HashMap getHashMapSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sp.getString(key, "");
        java.lang.reflect.Type type = new TypeToken<HashMap>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    /**
     * remove item from shared preference
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of the shared preference.
     */
    public static void removeSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     *Get boolean value about the key is exist or not.
     *
     * @param context the context of the activity or application itself.
     * @param key the string key of the shared preference.
     * @return boolean the boolean value of key's existence.
     */
    public static boolean isExistKey(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.contains(key);
    }
}
