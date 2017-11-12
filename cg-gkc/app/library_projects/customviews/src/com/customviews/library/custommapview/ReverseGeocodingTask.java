package com.customviews.library.custommapview;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by rashed on 3/14/16.
 */
public class ReverseGeocodingTask extends AsyncTask<LatLng, Void, String> {

    public interface AddressCallback {
        public abstract void onAddressUpdate(String address);
    }

    AddressCallback addressCallback;

    Context mContext;

    public ReverseGeocodingTask(Context context, AddressCallback addressCallback) {
        super();
        mContext = context;
        this.addressCallback = addressCallback;
    }

    @Override
    protected String doInBackground(LatLng... params) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        double latitude = params[0].latitude;
        double longitude = params[0].longitude;
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.d("ReverseGeocodingTask","Latitude: "+latitude+" Longitude: "+longitude);
//            Log.d("ReverseGeocodingTask","Address: "+addresses.get(0).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*************
         *Need to add this
         ***********/
//    HashMap mAddress = new HashMap();
//    if (addresses != null && addresses.size() > 0) {
//        Address address = addresses.get(0);
//        mAddress.put(STREET, address.getMaxAddressLineIndex() > 0 ? address
//                .getAddressLine(0) : "");
//        mAddress.put(LOCALITY,address.getLocality());
//        mAddress.put(COUNTRY,address.getCountryName());
//        mAddress.put(CITY,address.getAdminArea());
//        mAddress.put(POSTAL_CODE,address.getPostalCode());
//    }

        String addressText = "";
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            Log.d("Add reversegeocoding: ", address.toString());
            if (address.getMaxAddressLineIndex() >= 3) {
                addressText = String.format("%s, %s, %s", address.getAddressLine(0), address.getAddressLine(1),
                        address.getAddressLine(2));
            } else {
                addressText = String.format("%s, %s, %s",
                        address.getMaxAddressLineIndex() >= 0 ? address.getAddressLine(0) : "", address.getLocality(),
                        address.getCountryName());
            }
        }
        return addressText;
    }

    @Override
    protected void onPostExecute(String addressText) {
        addressCallback.onAddressUpdate(addressText);
    }

}
