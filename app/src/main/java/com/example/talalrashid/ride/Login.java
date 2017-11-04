package com.example.talalrashid.ride;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

public class Login extends AppCompatActivity {

    EditText loginField, passwordField;
    SharedPreferences sharedPref;
    public static final String MyPREF="UserDetails";
    user currUser;
    String ip;


    public class LoginController extends AsyncTask<String, Void, String> {

        Context context;

        LoginController(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_uri = ip+"login.php";
            String userN=params[0];
            String passW=params[1];

            try {
                URL url = new URL(login_uri);
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                connect.setRequestMethod("POST");
                connect.setDoOutput(true);
                connect.setDoInput(true);
                OutputStream os = connect.getOutputStream();
                BufferedWriter bff = new BufferedWriter(new OutputStreamWriter(os));
                String post_data = URLEncoder.encode("userN", "UTF-8") + "=" + URLEncoder.encode(userN, "UTF-8")
                        + "&" + URLEncoder.encode("passW", "UTF-8") + "=" + URLEncoder.encode(passW, "UTF-8");
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
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
                Log.e("MYAPP", "exception", e);
            }


            return null;
        }


        protected void onPreExecute() {

        }

        protected void onPostExecute(String result) {
            RedirectToLogin(result);
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("test", "hello3");
        //checkSession();
        init();
    }

    private void checkSession() {

        //sharedPref=getSharedPreferences(MyPREF,Context.MODE_PRIVATE);

        String id=sharedPref.getString("id","");
        String pass=sharedPref.getString("pass","");

        if(id.equals("") && pass.equals("")){
            init();
        }

        else
        {
            ReturnLoginView();
        }
    }

    private void init() {
        currUser=null;
        loginField=(EditText) findViewById(R.id.userID);
        passwordField=(EditText) findViewById(R.id.passID);
        sharedPref=getSharedPreferences(MyPREF,Context.MODE_PRIVATE);
        ip=sharedPref.getString("ip","");
    }

    public void LoginAccount(View v){

        String userName=loginField.getText().toString();
        String passWord=passwordField.getText().toString();

        if(!userName.equals("") && !passWord.equals("")){

            LoginController ww= new LoginController(this);
            ww.execute(userName, passWord);
           // Log.d("test", "loginAccount");
        }

        else {
            showMessage("Please type in Username/Password");
        }
    }

    public void SignUp(View v){

        Intent gotoContact=new Intent(getApplicationContext(),signup.class);
        startActivity(gotoContact);
    }

    public void RedirectToLogin(String result) {

        if (result == null) {
            showMessage("Failed to connect to the server");
            Log.d("test", "login failed to connect server");
        }
        else {

            if (result.equals("No")) {
                Log.d("test", "login no account found");
                showMessage("No Such account found");
            } else {
                Log.d("test", "login json object");


                Toast toast = Toast.makeText(this, "Login Successfull", Toast.LENGTH_SHORT);
                toast.show();
                String json_string = result;
                JSONObject jsonObject;
                JSONArray jsonArray;
                String email=null,password=null;


                try {
                    jsonObject = new JSONObject(json_string);
                    jsonArray = jsonObject.getJSONArray("server_response");
                    int count = 0;


                    while (count < jsonArray.length()) {

                        JSONObject jo = jsonArray.getJSONObject(count);


                        email = jo.getString("email");
                        password = jo.getString("password");



                        currUser = new user(email, password);
                        count++;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SessionSave();
                ReturnLoginView();
            }
        }
    }

    private void ReturnLoginView() {
        Log.d("test", "to main view");

        Intent gotoContact=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(gotoContact);
       finish();
    }

    private void SessionSave() {

        sharedPref=getSharedPreferences(MyPREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPref.edit();
        editor.putString("email", currUser.getEmail());
        editor.putString("password", currUser.getPassword());
        editor.commit();
    }

    private void showMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        super.onKeyDown(keyCode,event);
        //replaces the default 'Back' button action
        if(keyCode==KeyEvent.KEYCODE_BACK)   {


            new AlertDialog.Builder(Login.this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")

                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            Toast.makeText(getApplicationContext(), "Clicked: " + "back button",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), SplashScreen.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            startActivity(intent);
                        }
                    }).show();
            //  finish();
        }
        return true;
    }
    public class available_ride_controller extends AsyncTask<String, Void, String> {

        Context context;

        available_ride_controller(Context ctx) {
            this.context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_uri = ip+"login.php";
            String userN=params[0];
            String passW=params[1];

            try {
                URL url = new URL(login_uri);
                HttpURLConnection connect = (HttpURLConnection) url.openConnection();
                connect.setRequestMethod("POST");
                connect.setDoOutput(true);
                connect.setDoInput(true);
                OutputStream os = connect.getOutputStream();
                BufferedWriter bff = new BufferedWriter(new OutputStreamWriter(os));
                String post_data = URLEncoder.encode("userN", "UTF-8") + "=" + URLEncoder.encode(userN, "UTF-8")
                        + "&" + URLEncoder.encode("passW", "UTF-8") + "=" + URLEncoder.encode(passW, "UTF-8");
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
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
                Log.e("MYAPP", "exception", e);
            }


            return null;
        }


        protected void onPreExecute() {

        }

        protected void onPostExecute(String result) {
            RedirectToLogin(result);
        }

        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}