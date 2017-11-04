package com.example.talalrashid.ride;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

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
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Hamza Imtiaz on 10/26/2017.
 */

public class BookingRideController extends AsyncTask<String, Void, String> {

    Context context;
    String ip=null;
    SharedPreferences sharedPref;
    public static final String MyPREF="UserDetails";
    BookingRideController(Context ctx) {
        this.context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {

        sharedPref=context.getSharedPreferences(MyPREF,Context.MODE_PRIVATE);
        ip=sharedPref.getString("ip","");
        String ride_book_uri=ip+"booking.php";
        String src = params[0];
        String dest = params[1];
        String source_lat= params[2];
        String source_long= params[3];
        String dest_lat= params[4];
        String dest_long= params[5];
        String car = params[6];
        String seats = params[7];
        String tripTime = params[8];
        String emailTemp = params[9];
        String entry = params[10];

        try {

            URL url = new URL(ride_book_uri);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setRequestMethod("POST");
            connect.setDoOutput(true);
            connect.setDoInput(true);
            OutputStream os = connect.getOutputStream();
            BufferedWriter bff = new BufferedWriter(new OutputStreamWriter(os));

            String post_data =
                    URLEncoder.encode("src", "UTF-8") + "=" + URLEncoder.encode(src, "UTF-8") + "&" + URLEncoder.encode("dest", "UTF-8") + "=" + URLEncoder.encode(dest, "UTF-8") + "&" + URLEncoder.encode("source_lat", "UTF-8") + "=" + URLEncoder.encode(source_lat, "UTF-8") + "&" + URLEncoder.encode("source_long", "UTF-8") + "=" + URLEncoder.encode(source_long, "UTF-8") + "&" + URLEncoder.encode("dest_lat", "UTF-8") + "=" + URLEncoder.encode(dest_lat, "UTF-8") + "&" + URLEncoder.encode("dest_long", "UTF-8") + "=" + URLEncoder.encode(dest_long, "UTF-8") + "&" + URLEncoder.encode("emailTemp", "UTF-8") + "=" + URLEncoder.encode(emailTemp, "UTF-8") + "&" + URLEncoder.encode("car", "UTF-8") + "=" + URLEncoder.encode(car, "UTF-8")+ "&" + URLEncoder.encode("seats", "UTF-8") + "="
                            + URLEncoder.encode(seats, "UTF-8") + "&" + URLEncoder.encode("tripTime", "UTF-8") + "=" + URLEncoder.encode(tripTime, "UTF-8")+ "&" + URLEncoder.encode("entry", "UTF-8") + "=" + URLEncoder.encode(entry, "UTF-8");


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
