package com.reversecoder.gk7.driver.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.customviews.library.custommapview.MapWrapperLayout;
import com.customviews.library.custommapview.OnInfoWindowElemTouchListener;
import com.customviews.library.custommapview.ReverseGeocodingTask;
import com.customviews.library.custommapview.mapnavigator.Navigator;
import com.customviews.library.dialog.DialogMessage;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.utils.AllSettingsManager;
import com.customviews.library.utils.NetworkManager;
import com.customviews.library.utils.NotificationUtilManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reversecoder.gk7.driver.R;
import com.reversecoder.gk7.driver.activity.DashboardCustomerNavigationDrawerActivity;
import com.reversecoder.gk7.driver.activity.VerticalStepperCallATaxiActivity;
import com.reversecoder.gk7.driver.dialog.DialogSendMessage;
import com.reversecoder.gk7.driver.model.AcceptJobDetails;
import com.reversecoder.gk7.driver.model.DriverUpdatedLocation;
import com.reversecoder.gk7.driver.model.MapMarkerWithDetails;
import com.reversecoder.gk7.driver.utility.AllConstants;
import com.reversecoder.gk7.driver.utility.AppSettings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CallATaxiFragment extends Fragment {

    View parentView;
    SupportMapFragment fragmentMap;
    GoogleMap googleMap;
    TextView txtMapInfoWindowAddress;
    Button btnMapInfoWindowCallATaxi;
    //    Marker mapMarker;
    private OnInfoWindowElemTouchListener infoButtonListener;
    MapWrapperLayout mapWrapperLayout;
    private ViewGroup viewInfoWindow;
    private Map<Marker, AcceptJobDetails> allMarkersDetails = new HashMap<Marker, AcceptJobDetails>();
    //screen ui elements
    TextView txtStatusDriver;
    //    boolean isLoadingIconCancled = false;
    RelativeLayout rlShadowLayout;
    LinearLayout llHiddenMessage, llHiddenPhone, llHiddenConnectLayout, llHiddenRideCompleted;
    Button btnMessageDriver, btnPhoneDriver, btnRideCompleted;
    //    String statusText = "";
    DriverUpdatedLocation driverCurrentLocation = new DriverUpdatedLocation();
    DialogMessage dialogMessage;
//    TextLoadingIcon.TextLoadingIconBuilder textLoadingIconBuilder=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_call_a_taxi, container, false);
        Log.d("CallATaxiFragment", "onCreateView called");
        initCallATaxiUI();
        initCallATaxiAction();
        return parentView;
    }

    public DashboardCustomerNavigationDrawerActivity getDashboardCustomerNavigationDrawerActivityObject() {
        return ((DashboardCustomerNavigationDrawerActivity) getActivity());
    }

    private void initCallATaxiUI() {
        createSupportMapFragmentOnFragment();
        ((DashboardCustomerNavigationDrawerActivity) getActivity()).checkLocationSettings();
        rlShadowLayout = (RelativeLayout) parentView.findViewById(R.id.rl_shadow_layout);
        llHiddenPhone = (LinearLayout) parentView.findViewById(R.id.ll_hidden_phone);
        llHiddenMessage = (LinearLayout) parentView.findViewById(R.id.ll_hidden_message);
        llHiddenConnectLayout = (LinearLayout) parentView.findViewById(R.id.ll_hidden_connect_layout);
        llHiddenRideCompleted = (LinearLayout) parentView.findViewById(R.id.ll_hidden_ride_completed);
        txtStatusDriver = (TextView) parentView.findViewById(R.id.txt_status_driver);
        btnMessageDriver = (Button) parentView.findViewById(R.id.btn_message_driver);
        btnPhoneDriver = (Button) parentView.findViewById(R.id.btn_phone_driver);
        btnRideCompleted = (Button) parentView.findViewById(R.id.btn_ride_completed);
    }

    public void reInitializeCallATaxiUI(AllConstants.CUSTOMER_STATUS customerStatus) {
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
//        Toast.makeText(getActivity(), "Reinitialized map from: " + AppSettings.getCustomerStatus(getActivity()).toString(), Toast.LENGTH_SHORT).show();
        if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.IDLE) {
            rlShadowLayout.setVisibility(View.GONE);
            //remove notification from status bar
            NotificationUtilManager.clearNotificationFromNotificationBar(getActivity(), AllConstants.NOTIFICATION_ID_NEW_MESSAGE);
            //initialize map
            ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
            MapMarkerWithDetails markerWithDetails = new MapMarkerWithDetails(new LatLng(getDashboardCustomerNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardCustomerNavigationDrawerActivityObject().getCurrentLongitude()), new AcceptJobDetails());
            markersDetails.add(markerWithDetails);
            initializeMap(markersDetails);
        } else if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.REQUESTED_FOR_A_TAXI) {
            txtStatusDriver.setText(AppSettings.getCustomerStatusText(getActivity()));
            llHiddenConnectLayout.setVisibility(View.GONE);
            llHiddenPhone.setVisibility(View.VISIBLE);
            llHiddenMessage.setVisibility(View.VISIBLE);
            llHiddenRideCompleted.setVisibility(View.GONE);
            rlShadowLayout.setVisibility(View.VISIBLE);
            //initialize map
            ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
            MapMarkerWithDetails markerWithDetails = new MapMarkerWithDetails(new LatLng(getDashboardCustomerNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardCustomerNavigationDrawerActivityObject().getCurrentLongitude()), new AcceptJobDetails());
            markersDetails.add(markerWithDetails);
            initializeMap(markersDetails);
        } else if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.REQUEST_ACCEPTED) {
            txtStatusDriver.setText(AppSettings.getCustomerStatusText(getActivity()));
            llHiddenConnectLayout.setVisibility(View.VISIBLE);
            llHiddenPhone.setVisibility(View.VISIBLE);
            llHiddenMessage.setVisibility(View.VISIBLE);
            llHiddenRideCompleted.setVisibility(View.GONE);
            rlShadowLayout.setVisibility(View.VISIBLE);
            ArrayList<MapMarkerWithDetails> data = prepareMarkersDetails(getDriverUpdatedLocation());
            Log.d("reinitialize:", "final data is " + data.size());
            if (data.size() == 2) {
                Log.d("Current distance: ", AllSettingsManager.calculateDistance(data.get(0).getLatLng(), data.get(1).getLatLng()) + "");
                if (AllSettingsManager.calculateDistance(data.get(0).getLatLng(), data.get(1).getLatLng()) <= 300) {
                    if (dialogMessage != null) {
                    } else {
                        dialogMessage = new DialogMessage(getActivity(), "Taxi arrived!",
                                "Please find taxi around you. To find taxi quickly please call driver or message your exact location.", false);
                    }
                    if (!dialogMessage.isShowing()) {
                        dialogMessage.show();
                    }
                }
                Log.d("reinitialize:", "final data sent to map " + data.size());
                initializeMap(data);
            }
        } else if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.RIDE_STARTED) {
            txtStatusDriver.setText(AppSettings.getCustomerStatusText(getActivity()));
            llHiddenConnectLayout.setVisibility(View.VISIBLE);
            llHiddenPhone.setVisibility(View.VISIBLE);
            llHiddenMessage.setVisibility(View.VISIBLE);
            llHiddenRideCompleted.setVisibility(View.GONE);
            rlShadowLayout.setVisibility(View.VISIBLE);
            if (dialogMessage != null && dialogMessage.isShowing()) {
                dialogMessage.dismiss();
            }
//            ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
//            MapMarkerWithDetails markerWithDetails = new MapMarkerWithDetails(new LatLng(getDashboardCustomerNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardCustomerNavigationDrawerActivityObject().getCurrentLongitude()), new AcceptJobDetails());
//            markersDetails.add(markerWithDetails);
//            initializeMap(markersDetails);

            //set ride started marker detail
            ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
            //set customer current location marker detail
            if (getDashboardCustomerNavigationDrawerActivityObject().getCurrentLatitude() != 0.0
                    && getDashboardCustomerNavigationDrawerActivityObject().getCurrentLongitude() != 0.0) {
                MapMarkerWithDetails markerWithDetailsCurrentLocation = new MapMarkerWithDetails(new LatLng(getDashboardCustomerNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardCustomerNavigationDrawerActivityObject().getCurrentLongitude()), new AcceptJobDetails());
                markersDetails.add(markerWithDetailsCurrentLocation);
            }
            //set customer destination marker detail
            double lat = 0.0;
            double lon = 0.0;
            try {
                lat = Double.parseDouble(AppSettings.getCustomerDestinationLatitude(getActivity()));
                lon = Double.parseDouble(AppSettings.getCustomerDestinationLongitude(getActivity()));
            } catch (Exception ex) {
                lat = 0.0;
                lon = 0.0;
            }
            if (lat != 0.0 && lon != 0.0) {
                MapMarkerWithDetails markerWithDetailsDestination = new MapMarkerWithDetails(new LatLng(lat, lon), new AcceptJobDetails());
                markersDetails.add(markerWithDetailsDestination);
            }

            if (markersDetails.size() == 2) {
                initializeMap(markersDetails);
            }

        } else if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.RIDE_COMPLETED) {
            txtStatusDriver.setText(AppSettings.getCustomerStatusText(getActivity()));
            llHiddenConnectLayout.setVisibility(View.VISIBLE);
            rlShadowLayout.setVisibility(View.VISIBLE);
            llHiddenPhone.setVisibility(View.GONE);
            llHiddenMessage.setVisibility(View.GONE);
            llHiddenRideCompleted.setVisibility(View.VISIBLE);
            ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
            MapMarkerWithDetails markerWithDetails = new MapMarkerWithDetails(new LatLng(getDashboardCustomerNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardCustomerNavigationDrawerActivityObject().getCurrentLongitude()), new AcceptJobDetails());
            markersDetails.add(markerWithDetails);
            initializeMap(markersDetails);
        }


    }

