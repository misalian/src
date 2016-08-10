package net.iamavailable.app;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.CellInfoGsm;
import android.telephony.CellSignalStrengthGsm;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.location.Geocoder;
import java.util.Locale;
import java.util.List;
import android.location.Address;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;

import android.util.Base64;


import java.util.prefs.Preferences;
import android.net.Uri;

import net.iamavailable.app.data.DataService;
import net.iamavailable.app.data.GetCurrentLocation;
import android.widget.CheckBox;

import javax.mail.internet.ParseException;
import android.app.AlertDialog;
import android.content.DialogInterface;


//import javax.mail.Address;

public class PostStatus extends FragmentActivity implements OnMapReadyCallback{

    private static double lattitude = 0.0;
    private static String userName = null, cityName = null, countryName = null, id=null;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    SharedPreferences myPref;
    private final String TAG = "PostScreen1";
    double latitude = 0, longitude = 0;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    ImageView iv_user_pic, iv_venue_pic,markerimg;
    private GoogleMap googleMap;
    private MarkerOptions marker;
    private TextView postStatus, tv_date, tv_duration, tv_status, tv_location, load;
    Bundle profile;
    PostClass pc;
    ProgressBar pb;
    String display,userPic,venuePic,imagepathfront=" ",imagepathback=" ",imagebytesfront=" ",imagebytesback=" ";
    boolean isGPSEnable=false;
    LocationManager locationManager;
    Marker mymarker;
    int tCount=0;
    boolean manual=false;
    public boolean mutex=false;
    boolean locat_Recieved=false;
    public int tries=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PostActivity.action_View.setVisibility(View.VISIBLE);
            PostActivity.pb2.setVisibility(View.INVISIBLE);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnable=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(!isGPSEnable){
                Toast.makeText(getApplicationContext(),"To view marker on map, turn on location services",Toast.LENGTH_LONG).show();
            }
            myPref = getSharedPreferences(IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            profile = getIntent().getExtras();
            setContentView(R.layout.activity_post_status);
            tv_date = (TextView) findViewById(R.id.post2_date);
            tv_duration = (TextView) findViewById(R.id.post2_session);
            tv_status = (TextView) findViewById(R.id.post2_alias);
            postStatus = (TextView) findViewById(R.id.post2_btn_post);
            tv_location = (TextView) findViewById(R.id.post2_location);
            iv_user_pic = (ImageView) findViewById(R.id.post2_pic_user);
            iv_venue_pic = (ImageView) findViewById(R.id.post2_pic_venue);
            markerimg = (ImageView) findViewById(R.id.imageView);
            load=(TextView) findViewById(R.id.textView5);
            pb = (ProgressBar) findViewById(R.id.progressBar2);
            Intent receivedIntent = getIntent();
            imagepathfront=receivedIntent.getStringExtra("frontimagepath").toString();
            imagepathback=receivedIntent.getStringExtra("backimagepath").toString();
            pc = (PostClass) getIntent().getSerializableExtra("postobject");
            tv_date.setText(receivedIntent.getStringExtra("DATE").toString());
            tv_duration.setText(receivedIntent.getStringExtra("DURATION").toString());
            tv_status.setText(receivedIntent.getStringExtra("MOOD").toString());
            userPic = receivedIntent.getStringExtra("USER_PIC");
            venuePic = receivedIntent.getStringExtra("VENUE_PIC");
            try {
                iv_user_pic.setImageBitmap(getThumb(userPic));
                iv_venue_pic.setImageBitmap(getThumb(venuePic));
            }catch(Exception e1){}

            iv_user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int arrsize=0;
                    int check=0;
                    String img1=pc.getFrontpicpath();
                    String img2=pc.getBackpicpath();
                    if(img1!=null){
                        check=1;
                        arrsize++;
                    }else{
                        img1=" ";
                    }
                    if(img2!=null){
                        arrsize++;
                    }else{
                        img2=" ";
                    }

                    if(arrsize>0&&check==1){
                        String picuri=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
                        openInGallery(picuri + img1);
                    }
                }
            });
            iv_venue_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int arrsize=0;
                    int check=0;
                    String img1=pc.getBackpicpath();
                    String img2=pc.getFrontpicpath();
                    if(img1!=null){
                        check=1;
                        arrsize++;
                    }else{
                        img1=" ";
                    }
                    if(img2!=null){
                        arrsize++;
                    }else{
                        img2=" ";
                    }

                    if(arrsize>0&&check==1){
                        String picuri=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
                        openInGallery(picuri + img1);
                    }
                }
            });

           /* editmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(PostStatus.this, "Edit Map Mode", Toast.LENGTH_SHORT).show();
                    if(editmap.isChecked()){
                        markerimg.setVisibility(View.VISIBLE);
                       // LatLng ln=new LatLng(Double.parseDouble(myPref.getString("LAT", "0.0")), Double.parseDouble(myPref.getString("LNG","0.0")));
                       // mymarker=googleMap.addMarker(new MarkerOptions().position(ln).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).draggable(true));

                    }else{
                       // googleMap.clear();
                        markerimg.setVisibility(View.INVISIBLE);
                    }

                }
            });*/

            Log.i(TAG, "All variables are set -- Initializing Map ");
            try {
                // Loading map
                Log.i(TAG, " Initializing map ");
                initilizeMap();
                manual=false;
                mutex=false;
                locat_Recieved=false;

                Log.i(TAG, " Initializing map -- DONE ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            postStatus.setVisibility(View.INVISIBLE);
            load.setVisibility(View.VISIBLE);
            postStatus.postDelayed(new Runnable() {
                public void run() {
                    load.setVisibility(View.INVISIBLE);
                    postStatus.setVisibility(View.VISIBLE);
                }
            }, 6000);
            postStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Toast.makeText(PostStatus.this, "Post pressed", Toast.LENGTH_SHORT).show();
                    postStatus.setVisibility(View.GONE);
                    pb.setVisibility(View.VISIBLE);

                    //Toast.makeText(PostStatus.this, "Before Location request", Toast.LENGTH_SHORT).show();


                    if(manual){
                        redMarkerSaveLocation();
                    }else {

                        SharedPreferences.Editor editor = myPref.edit();
                        editor.putString("COUNTRY", " ");
                        editor.putString("LNG", "0.0");
                        editor.putString("LAT", "0.0");
                        editor.apply();
                        requestCurrentLocation();

                    }
/*
                    try {
                    //while(!locat_Recieved) {

                        Toast.makeText(PostStatus.this, "Processing location", Toast.LENGTH_SHORT).show();
                        Thread.sleep(3000);
                    //}
                    }catch(Exception ty){
                        Toast.makeText(PostStatus.this, "Location processing failed", Toast.LENGTH_SHORT).show();

                    }
                    /*while(myPref.getString("COUNTRY"," ").equals(" ")||myPref.getString("LNG","0.0").equals("0.0")||myPref.getString("LAT","0.0").equals("0.0")){
                       // Toast.makeText(PostStatus.this, "ERROR in location", Toast.LENGTH_SHORT).show();
                    }*/
  /*                  Toast.makeText(PostStatus.this, "After Location getting: "+myPref.getString("LNG", "0.0")+","+myPref.getString("LAT", "0.0"), Toast.LENGTH_SHORT).show();
                    Calendar cal = Calendar.getInstance(); // creates calendar
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(cal.getTime());
                    cal.setTime(new Date()); // sets calendar time/date
                    String timeStampNow = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
                    cal.add(Calendar.MINUTE, PostActivity.selectedtime);
                    String timeStampLater = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
                    pc.setDate(String.valueOf(formattedDate));
                    pc.setTstart(timeStampNow);
                    pc.setTend(timeStampLater);

                    pc.setLat(myPref.getString("LAT", "0.0"));
                    pc.setLang(myPref.getString("LNG", "0.0"));
                    try {
                        if (imagepathfront.length() > 0) {
                            imagebytesfront = uploadImage(imagepathfront);
                        }
                    }catch(Exception dk){}
                    try {
                        if (imagepathback.length() > 0) {
                            imagebytesback = uploadImage(imagepathback);
                        }
                    }catch(Exception dg){}
                    pc.setFrontpic(imagebytesfront);
                    pc.setBackpic(imagebytesback);


                    if (ContextCompat.checkSelfPermission(PostStatus.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PostStatus.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 123);
                    }

                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                    if (mWifi.isConnected()) {
                        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
                        int numberOfLevels = 5;
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
                        if(level<3){
                            Toast.makeText(getApplicationContext(),
                                    "WiFi signals strength weak, post may take somtime", Toast.LENGTH_LONG).show();
                        }
                    }
                    // TODO Auto-generated method stub
                    if(GetCurrentLocation.accuracy<=10.0){
                            AsyncCallWS ac = new AsyncCallWS();
                            ac.execute();
                    }else{
                        requestCurrentLocation();
                        pb.setVisibility(View.INVISIBLE);
                        postStatus.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),
                                "Cannot post, location accuracy is "+GetCurrentLocation.accuracy, Toast.LENGTH_LONG).show();

                    }*/
                }
            });
        } catch (Exception ex) {
            Toast.makeText(PostStatus.this, "Exception", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "PostButton Exception: "+ex.getStackTrace());

        }
    }
    public void saveTime(){
        Calendar cal = Calendar.getInstance(); // creates calendar
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(cal.getTime());
        cal.setTime(new Date()); // sets calendar time/date
        String timeStampNow = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
        cal.add(Calendar.MINUTE, PostActivity.selectedtime);
        String timeStampLater = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
        pc.setDate(String.valueOf(formattedDate));
        pc.setTstart(timeStampNow);
        pc.setTend(timeStampLater);

        formattedDate = new SimpleDateFormat("yyMMddHHmmssZ").format(cal.getTime());
        SharedPreferences.Editor editor = myPref.edit();
        editor.putString("EndTime", formattedDate);
        editor.apply();
    }
    public void saveLocation(){
        String locationText = myPref.getString("CITY","Capital City")+", "+myPref.getString("COUNTRY"," ")+"\nLat/Long: "+latitude +", "+longitude;
        tv_location.setText(locationText);
        saveTime();
        pc.setLat(myPref.getString("LAT", "0.0"));
        pc.setLang(myPref.getString("LNG", "0.0"));
        try {
            if (imagepathfront.length() > 0) {
                imagebytesfront = uploadImage(imagepathfront);
            }
        }catch(Exception dk){}
        try {
            if (imagepathback.length() > 0) {
                imagebytesback = uploadImage(imagepathback);
            }
        }catch(Exception dg){}
        pc.setFrontpic(imagebytesfront);
        pc.setBackpic(imagebytesback);


        if (ContextCompat.checkSelfPermission(PostStatus.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PostStatus.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 123);
        }

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            if(level<3){
                Toast.makeText(getApplicationContext(),
                        "WiFi signals strength weak, post may take somtime", Toast.LENGTH_LONG).show();
            }
        }
        // TODO Auto-generated method stub
        ///if(GetCurrentLocation.accuracy<=accuracy_threshhold){

        AsyncCallWS ac = new AsyncCallWS();
        ac.execute();
        /*}else{
            requestCurrentLocation();
            pb.setVisibility(View.INVISIBLE);
            postStatus.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),
                    "Cannot post, location accuracy is "+GetCurrentLocation.accuracy, Toast.LENGTH_LONG).show();

        }*/
    }
    public void redMarkerSaveLocation(){

        LatLng centerOfMap = googleMap.getCameraPosition().target;

        SharedPreferences.Editor editor = myPref.edit();
        editor.putString("Red Marker LNG", Double.toString(centerOfMap.longitude-0.000001));
        editor.putString("Red Marker LAT", Double.toString(centerOfMap.latitude+0.000006));
        editor.apply();


        pc.setLat(myPref.getString("Red Marker LAT", "0.0"));
        pc.setLang(myPref.getString("Red Marker LNG", "0.0"));
        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(Double.parseDouble(pc.getLat()), Double.parseDouble(pc.getLang()), 1);
            int i = 0;
            for (Address ad : address) {
                Log.i(DataService.TAG, "count : " + i++);
                Log.i(DataService.TAG, "ADRESSS: " + ad.toString());
            }
            countryName = address.get(0).getCountryName();

            Log.i(DataService.TAG, "subAdminArea:" + address.get(0).getSubAdminArea());
            Log.i(DataService.TAG, "AdminArea:" + address.get(0).getAdminArea());
            Log.i(DataService.TAG, "FeatureName:" + address.get(0).getFeatureName());
            Log.i(DataService.TAG, "Locality:" + address.get(0).getLocality());


            String subAd = address.get(0).getSubAdminArea();
            String Ad = address.get(0).getAdminArea();
            String fea = address.get(0).getFeatureName();
            String lo = address.get(0).getLocality();

            // Toast.makeText(getContext(),"subAd: "+subAd+" Ad: "+Ad+" fea: "+fea+" lo: "+lo,Toast.LENGTH_LONG).show();
            if (lo != null && !lo.isEmpty()) {
                // doSomething
                cityName = lo;
            } else {
                if (Ad != null && !Ad.isEmpty()) {
                    // doSomething
                    cityName = Ad;
                } else {
                    if (subAd != null && !subAd.isEmpty()) {
                        // doSomething
                        cityName = subAd;
                    } else {
                        if (fea != null && !fea.isEmpty()) {
                            // doSomething
                            cityName = fea;
                        } else {
                            cityName = "Not Found";
                        }
                    }
                }
            }
        }catch(Exception we){}
        pc.setCity(cityName);
        pc.setCountry(countryName);
        String locationText = cityName+", "+countryName+"\nLat/Long: "+pc.getLat() +", "+pc.getLang();
        tv_location.setText(locationText);

        try {
            if (imagepathfront.length() > 0) {
                imagebytesfront = uploadImage(imagepathfront);
            }
        }catch(Exception dk){}
        try {
            if (imagepathback.length() > 0) {
                imagebytesback = uploadImage(imagepathback);
            }
        }catch(Exception dg){}
        pc.setFrontpic(imagebytesfront);
        pc.setBackpic(imagebytesback);


        if (ContextCompat.checkSelfPermission(PostStatus.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PostStatus.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 123);
        }

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            int numberOfLevels = 5;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
            if(level<3){
                Toast.makeText(getApplicationContext(),
                        "WiFi signals strength weak, post may take somtime", Toast.LENGTH_LONG).show();
            }
        }
        // TODO Auto-generated method stub
        ///if(GetCurrentLocation.accuracy<=accuracy_threshhold){
        saveTime();
        AsyncCallWS ac = new AsyncCallWS();
        ac.execute();
        /*}else{
            requestCurrentLocation();
            pb.setVisibility(View.INVISIBLE);
            postStatus.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),
                    "Cannot post, location accuracy is "+GetCurrentLocation.accuracy, Toast.LENGTH_LONG).show();

        }*/
    }
    public  boolean setPostTime(){
        Calendar cal = Calendar.getInstance(); // creates calendar
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df1 = new SimpleDateFormat("yyMMddHHmmssZ");

        String formattedDate = df.format(cal.getTime());
        cal.setTime(new Date()); // sets calendar time/date
        String timeStampNow = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
        cal.add(Calendar.MINUTE, PostActivity.selectedtime);
        String timeStampLater = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
        pc.setDate(String.valueOf(formattedDate));
        pc.setTstart(timeStampNow);
        pc.setTend(timeStampLater);

        String oldTime=myPref.getString("EndTime","");
        if(oldTime.length()!=0){
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("hh:mm:ss");
            try {
                Date date = dateFormat2.parse(oldTime);
                if(cal.after(date)){
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("EndTime", pc.getTend());
                    editor.apply();
                }else{
                    return false;
                }

            } catch (Exception e) {
            }
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.i(TAG, "onMapReady:: calling currentLocation ");
        googleMap = map;
        currentLocation();
        Log.i(TAG, "onMapReady:: Calling currentLocation -- DONE");
    }

    private void initilizeMap() {
        Log.i(TAG, "initilizeMap:: making map ");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.post2_map);
        mapFragment.getMapAsync(this);
        Log.i(TAG, "initilizeMap:: making map -- DONE ");
        Log.i(TAG, "initilizeMap:: calling CurrentLocation ");
    }

    private void currentLocation() {
        googleMap.setMyLocationEnabled(true); // false to disable
        longitude = Double.parseDouble(myPref.getString("LNG", "0"));
        latitude = Double.parseDouble(myPref.getString("LAT", "0"));
        // longitude = MainMenuActivity.lang; //Double.parseDouble(myPref.getString("LNG", "0"));
        // latitude = MainMenuActivity.lat; //Double.parseDouble(myPref.getString("LAT", "0"));
        Log.i(TAG, "Current Location values:: longitude:"+longitude+", Latitude:"+latitude);
        String locationText = myPref.getString("CITY","Capital City")+", "+myPref.getString("COUNTRY"," ")+"\nLat/Long: "+latitude +", "+longitude;
        tv_location.setText(locationText);
        Log.i(TAG, "Making CameraPosition:: longitude:" + longitude + ", Latitude:" + latitude);
        CameraPosition camPos = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(18.0f)
                .build();
        CameraUpdate camUpdate = CameraUpdateFactory.newCameraPosition(camPos);
        Log.i(TAG, "Moving Cam to CamUpdate:: longitude:" + longitude + ", Latitude:" + latitude);
        googleMap.moveCamera(camUpdate);
        Log.i(TAG, "Animatin Cam to:: longitude:" + longitude + ", Latitude:" + latitude);
        googleMap.animateCamera(camUpdate);
        Log.i(TAG, "Cam updated and Zoomed:: longitude:" + longitude + ", Latitude:" + latitude);

       /* googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {

            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                SharedPreferences.Editor editor = myPref.edit();
                editor.putString("Red Marker LNG", Double.toString(arg0.getPosition().longitude));
                editor.putString("Red Marker LAT", Double.toString(arg0.getPosition().latitude));
                editor.apply();
                Toast.makeText(PostStatus.this, "Current location saved", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });*/

//Don't forget to Set draggable(true) to marker, if this not set marker does not drag.
        LatLng ln=new LatLng(Double.parseDouble(myPref.getString("LAT", "0.0")), Double.parseDouble(myPref.getString("LNG","0.0")));
        //mymarker=googleMap.addMarker(new MarkerOptions().position(ln));

        googleMap.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange (CameraPosition position) {

                // Get the center of the Map.
                // Update your Marker's position to the center of the Map.
                // mymarker.setPosition(centerOfMap);
            }
        });
        //googleMap.addMarker(new MarkerOptions().position(ln).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).draggable(true));

    }

    public void headerHome(View v) {
        Intent intentHome = new Intent(PostStatus.this, MainMenuActivity.class);
        startActivity(intentHome);
        finish();
    }

    private Bitmap getThumb(String path) {
        Bitmap bm=null;
        try {
            int thumbWidth = 0, thumbHeight = 0;
            thumbHeight = (int) getResources().getDimension(R.dimen.thumb_heigth);
            thumbWidth = (int) getResources().getDimension(R.dimen.thumb_width);
            Log.i("POST2", "Thumbnail With:" + thumbWidth + " Height: " + thumbHeight);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            bm = BitmapFactory.decodeFile(path, options);

            return ThumbnailUtils.extractThumbnail(bm, thumbWidth, thumbHeight);
        }catch (Exception ex2){
            if(bm!=null)
            {
                bm.recycle();
                bm=null;
                bm=getThumb(path);

            }
            return bm;
        }
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            //Toast.makeText(getApplicationContext(),"successfully!",Toast.LENGTH_LONG).show();
            display = PostWebService.postDataServer(pc.getUsername(),pc.getFrontpic(),pc.getBackpic(),pc.getMessage(),pc.getTstart(),pc.getTend(),pc.getDate(),pc.getLat(),pc.getLang(),pc.getStatusmood(),pc.getEmail(),pc.getImenumber(),pc.getCity(),pc.getCountry(),pc.getFrontpicpath(),pc.getBackpicpath(),"insertPost"); //emplogin method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                pb.setVisibility(View.INVISIBLE);
                postStatus.setVisibility(View.VISIBLE);
                String[] dis = display.split(",");
                if (dis[0].equals("true")) {
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("tokenss", dis[1]);
                    editor.apply();
                    MainMenuActivity.ac.finish();
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Post submitted successfully ", Toast.LENGTH_LONG).show();
                    MainMenuActivity.ac.finish();
                    Intent intentPost = new Intent(PostStatus.this, MainMenuActivity.class);
                    startActivity(intentPost);
                    finish();
                } else if (dis[0].equals("D:2")) {
                    Toast.makeText(getApplicationContext(),
                            "Please wait till your last post expires", Toast.LENGTH_LONG).show();
                } else if(dis[0].equals("false")){
                    Toast.makeText(getApplicationContext(),
                            "Post submission failed, check internet connection:", Toast.LENGTH_LONG).show();
                }else if(dis[0].equals("Error occured")){
                    Toast.makeText(getApplicationContext(),
                            "Post submission failed, check internet connection:", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(),
                            "Post submission failed, check internet connection:", Toast.LENGTH_LONG).show();
                }
                //tv.setText(fahren + "∞ F");
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),
                        "Post submission failed, check internet connection:", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    //Listener for permission
    @Override
    public void onRequestPermissionsResult (int requestCode, String [] permissions, int [] grantResult){
        if(requestCode==229){
            if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){

            }
        }
    }
    public String uploadImage(String photoPath1){
        File imagefile = new File(photoPath1);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(imagefile);
        } catch (Exception xe) {
        }

        Bitmap bm = BitmapFactory.decodeStream(fis);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap scaled = Bitmap.createScaledBitmap(bm, 720, 480, true);
        scaled.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
    public void openInGallery(String imageId) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + imageId), "image/*");
        startActivity(intent);
    }


    ///////////////////////////////////////////////////////////////////////////Location Receiver///////////////////////
    BroadcastReceiver receiverCurrentLocation = new BroadcastReceiver() {
        //@Override
        //public int tries=0;



        public void onReceive(Context context, Intent intent) {

            String saccuracy = Double.toString(GetCurrentLocation.accuracy);
            int floatindex = saccuracy.indexOf('.');
           // String locationText = myPref.getString("CITY","Capital City")+", "+myPref.getString("COUNTRY"," ")+" "+  saccuracy.substring(0,floatindex+2);
           // tv_location.setText(locationText);
           // Toast.makeText(getApplicationContext(),"Function call",Toast.LENGTH_SHORT).show();


          if(++tries>1) {
                //Toast.makeText(getApplicationContext(),"tries >1 ",Toast.LENGTH_SHORT).show();

                return;
            }
            while(mutex){                //Toast.makeText(getApplicationContext(),"in mutex",Toast.LENGTH_SHORT).show();
            }

            mutex=true;
           if(locat_Recieved) {
               //Toast.makeText(getApplicationContext(),"location done exiting ",Toast.LENGTH_SHORT).show();

               return;
           }
            //Toast.makeText(getApplicationContext(),"normal processing",Toast.LENGTH_SHORT).show();

            Log.i(TAG, " Splash::LocationReceived Long:" + intent.getDoubleExtra("longitude", 0.0) + ", Lat:" + 	intent.getDoubleExtra("latitude", 0.0));
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

            //       Toast.makeText(getApplicationContext(),"InReceive Current Location: "+GetCurrentLocation.accuracy+" "
            //             +myPref.getString("LNG","0.0")+" "+myPref.getString("LAT","0.0"),Toast.LENGTH_SHORT).show();

           /* while(myPref.getString("id"," ")==" "){


            }*/
            // updateDisplay(userName, longitude, lattitude);

           if(myPref.getString("COUNTRY", " ").equals(" ")||myPref.getString("LNG","0.0").equals("0.0")||
                    myPref.getString("LAT","0.0").equals("0.0")||GetCurrentLocation.accuracy>Splash.accuracy_threshhold){
                //if(tCount++<=2) {
                if(tries<=1){
                    //Toast.makeText(getApplicationContext()," popup",Toast.LENGTH_SHORT).show();

                    showAlert();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Failed to get desired accuracy, switching to manual mode",Toast.LENGTH_SHORT).show();
                    markerimg.setVisibility(View.VISIBLE);
                    postStatus.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                    manual=true;
                    mutex=false;
                    locat_Recieved=true;

                }

            }/*else{
                // Toast.makeText(getApplicationContext(),"Not Get Location",Toast.LENGTH_LONG).show();

            }*/

           else {
                saveLocation();
                locat_Recieved=true;
            }


        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, " Registering Receiver in Splash");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiverCurrentLocation, new IntentFilter(DataService.BROADCAST_CURRENT_LOCATION));
        Log.i(TAG, " Got Registered Receiver in Splash");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiverCurrentLocation);
        Log.i(TAG, " Unregistered Receiver in Splash");
    }

    public void requestCurrentLocation() {
        tries=0;
        startService(new Intent(this, DataService.class).setAction(DataService.ACTION_GET_CURRENT_LOCATION));
    }

    public void showAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setCancelable(false);

        // Setting Dialog Title
        alertDialog.setTitle("Inaccurate Location");

        String saccuracy = Double.toString(GetCurrentLocation.accuracy);
        int floatindex = saccuracy.indexOf('.');
        alertDialog.setMessage("Current accuracy is "+saccuracy.substring(0,floatindex+2)+ ". Would you like to try yourself?\n");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                markerimg.setVisibility(View.VISIBLE);
                postStatus.setVisibility(View.VISIBLE);
                pb.setVisibility(View.INVISIBLE);
                manual = true;
                locat_Recieved=true;
                Toast.makeText(PostStatus.this, "For better accuracy please zoom map", Toast.LENGTH_LONG).show();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Thread.sleep(3000);
                } catch (Exception exo) {
                }
                requestCurrentLocation();
                mutex=false;
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
}
