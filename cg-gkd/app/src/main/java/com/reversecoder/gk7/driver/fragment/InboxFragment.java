package com.reversecoder.gk7.driver.fragment;


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
import com.customviews.library.customasynctask.WeakHandler;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.reversecoder.gk7.driver.R;
import com.reversecoder.gk7.driver.adapter.InboxListAdapter;
import com.reversecoder.gk7.driver.model.WrapperMessages;
import com.reversecoder.gk7.driver.utility.AllConstants;
import com.reversecoder.gk7.driver.utility.AllURLs;
import com.reversecoder.gk7.driver.utility.AppSettings;

import java.util.Timer;
import java.util.TimerTask;

public class InboxFragment extends Fragment {

    private View parentView;
    ListView listInbox;
    ProgressDialog loadingDialog;
    InboxListAdapter inboxListAdapter;
    private WeakHandler refreshInboxHandler = new WeakHandler();
    private Timer refreshInboxTimer = null;
    private Runnable refreshInboxRunnable = null;

    private enum INBOX_REQUEST_TYPE {MAIN_THREAD, BACKGROUND_THREAD}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_inbox, container, false);
        initInboxUI();
        return parentView;
    }

    private void initInboxUI() {
        loadingDialog = new ProgressDialog(getActivity());
        inboxListAdapter = new InboxListAdapter(getActivity());
        listInbox = (ListView) parentView.findViewById(R.id.listInbox);
        listInbox.setAdapter(inboxListAdapter);
    }

    private void initInboxAction() {
        if (NetworkManager.isConnected(getActivity())) {
            getInboxMessages(AppSettings.getUserID(getActivity()), INBOX_REQUEST_TYPE.BACKGROUND_THREAD);
        } else {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void getInboxMessages(final String customerID, final INBOX_REQUEST_TYPE requestType) {
        AsyncJob.doOnAsyncJob(new AsyncJob.OnAsyncJob<TaskResult>() {
            @Override
            public void doOnForeground() {
                if (requestType == INBOX_REQUEST_TYPE.MAIN_THREAD) {
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
            }

            @Override
            public TaskResult doInBackground() {
                TaskResult response = HttpUtility.doGetRequest(AllURLs.getInboxMessagesURL(customerID));
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {
            }

            @Override
            public void doWhenFinished(TaskResult response) {
                if (requestType == INBOX_REQUEST_TYPE.MAIN_THREAD) {
                    if (loadingDialog != null
                            && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                }
                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperMessages responseData = WrapperMessages.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getResponseMessage())) {
                        Log.d("success message: ", responseData.toString());
                        inboxListAdapter.setData(responseData.getAllMessages());
                        inboxListAdapter.setSortOrder(AllConstants.SORT_ORDER.DESCENDING);

                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

    class RefreshInboxTimerTask extends TimerTask {
        @Override
        public void run() {
            refreshInboxHandler.post(refreshInboxRunnable);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshInboxRunnable == null) {
            refreshInboxRunnable = new Runnable() {
                @Override
                public void run() {
//                    Toast.makeText(getActivity(), "I am from inbox refresh handler.",
//                            Toast.LENGTH_SHORT).show();
                    if (NetworkManager.isConnected(getActivity())) {
                        getInboxMessages(AppSettings.getUserID(getActivity()), INBOX_REQUEST_TYPE.BACKGROUND_THREAD);
                    }
                }
            };
        }
        if (refreshInboxTimer == null) {
            refreshInboxTimer = new Timer();
            refreshInboxTimer.scheduleAtFixedRate(new RefreshInboxTimerTask(), AllConstants.INBOX_NOTIFY_INTERVAL, AllConstants.INBOX_NOTIFY_INTERVAL);
        }

        initInboxAction();
    }

    @Override
    public void onPause() {
        super.onPause();
        refreshInboxHandler.removeCallbacks(refreshInboxRunnable);
        if (refreshInboxTimer != null) {
            refreshInboxTimer.cancel();
            refreshInboxTimer=null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        refreshInboxHandler.removeCallbacksAndMessages(null);
        if (refreshInboxTimer != null) {
            refreshInboxTimer.cancel();
        }
    }

}
