package com.reversecoder.gk7.driver.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.customasynctask.WeakHandler;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.customviews.library.utils.NotificationUtilManager;
import com.customviews.library.utils.SessionManager;
import com.reversecoder.gk7.driver.R;
import com.reversecoder.gk7.driver.activity.DashboardCustomerNavigationDrawerActivity;
import com.reversecoder.gk7.driver.interfaces.ServiceCommunication;
import com.reversecoder.gk7.driver.model.WrapperAcceptJobDetailsAlert;
import com.reversecoder.gk7.driver.model.WrapperNewMessageAlert;
import com.reversecoder.gk7.driver.model.WrapperSpecificDriverUpdatedLocation;
import com.reversecoder.gk7.driver.utility.AllConstants;
import com.reversecoder.gk7.driver.utility.AllURLs;
import com.reversecoder.gk7.driver.utility.AppSettings;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by rashed on 3/22/16.
 */
public class CustomerService extends Service {

    private WeakHandler mHandler = new WeakHandler();
    private Timer mTimer = null;
    private Runnable mRunnable = null;
    private ExecutorService executorService = null;
    FutureTask ftAcceptJobNotification, ftNewMessageAlert, ftDriverUpdatedLocation;
    private final IBinder mBinder = new CustomerBinder();
    ServiceCommunication mServiceCommunication = null;

    public class CustomerBinder extends Binder {
        public CustomerService getServiceInstance() {
            return CustomerService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void createConnection(ServiceCommunication serviceCommunication) {
        mServiceCommunication = serviceCommunication;
    }

    @Override
    public void onCreate() {
        if (mRunnable != null) {
        } else {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    if (NetworkManager.isConnected(getApplicationContext())) {
                        try {
                            // This ExecutorService will run only a thread at a time
                            if (executorService == null) {
                                executorService = Executors.newSingleThreadExecutor();
                            }
                            //jobs are queued
                            if (AppSettings.getCustomerStatus(getApplicationContext()) == AllConstants.CUSTOMER_STATUS.IDLE) {
                            } else if (AppSettings.getCustomerStatus(getApplicationContext()) == AllConstants.CUSTOMER_STATUS.REQUESTED_FOR_A_TAXI) {
                                ftAcceptJobNotification = AsyncJob.doInBackground(getAcceptJobAlert(AppSettings.getUserID(getApplicationContext()), AppSettings.getJobAcceptedCustomerOrderID(getApplicationContext())), executorService);
                            } else if (AppSettings.getCustomerStatus(getApplicationContext()) == AllConstants.CUSTOMER_STATUS.REQUEST_ACCEPTED) {
                                ftAcceptJobNotification = AsyncJob.doInBackground(getAcceptJobAlert(AppSettings.getUserID(getApplicationContext()), AppSettings.getJobAcceptedCustomerOrderID(getApplicationContext())), executorService);
                                ftNewMessageAlert = AsyncJob.doInBackground(getNewMessageAlert(AppSettings.getUserID(getApplicationContext())), executorService);
                                ftDriverUpdatedLocation = AsyncJob.doInBackground(getDriverUpdatedLocation(), executorService);
                            } else if (AppSettings.getCustomerStatus(getApplicationContext()) == AllConstants.CUSTOMER_STATUS.RIDE_STARTED) {
                                ftAcceptJobNotification = AsyncJob.doInBackground(getAcceptJobAlert(AppSettings.getUserID(getApplicationContext()), AppSettings.getJobAcceptedCustomerOrderID(getApplicationContext())), executorService);
//                                ftDriverUpdatedLocation = AsyncJob.doInBackground(getDriverUpdatedLocation(), executorService);
                                ftNewMessageAlert = AsyncJob.doInBackground(getNewMessageAlert(AppSettings.getUserID(getApplicationContext())), executorService);
                            } else if (AppSettings.getCustomerStatus(getApplicationContext()) == AllConstants.CUSTOMER_STATUS.RIDE_COMPLETED) {
                                ftAcceptJobNotification = AsyncJob.doInBackground(getAcceptJobAlert(AppSettings.getUserID(getApplicationContext()), AppSettings.getJobAcceptedCustomerOrderID(getApplicationContext())), executorService);
//                                ftDriverUpdatedLocation = AsyncJob.doInBackground(getDriverUpdatedLocation(), executorService);
                            }
//                            Toast.makeText(getApplicationContext(), "Customer status: "+AppSettings.getCustomerStatus(getApplicationContext()), Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                        } finally {
                            if (executorService != null) {
                                executorService.shutdown();
                                executorService = null;
                            }
                        }
                    }
                }
            };
        }
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), AllConstants.SERVICE_NOTIFY_INTERVAL, AllConstants.SERVICE_NOTIFY_INTERVAL);
        SessionManager.setBooleanSetting(getApplicationContext(), AllConstants.SESSION_IS_CUSTOMER_SERVICE, true);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(mRunnable);
        }
    }

    //    private void getNewMessageAlert(final String driverOrCustomerID) {