//    public String getStatusText() {
//        return statusText;
//    }
//
//    public void setStatusText(String statusText) {
//        this.statusText = statusText;
//    }

    public void initCallATaxiAction() {
//        AsyncJob.doInBackground(pairWithDevice);
        btnPhoneDriver.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
//                if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.REQUEST_ACCEPTED) {
                Uri number = Uri.parse("tel:" + AppSettings.getJobAcceptedDrivePhone(getActivity()));
                Intent intent = new Intent(Intent.ACTION_CALL, number);
                startActivity(intent);
//                }
            }
        });

        btnMessageDriver.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                DialogSendMessage dialogSendMessage = new DialogSendMessage(getActivity(), "Message", AllConstants.USER_TYPE.CUSTOMER);
                dialogSendMessage.show();
            }
        });

        btnRideCompleted.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.RIDE_COMPLETED) {
                    AppSettings.setCustomerStatus(getActivity(), AllConstants.CUSTOMER_STATUS.IDLE);
                    reInitializeCallATaxiUI(AppSettings.getCustomerStatus(getActivity()));
                }
            }
        });
    }

    private void createSupportMapFragmentOnFragment() {
        //check google play service
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, getActivity(), requestCode);
            dialog.show();
            return;
        }
        //create map
//        FragmentManager fm = getChildFragmentManager();
//        fragmentMap = (SupportMapFragment) fm.findFragmentById(R.id.map_container);
//        if (fragmentMap == null) {
//            fragmentMap = SupportMapFragment.newInstance();
//            fm.beginTransaction().replace(R.id.map_container, fragmentMap).commit();
//        }

        fragmentMap = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapWrapperLayout = (MapWrapperLayout) parentView.findViewById(R.id.map_container);
    }

    public void initializeMap(final ArrayList<MapMarkerWithDetails> location) {
        if (googleMap == null) {
            googleMap = fragmentMap.getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.setMyLocationEnabled(true);
            //set custom marker view
            setCustomMarkerView();
            setCustomMarkerAction();
        } else {
            clearMapData();
        }
        for (int i = 0; i < location.size(); i++) {
            //set marker at current position
            Log.d("callataxinew:", "in map " + "lat: " + location.get(i).getLatLng().latitude + " lon: " + location.get(i).getLatLng().longitude);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location.get(i).getLatLng());
            final Marker mapMarker = googleMap.addMarker(markerOptions);
            Log.d("callataxinew", "marker added no " + i);
            allMarkersDetails.put(mapMarker, (AcceptJobDetails) location.get(i).getObject());
            if (NetworkManager.isConnected(getActivity())) {
                if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.IDLE) {
                    //get address from current latitude and longitude
                    try {
                        new ReverseGeocodingTask(getActivity(), new ReverseGeocodingTask.AddressCallback() {
                            @Override
                            public void onAddressUpdate(String address) {
                                getDashboardCustomerNavigationDrawerActivityObject().setCurrentAddress(address);
                                txtMapInfoWindowAddress.setText(address);
                                //always open marker infowindows
                                mapMarker.showInfoWindow();
                            }
                        }).execute(location.get(i).getLatLng());
                    } catch (Exception ex) {
                    }
                } else {
                    mapMarker.hideInfoWindow();
                }
            }
            //set marker click listner
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    if (NetworkManager.isConnected(getActivity())) {
                        if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.IDLE) {
                            //get address from current latitude and longitude
                            new ReverseGeocodingTask(getActivity(), new ReverseGeocodingTask.AddressCallback() {
                                @Override
                                public void onAddressUpdate(String address) {
                                    getDashboardCustomerNavigationDrawerActivityObject().setCurrentAddress(address);
                                    txtMapInfoWindowAddress.setText(address);
                                    //always open marker infowindows
                                    marker.showInfoWindow();
                                }
                            }).execute(marker.getPosition());
                        } else {
                            marker.hideInfoWindow();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please connect to internet getting details", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
            //set marker at touched location
            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.IDLE) {
                        ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
                        MapMarkerWithDetails markerWithDetails = new MapMarkerWithDetails(latLng, new AcceptJobDetails());
                        markersDetails.add(markerWithDetails);
                        initializeMap(markersDetails);
                    }
                }
            });
        }

        if (location.size() > 0) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location.get(0).getLatLng(), 16));
        }

        if (location.size() == 2) {
            if (AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.REQUEST_ACCEPTED
                    || AppSettings.getCustomerStatus(getActivity()) == AllConstants.CUSTOMER_STATUS.RIDE_STARTED) {
                Navigator nav = new Navigator(googleMap, location.get(0).getLatLng(), location.get(1).getLatLng());
                nav.findDirections(false);
            }
        }
