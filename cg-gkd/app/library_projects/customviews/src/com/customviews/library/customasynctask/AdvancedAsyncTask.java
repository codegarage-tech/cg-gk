package com.customviews.library.customasynctask;

import com.customviews.library.model.TaskParameter;
import com.customviews.library.model.TaskResult;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by rashed on 10/5/15.
 */
public abstract class AdvancedAsyncTask extends AsyncTask<Object, Long, TaskResult> {

    private Context mContext;

    public interface AdvancedAsyncTaskListener<Result>{

        public void onProgress(Long... progress);

        public void onSuccess(Result result);

        public void onFailure(Exception exception);
    }

    private AdvancedAsyncTaskListener mListner = null;

    public AdvancedAsyncTaskListener getAdvancedAsyncTaskListener() {
        return mListner;
    }

    public void setAdvancedAsyncTaskListener(AdvancedAsyncTaskListener listener) {
        this.mListner = listener;
    }

    public AdvancedAsyncTask(Context context, AdvancedAsyncTaskListener listener) {
        mContext = context;
        this.mListner = listener;
    }

    public Context getContext() {
        return mContext;
    }

//    @Override
//    public TaskResult doInBackground(TaskParameter... values){
//        return new TaskResult();
//    }

    @Override
    public void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        mListner.onProgress(values);
    }

    @Override
    public void onPostExecute(TaskResult result) {
        super.onPostExecute(result);
        if (result != null) {
            if (result.isSuccess()) {
                mListner.onSuccess(result.getResult());
            } else {
                mListner.onFailure(result.getError());
            }
        }
    }

    public void executeAdvancedAsyncTask(TaskParameter... value) {
        this.execute();
    }
}
