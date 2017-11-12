package com.customviews.library.custommapview;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.customviews.library.customasynctask.AdvancedAsyncTask;
import com.customviews.library.model.TaskParameter;
import com.customviews.library.model.TaskResult;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

/**
 * Created by rashed on 4/20/16.
 */
public class GeocoderAsyncTask extends AdvancedAsyncTask{

    private Context mContext;
    private GEOCODER_RETURN_TYPE geocodeType;

    public enum GEOCODER_RETURN_TYPE {LOCATION, ADDRESS}

    public GeocoderAsyncTask(Context context, GEOCODER_RETURN_TYPE geocodeType, AdvancedAsyncTaskListener listener) {
        super(context, listener);
        this.mContext = context;
        this.geocodeType = geocodeType;
    }

    public GEOCODER_RETURN_TYPE getGeocodeType() {
        return geocodeType;
    }

    @Override
    protected void onPreExecute() {
    }

    public TaskResult doInBackground(Object... param) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        List<Address> addresses = null;

        if (getGeocodeType() == GEOCODER_RETURN_TYPE.LOCATION) {
            try {
                String name = (String) param[0];
                Log.d("GeocodeAsyncTask", "address: "+name);
                addresses = geocoder.getFromLocationName(name, 1);
            } catch (Exception e) {
                Log.d("GeocodeAsyncTask", "Premature exception in GEOCODER_RETURN_TYPE.LOCATION");
                return new TaskResult(e);
            }
        } else if (getGeocodeType() == GEOCODER_RETURN_TYPE.ADDRESS) {
            try {
                LatLng latLng = (LatLng) param[0];
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                Log.d("GeocodeAsyncTask", "LatLon: "+latitude+" "+longitude);
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (Exception e) {
                Log.d("GeocodeAsyncTask", "Premature exception in GEOCODER_RETURN_TYPE.ADDRESS");
                return new TaskResult(e);
            }
        }
        if (addresses != null && addresses.size() > 0) {
            Log.d("GeocodeAsyncTask", "Got value and sending to post execute");
            return new TaskResult(addresses.get(0));
        } else {
            Log.d("GeocodeAsyncTask", "Finally got premature exception");
            return new TaskResult(new Exception("Unknown Error"));
        }
    }

    public void onPostExecute(TaskResult address) {
        super.onPostExecute(address);
//        if (address.isSuccess()) {
//        Address mAddress = (Address) address.getResult();
//        if (getGeocodeType() == GEOCODER_RETURN_TYPE.ADDRESS) {
//            String addressName = "";
//            for (int i = 0; i < mAddress.getMaxAddressLineIndex(); i++) {
//                addressName += " --- " + mAddress.getAddressLine(i);
//            }
//            Log.d("GeocodeAsyncTask", addressName);
//            getAdvancedAsyncTaskListener().onSuccess(addressName);
//        } else if (getGeocodeType() == GEOCODER_RETURN_TYPE.LOCATION) {
//            double latitude = mAddress.getLatitude();
//            double longitude = mAddress.getLongitude();
//            Log.d("GeocodeAsyncTask", "latitude: " + latitude + "longitude: " + longitude);
//            getAdvancedAsyncTaskListener().onSuccess(new LatLng(latitude, longitude));
//        }
//        }else{
//            getAdvancedAsyncTaskListener().onFailure(address.getError());
//        }
    }

    public static String getAddress(Address address){
        String addressText="";
        if (address.getMaxAddressLineIndex() >= 3) {
            addressText = String.format("%s, %s, %s", address.getAddressLine(0), address.getAddressLine(1),
                    address.getAddressLine(2));
        } else {
            addressText = String.format("%s, %s, %s",
                    address.getMaxAddressLineIndex() >= 0 ? address.getAddressLine(0) : "", address.getLocality(),
                    address.getCountryName());
        }
        return addressText;
    }
}