package com.reversecoder.gk7.customer.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.model.WrapperCommonResponse;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AllURLs;
import com.reversecoder.gk7.customer.utility.AppSettings;

public class DialogSendMessage extends Dialog {

    private Activity context_;
    private String messageTitle = "";
    private Button btnOk, btnClose;
    private TextView tvMessageTitle;
    private EditText edtMessage;
    private AllConstants.USER_TYPE userType = AllConstants.USER_TYPE.CUSTOMER;
    private DialogSendMessage dialogSendMessage;

    public DialogSendMessage(Activity context, String messageTitle, AllConstants.USER_TYPE userType) {
        super(context);
        this.context_ = context;
        this.messageTitle = messageTitle;
        this.userType = userType;
        dialogSendMessage = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.dialog_send_message);
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnClose = (Button) findViewById(R.id.btn_close);
        tvMessageTitle = (TextView) findViewById(R.id.message_title);
        edtMessage = (EditText) findViewById(R.id.edt_message);
        tvMessageTitle.setText(messageTitle);

        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (AllSettingsManager.isNullOrEmpty(edtMessage.getText().toString())) {
                    Toast.makeText(context_, "Please type message", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!NetworkManager.isConnected(context_)) {
                    Toast.makeText(context_, context_.getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (userType == AllConstants.USER_TYPE.CUSTOMER) {
                    doSendMessage(dialogSendMessage, AppSettings.getUserID(context_), AppSettings.getJobAcceptedCustomerOrderID(context_)
                            , AppSettings.getJobAcceptedDriverID(context_), AppSettings.getJobAcceptedCustomerReceiverType(context_)
                            , AppSettings.getJobAcceptedCustomerSenderType(context_), edtMessage.getText().toString());
                } else {
                    doSendMessage(dialogSendMessage, AppSettings.getUserID(context_), AppSettings.getJobAcceptedDriverOrderID(context_)
                            , AppSettings.getJobAcceptedCustomerID(context_), AppSettings.getJobAcceptedDriverReceiverType(context_)
                            , AppSettings.getJobAcceptedDriverSenderType(context_), edtMessage.getText().toString());
                }
            }
        });

        btnClose.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                dismiss();

            }
        });
    }

    private void doSendMessage(final Dialog dialog, final String driverOrCustomerID, final String orderID
            , final String receiverID, final String receiverType, final String senderType, final String message) {


        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {

                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getSendMessageURL(),
                        AllURLs.getSendMessageParams(driverOrCustomerID, orderID, receiverID, receiverType, senderType, message), null);
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
                        dialog.dismiss();
                        Log.d("success message: ", responseData.toString());
                        Toast.makeText(context_, responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(context_, responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

}
