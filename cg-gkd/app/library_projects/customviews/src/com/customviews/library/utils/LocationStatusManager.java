package com.customviews.library.utils;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

public class LocationStatusManager extends Service implements LocationListener {

    private final Context mContext;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location; // location
    double latitude=0.0; // latitude
    double longitude=0.0; // longitude
    //    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 0 meters
    //    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private static final long MIN_TIME_BW_UPDATES = 500; // 500 millisecond
    protected LocationManager locationManager;
    private static LocationStatusManager locationStatusManager = null;
    private static LocationStatusCallback locationStatusCallback = null;
    private String gpsProvider = "", networkProvider = "";

    public interface LocationStatusCallback {
        public abstract void onLocationStatusUpdate(Location location, String provider);
    }

    private LocationStatusManager(Context context) {
        this.mContext = context;
        startTracking();
    }

    public static LocationStatusManager getInstance(Context context) {
        if (locationStatusManager != null) {
            return locationStatusManager;
        } else {
            locationStatusManager = new LocationStatusManager(context);
            return locationStatusManager;
        }
    }

    public Location startTracking() {
        try {
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    networkProvider = LocationManager.NETWORK_PROVIDER;
                    if (networkProvider != null && networkProvider.length() > 0) {
                        locationManager.requestLocationUpdates(
                                networkProvider,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(networkProvider);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    gpsProvider = LocationManager.GPS_PROVIDER;
                    if (location == null) {
                        if (gpsProvider != null && gpsProvider.length() > 0) {
                            locationManager.requestLocationUpdates(
                                    gpsProvider,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            if (locationManager != null) {
                                location = locationManager
                                        .getLastKnownLocation(gpsProvider);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopTracking() {
        try {
            if (locationManager != null) {
                locationManager.removeUpdates(LocationStatusManager.this);
            }
        } catch (final Exception ex) {

        }
    }

    /**
     * Function to get provider
     */
    public String getProvider() {
        if (location != null) {
            return location.getProvider();
        }
        return "";
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (LocationStatusManager.locationStatusCallback != null && location != null) {
            LocationStatusManager.locationStatusCallback.onLocationStatusUpdate(location, location.getProvider());
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public LocationStatusManager setLocationStatusCallback(final LocationStatusCallback locationStatusCallback) {
        LocationStatusManager.locationStatusCallback = locationStatusCallback;
        return this;
    }

    public LocationStatusCallback getLocationStatusCallback() {
        return locationStatusCallback;
    }

}
