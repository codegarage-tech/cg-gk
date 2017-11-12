package com.customviews.library.model;


import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * @author Md. Rashadul Alam
 */
public class TaskResult<T> {

    private Long[] mData=null;
    private Exception mError=null;
    private T mValue=null;
    private boolean isSuccess=false;

    public TaskResult(Long... data) {
        mData = data;
        isSuccess=true;
    }

    public TaskResult(T value) {
        mValue = value;
        isSuccess=true;
    }

    public TaskResult(Exception error) {
        mError = error;
        isSuccess=false;
    }

    public Exception getError() {
        return mError;
    }

    public T getResult() {
        return mValue;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public Long[] getData() {
        return mData;
    }
}