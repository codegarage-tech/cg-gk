package com.reversecoder.gk7.customer.fragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.adapter.OutboxListAdapter;
import com.reversecoder.gk7.customer.model.WrapperMessages;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AllURLs;
import com.reversecoder.gk7.customer.utility.AppSettings;

public class OutboxFragment extends Fragment {

    private View parentView;
    ListView listOutbox;
    ProgressDialog loadingDialog;
    OutboxListAdapter outboxListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_outbox, container, false);
        initOutboxUI();
        initOutboxAction();
        return parentView;
    }

    private void initOutboxUI() {
        loadingDialog = new ProgressDialog(getActivity());
        outboxListAdapter = new OutboxListAdapter(getActivity());
        listOutbox = (ListView) parentView.findViewById(R.id.listOutbox);
        listOutbox.setAdapter(outboxListAdapter);
    }


    private void initOutboxAction() {
        getOutboxMessages(AppSettings.getUserID(getActivity()));
    }

    private void getOutboxMessages(final String customerID) {
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
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
                TaskResult response = HttpUtility.doGetRequest(AllURLs.getOutboxMessagesURL(customerID));
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
                    WrapperMessages responseData = WrapperMessages.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getResponseMessage())) {
                        Log.d("success message: ", responseData.toString());
                        outboxListAdapter.setData(responseData.getAllMessages());
                        outboxListAdapter.setSortOrder(AllConstants.SORT_ORDER.DESCENDING);
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

}
