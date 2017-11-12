package com.reversecoder.gk7.customer.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.customviews.library.customasynctask.AsyncJob;
import com.customviews.library.custommapview.MapWrapperLayout;
import com.customviews.library.custommapview.OnInfoWindowElemTouchListener;
import com.customviews.library.custommapview.ReverseGeocodingTask;
import com.customviews.library.custommapview.mapnavigator.Navigator;
import com.customviews.library.event.OnSingleClickListener;
import com.customviews.library.httprequest.HttpUtility;
import com.customviews.library.model.TaskResult;
import com.customviews.library.rippleview.RippleBackground;
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
import com.reversecoder.gk7.customer.R;
import com.reversecoder.gk7.customer.activity.AcceptJobDetailActivity;
import com.reversecoder.gk7.customer.activity.DashboardDriverNavigationDrawerActivity;
import com.reversecoder.gk7.customer.dialog.DialogSendMessage;
import com.reversecoder.gk7.customer.model.MapMarkerWithDetails;
import com.reversecoder.gk7.customer.model.NewJobAlertDetail;
import com.reversecoder.gk7.customer.model.WrapperCommonResponse;
import com.reversecoder.gk7.customer.model.WrapperCompleteJobResponse;
import com.reversecoder.gk7.customer.utility.AllConstants;
import com.reversecoder.gk7.customer.utility.AllURLs;
import com.reversecoder.gk7.customer.utility.AppSettings;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobStatusFragment extends Fragment {

    View parentView;
    SupportMapFragment fragmentMap;
    GoogleMap googleMap;
    //    AllConstants.ADDRESS_TYPE addressType = AllConstants.ADDRESS_TYPE.NOTHING;
    TextView txtMapInfoWindowAddress;
    Button btnMapInfoWindowAcceptJob;
    //    Marker mapMarker;
    private OnInfoWindowElemTouchListener infoButtonListener;
    MapWrapperLayout mapWrapperLayout;
    private ViewGroup viewInfoWindow;
    private Map<Marker, NewJobAlertDetail> allMarkersDetails = new HashMap<Marker, NewJobAlertDetail>();
    //screen ui elements
//    String statusText = "";
    TextView txtStatusDriver;
    //    boolean isLoadingIconCancled = false;
    RelativeLayout rlShadowLayout;
    LinearLayout llHiddenPhone, llHiddenMessage, llHiddenStartJob, llHiddenCompleteJob, llHiddenPaid, llHiddenUnpaid;
    Button btnMessageCustomer, btnPhoneCustomer, btnStartJob, btnCompleteJob, btnRippleBackgroundAcceptJob, btnPaid, btnUnpaid;
    ArrayList<NewJobAlertDetail> allNewJobs = new ArrayList<NewJobAlertDetail>();
    RippleBackground rippleBackgroundNewJob;
    CountDownTimer rippleCountDownTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_job_status, container, false);
        Log.d("JobStatusFragment", "onCreateView called");
        initJobStatusUI();
        initJobStatusAction();
        return parentView;
    }

    public DashboardDriverNavigationDrawerActivity getDashboardDriverNavigationDrawerActivityObject() {
        return ((DashboardDriverNavigationDrawerActivity) getActivity());
    }

    private void initJobStatusUI() {
        createSupportMapFragmentOnFragment();
        ((DashboardDriverNavigationDrawerActivity) getActivity()).checkLocationSettings();
        rlShadowLayout = (RelativeLayout) parentView.findViewById(R.id.rl_shadow_layout);
        llHiddenPhone = (LinearLayout) parentView.findViewById(R.id.ll_hidden_phone);
        llHiddenMessage = (LinearLayout) parentView.findViewById(R.id.ll_hidden_message);
        llHiddenStartJob = (LinearLayout) parentView.findViewById(R.id.ll_hidden_start_job);
        llHiddenCompleteJob = (LinearLayout) parentView.findViewById(R.id.ll_hidden_complete_job);
        llHiddenPaid = (LinearLayout) parentView.findViewById(R.id.ll_hidden_paid);
        llHiddenUnpaid = (LinearLayout) parentView.findViewById(R.id.ll_hidden_unpaid);
        txtStatusDriver = (TextView) parentView.findViewById(R.id.txt_status_customer);
        btnMessageCustomer = (Button) parentView.findViewById(R.id.btn_message_customer);
        btnPhoneCustomer = (Button) parentView.findViewById(R.id.btn_phone_customer);
        btnStartJob = (Button) parentView.findViewById(R.id.btn_start_job);
        btnCompleteJob = (Button) parentView.findViewById(R.id.btn_complete_job);
        btnPaid = (Button) parentView.findViewById(R.id.btn_paid);
        btnUnpaid = (Button) parentView.findViewById(R.id.btn_unpaid);
        rippleBackgroundNewJob = (RippleBackground) parentView.findViewById(R.id.ripple_bg_new_job);
        btnRippleBackgroundAcceptJob = (Button) parentView.findViewById(R.id.ripple_bg_btn_accept_job);
    }

