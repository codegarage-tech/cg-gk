package com.reversecoder.gk7.customer.service;

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
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.activity.DashboardDriverNavigationDrawerActivity;
import com.reversecoder.gk7.customer.interfaces.ServiceCommunication;
import com.reversecoder.gk7.customer.model.WrapperCommonResponse;
import com.reversecoder.gk7.customer.model.WrapperNewJobAlert;
import com.reversecoder.gk7.customer.model.WrapperNewMessageAlert;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AllURLs;
import com.reversecoder.gk7.customer.utility.AppSettings;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * Created by rashed on 3/22/16.
 */
public class DriverService extends Service {

    private WeakHandler mHandler = new WeakHandler();
    private Timer mTimer = null;
    private Runnable mRunnable = null;
    private ExecutorService executorService = null;
    FutureTask ftNewMessageAlert, ftNewJobAlert, ftSendDriverLocation;
    private final IBinder mBinder = new DriverBinder();
    ServiceCommunication mServiceCommunication = null;

    public class DriverBinder extends Binder {
        public DriverService getServiceInstance() {
            return DriverService.this;
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
//                    Toast.makeText(getApplicationContext(), "Driver Message alert request sent at " + AllSettingsManager.getDateAndTime(),
//                            Toast.LENGTH_SHORT).show();
                    if (NetworkManager.isConnected(getApplicationContext())) {
                        try {
                            // This ExecutorService will run only a thread at a time
                            if (executorService == null) {
                                executorService = Executors.newSingleThreadExecutor();
                            }
                            //jobs are queued
                            if (AllConstants.CURRENT_LATITUDE != 0.0 && AllConstants.CURRENT_LONGITUDE != 0.0 && !AllConstants.CURRENT_ADDRESS.equalsIgnoreCase("")) {
                                Log.d("dService before:", "Lat: " + AllConstants.CURRENT_LATITUDE + " Lon: " + AllConstants.CURRENT_LONGITUDE);
                                ftSendDriverLocation = AsyncJob.doInBackground(doUpdateDriverLocation(AppSettings.getUserID(getApplicationContext())
                                        , AllConstants.CURRENT_LATITUDE + "", AllConstants.CURRENT_LONGITUDE + "", AllConstants.CURRENT_ADDRESS), executorService);
                            }

                            /* test purpose id is set to 1 for getting new job alert*/
                            if (AppSettings.getDriverStatus(getApplicationContext()) == AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS) {
                                ftNewJobAlert = AsyncJob.doInBackground(getNewJobAlert(AppSettings.getUserID(getApplicationContext())), executorService);
//                                ftNewJobAlert = AsyncJob.doInBackground(getNewJobAlert("5"), executorService);
                            } else if (AppSettings.getDriverStatus(getApplicationContext()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
                                ftNewJobAlert = AsyncJob.doInBackground(getNewJobAlert(AppSettings.getUserID(getApplicationContext())), executorService);
//                                ftNewJobAlert = AsyncJob.doInBackground(getNewJobAlert("5"), executorService);
                            } else if (AppSettings.getDriverStatus(getApplicationContext()) == AllConstants.DRIVER_STATUS.JOB_ACCEPTED) {
                                ftNewMessageAlert = AsyncJob.doInBackground(getNewMessageAlert(AppSettings.getUserID(getApplicationContext())), executorService);
                            } else if (AppSettings.getDriverStatus(getApplicationContext()) == AllConstants.DRIVER_STATUS.JOB_STARTED) {
                                ftNewMessageAlert = AsyncJob.doInBackground(getNewMessageAlert(AppSettings.getUserID(getApplicationContext())), executorService);
                            } else if (AppSettings.getDriverStatus(getApplicationContext()) == AllConstants.DRIVER_STATUS.JOB_COMPLETED) {
                            }

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
        SessionManager.setBooleanSetting(getApplicationContext(), AllConstants.SESSION_IS_DRIVER_SERVICE, true);
    }

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            // run on another thread
            mHandler.post(mRunnable);
        }
    }

    private AsyncJob.AsyncAction<TaskResult> doUpdateDriverLocation(final String dirverID, final String latitude, final String longitude, final String address) {
        AsyncJob.AsyncAction<TaskResult> updateDriverLocation = new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                JSONObject mParam = AllURLs.getSendDriverUpdatedLocationParams(dirverID, latitude, longitude, address);
                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getSendDriverUpdatedLocationURL(), mParam, null);
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
//                        Toast.makeText(getParentContext(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getApplicationContext(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        };
        return updateDriverLocation;
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
                        Intent showTaskIntent = new Intent(getApplicationContext(), DashboardDriverNavigationDrawerActivity.class);
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

    private AsyncJob.AsyncAction<TaskResult> getNewJobAlert(final String driverID) {
        AsyncJob.AsyncAction<TaskResult> newJobAlert = new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                TaskResult response = HttpUtility.doGetRequest(AllURLs.getNewJobAlertURL(driverID));
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {
            }

            @Override
            public void doWhenFinished(TaskResult response) {
                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperNewJobAlert responseData = WrapperNewJobAlert.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
                        Intent showTaskIntent = new Intent(getApplicationContext(), DashboardDriverNavigationDrawerActivity.class);
                        if (!AllSettingsManager.isNullOrEmpty(responseData.getNewjobs().getNormal())
                                && !responseData.getNewjobs().getNormal().equalsIgnoreCase("0")) {
                            showTaskIntent.putExtra(AllConstants.INTENT_KEY_NOTIFICATION_TO_NORMAL_JOB, true);
                            showTaskIntent.putExtra(AllConstants.INTENT_KEY_NOTIFICATION_TO_BID_JOB, false);
                            NotificationUtilManager.NotificationBuilder.getInstance().setAppIconID(R.mipmap.ic_launcher)
                                    .setIntent(showTaskIntent).setText("You have " + responseData.getNewjobs().getNormal() + " new jobs").setTitle(AllConstants.APP_NAME)
                                    .setDefaultSound().setVibration()
                                    .setNotificationID(AllConstants.NOTIFICATION_ID_NEW_NORMAL_JOB).setPendingIntentRequestID(AllConstants.PENDING_INTENT_REQUEST_ID_NEW_NORMAL_JOB)
                                    .setRemoveNotificationOnClick(true).create(getApplicationContext());
                        }
                        if (!AllSettingsManager.isNullOrEmpty(responseData.getNewjobs().getBid())
                                && !responseData.getNewjobs().getBid().equalsIgnoreCase("0")) {
                            showTaskIntent.putExtra(AllConstants.INTENT_KEY_NOTIFICATION_TO_BID_JOB, true);
                            showTaskIntent.putExtra(AllConstants.INTENT_KEY_NOTIFICATION_TO_NORMAL_JOB, false);
                            NotificationUtilManager.NotificationBuilder.getInstance().setAppIconID(R.mipmap.ic_launcher)
                                    .setIntent(showTaskIntent).setText("You have " + responseData.getNewjobs().getBid() + " bid jobs").setTitle(AllConstants.APP_NAME)
                                    .setDefaultSound().setVibration()
                                    .setNotificationID(AllConstants.NOTIFICATION_ID_NEW_BID_JOB).setPendingIntentRequestID(AllConstants.PENDING_INTENT_REQUEST_ID_NEW_BID_JOB)
                                    .setRemoveNotificationOnClick(true).create(getApplicationContext());
                        }
                        if (mServiceCommunication != null) {
                            mServiceCommunication.getData(responseData.getNewjobs(), AllConstants.SERVICE_RETURN_TYPE.DRIVER_NEW_JOB_ALERT);
                        }
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        };
        return newJobAlert;
    }

//    public void setJobAlert(SendJobAlert jobAlert) {
//        mJobAlert = jobAlert;
//    }

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
        if (ftNewJobAlert != null) {
            ftNewJobAlert.cancel(true);
            ftNewJobAlert = null;
        }
        if (ftSendDriverLocation != null) {
            ftSendDriverLocation.cancel(true);
            ftSendDriverLocation = null;
        }
    }

}