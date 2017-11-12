package com.customviews.library.custommapview;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Rashed on 4/19/2016.
 */
public class DownloadTask extends AsyncTask<String, Void, String> {

    MapDataUpdate<String> downloadCallBack;
    Context mContext;

    public DownloadTask(Context context, MapDataUpdate<String> downloadCallBack) {
        super();
        mContext = context;
        this.downloadCallBack = downloadCallBack;
    }


    // Downloading data in non-ui thread
    @Override
    protected String doInBackground(String... url) {

        // For storing data from web service
        String data = "";

        try{
            // Fetching the data from web service
            data = MapUtilManager.downloadUrl(url[0]);
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
        return data;
    }

    // Executes in UI thread, after the execution of
    // doInBackground()
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        downloadCallBack.onUpdateData(result);

    }
}