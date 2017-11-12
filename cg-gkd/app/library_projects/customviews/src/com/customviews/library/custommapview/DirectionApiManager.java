package com.customviews.library.custommapview;

import android.content.Context;
import android.graphics.Color;

import com.customviews.library.R;
import com.customviews.library.utils.AllSettingsManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by Rashed on 4/19/2016.
 */
public class DirectionApiManager {

    static DirectionApiManager directionApiManager = null;

    private DirectionApiManager() {
    }

    public static DirectionApiManager getInstance() {
        if (directionApiManager != null) {
            return directionApiManager;
        } else {
            return new DirectionApiManager();
        }
    }


    public DirectionApiManager getDirectionApiUpdate(final Context context, ArrayList<LatLng> locations, MapUtilManager.MOOD mood, MapUtilManager.MAP_DISTANCE_UNIT distanceUnit, final int color, final DirectionAPIUpdateData update) {

        if (locations != null && locations.size() == 2) {
            // Getting URL to the Google Directions API
            String url = MapUtilManager.getDirectionsUrl(locations.get(0), locations.get(1), mood,distanceUnit);
            DownloadTask downloadTask = new DownloadTask(context, new MapDataUpdate<String>() {
                @Override
                public void onUpdateData(String data) {
                    if(!AllSettingsManager.isNullOrEmpty(data)){
                        ParserTask parserTask=new ParserTask(context, color, new DirectionAPIUpdateData() {
                            @Override
                            public void getDirectionURLData(String directionData) {
                                update.getDirectionURLData(directionData);
                            }

                            @Override
                            public void getDistance(String distance) {
                                update.getDistance(distance);
                            }

                            @Override
                            public void getDuration(String duration) {
                                update.getDuration(duration);
                            }

                            @Override
                            public void getPolylineOptionsData(PolylineOptions directionData) {
                                update.getPolylineOptionsData(directionData);
                            }
                        });
                        parserTask.execute(data);
                    }
                }
            });
            downloadTask.execute(url);

        }


        return getInstance();
    }
}