//    public String getStatusText() {
//        return statusText;
//    }
//
//    public void setStatusText(String statusText) {
//        this.statusText = statusText;
//    }

    public ArrayList<NewJobAlertDetail> getAllNewJobs() {
        return allNewJobs;
    }

    public void setAllNewJobs(ArrayList<NewJobAlertDetail> allNewJobs) {
        this.allNewJobs = allNewJobs;
    }

    public void initJobStatusAction() {
        btnPhoneCustomer.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
//                if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_ACCEPTED) {
                Uri number = Uri.parse("tel:" + AppSettings.getJobAcceptedCustomerPhone(getActivity()));
                Intent intent = new Intent(Intent.ACTION_CALL, number);
                startActivity(intent);
//                }
            }
        });

        btnMessageCustomer.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                DialogSendMessage dialogSendMessage = new DialogSendMessage(getActivity(), "Message", AllConstants.USER_TYPE.DRIVER);
                dialogSendMessage.show();
            }
        });

        btnStartJob.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_ACCEPTED) {
                    if (!NetworkManager.isConnected(getActivity())) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    doStartJob(AppSettings.getUserID(getActivity()), AppSettings.getJobAcceptedDriverOrderID(getActivity()));
                    //for test purpose
//                    AppSettings.setDriverStatus(getActivity(), AllConstants.DRIVER_STATUS.JOB_STARTED);
//                    setStatusText("Moving to the destination");
//                    AppSettings.setDriverStatusText(getActivity(), "Moving to the destination");
//                    reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));
                }
            }
        });

        btnCompleteJob.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                //wait 3 second after clicking button for igonring multiple press
                if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_STARTED) {
                    if (!NetworkManager.isConnected(getActivity())) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude() != 0.0
                            && getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude() != 0.0
                            && !AllSettingsManager.isNullOrEmpty(getDashboardDriverNavigationDrawerActivityObject().getCurrentAddress())) {
                        doDriverCompleteJob(AppSettings.getUserID(getActivity()), AppSettings.getJobAcceptedDriverOrderID(getActivity())
                                , getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude() + ""
                                , getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude() + ""
                                , getDashboardDriverNavigationDrawerActivityObject().getCurrentAddress());
                    }
                    //for test purpose
//                    AppSettings.setDriverStatus(getActivity(), AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS);
//                    isLoadingIconCancled=true;
//                    reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));
                }
            }
        });

        btnRippleBackgroundAcceptJob.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (getAllNewJobs() != null && getAllNewJobs().size() > 0) {
//                    doAcceptJob(AppSettings.getUserID(getActivity()), getAllNewJobs().get(0));
                    NewJobAlertDetail markerDetail = getAllNewJobs().get(getAllNewJobs().size() - 1);
                    if (markerDetail != null) {
                        Intent intent = new Intent(getActivity(), AcceptJobDetailActivity.class);
                        intent.putExtra(AllConstants.KEY_INTENT_ACCEPT_JOB_DETAIL, NewJobAlertDetail.getObjectData(markerDetail));
                        intent.putExtra(AllConstants.KEY_INTENT_ACCEPT_JOB_TYPE, AllConstants.DRIVER_JOB_TYPE.NORMAL.name());
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Could not get job detail.", Toast.LENGTH_SHORT).show();
                    }
                }
                    /*For test purpose id 1 is used*/
