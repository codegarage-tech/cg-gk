package com.reversecoder.gk7.driver.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.application.BaseActivity;
import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.reversecoder.gk7.driver.R;
import com.reversecoder.gk7.driver.model.Message;
import com.reversecoder.gk7.driver.model.WrapperCommonResponse;
import com.reversecoder.gk7.driver.utility.AllConstants;
import com.reversecoder.gk7.driver.utility.AllURLs;
import com.reversecoder.gk7.driver.utility.AppSettings;


public class MessageDetailActivity extends BaseActivity {

    Message mMessage = null;
    ProgressDialog loadingDialog;
    Context mContext;
    LinearLayout linearLayoutHiddenAddress, linearLayoutVisibleAddress, linearLayoutDeleteMessage, linearLayoutReplyToSender;
    TextView txtJobReference,txtSenderName, txtSenderEmail, txtReceiverEmail, txtShortDate, txtFullDate, txtMessage, txtReplyReceiverName, txtReplySenderEmail;
    EditText edtReply;
    ImageView ivDelete,ivSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityLayout(R.layout.activity_message_detail);
        setActivityTitle(AllConstants.TITLE_ACTIVITY_MESSAGE_DETAIL);
        initMessageDetailUI();
        initMessageDetailAction();
    }

    private void initMessageDetailUI() {
        mContext = MessageDetailActivity.this;
        loadingDialog = new ProgressDialog(mContext);
        linearLayoutHiddenAddress = (LinearLayout) findViewById(R.id.ll_hidden_message);
        linearLayoutVisibleAddress = (LinearLayout) findViewById(R.id.ll_visible_address);
        linearLayoutDeleteMessage = (LinearLayout) findViewById(R.id.ll_delete_message);
//        linearLayoutReplyMessage = (LinearLayout) findViewById(R.id.ll_reply_message);
        linearLayoutReplyToSender = (LinearLayout) findViewById(R.id.ll_reply_to_sender);
        txtJobReference = (TextView) findViewById(R.id.txt_job_reference);
        txtSenderName = (TextView) findViewById(R.id.txt_sender_name);
        txtSenderEmail = (TextView) findViewById(R.id.txt_sender_email);
        txtReceiverEmail = (TextView) findViewById(R.id.txt_receiver_email);
        txtShortDate = (TextView) findViewById(R.id.txt_short_date);
        txtFullDate = (TextView) findViewById(R.id.txt_full_date);
        txtMessage = (TextView) findViewById(R.id.txt_message);
        txtReplyReceiverName = (TextView) findViewById(R.id.txt_reply_receiver_name);
        txtReplySenderEmail = (TextView) findViewById(R.id.txt_reply_sender_email);
        edtReply = (EditText) findViewById(R.id.edt_reply);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);
        ivSend = (ImageView) findViewById(R.id.iv_send);

    }

    private void setMessageDetail() {
        txtJobReference.setText(mMessage.getJobReference());
        txtSenderName.setText(mMessage.getSenderName());
        txtReceiverEmail.setText(AppSettings.getUserInfo(mContext).getEmail());
        txtFullDate.setText(mMessage.getCreated());
        txtShortDate.setText(mMessage.getCreated());
        txtMessage.setText(mMessage.getMessage());
        txtReplyReceiverName.setText(AppSettings.getUserInfo(mContext).getName());
        txtReplySenderEmail.setText("to "+mMessage.getSenderEmail());
        linearLayoutVisibleAddress.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (linearLayoutHiddenAddress.getVisibility() == View.INVISIBLE) {
                    linearLayoutHiddenAddress.setVisibility(View.VISIBLE);
                    txtShortDate.setVisibility(View.INVISIBLE);
                    txtSenderEmail.setText(mMessage.getSenderEmail());
                } else {
                    linearLayoutHiddenAddress.setVisibility(View.INVISIBLE);
                    txtShortDate.setVisibility(View.VISIBLE);
                    txtSenderEmail.setText("to me");
                }
            }
        });

        linearLayoutDeleteMessage.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (NetworkManager.isConnected(getParentContext())) {
                    getDeleteMessageStatus(mMessage.getId(), AppSettings.getUserID(mContext));
                } else {
                    Toast.makeText(getParentContext(), getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        linearLayoutReplyToSender.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {

                if (AllSettingsManager.isNullOrEmpty(edtReply.getText().toString())) {
                    Toast.makeText(getParentContext(), getResources().getString(R.string.alert_empty_reply), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!NetworkManager.isConnected(getParentContext())) {
                    Toast.makeText(getParentContext(), getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                }
                getSendMessageStatus(mMessage.getReceiver(), mMessage.getOrder_id(), mMessage.getSender(),
                        mMessage.getSender_type(), mMessage.getReceiver_type(), edtReply.getText().toString());
            }
        });
    }

    private void initMessageDetailAction() {
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getBoolean(AllConstants.INTENT_KEY_MESSAGE_DETAIL_FROM_INBOX)) {
                mMessage = Message.getMessageObject(getIntent().getExtras().getString(AllConstants.INTENT_KEY_MESSAGE_DETAIL));
//                    Toast.makeText(getParentContext(), mMessage.getSenderName(), Toast.LENGTH_SHORT).show();
//                    ((TextView) findViewById(R.id.txt_message)).setText(mMessage.getMessage());
                setMessageDetail();
                if (mMessage.getIs_read().equalsIgnoreCase("0")) {
                    if (NetworkManager.isConnected(getParentContext())) {
                        getMarkAsReadMessageStatus(mMessage.getId(), AppSettings.getUserID(getParentContext()));
                    } else {
                        Toast.makeText(getParentContext(), getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            if (getIntent().getExtras().getBoolean(AllConstants.INTENT_KEY_MESSAGE_DETAIL_FROM_OUTBOX)) {

            }
        }

    }

    private void getMarkAsReadMessageStatus(final String messageID, final String driverOrCustomerID) {
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

                TaskResult response = HttpUtility.doGetRequest(AllURLs.getMarkAsReadMessageURL(messageID, driverOrCustomerID));
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
                    WrapperCommonResponse responseData = WrapperCommonResponse.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
//                      Toast.makeText(getParentContext(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getParentContext(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

    private void getDeleteMessageStatus(final String messageID, final String driverOrCustomerID) {
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

                TaskResult response = HttpUtility.doGetRequest(AllURLs.getDeleteMessageURL(messageID, driverOrCustomerID));
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
                    WrapperCommonResponse responseData = WrapperCommonResponse.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
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

    private void getSendMessageStatus(final String driverOrCustomerID, final String orderID
            , final String receiverID, final String receiverType, final String senderType, final String message) {


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

                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getSendMessageURL(),
                        AllURLs.getSendMessageParams(driverOrCustomerID, orderID, receiverID, receiverType, senderType, message), null);
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
                    WrapperCommonResponse responseData = WrapperCommonResponse.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
                        Toast.makeText(getParentContext(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getParentContext(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        Drawable deleteDrawable = ivDelete.getDrawable();
//        if (deleteDrawable instanceof BitmapDrawable) {
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) deleteDrawable;
//            Bitmap bitmap = bitmapDrawable.getBitmap();
//            bitmap.recycle();
//        }
//        Drawable sendDrawable = ivSend.getDrawable();
//        if (sendDrawable instanceof BitmapDrawable) {
//            BitmapDrawable bitmapDrawable = (BitmapDrawable) sendDrawable;
//            Bitmap bitmap = bitmapDrawable.getBitmap();
//            bitmap.recycle();
//        }
//        System.gc();
    }
}
