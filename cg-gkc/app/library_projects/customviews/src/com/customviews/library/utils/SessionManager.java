package com.customviews.library.utils;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.customviews.library.model.BaseModelItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.TypedValue;

public class SessionManager {

    /**
     * /** Set long value in shared preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setLongSetting(Context context, String key, long value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();

    }

    public static long getLongSetting(Context context, String key, long defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getLong(key, defaultValue);

    }

    public static void setStringSetting(Context context, String key, String value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static <K> void setObjectSetting(Context context, String key, K sharedPerferencesEntry) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(sharedPerferencesEntry);
        prefsEditor.putString(key, json);
        prefsEditor.commit();

    }

    public static <K> K getObjectSetting(Context context, String key, Class<K> clazz) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        Gson gson = new Gson();
        String json = appSharedPrefs.getString(key, "");
        return (K) gson.fromJson(json, clazz);
    }

    public static void setHashMapSetting(Context context, String key, HashMap value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.commit();
    }

    public static HashMap getHashMapSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sp.getString(key, "");
        java.lang.reflect.Type type = new TypeToken<HashMap>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void setArrayListSetting(Context context, String key, ArrayList<BaseModelItem> myCustomList)
            throws JSONException {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();

        JSONArray jarray = new JSONArray();
        for (int i = 0; i < myCustomList.size(); i++) {
            jarray.put(myCustomList.get(i).getJSONObject());
        }
        editor.putString(key, jarray.toString());
        editor.commit();
    }

    public static ArrayList<BaseModelItem> getArrayListSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        ArrayList<BaseModelItem> tempArray = new ArrayList<BaseModelItem>();
        JSONArray jarray;
        try {
            jarray = new JSONArray(sp.getString(key, ""));

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject myObject = jarray.getJSONObject(i);
                tempArray.add(new BaseModelItem(myObject.getString("Id"), myObject.getString("Name")));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return tempArray;

    }

    /**
     * get value from shared preference
     *
     * @param context
     * @param key
     * @return string
     */
    public static String getStringSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, "");

    }

    /**
     * get value from shared preference if not found return given default value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return string
     */
    public static String getStringSetting(Context context, String key, String defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(key, defaultValue);

    }

    /**
     * Set boolean value in shared preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setBooleanSetting(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    /**
     * get boolean value from shared preference if not found return given
     * default value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return boolean
     */
    public static boolean getBooleanSetting(Context context, String key, boolean defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean(key, defaultValue);

    }

    /**
     * Set int value in shared preference
     *
     * @param context
     * @param key
     * @param value
     */
    public static void setIntegerSetting(Context context, String key, int value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();

    }

    /**
     * get integer value from shared preference if not found return given
     * default value
     *
     * @param context
     * @param key
     * @param defaultValue
     * @return boolean
     */
    public static int getIntegerSetting(Context context, String key, int defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultValue);

    }

    /**
     * remove item from shared preference
     *
     * @param context
     * @param key
     */
    public static void removeSetting(Context context, String key) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public boolean removeAllSetting(Context context) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        return true;
    }

    /**
     * get shared preference editor
     *
     * @param context
     * @return
     */
    public static Editor getEditor(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.edit();
    }

    public static void addToCounter(Context context, String key, int value) {

        int myValue = getCounter(context, key, 0) + value;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = sp.edit();
        editor.putInt(key, myValue);
        editor.commit();

    }

    public static int getCounter(Context context, String key, int defaultValue) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getInt(key, defaultValue);

    }

    public static boolean checkingAccessPermission(Context context, String key) {

        if (SessionManager.getCounter(context, key, 0) < 5) {
            return true;
        } else {
            return false;
        }
    }

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

}