//                    AppSettings.setDriverStatus(getActivity(), AllConstants.DRIVER_STATUS.JOB_ACCEPTED);
//                    setStatusText("Customer is waiting for you at " + "Road No. 2D, Dhaka, Bangladesh");
//                    AppSettings.setDriverStatusText(getActivity(), "Customer is waiting for you at " + "Road No. 2D, Dhaka, Bangladesh");
//                    AppSettings.setJobAcceptedCustomerID(getActivity(), "3");
//                    AppSettings.setJobAcceptedCustomerPhone(getActivity(),"01794620787");
//                    AppSettings.setJobAcceptedDriverOrderID(getActivity(), "50");
//                    AsyncJob.doInBackground(doTextLoadingIcon);
//                    reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));
            }
        });

        btnPaid.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                doPaidJob(AppSettings.getUserID(getActivity()), AppSettings.getJobAcceptedDriverOrderID(getActivity()));

            }
        });

        btnUnpaid.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                doUnPaidJob(AppSettings.getUserID(getActivity()), AppSettings.getJobAcceptedDriverOrderID(getActivity()));
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

    public GoogleMap initializeMap(final ArrayList<MapMarkerWithDetails> location) {
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
            final MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location.get(i).getLatLng());
            final Marker mapMarker = googleMap.addMarker(markerOptions);
            allMarkersDetails.put(mapMarker, (NewJobAlertDetail) location.get(i).getObject());

//                if (NetworkManager.isConnected(getActivity())) {
//                    if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
//                        try {
//                            new ReverseGeocodingTask(getActivity(), new ReverseGeocodingTask.AddressCallback() {
//                                @Override
//                                public void onAddressUpdate(String address) {
//                                    txtMapInfoWindowAddress.setText(address);
//                                    mapMarker.showInfoWindow();
//                                }
//                            }).execute(mapMarker.getPosition());
//                        } catch (Exception ex) {
//                        }
//                    } else {
//                        mapMarker.hideInfoWindow();
//                    }
//                }

            //set marker click listner
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(final Marker marker) {
                    if (NetworkManager.isConnected(getActivity())) {
                        if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
                            try {
                                new ReverseGeocodingTask(getActivity(), new ReverseGeocodingTask.AddressCallback() {
                                    @Override
                                    public void onAddressUpdate(String address) {
                                        txtMapInfoWindowAddress.setText(address);
                                        marker.showInfoWindow();
                                    }
                                }).execute(marker.getPosition());
                            } catch (Exception ex) {
                            }
                        } else {
                            marker.hideInfoWindow();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please connect to internet getting details", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
            });
//        //set marker at touched location
//        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//                initializeMap(latLng);
//            }
//        });

        }

        if (location.size() > 0) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location.get(0).getLatLng(), 16));
        }

        if (location.size() == 2) {
            if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_ACCEPTED
                    || AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_STARTED) {
                Navigator nav = new Navigator(googleMap, location.get(0).getLatLng(), location.get(1).getLatLng());
                nav.findDirections(false);
            }
        }
