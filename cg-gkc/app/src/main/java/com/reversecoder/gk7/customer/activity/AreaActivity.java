package com.reversecoder.gk7.customer.activity;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.customviews.library.application.BaseActivity;
import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.model.WrapperArea;
import com.reversecoder.gk7.customer.utility.AllURLs;


public class AreaActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_area);
        setActivityTitle("Area");

        geAreas();
    }

    private void geAreas() {
        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {

                TaskResult response = HttpUtility.doGetRequest(AllURLs.getAreaURL());
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {

            }

            @Override
            public void doWhenFinished(TaskResult response) {

                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperArea responseData = WrapperArea.getResponseData(response.getResult().toString());
                    ((TextView)findViewById(R.id.textView2)).setText(responseData.toString());

//                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
//                        Log.d("success message: ", responseData.toString());
//                        ((TextView)findViewById(R.id.textView2)).setText(responseData.toString());
//                        Toast.makeText(getParentContext(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
//                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
//                        Toast.makeText(getParentContext(), responseData.getError(), Toast.LENGTH_SHORT).show();
//                        Log.d("error message: ", responseData.getError());
//                        ((TextView)findViewById(R.id.textView2)).setText(responseData.getError());
//                    }
                }
            }
        });
    }
}
