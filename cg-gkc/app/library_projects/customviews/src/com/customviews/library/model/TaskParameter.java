package com.customviews.library.model;


import java.util.ArrayList;

import org.json.JSONObject;

/**
 *
 * @author Md. Rashadul Alam
 */
public class TaskParameter<T> {

    private T mKey = null;
    private T mValue = null;
    private JSONObject mJSONParam = null;
    private ArrayList<TaskParameter> mArryListParam = null;
    private ArrayList<TaskParameter> mHeader = null;

    private TaskParameter(T key, T value) {
        mKey = key;
        mValue = value;
    }

    public static TaskParameter getInstance() {
        return new TaskParameter();
    }

    private TaskParameter() {
        mArryListParam = new ArrayList<TaskParameter>();
        mHeader = new ArrayList<TaskParameter>();
    }

    public T getKey() {
        return mKey;
    }

    public TaskParameter setKey(T mKey) {
        this.mKey = mKey;
        return getInstance();
    }

    public T getValue() {
        return mValue;
    }

    public TaskParameter setValue(T mValue) {
        this.mValue = mValue;
        return getInstance();
    }

    public TaskParameter addJSONParam(T key, T value) {
        try {
            if (mJSONParam == null) {
                mJSONParam = new JSONObject();
            }
            mJSONParam.put(key.toString(), value.toString());

            return this;
        } catch (Exception e) {
            return null;
        }
    }

    public TaskParameter addArrayListParam(T key, T value) {
        try {
            mArryListParam.add(new TaskParameter(key, value));
            return this;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<TaskParameter> getArrayListParam() {
        if (mArryListParam != null && mArryListParam.size() > 0) {
            return mArryListParam;
        }
        return null;
    }

    public JSONObject getJSONParam() {
        if (mJSONParam != null) {
            return mJSONParam;
        }
        return null;
    }

    public TaskParameter addHeader(T key, T value) {
        try {
            mHeader.add(new TaskParameter(key, value));
            return this;
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<TaskParameter> getHeader() {
        if (mHeader != null && mArryListParam.size() > 0) {
            return mHeader;
        }
        return null;
    }
}