//        else if(location.size() == 2) {
////            showMarkersInVisibleScreen(fragmentMap, googleMap, location);
//            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location.get(0).getLatLng(), 16));
//        }

        return googleMap;
    }

    private void setCustomMarkerView() {
        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge
        mapWrapperLayout.init(googleMap, AllSettingsManager.getPixelsFromDp(getActivity(), 39 + 20));
        // We want to reuse the info window for all the markers,
        // so let's create only one class member instance
        viewInfoWindow = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.map_info_window_driver, null);
        txtMapInfoWindowAddress = (TextView) viewInfoWindow.findViewById(R.id.map_address);
        btnMapInfoWindowAcceptJob = (Button) viewInfoWindow.findViewById(R.id.map_btn_accept_job);
    }

    private void setCustomMarkerAction() {
        // Setting custom OnTouchListener which deals with the pressed state
        // so it shows up
        infoButtonListener = new OnInfoWindowElemTouchListener(btnMapInfoWindowAcceptJob,
                getResources().getDrawable(R.drawable.border_circle_teal),
                getResources().getDrawable(R.drawable.border_circle_teal)) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                // Here we can perform some action triggered after clicking the button
                if (!NetworkManager.isConnected(getActivity())) {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
                    NewJobAlertDetail markerDetail = allMarkersDetails.get(marker);
                    if (markerDetail != null) {
                        Intent intent = new Intent(getActivity(), AcceptJobDetailActivity.class);
                        intent.putExtra(AllConstants.KEY_INTENT_ACCEPT_JOB_DETAIL, NewJobAlertDetail.getObjectData(markerDetail));
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "Could not get job detail.", Toast.LENGTH_SHORT).show();
                    }

//                    NewJobAlertDetail markerDetail = allMarkersDetails.get(marker);
//                    doAcceptJob(AppSettings.getUserID(getActivity()), markerDetail);


                    /*For test purpose id 1 is used*/
//                    AppSettings.setDriverStatus(getActivity(), AllConstants.DRIVER_STATUS.JOB_ACCEPTED);
//                    setStatusText("Customer is waiting for you at " + "Road No. 2D, Dhaka, Bangladesh");
//                    AppSettings.setDriverStatusText(getActivity(), "Customer is waiting for you at " + "Road No. 2D, Dhaka, Bangladesh");
//                    AppSettings.setJobAcceptedCustomerID(getActivity(), "3");
//                    AppSettings.setJobAcceptedCustomerPhone(getActivity(),"01794620787");
//                    AppSettings.setJobAcceptedDriverOrderID(getActivity(), "50");
//                    AsyncJob.doInBackground(doTextLoadingIcon);
//                    reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));

                }
            }
        };
        btnMapInfoWindowAcceptJob.setOnTouchListener(infoButtonListener);
        // Setting a custom info window adapter for the google map
        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Setting up the infoWindow with current's marker info
                infoButtonListener.setMarker(marker);
                // We must call this to set the current marker and infoWindow references
                // to the MapWrapperLayout
                mapWrapperLayout.setMarkerWithInfoWindow(marker, viewInfoWindow);
                return viewInfoWindow;
            }
        });
    }

    public ArrayList<MapMarkerWithDetails> prepareMarkersDetails(ArrayList<NewJobAlertDetail> latLng) {
        Log.d("AcceptJob", "initialize multiple marker");
        ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
        for (int i = 0; i < latLng.size(); i++) {
            double lat = Double.parseDouble(latLng.get(i).getCustomerLat());
            double lon = Double.parseDouble(latLng.get(i).getCustomerLon());
            if (lat != 0.0 && lon != 0.0) {
                LatLng position = new LatLng(lat, lon);
                MapMarkerWithDetails markerWithDetails = new MapMarkerWithDetails(position, latLng.get(i));
                markersDetails.add(markerWithDetails);
            }
        }
        ////////////////
////for test purpose
//        double lat = 23.895867;
//        double lon = 90.5262402;
//        LatLng position = new LatLng(lat, lon);
//        MapMarkerWithDetails markerWithDetails = new MapMarkerWithDetails(position, latLng.get(0));
//        markersDetails.add(markerWithDetails);
        //////////////
        Log.d("AcceptJob", "Total markers " + markersDetails.size());
//        initializeMap(markersDetails);
        if (markersDetails.size() > 0) {
            return markersDetails;
        } else {
            return new ArrayList<MapMarkerWithDetails>();
        }
    }

