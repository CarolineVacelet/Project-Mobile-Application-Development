package com.example.mobile_app_bank_account;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity2 extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        StrictMode.ThreadPolicy policy = new StrictMode.
                                        ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE); //to stock our data
        Button resfreshButton = (Button) findViewById(R.id.refreshButton);
        String stringValueUserID = ""; //User ID
        String checkbox = ""; //checkbox for online or offline mode
        Intent intent = getIntent();
        if (intent != null){
            if (intent.hasExtra("value")){ //check if there is a value associated to the key "value"
                stringValueUserID = intent.getStringExtra("value"); //we recover the value associated to the key
                checkbox = intent.getStringExtra("checkboxValue");//same here but for the checkbox

                        if(isNetworkAvailable() && checkbox.equals("0"))//user is connected to a network
                            // and have not checked the offline mode
                        {
                            try {
                                URL url = new URL("https://60102f166c21e10017050128.mockapi.io/labbbank/accounts/"+ stringValueUserID);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.connect();


                                //Creating a variable for editor to store data in shared preferences.
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                //If the server answers us with the OK code
                                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                    InputStream text = connection.getInputStream();
                                    if(text != null) {
                                        String contentAsString = readIt(text);
                                        TextView reponse = (TextView) findViewById(R.id.info);
                                        reponse.setText(contentAsString);

                                        //If we are in online mode, we store the data

                                        //to save data in shared prefs in the form of string.
                                        editor.putString("accounts"+ stringValueUserID, contentAsString);

                                        //to apply changes and save data in shared prefs.
                                        editor.apply();

                                    }
                                }

                                connection.disconnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            String finalStringValueUserID = stringValueUserID;
                            resfreshButton .setOnClickListener(new View.OnClickListener() { //if the user click on the refresh  button
                                @Override
                                public void onClick(View v) {
                                    try {
                                        URL url = new URL("https://60102f166c21e10017050128.mockapi.io/labbbank/accounts/"+ finalStringValueUserID);
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.connect();


                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                            InputStream text = connection.getInputStream();
                                            if(text != null) {
                                                String contentAsString = readIt(text);
                                                TextView reponse = (TextView) findViewById(R.id.info);
                                                reponse.setText(contentAsString);

                                                editor.putString("accounts"+ finalStringValueUserID, contentAsString);

                                                editor.apply();

                                            }
                                        }

                                    connection.disconnect();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                        });
                        }
                        if(!isNetworkAvailable() || checkbox.equals("1")) //if the user is not connected to a network
                            // or if he wants to be in offline mode
                        {
                            //If we are in offline mode, we load the data
                            String json = sharedPreferences.getString("accounts"+ stringValueUserID, null);
                            if(json == null)//If we don't have the data stored yet or if the user doesn't exists
                            {
                                json = "Could not find this account in offline mode";
                            }
                            TextView reponse = (TextView) findViewById(R.id.info);
                            reponse.setText(json);


                            String stringUrl = "Offline mode";
                            Context context = getApplicationContext();
                            CharSequence text = stringUrl;
                            int duration = Toast.LENGTH_LONG;
                            Toast.makeText(context, text, duration).show();


                            resfreshButton .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) { //We cannot update the account because in offline mode
                                    //we are using the last version of stored data

                                    String stringUrl = "Cannot refresh because you're offline";
                                    Context context = getApplicationContext();
                                    CharSequence text = stringUrl;
                                    int duration = Toast.LENGTH_LONG;
                                    Toast.makeText(context, text, duration).show();
                                }
                            });

                        }

            }

        }
    }


    private Boolean isNetworkAvailable() //To check if we are connected to a network
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


   public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        return builder.toString();
    }


}




