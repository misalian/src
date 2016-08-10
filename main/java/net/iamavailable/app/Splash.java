package net.iamavailable.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.iamavailable.app.data.CheckUpdateService;
import net.iamavailable.app.data.DataService;
import net.iamavailable.app.data.GetCurrentLocation;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;


public class Splash extends Activity {
    public static double accuracy_threshhold=11.0;
    int count=0;
    TextView tv;
    NetworkInfo mMobile,mWifi;
    ConnectivityManager connManager;
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    public static final String TAG = "IAA";
    boolean isGPSEnable=false;
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static double longitude = 0.0, lattitude = 0.0;
    private static String userName = null, cityName = null, countryName = null, id=null;
    SharedPreferences myPref;
    LocationManager locationManager;
    Handler mHandler = new Handler();
    BroadcastReceiver receiverSignInRequired = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().getParcelable("resolution") != null) {
                PendingIntent resolution = intent.getExtras().getParcelable("resolution");
                try {
                    startIntentSenderForResult(resolution.getIntentSender(), REQUEST_CODE_SIGN_IN, null, 0, 0, 0);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    };
    BroadcastReceiver receiverGetDisplayName = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Connected: Display Name: " + intent.getStringExtra("displayName"));
            userName = intent.getStringExtra("displayName");
            id=intent.getStringExtra("id");
            updateDisplay(userName, longitude, lattitude);
        }
    };
    BroadcastReceiver receiverCurrentLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, " Splash::LocationReceived Long:" + intent.getDoubleExtra("longitude", 0.0) + ", Lat:" + intent.getDoubleExtra("latitude", 0.0));
            countryName = intent.getStringExtra("country");
            cityName = intent.getStringExtra("city");
            longitude = intent.getDoubleExtra("longitude", 0.0);
            lattitude = intent.getDoubleExtra("latitude", 0.0);
            Log.i(TAG, "SPLASH:INFO:: Name:" + userName + " LNG:" + longitude + " LAT:" + lattitude);

            SharedPreferences.Editor editor = myPref.edit();
            editor.putString("CITY", cityName);
            editor.putString("COUNTRY", countryName);
            editor.putString("LNG", Double.toString(longitude));
            editor.putString("LAT", Double.toString(lattitude));
            editor.apply();
           // updateDisplay(userName, longitude, lattitude);
            if(myPref.getString("COUNTRY", " ").equals(" ")||myPref.getString("LNG","0.0").equals("0.0")||
                    myPref.getString("LAT","0.0").equals("0.0")||GetCurrentLocation.accuracy>accuracy_threshhold){
                tv = (TextView) findViewById(R.id.textView6);
                tv.setText("GETTING LOCATION, PLEASE WAIT... "+ GetCurrentLocation.accuracy);

                requestCurrentLocation();
            }
        }
    };
    private ImageView progress;

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, " Registering Receiver in Splash");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverGetDisplayName, new IntentFilter(DataService.BROADCAST_GET_DISPLAY_NAME));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverSignInRequired, new IntentFilter(DataService.BROADCAST_SIGN_IN_REQUIRED));
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverCurrentLocation, new IntentFilter(DataService.BROADCAST_CURRENT_LOCATION));
        Log.i(TAG, " Got Registered Receiver in Splash");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverSignInRequired);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverGetDisplayName);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverCurrentLocation);
        Log.i(TAG, " Unregistered Receiver in Splash");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.content_splash);
            Log.i(TAG, " Requesting Display Name ");
            myPref = getSharedPreferences(IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            locationManager=locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnable=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            SharedPreferences.Editor editor = myPref.edit();
            editor.putString("COUNTRY"," ");
            editor.apply();

            connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            // requestDisplayName();
            Log.i(TAG, " Requesting Current Location ");


            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 221);
            }
            requestCurrentLocation();
            //setContentView(R.layout.content_splash);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                   if(myPref.getString("id"," ")!=" "){
                      // if(mWifi.isConnected()||mMobile.isConnectedOrConnecting()) {

                          // if(hasInternetAccess()) {
                               if (!isGPSEnable) {
                                   //showSettingsAlert();
                                   Toast.makeText(getApplicationContext(),"Please turn on your location service",Toast.LENGTH_LONG).show();
                                   count=0;
                                   new Thread(new Runnable() {
                                       @Override
                                       public void run() {
                                           // TODO Auto-generated method stub
                                           while (true) {
                                               try {
                                                   Thread.sleep(1000);
                                                   mHandler.post(new Runnable() {

                                                       @Override
                                                       public void run() {
                                                           // TODO Auto-generated method stub
                                                           if (mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                                                               if(isGPSEnable&&count==0){
                                                                   count=1;
                                                                   requestCurrentLocation();
                                                               }
                                                                   if (isGPSEnable && (myPref.getString("COUNTRY", " ") != " ")) {
                                                                       count = 1;
                                                                       mHandler = null;
                                                                       Intent mainIntent = new Intent(Splash.this, MainMenuActivity.class);
                                                                       startActivity(mainIntent);
                                                                       Splash.this.finish();
                                                                   }
                                                                  // requestCurrentLocation();
                                                                   tv = (TextView) findViewById(R.id.textView6);
                                                               if(!isGPSEnable){
                                                                   tv.setText("Turn on GPS");
                                                               }else {
                                                                   tv.setText("GETTING LOCATION, PLEASE WAIT... "+ GetCurrentLocation.accuracy);
                                                               }
                                                                   tv.setVisibility(View.VISIBLE);
                                                                   isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                                                           }else{
                                                               tv=(TextView) findViewById(R.id.textView6);
                                                               tv.setText("Check internet connection");
                                                               tv.setVisibility(View.VISIBLE);
                                                           }
                                                           mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                                           mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                                                       }
                                                   });
                                               } catch (Exception e) {
                                                   // TODO: handle exception
                                               }
                                           }
                                       }
                                   }).start();

                                   //Intent mainIntent = new Intent(Splash.this, Login.class);
                                   //startActivity(mainIntent);
                                   //Splash.this.finish();
                               } else {
                                   new Thread(new Runnable() {
                                       @Override
                                       public void run() {
                                           // TODO Auto-generated method stub
                                           while (true) {
                                               try {
                                                   Thread.sleep(1000);
                                                   mHandler.post(new Runnable() {

                                                       @Override
                                                       public void run() {
                                                           // TODO Auto-generated method stub
                                                           if (mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                                                               if (isGPSEnable && (myPref.getString("COUNTRY", " ") != " ") && (mWifi.isConnected() || mMobile.isConnectedOrConnecting())) {
                                                                   mHandler = null;
                                                                   Intent mainIntent = new Intent(Splash.this, MainMenuActivity.class);
                                                                   startActivity(mainIntent);
                                                                   Splash.this.finish();
                                                               }
                                                              // requestCurrentLocation();
                                                               tv = (TextView) findViewById(R.id.textView6);
                                                               if(!isGPSEnable){
                                                                   tv.setText("Turn on GPS");
                                                               }else {
                                                                   tv.setText("GETTING LOCATION, PLEASE WAIT... "+ GetCurrentLocation.accuracy);
                                                               }
                                                               tv.setVisibility(View.VISIBLE);
                                                               isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                                           }
                                                           else{
                                                               tv=(TextView) findViewById(R.id.textView6);
                                                               tv.setText("Check internet connection");
                                                               tv.setVisibility(View.VISIBLE);
                                                           }
                                                           mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                                           mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                                                       }
                                                   });
                                               } catch (Exception e) {
                                                   // TODO: handle exception
                                               }
                                           }
                                       }
                                   }).start();
                                   /*
                                   Intent mainIntent = new Intent(Splash.this, MainMenuActivity.class);
                                   startActivity(mainIntent);
                                   Splash.this.finish();*/
                               }
                           /*}else{
                               Toast.makeText(getApplicationContext(),"Connection weak try again",Toast.LENGTH_LONG).show();
                           }*/
                      /* }else{
                           tv=(TextView) findViewById(R.id.textView6);
                           tv.setText("Check internet connection, try again.");
                           tv.setVisibility(View.VISIBLE);                       }*/

                    }else {
                       //if(mWifi.isConnected()||mMobile.isConnectedOrConnecting()) {
                           if (!isGPSEnable) {
                               //showSettingsAlert();
                               Toast.makeText(getApplicationContext(),"Please turn on your location service",Toast.LENGTH_LONG).show();
                               count=0;
                               requestCurrentLocation();
                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       // TODO Auto-generated method stub
                                       while (true) {
                                           try {
                                               Thread.sleep(1000);
                                               mHandler.post(new Runnable() {

                                                   @Override
                                                   public void run() {
                                                       // TODO Auto-generated method stub
                                                       if (mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                                                           if(isGPSEnable&&count==0){
                                                               count=1;
                                                               requestCurrentLocation();
                                                           }
                                                       if (isGPSEnable && (myPref.getString("COUNTRY", " ") != " ")) {
                                                           mHandler = null;
                                                           count = 1;
                                                           Intent mainIntent = new Intent(Splash.this, Login.class);
                                                           startActivity(mainIntent);
                                                           Splash.this.finish();
                                                       }
                                                       //requestCurrentLocation();
                                                       tv = (TextView) findViewById(R.id.textView6);
                                                           if(!isGPSEnable){
                                                               tv.setText("Turn on GPS");
                                                           }else {
                                                               tv.setText("GETTING LOCATION, PLEASE WAIT... "+ GetCurrentLocation.accuracy);
                                                           }
                                                       tv.setVisibility(View.VISIBLE);
                                                       isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                                       }
                                                       else{
                                                           tv=(TextView) findViewById(R.id.textView6);
                                                           tv.setText("Check internet connection");
                                                           tv.setVisibility(View.VISIBLE);
                                                       }
                                                       mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                                       mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                                                   }
                                               });
                                           } catch (Exception e) {
                                               // TODO: handle exception
                                           }
                                       }
                                   }
                               }).start();

                               //Intent mainIntent = new Intent(Splash.this, Login.class);
                               //startActivity(mainIntent);
                               //Splash.this.finish();
                           } else {
                               new Thread(new Runnable() {
                                   @Override
                                   public void run() {
                                       // TODO Auto-generated method stub
                                       while (true) {
                                           try {
                                               Thread.sleep(1000);
                                               mHandler.post(new Runnable() {

                                                   @Override
                                                   public void run() {
                                                       // TODO Auto-generated method stub
                                                       if (mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                                                       if (isGPSEnable && (myPref.getString("COUNTRY", " ") != " ")) {
                                                           mHandler = null;
                                                           count = 1;
                                                           Intent mainIntent = new Intent(Splash.this, Login.class);
                                                           startActivity(mainIntent);
                                                           Splash.this.finish();
                                                       }
                                                      // requestCurrentLocation();
                                                       tv = (TextView) findViewById(R.id.textView6);
                                                           if(!isGPSEnable){
                                                               tv.setText("Turn on GPS");
                                                           }else {
                                                               tv.setText("GETTING LOCATION, PLEASE WAIT... "+ GetCurrentLocation.accuracy);
                                                           }
                                                       tv.setVisibility(View.VISIBLE);
                                                       isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                                       }
                                                       else{
                                                           tv=(TextView) findViewById(R.id.textView6);
                                                           tv.setText("Check internet connection");
                                                           tv.setVisibility(View.VISIBLE);
                                                       }
                                                       mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                                       mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                                                   }
                                               });
                                           } catch (Exception e) {
                                               // TODO: handle exception
                                           }
                                       }
                                   }
                               }).start();
                           /*
                           Intent mainIntent = new Intent(Splash.this, Login.class);
                           startActivity(mainIntent);
                           Splash.this.finish();*/
                           }
                      /* }else{
                           tv=(TextView) findViewById(R.id.textView6);
                           tv.setText("Check internet connection, try again.");
                           tv.setVisibility(View.VISIBLE);
                       }*/
                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        }catch (Exception ex){}
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String [] permissions, int [] grantResult){
        if(requestCode==221){
            if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
                requestCurrentLocation();
            }
        }
    }


    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Activating GPS");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled.\n");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }


    @Override
    protected void onActivityResult(int request, int result, Intent data) {
        super.onActivityResult(request, result, data);
        switch (request) {
            case REQUEST_CODE_SIGN_IN:
                if (result == RESULT_OK) {
                    // TODO :)
                }
        }
    }

    private void updateDisplay(String name, double lon, double lat) {
        Log.i(TAG, " Called UpdateDisplay with values " + name + " " + lon + " " + lat);
        if (name == null || lon == 0 || lat == 0)
            return;
       // ProgressBar p = (ProgressBar) findViewById(R.id.loading_indicator);
        //p.setVisibility(View.GONE);

        TextView t = (TextView) findViewById(R.id.welcome_text);
        t.setText("Hi, " + name + "\n" + cityName + ", " + countryName);

        Toast.makeText(getApplicationContext(),cityName+countryName,Toast.LENGTH_LONG).show();

        Intent mainIntent = new Intent(Splash.this, Login.class);
        SharedPreferences.Editor editor = myPref.edit();
        editor.putString("CITY", cityName);
        editor.putString("COUNTRY", countryName);
        editor.putString("LNG", Double.toString(longitude));
        editor.putString("LAT", Double.toString(lattitude));
        editor.apply();
        Splash.this.startActivity(mainIntent);
        Splash.this.finish();
    }

    private void requestDisplayName() {
        startService(new Intent(this, DataService.class).setAction(DataService.ACTION_GET_DISPLAY_NAME));
    }

    public void requestCurrentLocation() {
        startService(new Intent(this, DataService.class).setAction(DataService.ACTION_GET_CURRENT_LOCATION));
    }

    public void onSigninClicked(View v) {
        requestDisplayName();
    }

    public boolean hasInternetAccess() {
        Runtime runtime = Runtime.getRuntime();
        Process ping = null; // google.com
        try {
            ping = runtime.exec("/system/bin/ping -c 1 173.194.39.4");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int result = 0;
        try {
            result = ping.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(result == 0) return true;
        else return false;
    }

}
