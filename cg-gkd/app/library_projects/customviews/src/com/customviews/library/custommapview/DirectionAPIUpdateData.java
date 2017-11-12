package com.customviews.library.custommapview;

import com.google.android.gms.maps.model.PolylineOptions;

public interface DirectionAPIUpdateData {

    public void getDirectionURLData(String directionData);

    public void getDistance(String distance);

    public void getDuration(String duration);

    public void getPolylineOptionsData(final PolylineOptions directionData);
}