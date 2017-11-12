package com.reversecoder.gk7.customer.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.application.BaseActivity;
import com.customviews.library.callback.AppCallBack;
import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.event.ClickGuard;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.model.NewJobAlertDetail;
import com.reversecoder.gk7.customer.model.WrapperCommonResponse;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AllURLs;
import com.reversecoder.gk7.customer.utility.AppSettings;

public class AcceptJobDetailActivity extends BaseActivity {

    NewJobAlertDetail jobAlertDetail;
    TextView txtAddressFrom, txtAddressTo, txtNumberOfPassanger, txtEstimatedFare;
    LinearLayout linearLayoutHiddenConfirm;
    Button btnConfirm;
    AllConstants.DRIVER_JOB_TYPE jobType;
//    static AppCallBack mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_accept_job_detail);
        setActivityTitle(AllConstants.TITLE_ACTIVITY_ACCEPT_JOB_DETAIL);

        initAcceptJobDetailUI();
        initAcceptJobDetailAction();
    }

//    public static void setAppCallBack(AppCallBack callBack){
//        mCallBack=callBack;
//    }

    private void initAcceptJobDetailUI() {
        Intent intent = getIntent();
        if (intent != null) {
            jobAlertDetail = NewJobAlertDetail.getObject(intent.getStringExtra(AllConstants.KEY_INTENT_ACCEPT_JOB_DETAIL));
            jobType= AllConstants.DRIVER_JOB_TYPE.valueOf(intent.getStringExtra(AllConstants.KEY_INTENT_ACCEPT_JOB_TYPE));
        }

        txtAddressFrom = (TextView) findViewById(R.id.txt_address_from);
        txtAddressTo = (TextView) findViewById(R.id.txt_address_to);
        txtNumberOfPassanger = (TextView) findViewById(R.id.txt_number_of_passenger);
        txtEstimatedFare = (TextView) findViewById(R.id.txt_estimated_fare);
        linearLayoutHiddenConfirm = (LinearLayout) findViewById(R.id.ll_hidden_confirm);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);

        txtAddressFrom.setText(jobAlertDetail.getCustomerAddress());
        txtAddressTo.setText(jobAlertDetail.getCustomerDestinationAddress());
        if(jobAlertDetail.getIsFourPassenger().equalsIgnoreCase("0")){
            txtNumberOfPassanger.setText("4");
        }else if(jobAlertDetail.getIsFourPassenger().equalsIgnoreCase("1")){
            txtNumberOfPassanger.setText("More than 4");
        }else{
            txtNumberOfPassanger.setText(jobAlertDetail.getIsFourPassenger());
        }
        txtEstimatedFare.setText(getResources().getString(R.string.symbol_pound)+jobAlertDetail.getEstimatedFare());
    }

    private void initAcceptJobDetailAction() {
        btnConfirm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!NetworkManager.isConnected(getParentContext())) {
                    Toast.makeText(getParentContext(), getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                doAcceptJob(AppSettings.getUserID(getParentContext()), jobAlertDetail);

            }
        });
    }

    private void doAcceptJob(final String driverID, final NewJobAlertDetail newJobDetail) {
        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                TaskResult response = HttpUtility.doGetRequest(AllURLs.getDriverAcceptJobURL(driverID, newJobDetail.getOrderId()));
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
//                        Toast.makeText(getActivity(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                        AppSettings.setDriverStatus(getParentContext(), AllConstants.DRIVER_STATUS.JOB_ACCEPTED);
//                        setStatusText("Customer is waiting for you at " + newJobDetail.getCustomerAddress());
                        AppSettings.setDriverStatusText(getParentContext(), "Customer is waiting for you at " + newJobDetail.getCustomerAddress());
                        AppSettings.setJobAcceptedCustomerID(getParentContext(), newJobDetail.getCustomerId());
                        AppSettings.setJobAcceptedCustomerPhone(getParentContext(), newJobDetail.getCustomerMobile());
                        AppSettings.setJobAcceptedDriverOrderID(getParentContext(), newJobDetail.getOrderId());
                        AppSettings.setJobAcceptedCustomerLatitude(getParentContext(), newJobDetail.getCustomerLat());
                        AppSettings.setJobAcceptedCustomerLongitude(getParentContext(), newJobDetail.getCustomerLon());
                        AppSettings.setJobAcceptedCustomerDestinationLatitude(getParentContext(), newJobDetail.getCustomerDestinationLat());
                        AppSettings.setJobAcceptedCustomerDestinationLongitude(getParentContext(), newJobDetail.getCustomerDestinationLon());
//                        reInitializeAcceptJobUI(AppSettings.getDriverStatus(getParentContext()));
                        Toast.makeText(getParentContext(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getParentContext(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }


}
