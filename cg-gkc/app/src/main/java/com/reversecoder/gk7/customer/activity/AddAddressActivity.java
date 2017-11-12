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
import com.reversecoder.gk7.customer.model.WrapperCommonResponse;
import com.reversecoder.gk7.customer.utility.AllURLs;

import org.json.JSONObject;


public class AddAddressActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_add_address);
        setActivityTitle("Add Address");

        addAddress("4", "Basundhara, Dhaka", "Kuril", "1", "CF243DT");
    }

    private void addAddress(final String customerID, final String addressTitle
            , final String streetName, final String areaID, final String postalCode) {
        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {

                JSONObject mParam = AllURLs.getAddAddressParams(customerID, addressTitle
                        , streetName, areaID, postalCode);

                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getAddOrEditAddressURL(), mParam, null);
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {

            }

            @Override
            public void doWhenFinished(TaskResult response) {

                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperCommonResponse responseData = WrapperCommonResponse.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
                        ((TextView) findViewById(R.id.textView2)).setText(responseData.toString());
                        Toast.makeText(getParentContext(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getParentContext(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                        ((TextView) findViewById(R.id.textView2)).setText(responseData.getError());
                    }
                }
            }
        });
    }
}
