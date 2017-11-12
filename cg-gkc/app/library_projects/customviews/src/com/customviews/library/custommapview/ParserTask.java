package com.customviews.library.custommapview;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.customviews.library.utils.AllSettingsManager;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rashed on 4/19/2016.
 */
public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

    DirectionAPIUpdateData parserCallBack;
    Context mContext;
    int pathColor=-1;

    public ParserTask(Context context,int pathColor, DirectionAPIUpdateData parserCallBack) {
        super();
        mContext = context;
        this.pathColor=pathColor;
        this.parserCallBack = parserCallBack;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try{
            if(!AllSettingsManager.isNullOrEmpty(jsonData[0])){
                parserCallBack.getDirectionURLData(jsonData[0]);
                jObject = new JSONObject(jsonData[0]);

                // Starts parsing data
                routes = MapUtilManager.parse(jObject);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//        parserCallBack.onUpdateData(result);


        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();
        String distance = "";
        String duration = "";



        if(result.size()<1){
            Toast.makeText(mContext, "No Points", Toast.LENGTH_SHORT).show();
            return;
        }


        // Traversing through all the routes
        for(int i=0;i<result.size();i++){
            points = new ArrayList<LatLng>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                if(j==0){	// Get distance from the list
                    distance = (String)point.get("distance");
                    continue;
                }else if(j==1){ // Get duration from the list
                    duration = (String)point.get("duration");
                    continue;
                }

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(2);
            lineOptions.color(pathColor);

        }

        if(!AllSettingsManager.isNullOrEmpty(distance)){
            parserCallBack.getDistance(distance);
        }

        if(!AllSettingsManager.isNullOrEmpty(duration)){
            parserCallBack.getDuration(duration);
        }

        if(lineOptions!=null){
            parserCallBack.getPolylineOptionsData(lineOptions);
        }
    }
}