//    private void hiddenLayoutManager(AllConstants.DRIVER_STATUS driverStatus) {
//        if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS) {
//            rlShadowLayout.setVisibility(View.GONE);
//            rippleBackgroundNewJob.setVisibility(View.GONE);
//        } else if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
//            rlShadowLayout.setVisibility(View.GONE);
//        } else if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_ACCEPTED) {
//        } else if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_STARTED) {
//        }
//    }

    public void reInitializeAcceptJobUI(AllConstants.DRIVER_STATUS driverStatus) {
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
//        Toast.makeText(getActivity(), "Reinitialized map from: " + AppSettings.getDriverStatus(getActivity()).toString(), Toast.LENGTH_SHORT).show();
        if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS) {
            rlShadowLayout.setVisibility(View.GONE);
            rippleBackgroundNewJob.setVisibility(View.GONE);
            //remove help button
            getDashboardDriverNavigationDrawerActivityObject().removeHelpButton();
            //remove all notification from status bar
            NotificationUtilManager.clearNotificationFromNotificationBar(getActivity(), AllConstants.NOTIFICATION_ID_NEW_MESSAGE);
            NotificationUtilManager.clearNotificationFromNotificationBar(getActivity(), AllConstants.NOTIFICATION_ID_NEW_NORMAL_JOB);
            NotificationUtilManager.clearNotificationFromNotificationBar(getActivity(), AllConstants.NOTIFICATION_ID_NEW_BID_JOB);

            MapMarkerWithDetails markerWithDetails = new MapMarkerWithDetails(new LatLng(getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude()), new NewJobAlertDetail());
            ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
            markersDetails.add(markerWithDetails);
            initializeMap(markersDetails);
//            initializeMap(new LatLng(getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude()));
        } else if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
            rlShadowLayout.setVisibility(View.GONE);
            //remove help button
            getDashboardDriverNavigationDrawerActivityObject().removeHelpButton();

            ArrayList<MapMarkerWithDetails> data = prepareMarkersDetails(getAllNewJobs());
            if (data.size() > 0) {
                initializeMap(data);
                rippleBackgroundNewJob.setVisibility(View.VISIBLE);
                if (!rippleBackgroundNewJob.isRippleAnimationRunning()) {
                    rippleBackgroundNewJob.startRippleAnimation();
                    rippleCountDownTimer = new CountDownTimer(20000, 1000) {

                        public void onTick(long millisUntilFinished) {
//                            mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                        }

                        public void onFinish() {
                            if (rippleBackgroundNewJob.isRippleAnimationRunning()) {
                                rippleBackgroundNewJob.stopRippleAnimation();
                                rippleBackgroundNewJob.setVisibility(View.GONE);
                            }
                        }
                    }.start();
                }
            }
        } else if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_ACCEPTED) {
            if (rippleBackgroundNewJob.isRippleAnimationRunning()) {
                rippleBackgroundNewJob.stopRippleAnimation();
                if (rippleCountDownTimer != null) {
                    rippleCountDownTimer.cancel();
                }
            }
            rippleBackgroundNewJob.setVisibility(View.GONE);
            txtStatusDriver.setText(AppSettings.getDriverStatusText(getActivity()));
            rlShadowLayout.setVisibility(View.VISIBLE);
            llHiddenPhone.setVisibility(View.VISIBLE);
            llHiddenMessage.setVisibility(View.VISIBLE);
            llHiddenStartJob.setVisibility(View.VISIBLE);
            llHiddenCompleteJob.setVisibility(View.GONE);
            llHiddenPaid.setVisibility(View.GONE);
            llHiddenUnpaid.setVisibility(View.GONE);

            //initialized help button
            getDashboardDriverNavigationDrawerActivityObject().initializeHelpButton();

            //clear notification from status bar
            NotificationUtilManager.clearNotificationFromNotificationBar(getActivity(), AllConstants.NOTIFICATION_ID_NEW_NORMAL_JOB);
            NotificationUtilManager.clearNotificationFromNotificationBar(getActivity(), AllConstants.NOTIFICATION_ID_NEW_BID_JOB);

            ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
            markersDetails.clear();
            //driver marker detail
            if (getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude() != 0.0
                    && getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude() != 0.0) {
                MapMarkerWithDetails markerWithDetailsDriver = new MapMarkerWithDetails(new LatLng(getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude()), new NewJobAlertDetail());
                markersDetails.add(markerWithDetailsDriver);
            }
            //customer marker detail
            double lat = 0.0;
            double lon = 0.0;
            try {
                lat = Double.parseDouble(AppSettings.getJobAcceptedCustomerLatitude(getActivity()));
                lon = Double.parseDouble(AppSettings.getJobAcceptedCustomerLongitude(getActivity()));
            } catch (Exception ex) {
                lat = 0.0;
                lon = 0.0;
            }
            if (lat != 0.0 && lon != 0.0) {
                MapMarkerWithDetails markerWithDetailsCustomer = new MapMarkerWithDetails(new LatLng(lat, lon), new NewJobAlertDetail());
                markersDetails.add(markerWithDetailsCustomer);
            }
            if (markersDetails.size() == 2) {
                initializeMap(markersDetails);
            }
            //set current customer lat-lon
//            if (getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude() != 0.0
//                    && getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude() != 0.0) {
//                markersDetails.add(new MapMarkerWithDetails(new LatLng(getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude()
//                        , getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude()), new AcceptJobDetails()));
//            }
        } else if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_STARTED) {
            txtStatusDriver.setText(AppSettings.getDriverStatusText(getActivity()));
//            llShadowConnectLayout.setVisibility(View.GONE);
            rlShadowLayout.setVisibility(View.VISIBLE);
            rippleBackgroundNewJob.setVisibility(View.GONE);
            llHiddenPhone.setVisibility(View.VISIBLE);
            llHiddenMessage.setVisibility(View.VISIBLE);
            llHiddenStartJob.setVisibility(View.GONE);
            llHiddenCompleteJob.setVisibility(View.VISIBLE);
            llHiddenPaid.setVisibility(View.GONE);
            llHiddenUnpaid.setVisibility(View.GONE);

            //initialized help button
            getDashboardDriverNavigationDrawerActivityObject().initializeHelpButton();

            //set start job marker detail
            ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
            //set driver current location marker detail
            if (getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude() != 0.0
                    && getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude() != 0.0) {
                MapMarkerWithDetails markerWithDetailsDriver = new MapMarkerWithDetails(new LatLng(getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude()), new NewJobAlertDetail());
                markersDetails.add(markerWithDetailsDriver);
            }
            //set customer destination marker detail
            double lat = 0.0;
            double lon = 0.0;
            try {
                lat = Double.parseDouble(AppSettings.getJobAcceptedCustomerDestinationLatitude(getActivity()));
                lon = Double.parseDouble(AppSettings.getJobAcceptedCustomerDestinationLongitude(getActivity()));
            } catch (Exception ex) {
                lat = 0.0;
                lon = 0.0;
            }
            if (lat != 0.0 && lon != 0.0) {
                MapMarkerWithDetails markerWithDetailsCustomer = new MapMarkerWithDetails(new LatLng(lat, lon), new NewJobAlertDetail());
                markersDetails.add(markerWithDetailsCustomer);
            }
            if (markersDetails.size() == 2) {
                initializeMap(markersDetails);
            }
        } else if (AppSettings.getDriverStatus(getActivity()) == AllConstants.DRIVER_STATUS.JOB_COMPLETED) {
            txtStatusDriver.setText(AppSettings.getDriverStatusText(getActivity()));
            rlShadowLayout.setVisibility(View.VISIBLE);
            rippleBackgroundNewJob.setVisibility(View.GONE);
            llHiddenPhone.setVisibility(View.GONE);
            llHiddenMessage.setVisibility(View.GONE);
            llHiddenStartJob.setVisibility(View.GONE);
            llHiddenCompleteJob.setVisibility(View.GONE);
            llHiddenPaid.setVisibility(View.VISIBLE);
            llHiddenUnpaid.setVisibility(View.VISIBLE);

            //initialized help button
            getDashboardDriverNavigationDrawerActivityObject().initializeHelpButton();

            //remove notification from status bar
            NotificationUtilManager.clearNotificationFromNotificationBar(getActivity(), AllConstants.NOTIFICATION_ID_NEW_MESSAGE);

            MapMarkerWithDetails markerWithDetails = new MapMarkerWithDetails(new LatLng(getDashboardDriverNavigationDrawerActivityObject().getCurrentLatitude(), getDashboardDriverNavigationDrawerActivityObject().getCurrentLongitude()), new NewJobAlertDetail());
            ArrayList<MapMarkerWithDetails> markersDetails = new ArrayList<MapMarkerWithDetails>();
            markersDetails.add(markerWithDetails);
            initializeMap(markersDetails);

        }

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
        Log.d("JobStatusFragment", "onResume called");
        if (!NetworkManager.isConnected(getActivity())) {
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.alert_network_error), Toast.LENGTH_SHORT).show();
            return;
        }
