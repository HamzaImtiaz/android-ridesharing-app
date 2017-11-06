package com.example.talalrashid.ride;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.AsyncTask;
import android.content.SharedPreferences;
import android.content.Context;
import android.provider.Settings;
import android.widget.Toast;

public class SplashScreen extends Activity {

    SharedPreferences sharedPref;
    public static final String MyPREF="UserDetails";

    public class Screen extends AsyncTask<String, Integer, String> {


        Context context;

        Screen(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            sharedPref = getSharedPreferences(MyPREF, Context.MODE_PRIVATE);

            for (int i = 1; i <= 10; i++) {
                try {

                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("ip", "http://192.168.15.126/Ride/");
            editor.commit();
            sharedPref = getSharedPreferences(MyPREF, Context.MODE_PRIVATE);
            for (int i = 11; i <= 20; i++) {
                try {

                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String id = sharedPref.getString("email", "");
            for (int i = 21; i <= 30; i++) {
                try {

                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String pass = sharedPref.getString("password", "");


            if (id.equals("") && pass.equals("")) {

                return "No User logged in";
            } else {
                return "Found User " + id;
            }

        }


        protected void onPreExecute() {

            String first = "Checking User session";


        }

        protected void onPostExecute(String result) {


            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (result.equals("No User logged in")) {

                Toast.makeText(context, "No user Connected", Toast.LENGTH_SHORT)
                        .show();
                Intent i = new Intent(SplashScreen.this, Login.class);
                startActivity(i);
            } else {
                Intent go = new Intent(context, MainActivity.class);
                startActivity(go);
            }
            finish();
        }

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
    }

        // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_screen);

        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish ();
            System.exit(0);
        }
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    init();


                    // close this activity
                    //  finish();
                }
            }, SPLASH_TIME_OUT);


    }
        private void init() {


                Screen ss = new Screen(this);
                ss.execute();


        }
}