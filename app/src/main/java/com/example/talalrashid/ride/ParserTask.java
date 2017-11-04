package com.example.talalrashid.ride;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.*;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;

/**
 * Created by Hamza Imtiaz on 10/26/2017.
 */

public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    private GoogleMap mMap;
    SharedPreferences sharedPref;
    Context ctx;
    List<LatLng> diff_point=null;
    int entries;
    LatLng origin, dest;
    public static final String MyPREF="UserDetails";

    // Parsing the data in non-ui thread

    public ParserTask(Context ctx, GoogleMap map,List<LatLng> points,int entries,LatLng origin, LatLng dest)
    {
        this.ctx=ctx;
        this.mMap=map;
        this.diff_point=points;
        this.entries=entries;
        this.origin=origin;
        this.dest=dest;

    }
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);

            DirectionsJSONParser parser = new DirectionsJSONParser(ctx,mMap);

            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList points = null;
        LatLng m_points=null;
        int count=0;
        PolylineOptions lineOptions = null;

        MarkerOptions markerOptions = new MarkerOptions();

        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList();
            lineOptions = new PolylineOptions();

            List<HashMap<String, String>> path = result.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                if(count%8==0)
                diff_point.add(position);
                count++;

                points.add(position);
            }

            lineOptions.addAll(points);
            lineOptions.width(25);
            lineOptions.color(Color.GREEN);
            lineOptions.geodesic(true);

        }

        drawMarkers(diff_point);
        add_entry(diff_point);
// Drawing polyline in the Google Map for the i-th route
        mMap.addPolyline(lineOptions);
    }

    /**
     * A method to download json data from url
     */

    public void add_entry(List<LatLng> diff_points)
    {
        lat_lon_entry obj= new lat_lon_entry(ctx,entries,origin,dest);
        obj.execute(diff_point);
    }
    public void drawMarkers(List<LatLng> diff_points)
    {
        for (int i = 0; i < diff_points.size(); i++)
        {
            Marker Custom = mMap.addMarker(new MarkerOptions()
                    .position(diff_points.get(i)).title("Here is the road location")
                    .snippet("Hon the lads"));
        }
    }
}