//        else if (location.size() == 2) {
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location.get(0).getLatLng(), 16));
////            showMarkersInVisibleScreen(fragmentMap, googleMap, location);
//        }
    }

    private void setCustomMarkerAction() {
        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        infoButtonListener = new OnInfoWindowElemTouchListener(btnMapInfoWindowCallATaxi,
                getResources().getDrawable(R.drawable.border_circle_teal),
                getResources().getDrawable(R.drawable.border_circle_teal)) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                DashboardCustomerNavigationDrawerActivity mDashboard = ((DashboardCustomerNavigationDrawerActivity) getActivity());
                if (mDashboard.getCurrentLatitude() != 0.0 & mDashboard.getCurrentLongitude() != 0.0 && !mDashboard.getCurrentAddress().equalsIgnoreCase("")) {
//                      Need to implement this in VerticalStepperCallATaxiFragment fragment
//                    doCallATaxi(AppSettings.getUserID(getActivity()), mDashboard.getCurrentLatitude() + "", mDashboard.getCurrentLongitude() + "", mDashboard.getCurrentAddress(), "come quick");

                    Intent intent = new Intent(getActivity(), VerticalStepperCallATaxiActivity.class);
                    intent.putExtra(AllConstants.KEY_INTENT_CALL_A_TAXI_LATITUDE, mDashboard.getCurrentLatitude());
                    intent.putExtra(AllConstants.KEY_INTENT_CALL_A_TAXI_LONGITUDE, mDashboard.getCurrentLongitude());
                    intent.putExtra(AllConstants.KEY_INTENT_CALL_A_TAXI_ADDRESS, mDashboard.getCurrentAddress());
                    startActivity(intent);

                } else {
                    Toast.makeText(getActivity(), "Could not retrieve proper location", Toast.LENGTH_SHORT).show();
                }
            }
        };
        btnMapInfoWindowCallATaxi.setOnTouchListener(infoButtonListener);
        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                txtMapInfoWindowAddress.setText(getDashboardCustomerNavigationDrawerActivityObject().getCurrentAddress());
                infoButtonListener.setMarker(marker);
                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, viewInfoWindow);
                return viewInfoWindow;
            }
        });
    }

    public ArrayList<MapMarkerWithDetails> prepareMarkersDetails(DriverUpdatedLocation latLng) {
        Log.d("AcceptJob", "initialize multiple marker");
        ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
        markersDetails.clear();
        //set current customer lat-lon
        if (getDashboardCustomerNavigationDrawerActivityObject().getCurrentLatitude() != 0.0
                && getDashboardCustomerNavigationDrawerActivityObject().getCurrentLongitude() != 0.0) {
            LatLng customerPosition = new LatLng(getDashboardCustomerNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardCustomerNavigationDrawerActivityObject().getCurrentLongitude());
            MapMarkerWithDetails markerWithDetailsCustomer = new MapMarkerWithDetails(customerPosition, new AcceptJobDetails());
            markersDetails.add(markerWithDetailsCustomer);
            Log.d("customer position", "lat: " + customerPosition.latitude + " lon: " + customerPosition.longitude);
        }
        //set driver location
        double lat = 0.0;
        double lon = 0.0;
        try {
            lat = Double.parseDouble(latLng.getLat());
            lon = Double.parseDouble(latLng.getLon());
            Log.d("driver position", "lat: " + lat + " lon: " + lon);

        } catch (Exception ex) {
            lat = 0.0;
            lon = 0.0;
        }
        if (lat != 0.0 && lon != 0.0) {
            LatLng driverPosition = new LatLng(lat, lon);
            MapMarkerWithDetails markerWithDetailsDriver = new MapMarkerWithDetails(driverPosition, new AcceptJobDetails());
            markersDetails.add(markerWithDetailsDriver);
        }
        Log.d("Call a taxi", "Total markers " + markersDetails.size());
        if (markersDetails.size() == 2) {
            return markersDetails;
        } else {
            return new ArrayList<MapMarkerWithDetails>();
        }
    }

    private void setCustomMarkerView() {
        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(googleMap, AllSettingsManager.getPixelsFromDp(getActivity(), 39 + 20));
        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        viewInfoWindow = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.map_info_window_customer, null);
        txtMapInfoWindowAddress = (TextView) viewInfoWindow.findViewById(R.id.map_address);
        btnMapInfoWindowCallATaxi = (Button) viewInfoWindow.findViewById(R.id.map_btn_call_a_taxi);
    }

    public DriverUpdatedLocation getDriverUpdatedLocation() {
        return driverCurrentLocation;
    }

    public void setDriverUpdatedLocation(DriverUpdatedLocation driverLocation) {
        this.driverCurrentLocation = driverLocation;
    }

    public void clearMapData() {
        if (googleMap != null) {
            googleMap.clear();
            allMarkersDetails.clear();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("CallATaxiFragment", "onResume called");
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
//        if (AppSettings.getCustomerStatus(getActivity()) != AllConstants.CUSTOMER_STATUS.IDLE) {


//            setStatusText(AppSettings.getCustomerStatusText(getActivity()));
//            AsyncJob.doInBackground(doTextLoadingIcon);
        reInitializeCallATaxiUI(AppSettings.getCustomerStatus(getActivity()));
//        }


//        reInitializeCallATaxiUI(AppSettings.getCustomerStatus());
        //Initialize map pin location on fragment.
//        if (getDashboardCustomerNavigationDrawerActivityObject().mGoogleApiClient.isConnected() && getDashboardCustomerNavigationDrawerActivityObject().getCurrentLat() != 0.0 && getDashboardCustomerNavigationDrawerActivityObject().getCurrentLng() != 0.0) {
//            initializeMap(new LatLng(getDashboardCustomerNavigationDrawerActivityObject().getCurrentLat(), getDashboardCustomerNavigationDrawerActivityObject().getCurrentLng()));
//        } else {
//            Toast.makeText(getActivity(), "Could not retrieve user location.", Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CallATaxiFragment", "onPause called");
//        if (getDashboardCustomerNavigationDrawerActivityObject().mGoogleApiClient.isConnected()) {
//            clearMapData();
//        }
    }

    @Override
    public void onDestroy() {
        Log.d("CallATaxiFragment", "onDestroy called");
//        isLoadingIconCancled = true;
//        if (AppSettings.getCustomerStatus(getActivity()) != AllConstants.CUSTOMER_STATUS.IDLE) {
//            AppSettings.setCustomerStatusText(getActivity(), getStatusText());
//        }
        clearMapData();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

//    private void doCallATaxi(final String customerID, final String latitude, final String longitude, final String fromAddress, final String note) {
//
//
//        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
//            @Override
//            public TaskResult doOnBackground() {
//                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getDynamicCallATaxiURL(),
//                        AllURLs.getDynamicCallATaxiParams(customerID, latitude, longitude, fromAddress, note), null);
//                return response;
//            }
//
//            @Override
//            public void doOnProgress(Long... progress) {
//
//            }
//
//            @Override
//            public void doWhenFinished(TaskResult response) {
//                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
//                    Log.d("success response: ", response.getResult().toString());
//                    WrapperCallATaxiResponse responseData = WrapperCallATaxiResponse.getResponseData(response.getResult().toString());
//                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
//                        Log.d("success message: ", responseData.toString());
//                        AppSettings.setCustomerStatus(getActivity(), AllConstants.CUSTOMER_STATUS.REQUESTED_FOR_A_TAXI);
//                        AppSettings.setJobAcceptedCustomerOrderID(getActivity(), responseData.getOrderId());
//                        setStatusText("Connecting you with a nearest taxi driver");
//                        AppSettings.setCustomerStatusText(getActivity(), "Connecting you with a nearest taxi driver");
////                        AsyncJob.doInBackground(doTextLoadingIcon);
//                        reInitializeCallATaxiUI(AllConstants.CUSTOMER_STATUS.REQUESTED_FOR_A_TAXI);
//                        setStatusText(responseData.getSuccess());
////                        Toast.makeText(getActivity(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
//                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
//                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
//                        Log.d("error message: ", responseData.getError());
//                    }
//                }
//            }
//        });
//    }

//    AsyncJob.AsyncAction<TaskResult<String>> doTextLoadingIcon = new AsyncJob.AsyncAction<TaskResult<String>>() {
//        @Override
//        public TaskResult<String> doOnBackground() {
//            shape = "/";
//            int iCounter = 0; //1 unit = 250ms
//            while (!isLoadingIconCancled) {
//                iCounter++;
//                switch (shape) {
//                    case "/":
//                        shape = "\\";
//                        break;
//                    case "\\":
//                        shape = "--";
//                        break;
//                    case "--":
//                        shape = "/";
//                        break;
//                }
//                //Update Animation
////                AsyncJob.publishProgress(0L);
//                //Sleep for 250ms;
//                try {
//                    Thread.sleep(250);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (iCounter == 40) {
//                    //let's check if license is available
//
//                    //reset
//                    iCounter = 0;
//                }
//                if (isLoadingIconCancled) {
//                    shape = "";
////                    AsyncJob.publishProgress(0L);
//                    return new TaskResult<String>("");
//                }
//                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
//                    @Override
//                    public void doInUIThread() {
//                        txtLoadingIcon.setText(shape);
//                    }
//                });
//            }
//            return new TaskResult<String>("");
//        }
//
//        @Override
//        public void doOnProgress(Long... progress) {
//        }
//
//        @Override
//        public void doWhenFinished(TaskResult<String> taskResult) {
//
//        }
//    };

//    private void cancelTextLoading(boolean isCanceled) {
//        isLoadingIconCancled = true;
//    }

//    private void startTextLoading(final TextView loadingTextView){
//        if(textLoadingIconBuilder==null){
//            textLoadingIconBuilder= TextLoadingIcon.TextLoadingIconBuilder.getInstance();
//        }
//        textLoadingIconBuilder.create(new TextLoadingIcon.TextLoadingUpdate() {
//            @Override
//            public void getUpdate(String update) {
//                loadingTextView.setText(update);
//            }
//        });
//    }
//
//    private void cancelTextLoading(){
//        if(textLoadingIconBuilder==null){
//            textLoadingIconBuilder= TextLoadingIcon.TextLoadingIconBuilder.getInstance();
//        }
//        textLoadingIconBuilder.cancel(true);
//    }

    private void showMarkersInVisibleScreen(SupportMapFragment fragmentMap, final GoogleMap mGoogleMap, final ArrayList<MapMarkerWithDetails> markersDetails) {

        final View mapView = fragmentMap.getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @SuppressLint("NewApi")
                        // We check which build version we are using.
                        @Override
                        public void onGlobalLayout() {

                            int counter = 0;
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (int i = 0; i < markersDetails.size(); i++) {
                                double mLatitude = markersDetails.get(i).getLatLng().latitude;
                                double mLongitude = markersDetails.get(i).getLatLng().latitude;

                                builder.include(new LatLng(
                                        mLatitude,
                                        mLongitude));
                                counter++;

                            }

                            if (counter > 0) {
                                // build the LatLngBounds object
                                LatLngBounds bounds = builder.build();

                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                    mapView.getViewTreeObserver()
                                            .removeGlobalOnLayoutListener(
                                                    this);
                                } else {
                                    mapView.getViewTreeObserver()
                                            .removeOnGlobalLayoutListener(
                                                    this);
                                }
                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
//                                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                                mGoogleMap.animateCamera(cu);

                            }
                        }
                    });
        }
    }


}
