package com.reversecoder.gk7.customer.activity;


import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.application.BaseActivity;
import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.model.WrapperAddress;
import com.reversecoder.gk7.customer.utility.AllURLs;


public class AddressActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_address);
        setActivityTitle("Address");

        getAddress("3");
    }

    private void getAddress(final String customerID) {
        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {

                TaskResult response = HttpUtility.doGetRequest(AllURLs.getAddressURL(customerID));
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {

            }

            @Override
            public void doWhenFinished(TaskResult response) {

                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperAddress responseData = WrapperAddress.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
                        ((TextView)findViewById(R.id.textView2)).setText(responseData.toString());
                        Toast.makeText(getParentContext(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getParentContext(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                        ((TextView)findViewById(R.id.textView2)).setText(responseData.getError());
                    }
                }
            }
        });
    }
}
