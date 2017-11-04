package com.example.talalrashid.ride;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

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
import java.util.List;

/**
 * Created by Hamza Imtiaz on 10/28/2017.
 */

public class lat_lon_entry extends AsyncTask<List<LatLng>, Void, String> {

    Context context;
    String ip=null;
    int entries;
    LatLng origin, dest;
    SharedPreferences sharedPref;
    public static final String MyPREF="UserDetails";
    lat_lon_entry(Context ctx,int entries,LatLng origin, LatLng dest) {
        this.context = ctx;
        this.entries=entries;
        this.origin=origin;
        this.dest=dest;
    }

    @Override
    protected String doInBackground(List<LatLng>... params) {
        StringBuffer all_lat_long= new StringBuffer();
        sharedPref=context.getSharedPreferences(MyPREF,Context.MODE_PRIVATE);
        ip=sharedPref.getString("ip","");
        String ride_book_uri=ip+"lat_long_entry.php";

        for(int i=0;i<params[0].size();i++)
        {
            all_lat_long.append(params[0].get(i).toString());
            all_lat_long.append(",");
        }
       String all_lat_long_1=all_lat_long.toString();
        String entry= Integer.toString(entries);

        try {

            URL url = new URL(ride_book_uri);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("POST");
            connect.setDoOutput(true);
            connect.setDoInput(true);
            OutputStream os = connect.getOutputStream();
            BufferedWriter bff = new BufferedWriter(new OutputStreamWriter(os));
            String source_points=origin.toString();
            String dest_points=dest.toString();

            String post_data =
                    URLEncoder.encode("entries", "UTF-8") + "=" + URLEncoder.encode(entry, "UTF-8") + "&" + URLEncoder.encode("lat_long", "UTF-8") + "=" + URLEncoder.encode(all_lat_long_1, "UTF-8")
                            + "&" + URLEncoder.encode("source_points", "UTF-8") + "=" + URLEncoder.encode(source_points, "UTF-8")+ "&" + URLEncoder.encode("dest_points", "UTF-8") + "=" + URLEncoder.encode(dest_points, "UTF-8");


            bff.write(post_data);
            bff.flush();
            bff.close();
            os.close();

            InputStream is = connect.getInputStream();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));

            String result = "", line;

            while ((line = bfr.readLine()) != null) {

                result += line;
            }

            bfr.close();
            is.close();
            connect.disconnect();

            return result;


        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    protected void onPreExecute() {

    }

    protected void onPostExecute(String result) {
        // CheckBooking(result);
    }

    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

