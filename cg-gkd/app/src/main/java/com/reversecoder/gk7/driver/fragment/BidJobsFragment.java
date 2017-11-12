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
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.reversecoder.gk7.driver.R;
import com.reversecoder.gk7.driver.adapter.BidJobsListAdapter;
import com.reversecoder.gk7.driver.model.WrapperDriverBidJobs;
import com.reversecoder.gk7.driver.utility.AllConstants;
import com.reversecoder.gk7.driver.utility.AllURLs;
import com.reversecoder.gk7.driver.utility.AppSettings;

public class BidJobsFragment extends Fragment {

    private View parentView;
    ListView listBidJobs;
    ProgressDialog loadingDialog;
    BidJobsListAdapter bidJobsListAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_bid_jobs, container, false);
        return parentView;
    }

    @Override
    public void onResume(){
        super.onResume();
        initBidJobsUI();
        initBidJobsAction();
    }

    private void initBidJobsUI() {
        loadingDialog = new ProgressDialog(getActivity());
        bidJobsListAdapter = new BidJobsListAdapter(getActivity());
        listBidJobs = (ListView) parentView.findViewById(R.id.listBidJobs);
        listBidJobs.setAdapter(bidJobsListAdapter);
    }

    private void initBidJobsAction() {
        getBidJobs(AppSettings.getUserID(getActivity()));
    }

    private void getBidJobs(final String driverID) {
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
                TaskResult response = HttpUtility.doGetRequest(AllURLs.getDriverBidJobsURL(driverID));
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
                    WrapperDriverBidJobs responseData = WrapperDriverBidJobs.getResponseData(response.getResult().toString());
//                    WrapperDriverBidJobs responseData = WrapperDriverBidJobs.getResponseData(AllConstants.bidJobs);

                    if (!AllSettingsManager.isNullOrEmpty(responseData.getResponseMessage())) {
                        Log.d("success message: ", responseData.toString());
                        bidJobsListAdapter.setData(responseData.getDriverBidJobs());
                        bidJobsListAdapter.setSortOrder(AllConstants.SORT_ORDER.DESCENDING);
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

}
