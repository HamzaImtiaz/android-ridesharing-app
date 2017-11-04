package com.example.talalrashid.ride;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class searchRides extends AppCompatActivity {
    ListView rideList;
    search_ride_adapter adapter;
    SwipeRefreshLayout swiper;
    AlertDialog alertDialog;
    String ip;
    Ride_class currRide;
    ArrayList<Ride_class> allRides;
    SharedPreferences sharedPref;
    public String result=null;
    public static final String MyPREF="UserDetails";
    public String searched_place=null; ;


    public class GetAvailableRide extends AsyncTask<String, Void, String> {

        Context context;

        GetAvailableRide(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String ride_uri = ip+"getAvailableRide.php";
            String place =params[0];
            String result1 = "", line;

            try {
                URL url = new URL(ride_uri);
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                connect.setRequestMethod("POST");
                connect.setDoOutput(true);
                connect.setDoInput(true);


                OutputStream os = connect.getOutputStream();
                BufferedWriter bff = new BufferedWriter(new OutputStreamWriter(os));
                String post_data =
                        URLEncoder.encode("place", "UTF-8") + "=" + URLEncoder.encode(place, "UTF-8");

                bff.write(post_data);
                bff.flush();
                bff.close();
                os.close();

                InputStream is = connect.getInputStream();
                BufferedReader bfr = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));





                while ((line = bfr.readLine()) != null) {

                    result1 += line;
                }

                bfr.close();
                is.close();
                connect.disconnect();



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return result1;
        }


        protected void onPreExecute() {

        }

        protected void onPostExecute(String result) {
            RedirectToGetRides(result);
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


    public void RedirectToGetRides(String result) {

        if (result == null) {
            showMessage("Failed to connect to the server");
        }
        else {

            if (result.equals("No")) {

                showMessage("No Rides Available");
            } else {

                allRides=new ArrayList<Ride_class>();
                String json_string = result;
                JSONObject jsonObject;
                JSONArray jsonArray;
                String entry_no=null,destination_points=null;
                List<LatLng> all_points=null;
                Geocoder g;
                List<Address> search_address_list=null;
                Address search_address=null;


                try {
                    jsonObject = new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    int count = 0;


                    while (count < jsonArray.length()) {

                        JSONObject jo = jsonArray.getJSONObject(count);

                        entry_no = jo.getString("entry_no");
                        destination_points = jo.getString("dest_point");
                        g = new Geocoder(getBaseContext(), Locale.getDefault());
                        ssearch_address_list = g.getFromLocationName(searched_place, 1);

                        search_address = search_address_list.get(0);

                        double search_Lat = search_address.getLatitude();
                        double search_Lng = search_address.getLongitude();
                        LatLng search_latlng = new LatLng(search_Lat, search_Lng);
                        if(search_latlng.toString().equals(destination_points))
                        {
                            currRide = new Ride_class(entry_no,destination_points);
                            allRides.add(currRide);

                        }
                        else if()
                        count++;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
        LoadRides();
    }

    public void LoadRides(){

        adapter=new search_ride_adapter(this,allRides);
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
      //  swiper.setRefreshing(false);
    }

    private void showMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    public void init() {

        sharedPref=getSharedPreferences(MyPREF,Context.MODE_PRIVATE);
        ip=sharedPref.getString("ip","");
        // Screen ss = new Screen(this);
        //ss.execute();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searched_place= getIntent().getExtras().getString("Place","");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();
        GetAvailableRide abc= new GetAvailableRide(getBaseContext());
        abc.execute(searched_place);
    }
}