//        if (AppSettings.getDriverStatus(getActivity()) != AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS
//                || AppSettings.getDriverStatus(getActivity()) != AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {


//            setStatusText(AppSettings.getDriverStatusText(getActivity()));
//            AsyncJob.doInBackground(doTextLoadingIcon);


        reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));
//        }


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
        Log.d("JobStatusFragment", "onPause called");
//        if (getDashboardCustomerNavigationDrawerActivityObject().mGoogleApiClient.isConnected()) {
//            clearMapData();
//        }
    }

    @Override
    public void onDestroy() {
        Log.d("JobStatusFragment", "onDestroy called");
//        isLoadingIconCancled = true;
//        if (AppSettings.getDriverStatus(getActivity()) != AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS
//                || AppSettings.getDriverStatus(getActivity()) != AllConstants.DRIVER_STATUS.IDLE_WITH_JOBS) {
//            AppSettings.setDriverStatusText(getActivity(), getStatusText());
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

//    private void doAcceptJob(final String driverID, final NewJobAlertDetail newJobDetail) {
//        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
//            @Override
//            public TaskResult doOnBackground() {
//                TaskResult response = HttpUtility.doGetRequest(AllURLs.getDriverAcceptJobURL(driverID, newJobDetail.getOrderId()));
//                return response;
//            }
//
//            @Override
//            public void doOnProgress(Long... progress) {
//            }
//
//            @Override
//            public void doWhenFinished(TaskResult response) {
//                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
//                    Log.d("success response: ", response.getResult().toString());
//                    WrapperCommonResponse responseData = WrapperCommonResponse.getResponseData(response.getResult().toString());
//                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
//                        Log.d("success message: ", responseData.toString());
////                        Toast.makeText(getActivity(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
//                        AppSettings.setDriverStatus(getActivity(), AllConstants.DRIVER_STATUS.JOB_ACCEPTED);
//                        setStatusText("Customer is waiting for you at " + newJobDetail.getCustomerAddress());
//                        AppSettings.setDriverStatusText(getActivity(), "Customer is waiting for you at " + newJobDetail.getCustomerAddress());
//                        AppSettings.setJobAcceptedCustomerID(getActivity(), newJobDetail.getCustomerId());
//                        AppSettings.setJobAcceptedCustomerPhone(getActivity(), newJobDetail.getCustomerMobile());
//                        AppSettings.setJobAcceptedDriverOrderID(getActivity(), newJobDetail.getOrderId());
//                        AppSettings.setJobAcceptedCustomerLatitude(getActivity(), newJobDetail.getCustomerLat());
//                        AppSettings.setJobAcceptedCustomerLongitude(getActivity(), newJobDetail.getCustomerLon());
//                        reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));
//                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
//                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
//                        Log.d("error message: ", responseData.getError());
//                    }
//                }
//            }
//        });
//    }

    private void doStartJob(final String driverID, final String orderID) {
        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                TaskResult response = HttpUtility.doGetRequest(AllURLs.getStartJobURL(driverID, orderID));
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
//                        Toast.makeText(getActivity(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                        AppSettings.setDriverStatus(getActivity(), AllConstants.DRIVER_STATUS.JOB_STARTED);
//                        setStatusText("Moving to the destination");
                        AppSettings.setDriverStatusText(getActivity(), "Moving to the destination");
                        reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }


    private void doDriverCompleteJob(final String driverID, final String orderID, final String currentLatitude, final String currentLongitude, final String currentAddress) {
        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                JSONObject param = AllURLs.getDriverCompleteJobParam(driverID, orderID, currentLatitude, currentLongitude
                        , currentAddress);
                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getDriverCompleteJobURL(), param, null);
                return response;
            }

            @Override
            public void doOnProgress(Long... progress) {

            }

            @Override
            public void doWhenFinished(TaskResult response) {

                if (response.isSuccess() && !AllSettingsManager.isNullOrEmpty(response.getResult().toString())) {
                    Log.d("success response: ", response.getResult().toString());
                    WrapperCompleteJobResponse responseData = WrapperCompleteJobResponse.getResponseData(response.getResult().toString());
                    if (!AllSettingsManager.isNullOrEmpty(responseData.getSuccess())) {
                        Log.d("success message: ", responseData.toString());
                        AppSettings.setDriverStatus(getActivity(), AllConstants.DRIVER_STATUS.JOB_COMPLETED);
                        String msg = "Paid distance: " + responseData.getInfo().getPaidDistance() + ", Duration: " + responseData.getInfo().getDurationTaken()
                                + ", Cost: " + getResources().getString(R.string.symbol_pound) + responseData.getInfo().getBillToPay() + "."
                                + "Please press paid if paid by customer, otherwise press unpaid.";
                        AppSettings.setDriverStatusText(getActivity(), msg);
//                        setStatusText(msg);
                        reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));
//                        Toast.makeText(getActivity(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

    private void doPaidJob(final String driverID, final String orderID) {
        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                JSONObject param = AllURLs.getPaidParams(driverID, orderID);
                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getPaidURL(), param, null);
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
                        AppSettings.setDriverStatus(getActivity(), AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS);
                        reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));
                        Toast.makeText(getActivity(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

    private void doUnPaidJob(final String driverID, final String orderID) {
        AsyncJob.doInBackground(new AsyncJob.AsyncAction<TaskResult>() {
            @Override
            public TaskResult doOnBackground() {
                JSONObject param = AllURLs.getUnpaidParams(driverID, orderID);
                TaskResult response = HttpUtility.doRestPostRequest(AllURLs.getUnpaidURL(), param, null);
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
                        AppSettings.setDriverStatus(getActivity(), AllConstants.DRIVER_STATUS.IDLE_WITH_NO_JOBS);
                        reInitializeAcceptJobUI(AppSettings.getDriverStatus(getActivity()));
                        Toast.makeText(getActivity(), responseData.getSuccess(), Toast.LENGTH_SHORT).show();
                    } else if (!AllSettingsManager.isNullOrEmpty(responseData.getError())) {
                        Toast.makeText(getActivity(), responseData.getError(), Toast.LENGTH_SHORT).show();
                        Log.d("error message: ", responseData.getError());
                    }
                }
            }
        });
    }

    private void showMarkersInVisibleScreen(final SupportMapFragment fragmentMap, final GoogleMap mGoogleMap, final ArrayList<MapMarkerWithDetails> markersDetails) {

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

                                int paddingInPixels = (int) (Math
                                        .min(mapView.getMeasuredWidth(), mapView.getMeasuredHeight()) * AllConstants.PADDING_RATIO);
                                CameraUpdate cu = CameraUpdateFactory
                                        .newLatLngBounds(bounds,
                                                paddingInPixels);
                                mGoogleMap.moveCamera(cu);
                                mGoogleMap.animateCamera(cu);

                            }
                        }
                    });
        }
    }


}
