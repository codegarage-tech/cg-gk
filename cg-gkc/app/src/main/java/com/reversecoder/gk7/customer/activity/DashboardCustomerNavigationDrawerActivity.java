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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.application.BaseNavigationDrawerActivity;
import com.customviews.library.storehouse.SessionManager;
import com.customviews.library.utils.AllSettingsManager;
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
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.fragment.CallATaxiFragment;
import com.reversecoder.gk7.customer.fragment.InboxFragment;
import com.reversecoder.gk7.customer.fragment.OutboxFragment;
import com.reversecoder.gk7.customer.interfaces.ServiceCommunication;
import com.reversecoder.gk7.customer.model.DriverUpdatedLocation;
import com.reversecoder.gk7.customer.model.User;
import com.reversecoder.gk7.customer.model.WrapperAcceptJobDetailsAlert;
import com.reversecoder.gk7.customer.service.CustomerService;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AppSettings;

import java.text.DateFormat;
import java.util.Date;


public class DashboardCustomerNavigationDrawerActivity extends BaseNavigationDrawerActivity {

    Handler drawerHandler = new Handler();
    TextView profileName, profileEmail;
    ImageView profileImage;
    User userDetail;
    Intent customerServiceIntent;
    //Accessing customer service
    private CustomerService mCustomerService;
    private boolean isCustomerServiceStarted = false;
    //Location setting variable
    protected static final String TAG = "DashboardCustomerNavigationDrawerActivity";
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 30 * 1000;//30 second
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS;
    //public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "KEY_REQUESTING_LOCATION_UPDATES";
    protected final static String KEY_LOCATION = "KEY_LOCATION";
    protected final static String KEY_LAST_UPDATED_TIME_STRING = "KEY_LAST_UPDATED_TIME_STRING";
    public GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    // Location settings Labels.
    protected Boolean mRequestingLocationUpdates;
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
//                Toast.makeText(getParentContext(), "Primary Lat: " + mCurrentLocation.getLatitude() + "\nPrimary Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(getParentContext(), "Updated Lat: " + mCurrentLocation.getLatitude() + "\nUpdated Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
            if (mCurrentLocation != null) {
                if (getCurrentLatitude() != 0.0 && getCurrentLongitude() != 0.0) {
//                    if (NetworkManager.isConnected(getParentContext())) {
//                        if (NetworkManager.isConnected(getParentContext())) {
//                            new ReverseGeocodingTask(getParentContext(), new ReverseGeocodingTask.AddressCallback() {
//                                @Override
//                                public void onAddressUpdate(String address) {
//                                    setCurrentAddress(address);
//                                }
//                            }).execute(new LatLng(getCurrentLatitude(), getCurrentLongitude()));
//                        }
//                    }

                    //Initialize map pin location on fragment.
                    if (getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_CALL_A_TAXI) != null) {
//                        if (AppSettings.getCustomerStatus(getParentContext()) != AllConstants.CUSTOMER_STATUS.REQUEST_ACCEPTED) {
                        getCallATaxiFragmentObject().reInitializeCallATaxiUI(AppSettings.getCustomerStatus(getParentContext()));
//                        }
//                        else if (AppSettings.getCustomerStatus(getParentContext()) == AllConstants.CUSTOMER_STATUS.REQUESTED_FOR_A_TAXI) {
//                            getCallATaxiFragmentObject().reInitializeCallATaxiUI(AppSettings.getCustomerStatus(getParentContext()));
//                        } else if (AppSettings.getCustomerStatus(getParentContext()) == AllConstants.CUSTOMER_STATUS.REQUEST_ACCEPTED) {
//                        } else if (AppSettings.getCustomerStatus(getParentContext()) == AllConstants.CUSTOMER_STATUS.RIDE_STARTED) {
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
                        status.startResolutionForResult(DashboardCustomerNavigationDrawerActivity.this, REQUEST_CHECK_SETTINGS);
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
            stopLocationUpdates();
            // forced for getting location update while pause to resume
            setRequestingLocationUpdates(true);
        }
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
////        mGoogleApiClient.disconnect();
//    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);  // "Hide" your current Activity
    }

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
//                mRequestingLocationUpdates = savedInstanceState.getBoolean(
//                        KEY_REQUESTING_LOCATION_UPDATES);
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

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
//                        mRequestingLocationUpdates = true;
                        setRequestingLocationUpdates(true);
//                        setButtonsEnabledState();

                    }
                });

    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, locationListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
//                        mRequestingLocationUpdates = false;
                        setRequestingLocationUpdates(false);
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

