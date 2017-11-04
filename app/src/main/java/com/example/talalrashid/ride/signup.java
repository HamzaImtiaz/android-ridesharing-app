package com.example.talalrashid.ride;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import java.util.Random;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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

public class signup extends AppCompatActivity {


    EditText user,pass,fulln,em,ph,addd,city;
    private static final int SELECTED_PICTURE = 1;
    SharedPreferences sharedPref;
    public static final String MyPREF="UserDetails";
    ImageView contactPhoto;
    String ip;
    String id;


    public void signup(View view) {
        Addit();
    }

    public class SignupController extends AsyncTask<String, Void, String> {

        Context context;

        SignupController(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String signup_uri=ip+"signup.php";
            String userN = params[0];
            String passW = params[1];
            String fullN = params[2];
            String email = params[3];
            String mobile = params[4];
            String postal = params[5];
            String city = params[6];

            try {

                URL url = new URL(signup_uri);
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                connect.setRequestMethod("POST");
                connect.setDoOutput(true);
                connect.setDoInput(true);
                OutputStream os = connect.getOutputStream();
                BufferedWriter bff = new BufferedWriter(new OutputStreamWriter(os));

                String post_data =
                        URLEncoder.encode("userN", "UTF-8") + "=" + URLEncoder.encode(userN, "UTF-8")
                                + "&" + URLEncoder.encode("passW", "UTF-8") + "=" + URLEncoder.encode(passW, "UTF-8")
                                + "&" + URLEncoder.encode("phoneN", "UTF-8") + "=" + URLEncoder.encode(mobile, "UTF-8")
                                + "&" + URLEncoder.encode("eMail", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8")
                                + "&" + URLEncoder.encode("fullName", "UTF-8") + "=" + URLEncoder.encode(fullN, "UTF-8")
                                + "&" + URLEncoder.encode("addresss", "UTF-8") + "=" + URLEncoder.encode(postal, "UTF-8")
                                + "&" + URLEncoder.encode("city", "UTF-8") + "=" + URLEncoder.encode(city, "UTF-8");

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
            CheckSignup(result);
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    public class sendemail extends AsyncTask<String, Void, String> {

        Context context;

        sendemail(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            Random rand = new Random();
            id = String.format("%04d", rand.nextInt(10000));
            Log.e("SendMail", id);
            try {
                emailsender sender = new emailsender("campusridenu@gmail.com", "3366997809x");
                sender.sendMail("Authentication Code",
                        "Your Code is :"+id,
                        "campusridenu@gmail.com",
                        em.getText().toString());
            }
            catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }

            return null;


    }

        protected void onPreExecute() {

        }

        protected void onPostExecute(String result) {
            Toast.makeText(signup.this, "email sent", Toast.LENGTH_LONG).show();
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nahal_activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.n_menu, menu);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();


        boolean Choice=sharedPref.getBoolean("notifSett",false);

       // if (Choice)
       //     startService(new Intent(this, Notifier.class));
    }

    private void init(){

        sharedPref=getSharedPreferences(MyPREF,Context.MODE_PRIVATE);
        ip=sharedPref.getString("ip","");
        user=(EditText) findViewById(R.id.usser);
        pass=(EditText) findViewById(R.id.passs);
        fulln=(EditText) findViewById(R.id.fulln);
        em=(EditText) findViewById(R.id.email);
        ph=(EditText) findViewById(R.id.nmber);
        addd=(EditText) findViewById(R.id.addr);
        city=(EditText) findViewById(R.id.city);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.addit) {
            Addit();
        }

        return super.onOptionsItemSelected(item);
    }

    private void Addit() {

        if (user.getText().toString().equals("") || pass.getText().toString().equals("") || fulln.getText().toString().equals("") || em.getText().toString().equals("") || ph.getText().toString().equals("") || addd.getText().toString().equals("") || city.getText().toString().equals("")){

            showMessage("Please fill out the details first");
        }
        else if(!em.getText().toString().contains("@lhr.nu.edu.pk"))
        {
            showMessage("Please enter your NU email");
        }
        else {

            SignupController ss=new SignupController(this);
            ss.execute(user.getText().toString(), pass.getText().toString(),fulln.getText().toString(),em.getText().toString(), ph.getText().toString(), addd.getText().toString(), city.getText().toString());
        }

    }

    public void CheckSignup(String result){
        Log.d("test", "checksignup");


        if(result==null){
            Log.d("test", "failedtoserver");
            showMessage("Failed to connect to the server");
        }
        else {
            if(result.equals("-1")){
                Log.d("test", "already");
                showMessage("Sorry, username Already exists!!");
            }

            else if(result.equals("1")){
                Log.d("test", "succ");
                showMessage("Successfully registered!!");
               sendemail obj = new sendemail(signup.this);
                obj.execute();
                transferToLogin();
                finish();
            }

            else if(result.equals("0")){
                Log.d("test", "internal");
                showMessage("Sign up failed. Internal issue!!");
            }

        }
    }


    private void transferToLogin(){


        Intent loginScreen=new Intent(this,Login.class);
         startActivity(loginScreen);
    }


    private void showMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }


}