//        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
    private AsyncJob.AsyncAction<TaskResult> getAcceptJobAlert(final String customerID, final String orderID) {
        AsyncJob.AsyncAction<TaskResult> newMessageAlert = new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                TaskResult response = HttpUtility.doGetRequest(AllURLs.getAcceptJobNotificationURL(customerID, orderID));
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {

            }

            @Override
            public void doWhenFinished(TaskResult response) {
                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperAcceptJobDetailsAlert responseData = WrapperAcceptJobDetailsAlert.getResponseData(response.getResult().toString());
                    if (mServiceCommunication != null) {
                        mServiceCommunication.getData(responseData, AllConstants.SERVICE_RETURN_TYPE.CUSTOMER_ACCEPTED_JOB_ALERT);
                    }
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
//                        Intent showTaskIntent = new Intent(getApplicationContext(), DashboardCustomerNavigationDrawerActivity.class);
//                        showTaskIntent.putExtra(AllConstants.INTENT_KEY_NOTIFICATION_TO_ACCEPT_JOB, true);
//                        NotificationUtilManager.NotificationBuilder.getInstance().setAppIconID(R.mipmap.ic_launcher)
//                                .setIntent(showTaskIntent).setText(responseData.getSuccess()).setTitle(AllConstants.APP_NAME).setDefaultSound()
//                                .setNotificationID(AllConstants.NOTIFICATION_ID_ACCEPT_JOB).setPendingIntentRequestID(AllConstants.PENDING_INTENT_REQUEST_ID_ACCEPT_JOB)
//                                .setRemoveNotificationOnClick(true).setOnlyAlertOnce(true).create(getApplicationContext());
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        };
        return newMessageAlert;
    }

    private AsyncJob.AsyncAction<TaskResult> getNewMessageAlert(final String driverOrCustomerID) {
        AsyncJob.AsyncAction<TaskResult> newMessageAlert = new AsyncJob.AsyncAction<TaskResult>() {

            @Override
            public TaskResult doOnBackground() {

                TaskResult response = HttpUtility.doGetRequest(AllURLs.getNewMessageAlertURL(driverOrCustomerID));
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {

            }

            @Override
            public void doWhenFinished(TaskResult response) {
                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperNewMessageAlert responseData = WrapperNewMessageAlert.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
                        Intent showTaskIntent = new Intent(getApplicationContext(), DashboardCustomerNavigationDrawerActivity.class);
                        showTaskIntent.putExtra(AllConstants.INTENT_KEY_NOTIFICATION_TO_INBOX, true);
                        NotificationUtilManager.NotificationBuilder.getInstance().setAppIconID(R.mipmap.ic_launcher)
                                .setIntent(showTaskIntent).setText(responseData.getSuccess()).setTitle(AllConstants.APP_NAME)
                                .setDefaultSound().setVibration()
                                .setNotificationID(AllConstants.NOTIFICATION_ID_NEW_MESSAGE).setPendingIntentRequestID(AllConstants.PENDING_INTENT_REQUEST_ID_NEW_MESSAGE)
                                .setRemoveNotificationOnClick(true).create(getApplicationContext());
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        };
        return newMessageAlert;
    }


    private AsyncJob.AsyncAction<TaskResult> getDriverUpdatedLocation() {
        AsyncJob.AsyncAction<TaskResult> updateDriverLocation = new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                TaskResult response = HttpUtility.doGetRequest(AllURLs.getSpecificDriverUpdatedLocationURL(AppSettings.getJobAcceptedDriverID(getApplicationContext())));
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {
            }

            @Override
            public void doWhenFinished(TaskResult response) {
                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperSpecificDriverUpdatedLocation responseData = WrapperSpecificDriverUpdatedLocation.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
                        if (mServiceCommunication != null) {
                            mServiceCommunication.getData(responseData.getDriverCurrentLocation(), AllConstants.SERVICE_RETURN_TYPE.CUSTOMER_GET_DRIVER_CURRENT_LOCATION);
                        }
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getApplicationContext(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        };
        return updateDriverLocation;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
        if (ftNewMessageAlert != null) {
            ftNewMessageAlert.cancel(true);
            ftNewMessageAlert = null;
        }
        if (ftDriverUpdatedLocation != null) {
            ftDriverUpdatedLocation.cancel(true);
            ftDriverUpdatedLocation = null;
        }

    }

}