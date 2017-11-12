package com.reversecoder.gk7.customer.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.customviews.library.application.BaseActivity;
import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.segmentradiogroup.SegmentedRadioGroup;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.model.WrapperUser;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AllURLs;
import com.reversecoder.gk7.customer.utility.AppSettings;


public class LoginActivity extends BaseActivity {

    SegmentedRadioGroup segmentedRadioGroup;
    RadioButton segmentedRadioButtonCustomer, segmentedRadioButtondriver;
    EditText edtEmail, edtPassword;
    Button btnLogin;
    ProgressDialog loadingDialog;
    Context mContext;
    String userType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_login);
//        setActivityTitle(AllConstants.TITLE_ACTIVITY_LOG_IN_DRIVER);
        setActivityTitle(AllConstants.TITLE_ACTIVITY_LOG_IN_CUSTOMER);
//        setActivityTitle(AllConstants.TITLE_ACTIVITY_LOG_IN);

        initLoginUI();
    }

    private void initLoginUI() {
        //set login view
        mContext = this;
        loadingDialog = new ProgressDialog(mContext);
        segmentedRadioGroup = (SegmentedRadioGroup) findViewById(R.id.segmented_rgroup_user_type);
        segmentedRadioButtonCustomer = (RadioButton) findViewById(R.id.segmented_rbtn_customer);
        segmentedRadioButtondriver = (RadioButton) findViewById(R.id.segmented_rbtn_driver);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        segmentedRadioButtonCustomer.setChecked(true);
        edtEmail.setText("aktarcse152@gmail.com");
        edtPassword.setText("123456");

//        segmentedRadioButtondriver.setChecked(true);
//        edtEmail.setText("aktar.bd84@gmail.com");
//        edtPassword.setText("123456");

        segmentedRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                                           @Override
                                                           public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                               switch (checkedId) {
                                                                   case R.id.segmented_rbtn_customer:
                                                                       edtEmail.setText("aktarcse152@gmail.com");
                                                                       edtPassword.setText("123456");
                                                                       userType = getResources().getString(R.string.user_type_customer);
                                                                       break;
                                                                   case R.id.segmented_rbtn_driver:
                                                                       edtEmail.setText("aktar.bd84@gmail.com");
//                                                                       edtEmail.setText("zaman@isoftware.com.bd");
                                                                       edtPassword.setText("123456");
                                                                       userType = getResources().getString(R.string.user_type_driver);
                                                                       break;
                                                               }
                                                           }
                                                       }

        );

        btnLogin.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                                            userType = "";
                                            String userEmail = "";
                                            String userPassword = "";
                                            userEmail = edtEmail.getText().toString().trim();
                                            userPassword = edtPassword.getText().toString().trim();

                                            if (AllSettingsManager.isNullOrEmpty(userEmail)) {
                                                Toast.makeText(getParentContext(), getResources().getString(R.string.alert_email), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            if (!AllSettingsManager.validateEmail(userEmail)) {
                                                Toast.makeText(getParentContext(), getResources().getString(R.string.alert_valid_email), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            if (AllSettingsManager.isNullOrEmpty(userPassword)) {
                                                Toast.makeText(getParentContext(), getResources().getString(R.string.alert_password), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            switch (segmentedRadioGroup.getCheckedRadioButtonId()) {

                                                case R.id.segmented_rbtn_customer:
                                                    userType = getResources().getString(R.string.user_type_customer);
                                                    break;
                                                case R.id.segmented_rbtn_driver:
                                                    userType = getResources().getString(R.string.user_type_driver);
                                                    break;
                                            }

                                            if (!NetworkManager.isConnected(getParentContext())) {
                                                Toast.makeText(getParentContext(), getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            doLogin(userType, userEmail, userPassword);
                                        }
                                    }

        );
    }

    private void doLogin(final String userType, final String email, final String password) {
        AsyncJob.doOnAsyncJob(new AsyncJob.OnAsyncJob<TaskResult>() {
            @Override
            public void doOnForeground() {
                loadingDialog.setMessage(getResources().getString(
                        R.string.dialog_loading));
                loadingDialog.setIndeterminate(false);
                loadingDialog.setCancelable(true);
                loadingDialog.setCanceledOnTouchOutside(false);
                loadingDialog.show();
                loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface arg0) {
                        if (loadingDialog != null
                                && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                    }
                });
            }

            @Override
            public TaskResult doInBackground() {
                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getLoginURL(), AllURLs.getLoginParams(userType, email, password), null);
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {

            }

            @Override
            public void doWhenFinished(TaskResult response) {
                if (loadingDialog != null
                        && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperUser responseData = WrapperUser.getResponseData(response.getResult().toString());
                    Log.d("success wrapper: ", responseData.getResponseMessage());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getResponseMessage())) {
                        Log.d("success message: ", responseData.getResponseMessage());
                        Log.d("success message:+id ", responseData.getUser().getId().toString());
//                        Toast.makeText(getParentContext(), responseData.getResponseMessage(), Toast.LENGTH_SHORT).show();
                        AppSettings.saveUserDetail(getParentContext(), userType, responseData.getUser());
                        Log.d("success message:+user", responseData.getUser().toString());
                        AppSettings.setDriverAverageBaseFare(getParentContext(),responseData.getUser().getAvgMileAndBaseFare().getAvgBaseFare());
                        Log.d("success message:","setDriverAverageBaseFare: "+AppSettings.getDriverAverageBaseFare(getParentContext()) );
                        AppSettings.setDriverAveragePerMileFare(getParentContext(),responseData.getUser().getAvgMileAndBaseFare().getAvgPerMileFare());
                        Log.d("success message:","setDriverAveragePerMileFare: "+AppSettings.getDriverAveragePerMileFare(getParentContext()) );
                        Intent in;
                        if (AppSettings.getUserType(getParentContext()).equalsIgnoreCase(AllConstants.TEXT_DRIVER)) {
                            in = new Intent(getParentContext(), DashboardDriverNavigationDrawerActivity.class);
                        } else {
                            in = new Intent(getParentContext(), DashboardCustomerNavigationDrawerActivity.class);
                        }
                        startActivity(in);
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