//    private void updateUI() {
//        setButtonsEnabledState();
//        updateLocationUI();
//    }
//
//    private void setButtonsEnabledState() {
//        if (mRequestingLocationUpdates) {
//            mStartUpdatesButton.setEnabled(false);
//            mStopUpdatesButton.setEnabled(true);
//        } else {
//            mStartUpdatesButton.setEnabled(true);
//            mStopUpdatesButton.setEnabled(false);
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationDrawerHeaderView(R.layout.nav_header_main);
        setNavigationDrawerMenuView(R.menu.menu_customer_drawer);
        initCustomerDashboardAction();
        setContentView(R.layout.activity_dashboard_customer_navigation_drawer);
        setActivityTitle(AllConstants.TITLE_ACTIVITY_CUSTOMER_DASHBOARD);
        initCustomerDashboardUI();
        startCustomerServices();
        //set call a taxi at first launch of the activity
        if (getIntent().getExtras() != null) {
            onNewIntent(getIntent());
        } else {
            if (savedInstanceState == null) {
                changeFragment(new CallATaxiFragment(), AllConstants.FRAGMENT_NAME_CALL_A_TAXI);
                setActivityTitle(AllConstants.TITLE_ACTIVITY_CALL_A_TAXI);
                MenuItem menuItem = getNavigationView().getMenu().findItem(R.id.nav_call_a_taxi);
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

    public CallATaxiFragment getCallATaxiFragmentObject() {
        FragmentManager fm = getSupportFragmentManager();
        CallATaxiFragment fragment = (CallATaxiFragment) fm.findFragmentByTag(AllConstants.FRAGMENT_NAME_CALL_A_TAXI);
        return fragment;
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

    public void initCustomerDashboardUI() {
        userDetail = AppSettings.getUserInfo(getParentContext());
        profileName = (TextView) getNavigationHeaderView().findViewById(R.id.tv_profile_name);
        profileName.setText(userDetail.getName());
        profileEmail = (TextView) getNavigationHeaderView().findViewById(R.id.tv_profile_email);
        profileEmail.setText(userDetail.getEmail());
        profileImage = (ImageView) getNavigationHeaderView().findViewById(R.id.iv_profile_image);
        customerServiceIntent = new Intent(getParentContext(), CustomerService.class);
        //location settng variable
//        mRequestingLocationUpdates = false;
        setRequestingLocationUpdates(false);
        mLastUpdateTime = "";
    }

    public void initCustomerDashboardAction() {
        setNavigationDrawerMenuListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                setMenuItem(item);
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
                if (itemId == R.id.nav_call_a_taxi) {
                    fragment = (CallATaxiFragment) getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_CALL_A_TAXI);
                    if (fragment instanceof CallATaxiFragment) {

                    } else {
                        changeFragment(new CallATaxiFragment(), AllConstants.FRAGMENT_NAME_CALL_A_TAXI);
                        setActivityTitle(AllConstants.TITLE_ACTIVITY_CALL_A_TAXI);
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
                } else if (itemId == R.id.nav_logout) {
                    if (AppSettings.getCustomerStatus(getParentContext()) == AllConstants.CUSTOMER_STATUS.IDLE) {
                        AppSettings.removeUserDetail(getParentContext());
                        if (isGoogleApiClient()) {
                            stopLocationUpdates();
                            mGoogleApiClient.disconnect();
                        }
                        stopCustomerServices();
                        Intent intent = new Intent(getParentContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getParentContext(), "You can not logout before reaching your destination.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, 250);
    }

    //service related tasks
    @Override
    public void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        boolean reloadInboxFromNotification = extras.getBoolean(AllConstants.INTENT_KEY_NOTIFICATION_TO_INBOX, false);
        if (reloadInboxFromNotification) {
            changeFragment(new InboxFragment(), AllConstants.FRAGMENT_NAME_INBOX);
            setActivityTitle(AllConstants.TITLE_ACTIVITY_INBOX);
            MenuItem menuItem = getNavigationView().getMenu().findItem(R.id.nav_inbox);
            setMenuItemId(menuItem.getItemId());
            setMenuItem(menuItem);
            menuItem.setChecked(true);
        } else {

        }
    }

    private void startCustomerServices() {
//        if (!SessionManager.getBooleanSetting(getParentContext(), AllConstants.SESSION_IS_CUSTOMER_SERVICE, false)) {
            startService(customerServiceIntent);
            bindService(customerServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            isCustomerServiceStarted = true;
            SessionManager.setBooleanSetting(getParentContext(), AllConstants.SESSION_IS_CUSTOMER_SERVICE, true);
//        }
    }

    private void stopCustomerServices() {
//        if (isCustomerServiceStarted
//                && SessionManager.getBooleanSetting(getParentContext(), AllConstants.SESSION_IS_CUSTOMER_SERVICE, false)) {
            unbindService(mServiceConnection);
            stopService(customerServiceIntent);
            isCustomerServiceStarted = false;
            SessionManager.setBooleanSetting(getParentContext(), AllConstants.SESSION_IS_CUSTOMER_SERVICE, false);
//        }
        //clear notification from notification bar
        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_NEW_MESSAGE);
        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_ACCEPT_JOB);
    }

//    private void startCustomerServices() {
//        if (SessionManager.getBooleanSetting(getParentContext(), AllConstants.SESSION_IS_CUSTOMER_SERVICE, false)) {
//            stopCustomerServices();
//        }
//        bindService(customerServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
//        startService(customerServiceIntent);
////            isCustomerServiceStarted = true;
//
//        SessionManager.setBooleanSetting(getParentContext(), AllConstants.SESSION_IS_CUSTOMER_SERVICE, true);
//    }
//
//    private void stopCustomerServices() {
//        if (SessionManager.getBooleanSetting(getParentContext(), AllConstants.SESSION_IS_CUSTOMER_SERVICE, false)) {
//            stopService(customerServiceIntent);
//            unbindService(mServiceConnection);
////            isCustomerServiceStarted = false;
//            SessionManager.setBooleanSetting(getParentContext(), AllConstants.SESSION_IS_CUSTOMER_SERVICE, false);
//        }
//        //clear notification from notification bar
//        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_NEW_MESSAGE);
//        NotificationUtilManager.clearNotificationFromNotificationBar(getApplicationContext(), AllConstants.NOTIFICATION_ID_ACCEPT_JOB);
//    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            CustomerService.CustomerBinder mBinder = (CustomerService.CustomerBinder) service;
            mCustomerService = mBinder.getServiceInstance();
            mCustomerService.createConnection(mServiceCommunication);
            isCustomerServiceStarted = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mCustomerService = null;
            isCustomerServiceStarted = false;
        }
    };

    private ServiceCommunication mServiceCommunication = new ServiceCommunication() {
        @Override
        public void getData(Object data, AllConstants.SERVICE_RETURN_TYPE returnType) {
            Log.d("DriverService", "call back from service class");
            if (returnType == AllConstants.SERVICE_RETURN_TYPE.CUSTOMER_ACCEPTED_JOB_ALERT) {
                WrapperAcceptJobDetailsAlert acceptJobStatus = (WrapperAcceptJobDetailsAlert) data;
                if (acceptJobStatus != null && !AllSettingsManager.isNullOrEmpty(acceptJobStatus.getSuccess())) {
                    if (AppSettings.getCustomerStatus(getParentContext()) == AllConstants.CUSTOMER_STATUS.REQUESTED_FOR_A_TAXI) {
                        if (acceptJobStatus.getSuccess().contains("accept")
                                && acceptJobStatus.getJobDetails().getIsStarted().equalsIgnoreCase("0")
                                && acceptJobStatus.getJobDetails().getJobStatus().equalsIgnoreCase("2")) {
                            AppSettings.setCustomerStatusText(getParentContext(), acceptJobStatus.getSuccess());
                            AppSettings.setCustomerStatus(getParentContext(), AllConstants.CUSTOMER_STATUS.REQUEST_ACCEPTED);
                            AppSettings.setJobAcceptedDriverID(getParentContext(), acceptJobStatus.getJobDetails().getDriverId());
                            AppSettings.setJobAcceptedDriverPhone(getParentContext(), acceptJobStatus.getJobDetails().getDriverDetails().getPhone());
                            AppSettings.setJobAcceptedCustomerReceiverType(getParentContext(),acceptJobStatus.getJobDetails().getReceiver_type());
                            AppSettings.setJobAcceptedCustomerSenderType(getParentContext(),acceptJobStatus.getJobDetails().getSender_type());

                            if (getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_CALL_A_TAXI) != null) {
//                                getCallATaxiFragmentObject().setStatusText(acceptJobStatus.getSuccess());
                                getCallATaxiFragmentObject().reInitializeCallATaxiUI(AppSettings.getCustomerStatus(getParentContext()));
                            }
                        }
                    } else if (AppSettings.getCustomerStatus(getParentContext()) == AllConstants.CUSTOMER_STATUS.REQUEST_ACCEPTED) {
                        if (acceptJobStatus.getSuccess().contains("accept")
                                && acceptJobStatus.getJobDetails().getIsStarted().equalsIgnoreCase("1")
                                && acceptJobStatus.getJobDetails().getJobStatus().equalsIgnoreCase("2")) {
                            AppSettings.setCustomerStatusText(getParentContext(), "Moving to the destination");
                            AppSettings.setCustomerStatus(getParentContext(), AllConstants.CUSTOMER_STATUS.RIDE_STARTED);
//                            AppSettings.setJobAcceptedDriverID(getParentContext(), acceptJobStatus.getJobDetails().getDriverId());
//                            AppSettings.setJobAcceptedDriverPhone(getParentContext(), acceptJobStatus.getJobDetails().getDriverDetails().getPhone());
                            if (getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_CALL_A_TAXI) != null) {
//                                getCallATaxiFragmentObject().setStatusText("Moving to the destination");
                                getCallATaxiFragmentObject().reInitializeCallATaxiUI(AppSettings.getCustomerStatus(getParentContext()));
                            }
                        }
                    } else if (AppSettings.getCustomerStatus(getParentContext()) == AllConstants.CUSTOMER_STATUS.RIDE_STARTED) {
                        if (acceptJobStatus.getSuccess().contains("complete") && acceptJobStatus.getJobDetails().getJobStatus().equalsIgnoreCase("3")) {
                            AppSettings.setCustomerStatusText(getParentContext(), acceptJobStatus.getSuccess());
                            AppSettings.setCustomerStatus(getParentContext(), AllConstants.CUSTOMER_STATUS.RIDE_COMPLETED);
                            if (getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_CALL_A_TAXI) != null) {
//                                getCallATaxiFragmentObject().setStatusText(acceptJobStatus.getSuccess());
                                getCallATaxiFragmentObject().reInitializeCallATaxiUI(AppSettings.getCustomerStatus(getParentContext()));
                            }
                        }
                    } else if (AppSettings.getCustomerStatus(getParentContext()) == AllConstants.CUSTOMER_STATUS.RIDE_COMPLETED) {
                        if (acceptJobStatus.getSuccess().contains("complete") && acceptJobStatus.getJobDetails().getJobStatus().equalsIgnoreCase("3")) {
                            AppSettings.setCustomerStatusText(getParentContext(), "Paid distance: " + acceptJobStatus.getJobDetails().getPaid_distance()
                                    + ", Duration: " + acceptJobStatus.getJobDetails().getDuration() + ", Cost: " + getResources().getString(R.string.symbol_pound) + acceptJobStatus.getJobDetails().getCost());
//                            AppSettings.setCustomerStatus(getParentContext(), AllConstants.CUSTOMER_STATUS.IDLE);
                            if (getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_CALL_A_TAXI) != null) {
//                                getCallATaxiFragmentObject().setStatusText("Paid distance: " + acceptJobStatus.getJobDetails().getPaid_distance()
//                                        + ", Duration: " + acceptJobStatus.getJobDetails().getDuration() + ", Cost: " + getResources().getString(R.string.symbol_pound) + acceptJobStatus.getJobDetails().getCost());
                                getCallATaxiFragmentObject().reInitializeCallATaxiUI(AppSettings.getCustomerStatus(getParentContext()));
                            }
                        }
                    }
                }
            } else if (returnType == AllConstants.SERVICE_RETURN_TYPE.CUSTOMER_GET_DRIVER_CURRENT_LOCATION) {
                DriverUpdatedLocation driverUpdatedLocations = (DriverUpdatedLocation) data;
                if (driverUpdatedLocations != null) {
                    if (AppSettings.getCustomerStatus(getParentContext()) == AllConstants.CUSTOMER_STATUS.REQUEST_ACCEPTED) {
                        if (getCurrentVisibleFragmentObject(AllConstants.FRAGMENT_NAME_CALL_A_TAXI) != null) {
                            Log.d("sent driver position", "lat: " + driverUpdatedLocations.getLat() + " lon: " + driverUpdatedLocations.getLon());
                            getCallATaxiFragmentObject().setDriverUpdatedLocation(driverUpdatedLocations);
                            getCallATaxiFragmentObject().reInitializeCallATaxiUI(AppSettings.getCustomerStatus(getParentContext()));
                        }
                    }
                }
            }
        }
    };

}
