package com.reversecoder.gk7.customer.model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by rashed on 4/6/16.
 */
public class MapMarkerWithDetails {

    private LatLng latLng = new LatLng(0.0, 0.0);
    private Object object = new Object();

    public MapMarkerWithDetails(LatLng latLng, Object object) {
        this.latLng = latLng;
        this.object = object;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
