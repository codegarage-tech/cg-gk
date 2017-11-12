package com.reversecoder.gk7.customer.activity;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.application.BaseNavigationDrawerActivity;
import com.customviews.library.custommapview.ReverseGeocodingTask;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.storehouse.SessionManager;
import com.customviews.library.utils.NetworkManager;
import com.customviews.library.utils.NotificationUtilManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.fragment.JobStatusFragment;
import com.reversecoder.gk7.customer.fragment.BidJobsFragment;
import com.reversecoder.gk7.customer.fragment.InboxFragment;
import com.reversecoder.gk7.customer.fragment.MyJobsFragment;
import com.reversecoder.gk7.customer.fragment.NewJobsFragment;
import com.reversecoder.gk7.customer.fragment.OutboxFragment;
import com.reversecoder.gk7.customer.fragment.WorkingStatusFragment;
import com.reversecoder.gk7.customer.interfaces.ServiceCommunication;
import com.reversecoder.gk7.customer.model.NewJobAlert;
import com.reversecoder.gk7.customer.model.NewJobAlertDetail;
import com.reversecoder.gk7.customer.model.User;
import com.reversecoder.gk7.customer.service.DriverService;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AppSettings;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DashboardDriverNavigationDrawerActivity extends BaseNavigationDrawerActivity {

    Handler drawerHandler = new Handler();
    TextView profileName, profileEmail;
    ImageView profileImage;
    User userDetail;
    Intent driverServiceIntent;
    //Accessing driver service
    private DriverService mDriverService;
    private boolean isDriverServiceStarted = false;
    //Location setting variable
    protected static final String TAG = "DashboardDriverNavigationDrawerActivity";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30 * 1000;//30 second
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS;
    //    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "KEY_REQUESTING_LOCATION_UPDATES";
    protected final static String KEY_LOCATION = "KEY_LOCATION";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "KEY_LAST_UPDATED_TIME_STRING";
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    // Location settings Labels.
    private Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;


    GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {
        //Runs when a GoogleApiClient object successfully connects.
        @Override
        public void onConnected(Bundle bundle) {
            Log.i(TAG, "Connected to GoogleApiClient");
            if (mCurrentLocation == null) {
                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
//                Toast.makeText(getParentContext(), "First\nLat: " + mCurrentLocation.getLatitude() + "Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.i(TAG, "Connection suspended");
        }
    };

    GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(ConnectionResult result) {
            Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
        }
    };

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mCurrentLocation = location;
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateLocationUI();
//            Toast.makeText(getParentContext(), "Current\nLat: " + mCurrentLocation.getLatitude() + "Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            if (mCurrentLocation != null) {
                if (getCurrentLatitude() != 0.0 && getCurrentLongitude() != 0.0) {
                    if (NetworkManager.isConnected(getParentContext())) {
                        if (NetworkManager.isConnected(getParentContext())) {
                            try {
                                new ReverseGeocodingTask(getParentContext(), new ReverseGeocodingTask.AddressCallback() {
                                    @Override
                                    public void onAddressUpdate(String address) {
                                        setCurrentAddress(address);
                                    }
                                }).execute(new LatLng(getCurrentLatitude(), getCurrentLongitude()));
                            } catch (Exception ex) {
                            }
                        }
                    }

//                    //Initialize map pin location on fragment.
                    if (getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_JOB_STATUS) != null) {

//                        if (AppSettings.getDriverStatus(getParentContext()) != AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS
//                                && AppSettings.getDriverStatus(getParentContext()) != AllConstants.DRIVER_STATUS.JOB_ACCEPTED) {
                        getAcceptJobFragmentObject().reInitializeAcceptJobUI(AppSettings.getDriverStatus(getParentContext()));
//                        }
                    }

                } else {
                    Toast.makeText(getParentContext(), "Could not retrieve user location.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    ResultCallback<LocationSettingsResult> locationSettingsResultResultCallback = new ResultCallback<LocationSettingsResult>() {
        @Override
        public void onResult(LocationSettingsResult locationSettingsResult) {
            final Status status = locationSettingsResult.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i(TAG, "All location settings are satisfied.");
                    startLocationUpdates();
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                    try {
                        // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                        status.startResolutionForResult(DashboardDriverNavigationDrawerActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException e) {
                        Log.i(TAG, "PendingIntent unable to execute request.");
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && isRequestingLocationUpdates()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
//            stopLocationUpdates();
            // forced for getting location update while pause to resume
            setRequestingLocationUpdates(true);
        }
    }

    public void onBackPressed() {
        moveTaskToBack(true);  // "Hide" your current Activity
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
////        mGoogleApiClient.disconnect();
//    }

//    @Override
//    public void onDestroy() {
//        mGoogleApiClient.disconnect();
//        super.onDestroy();
//    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, isRequestingLocationUpdates());
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        savedInstanceState.putString(KEY_LAST_UPDATED_TIME_STRING, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                setRequestingLocationUpdates(savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES));
            }
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
//            updateUI();
            updateLocationUI();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(onConnectionFailedListener)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    public void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(locationSettingsResultResultCallback);
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        setRequestingLocationUpdates(true);
//                        mRequestingLocationUpdates = true;
//                        setButtonsEnabledState();

                    }
                });

    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        setRequestingLocationUpdates(false);
