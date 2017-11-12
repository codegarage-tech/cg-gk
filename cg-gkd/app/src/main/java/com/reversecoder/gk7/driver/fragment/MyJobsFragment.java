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
import com.reversecoder.gk7.driver.adapter.MyJobsListAdapter;
import com.reversecoder.gk7.driver.model.WrapperDriverJobs;
import com.reversecoder.gk7.driver.utility.AllConstants;
import com.reversecoder.gk7.driver.utility.AllURLs;
import com.reversecoder.gk7.driver.utility.AppSettings;

public class MyJobsFragment extends Fragment {

    private View parentView;
    ListView listMyJobs;
    ProgressDialog loadingDialog;
    MyJobsListAdapter myJobsListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_my_jobs, container, false);
        initMyJobsUI();
        initMyJobsAction();
        return parentView;
    }

    private void initMyJobsUI() {
        loadingDialog = new ProgressDialog(getActivity());
        myJobsListAdapter = new MyJobsListAdapter(getActivity());
        listMyJobs = (ListView) parentView.findViewById(R.id.listMyJobs);
        listMyJobs.setAdapter(myJobsListAdapter);
    }


    private void initMyJobsAction() {
        getMyJobs(AppSettings.getUserID(getActivity()));
    }

    private void getMyJobs(final String driverID) {
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
                TaskResult response = HttpUtility.doGetRequest(AllURLs.getDriverJobsURL(driverID));
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
                    WrapperDriverJobs responseData = WrapperDriverJobs.getResponseData(response.getResult().toString());
//                    WrapperDriverJobs responseData = WrapperDriverJobs.getResponseData(AllConstants.myJobs);
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getResponseMessage())) {
                        Log.d("success message: ", responseData.toString());
                        myJobsListAdapter.setData(responseData.getDriverJobs());
                        myJobsListAdapter.setSortOrder(AllConstants.SORT_ORDER.DESCENDING);
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

}
