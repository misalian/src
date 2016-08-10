package net.iamavailable.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainMenuActivity extends ActionBarActivity{

    protected static final String TAG = "location-updates-sample";
    protected String formattedDate;
    protected Boolean mRequestingLocationUpdates;
    public static  double lang,lat;
    String display,display2;
    ProgressBar pb;
    TextView tv_Post, tv_Check, main_message,t;
   public static TextView lik, doll;
    LinearLayout le;
    Bundle profile;
    SharedPreferences myPref;
    public static String IMENUMBER;
    LocationManager locationManager;
    boolean isGPSEnable=false;
    int count=0;
    public static MainMenuActivity ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profile = getIntent().getExtras();
        setContentView(R.layout.activity_main_menu);
        try {
            ac=MainMenuActivity.this;
            myPref = getSharedPreferences(Splash.IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            if(myPref.getString("id"," ")!=" ") {

                pb=(ProgressBar) findViewById(R.id.progressB);
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                formattedDate = df.format(cal.getTime());
                le = (LinearLayout) findViewById(R.id.include2);
                lik = (TextView) le.findViewById(R.id.likes);
                doll = (TextView) le.findViewById(R.id.dollars);
                doll.setText(myPref.getString("tokenss", " "));
                lik.setText(myPref.getString("likess", " "));


                // Kick off the process of building a GoogleApiClient and requesting the LocationServices
                // API.

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 220);
                }

              //  Intent intent = new Intent(MainMenuActivity.this, CheckUpdateService.class);
              //  startService(intent); ////// Start Service

                IMENUMBER = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                String cityName = myPref.getString("CITY", "Capital City").toUpperCase();
                String countryName = myPref.getString("COUNTRY", " ").toUpperCase();
                String mainMessage = getResources().getString(R.string.main_menu_p1) + " " + cityName + " " + countryName+"." + getResources().getString(R.string.main_menu_p2);
                tv_Post = (TextView) findViewById(R.id.main_post_status);
                tv_Check = (TextView) findViewById(R.id.main_check_status);
                main_message = (TextView) findViewById(R.id.main_message);
                main_message.setText(mainMessage);
                t = (TextView) findViewById(R.id.main_intricate);
                t.setText(myPref.getString("NAME", " ").toUpperCase() + "\nLUCID");

                AsyncCallWS ac = new AsyncCallWS();
                ac.execute();

                tv_Post.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!isPostTimeExpire()){
                            Toast.makeText(getApplicationContext(),
                                    "Please wait till your last post expires", Toast.LENGTH_LONG).show();
                            return;
                        }
                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                        if (mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if (!isGPSEnable) {
                                Toast.makeText(getApplicationContext(), "Please turn on your Location", Toast.LENGTH_LONG).show();
                            } else {

                                SharedPreferences.Editor editor = myPref.edit();
                                // editor.putString("LNG", Double.toString(lang));
                                //editor.putString("LAT", Double.toString(lat));
                                editor.apply();
                                // TODO Auto-generated method stub
                                Intent intentPost = new Intent(MainMenuActivity.this, PostActivity.class);
                                intentPost.putExtra("bundle", profile);
                                intentPost.putExtra("name", myPref.getString("NAME", " ").toUpperCase());
                                startActivity(intentPost);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Check internet connection and enable Location based services/ GPS", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                tv_Check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                        if (mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if (!isGPSEnable) {
                                Toast.makeText(getApplicationContext(), "Please turn on your Location", Toast.LENGTH_LONG).show();
                            } else {
                                Intent intentCheck = new Intent(MainMenuActivity.this, CheckActivity.class);
                                intentCheck.putExtra("bundle", profile);
                                startActivity(intentCheck);

                                // TODO Auto-generated method stub
                            }
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Check internet connection and enable Location based services/ GPS", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                TextView help=(TextView) findViewById(R.id.main_help);
                help.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainMenuActivity.this, Help.class);
                        startActivity(i);

                    }
                });

                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //t.setTypeface(t.getTypeface(), Typeface.BOLD);
                        Intent i = new Intent(MainMenuActivity.this, profile.class);
                        startActivity(i);

                    }
                });
            }else{
                Intent mainIntent = new Intent(MainMenuActivity.this, Login.class);
                startActivity(mainIntent);
            }
        }catch (Exception ex){}
    }

    public boolean isPostTimeExpire(){
        //Calendar cal = Calendar.getInstance(); // creates calendar
        SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmssZ");
        Date now;
        String formattedDate = df.format(now=new Date());
        //cal.setTime(new Date()); // sets calendar time/date
        //String timeStampNow = df.format(cal.getTime());
        //String timeStampLater = df.format(cal.getTime());
       // pc.setDate(String.valueOf(formattedDate));
        //pc.setTstart(timeStampNow);
        //pc.setTend(timeStampLater);

        String endTime=myPref.getString("EndTime","");
        if(endTime.length()!=0){
            try {
                Date date = df.parse(endTime);
                if(now.before(date)){
                    return false;
                }
                Log.i(TAG, " PERFORM ACTION CALLED : " + "Endtime: "+endTime+" now "+formattedDate);
                return true;

            } catch (Exception e) {
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String [] permissions, int [] grantResult){
        if(requestCode==220){
            if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){
            }
        }
    }


    public void headerHome(View v) {
        //there should be nothing
        Intent i = new Intent(MainMenuActivity.this, profile.class);
        startActivity(i);
    }

    //////////////////////////////////////////////////////
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_local_test2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            //Toast.makeText(getApplicationContext(),"successfully!",Toast.LENGTH_LONG).show();
            if(count==1){
                display2 = PostWebService.delCurrentPost(myPref.getString("id"," "), formattedDate, "deleteCurrentPost"); //emplogin method name
                count=2;
            }
            display = PostWebService.getLikDolrSplash(myPref.getString("id"," "), formattedDate, "getLDSplash"); //emplogin method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
           try {
                if(count==2){
                    try {
                        pb.setVisibility(View.INVISIBLE);
                        count=0;
                        if (display2.equals("true")) {
                            Toast.makeText(getApplicationContext(),
                                    "Logged out Successfully",
                                    Toast.LENGTH_LONG).show();
                            SharedPreferences.Editor editor = myPref.edit();
                            editor.remove("id").commit();
                            Intent i = new Intent(MainMenuActivity.this, Login.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } else if(display2.equals("false")) {
                            Toast.makeText(getApplicationContext(),
                                    "Logged out Successfully",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),
                                    "Check internet connection and enable Location based services/ GPS",
                                    Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception ep){
                        Toast.makeText(getApplicationContext(),
                                "Check internet connection and enable Location based services/ GPS",
                                Toast.LENGTH_LONG).show();
                    }
                }
                String[] d = display.split(",");
                if(d[0].equals("true")) {
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("tokenss", d[1]);
                    editor.putString("likess", d[2]);
                    //editor.putString("LNG", Double.toString(longitude));
                    // editor.putString("LAT", Double.toString(lattitude));
                    editor.apply();
                    doll.setText(d[1]);
                    lik.setText(d[2]);
                }else if(d[0].equals("false")){
                }else{
                   /* Toast.makeText(getApplicationContext(),
                            "Check your connectivity and try again!!",
                            Toast.LENGTH_LONG).show();*/
                }
                //tv.setText(fahren + "° F");
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),
                        "Check internet connection and enable Location based services/ GPS",
                        Toast.LENGTH_LONG).show();
               }
        }


        @Override
        protected void onPreExecute() {
            if(count==1) {
                pb.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