//                        mRequestingLocationUpdates = false;
//                        setButtonsEnabledState();
                    }
                });
    }

    public void updateLocationUI() {
        if (mCurrentLocation != null) {
            setCurrentLatitude(mCurrentLocation.getLatitude());
            setCurrentLongitude(mCurrentLocation.getLongitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationDrawerHeaderView(R.layout.nav_header_main);
        setNavigationDrawerMenuView(R.menu.menu_driver_drawer);
        initDriverDashboardAction();
        setContentView(R.layout.activity_dashboard_driver_navigation_drawer);
        setActivityTitle(AllConstants.TITLE_ACTIVITY_DRIVER_DASHBOARD);
        initDriverDashboardUI();
        startDriverServices();
        //set call a taxi at first launch of the activity
        if (getIntent().getExtras() != null) {
            onNewIntent(getIntent());
        } else {
            if (savedInstanceState == null) {
                changeFragment(new JobStatusFragment(), AllConstants.FRAGMENT_NAME_JOB_STATUS);
                setActivityTitle(AllConstants.TITLE_ACTIVITY_JOB_STATUS);
                MenuItem menuItem = getNavigationView().getMenu().findItem(R.id.nav_job_status);
                setMenuItemId(menuItem.getItemId());
                setMenuItem(menuItem);
                menuItem.setChecked(true);
            }
        }

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);
        //for getting current location for the first time
        // and here no needs to gps or location setting or internet connection.
        buildGoogleApiClient();
        //for getting continous update of location
        // and here needs to enable location service from setting,
        // but no needs internet connection.
        createLocationRequest();
        buildLocationSettingsRequest();
//        checkLocationSettings();
    }

    public double getCurrentLongitude() {
        return AllConstants.CURRENT_LONGITUDE;
    }

    public double getCurrentLatitude() {
        return AllConstants.CURRENT_LATITUDE;
    }

    public String getCurrentAddress() {
        return AllConstants.CURRENT_ADDRESS;
    }

    public void setCurrentAddress(String currentAddress) {
        AllConstants.CURRENT_ADDRESS = currentAddress;
    }

    public void setCurrentLatitude(double currentLatitude) {
        AllConstants.CURRENT_LATITUDE = currentLatitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        AllConstants.CURRENT_LONGITUDE = currentLongitude;
    }

    public void setRequestingLocationUpdates(boolean isRequestingLocationUpdates) {
        mRequestingLocationUpdates = isRequestingLocationUpdates;
    }

    public boolean isRequestingLocationUpdates() {
        return mRequestingLocationUpdates;
    }

    public boolean isGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return mGoogleApiClient.isConnected();
        }
        return false;
    }

    public JobStatusFragment getAcceptJobFragmentObject() {
        FragmentManager fm = getSupportFragmentManager();
        JobStatusFragment fragment = (JobStatusFragment) fm.findFragmentByTag(AllConstants.FRAGMENT_NAME_JOB_STATUS);
        return fragment;
    }

    public void initDriverDashboardUI() {
        userDetail = AppSettings.getUserInfo(getParentContext());
        profileName = (TextView) getNavigationHeaderView().findViewById(R.id.tv_profile_name);
        profileName.setText(userDetail.getName());
        profileEmail = (TextView) getNavigationHeaderView().findViewById(R.id.tv_profile_email);
        profileEmail.setText(userDetail.getEmail());
        profileImage = (ImageView) getNavigationHeaderView().findViewById(R.id.iv_profile_image);
        driverServiceIntent = new Intent(getParentContext(), DriverService.class);
        //location settng variable
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
    }

    public void initDriverDashboardAction() {
        setNavigationDrawerMenuListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                navigate(item.getItemId());
                closeDrawer();
                return true;
            }
        });
    }

    public void changeFragment(Fragment targetFragment, String fragmentName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, fragmentName)
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void navigate(final int itemId) {
        drawerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setMenuItemId(itemId);
                Fragment fragment;
                if (itemId == R.id.nav_job_status) {
                    fragment = (JobStatusFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_JOB_STATUS);
                    if (fragment instanceof JobStatusFragment) {
                    } else {
                        changeFragment(new JobStatusFragment(), AllConstants.FRAGMENT_NAME_JOB_STATUS);
                        setActivityTitle(AllConstants.TITLE_ACTIVITY_JOB_STATUS);
                    }
                } else if (itemId == R.id.nav_my_jobs) {
                    fragment = (MyJobsFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_MY_JOBS);
                    if (fragment instanceof MyJobsFragment) {

                    } else {
                        changeFragment(new MyJobsFragment(), AllConstants.FRAGMENT_NAME_MY_JOBS);
                        setActivityTitle(AllConstants.TITLE_ACTIVITY_MY_JOBS);
                    }
                } else if (itemId == R.id.nav_bid_jobs) {
                    fragment = (BidJobsFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_BID_JOBS);
                    if (fragment instanceof BidJobsFragment) {

                    } else {
                        changeFragment(new BidJobsFragment(), AllConstants.FRAGMENT_NAME_BID_JOBS);
                        setActivityTitle(AllConstants.TITLE_ACTIVITY_BID_JOBS);
                    }
                } else if (itemId == R.id.nav_new_jobs) {
                    fragment = (NewJobsFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_NEW_JOBS);
                    if (fragment instanceof NewJobsFragment) {

                    } else {
                        changeFragment(new NewJobsFragment(), AllConstants.FRAGMENT_NAME_NEW_JOBS);
                        setActivityTitle(AllConstants.TITLE_ACTIVITY_NEW_JOBS);
                    }
                } else if (itemId == R.id.nav_inbox) {
                    fragment = (InboxFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_INBOX);
                    if (fragment instanceof InboxFragment) {

                    } else {
                        changeFragment(new InboxFragment(), AllConstants.FRAGMENT_NAME_INBOX);
                        setActivityTitle(AllConstants.TITLE_ACTIVITY_INBOX);
                    }
                } else if (itemId == R.id.nav_outbox) {
                    fragment = (OutboxFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_OUTBOX);
                    if (fragment instanceof OutboxFragment) {

                    } else {
                        changeFragment(new OutboxFragment(), AllConstants.FRAGMENT_NAME_OUTBOX);
                        setActivityTitle(AllConstants.TITLE_ACTIVITY_OUTBOX);
                    }
                } else if (itemId == R.id.nav_working_status) {
                    fragment = (WorkingStatusFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_WORKING_STATUS);
                    if (fragment instanceof OutboxFragment) {

                    } else {
                        changeFragment(new WorkingStatusFragment(), AllConstants.FRAGMENT_NAME_WORKING_STATUS);
                        setActivityTitle(AllConstants.TITLE_ACTIVITY_WORKING_STATUS);
                    }
                } else if (itemId == R.id.nav_logout) {
                    if (AppSettings.getDriverStatus(getParentContext()) == AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS
                            || AppSettings.getDriverStatus(getParentContext()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
                        AppSettings.removeUserDetail(getParentContext());
                        if (isGoogleApiClient()) {
                            stopLocationUpdates();
                            mGoogleApiClient.disconnect();
                        }
                        stopDriverServices();
                        Intent intent = new Intent(getParentContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getParentContext(), "You can not logout before completing the current job.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, 250);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        boolean reloadInboxFromNotification = extras.getBoolean(AllConstants.INTENT_KEY_NOTIFICATION_TO_INBOX, false);
        boolean reloadNormalJobFromNotification = extras.getBoolean(AllConstants.INTENT_KEY_NOTIFICATION_TO_NORMAL_JOB, false);
        boolean reloadBidJobFromNotification = extras.getBoolean(AllConstants.INTENT_KEY_NOTIFICATION_TO_BID_JOB, false);
        if (reloadInboxFromNotification) {
            changeFragment(new InboxFragment(), AllConstants.FRAGMENT_NAME_INBOX);
            setActivityTitle(AllConstants.TITLE_ACTIVITY_INBOX);
            MenuItem menuItem = getNavigationView().getMenu().findItem(R.id.nav_inbox);
            setMenuItemId(menuItem.getItemId());
            setMenuItem(menuItem);
            menuItem.setChecked(true);
        } else if (reloadNormalJobFromNotification) {
            changeFragment(new NewJobsFragment(), AllConstants.FRAGMENT_NAME_NEW_JOBS);
            setActivityTitle(AllConstants.TITLE_ACTIVITY_NEW_JOBS);
            MenuItem menuItem = getNavigationView().getMenu().findItem(R.id.nav_new_jobs);
            setMenuItemId(menuItem.getItemId());
            setMenuItem(menuItem);
            menuItem.setChecked(true);
        } else if (reloadBidJobFromNotification) {
            changeFragment(new BidJobsFragment(), AllConstants.FRAGMENT_NAME_BID_JOBS);
            setActivityTitle(AllConstants.TITLE_ACTIVITY_BID_JOBS);
            MenuItem menuItem = getNavigationView().getMenu().findItem(R.id.nav_bid_jobs);
            setMenuItemId(menuItem.getItemId());
            setMenuItem(menuItem);
            menuItem.setChecked(true);
        }
    }

//    public void startDriverServices() {
//        if (SessionManager.getBooleanSetting(getParentContext(), AllConstants.SESSION_IS_DRIVER_SERVICE, false)) {
//            stopDriverServices();
//        }
//        bindService(driverServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
//        startService(driverServiceIntent);
////            isDriverServiceStarted = true;
//        SessionManager.setBooleanSetting(getParentContext(), AllConstants.SESSION_IS_DRIVER_SERVICE, true);
//
//    }
//
//    public void stopDriverServices() {
//        if (SessionManager.getBooleanSetting(getParentContext(), AllConstants.SESSION_IS_DRIVER_SERVICE, false)) {
//            stopService(driverServiceIntent);
//            unbindService(mServiceConnection);
////            isDriverServiceStarted = false;
//            SessionManager.setBooleanSetting(getParentContext(), AllConstants.SESSION_IS_DRIVER_SERVICE, false);
//        }
//        //clear notification from notification bar
//        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_NEW_MESSAGE);
//        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_NEW_NORMAL_JOB);
//        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_NEW_BID_JOB);
//    }

    public void startDriverServices() {
//        if (!SessionManager.getBooleanSetting(getParentContext(), AllConstants.SESSION_IS_DRIVER_SERVICE, false)) {
            startService(driverServiceIntent);
            bindService(driverServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            isDriverServiceStarted = true;
            SessionManager.setBooleanSetting(getParentContext(), AllConstants.SESSION_IS_DRIVER_SERVICE, true);
//        }
    }

    public void stopDriverServices() {
//        if (isDriverServiceStarted
//                && SessionManager.getBooleanSetting(getParentContext(), AllConstants.SESSION_IS_DRIVER_SERVICE, false)) {
            unbindService(mServiceConnection);
            stopService(driverServiceIntent);
            isDriverServiceStarted = false;
            SessionManager.setBooleanSetting(getParentContext(), AllConstants.SESSION_IS_DRIVER_SERVICE, false);
//        }
        //clear notification from notification bar
        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_NEW_MESSAGE);
        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_NEW_NORMAL_JOB);
        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_NEW_BID_JOB);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DriverService.DriverBinder mBinder = (DriverService.DriverBinder) service;
            mDriverService = mBinder.getServiceInstance();
            mDriverService.createConnection(mServiceCommunication);
            isDriverServiceStarted = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDriverService = null;
            isDriverServiceStarted = false;
        }
    };

    private ServiceCommunication mServiceCommunication = new ServiceCommunication() {
        @Override
        public void getData(Object data, AllConstants.SERVICE_RETURN_TYPE serviceReturnType) {
            Log.d("DriverService", "call back from service class");

            if (serviceReturnType == AllConstants.SERVICE_RETURN_TYPE.DRIVER_NEW_JOB_ALERT) {
                NewJobAlert newJobAlert = (NewJobAlert) data;
                if (AppSettings.getDriverStatus(getParentContext()) == AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS
                        || AppSettings.getDriverStatus(getParentContext()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
                    ArrayList<NewJobAlertDetail> allJobs = new ArrayList<NewJobAlertDetail>();
                    if (newJobAlert != null) {
                        if (newJobAlert.getNormalJobsDetails() != null && newJobAlert.getNormalJobsDetails().size() > 0) {
                            allJobs.addAll(newJobAlert.getNormalJobsDetails());
                        }
                        if (newJobAlert.getBidJobsDetails() != null && newJobAlert.getBidJobsDetails().size() > 0) {
                            allJobs.addAll(newJobAlert.getBidJobsDetails());
                        }
                    }
                    if (getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_JOB_STATUS) != null && allJobs.size() > 0) {

//                        getAcceptJobFragmentObject().setStatusText(acceptJobStatus.getSuccess());
//                        AppSettings.setJobAcceptedDriverID(getParentContext(), acceptJobStatus.getJobDetails().getDriverId());
//                        AppSettings.setJobAcceptedDriverPhone(getParentContext(), acceptJobStatus.getJobDetails().getDriverDetails().getPhone());

                        AppSettings.setDriverStatus(getApplicationContext(), AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS);
                        AppSettings.setJobAcceptedDriverReceiverType(getParentContext(),newJobAlert.getReceiver_type());
                        AppSettings.setJobAcceptedDriverSenderType(getParentContext(),newJobAlert.getSender_type());

                        Log.d("AcceptJob", "Total job before send to map " + allJobs.size());
                        getAcceptJobFragmentObject().setAllNewJobs(allJobs);
                        getAcceptJobFragmentObject().reInitializeAcceptJobUI(AppSettings.getDriverStatus(getParentContext()));

                    } else {
                        //update current location as work with no job
                    }
                }
            }
        }
    };

    public void initializeHelpButton() {
        setHelpButton();
        getHelpButton().setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Toast.makeText(getParentContext(), "Asked for help", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
