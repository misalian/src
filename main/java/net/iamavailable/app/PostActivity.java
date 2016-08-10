package net.iamavailable.app;

import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.content.BroadcastReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings.Secure;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Matrix;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.HttpResponse;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.media.ExifInterface;

import net.iamavailable.app.data.DataService;
import net.iamavailable.app.data.GetCurrentLocation;

import java.io.FileOutputStream;
public class PostActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static double lattitude = 0.0;
    private static String userName = null, cityName = null, countryName = null, id=null;
    private static final String TAG = "PostScreen1", USER_PIC = "USER_PIC", VENUE_PIC = "VENUE_PIC", LOCATION = "LOCATION", CURRENT_PIC = "CURRENT_PIC";
    private static final int USER_PIC_REQUEST = 20, VENUE_PIC_REQUEST = 21;
    private static final String[] timeItems = {"Select a duration", "Available for 2.0 Hour", "Available for 1.5 Hour", "Available for 1.0 Hour", "Available for 0.5 Hour"};
    public static String photoPath = null;
    String display, imenumber, pos,imagebytesfront=" ",imagebytesback=" ",imagepathfront=" ",imagepathback=" ";
    String userPic = "#", venuePic = "#";
    private EditText et_Location;
    private TextView action_Post;
    public static TextView action_View;
    private Spinner timeSpinner;
    private ImageView iv_post_pic_user, iv_post_pic_venue;
    private int thumbHeight = 0, thumbWidth = 0;
    Bundle profile;
    PostClass pc;
    ProgressBar pb;
    public static ProgressBar pb2;
    double longitude = 0.0;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    SharedPreferences myPref;
    TextView lik, doll;
    LinearLayout le;
    boolean isGPSEnable = false;
    LocationManager locationManager;
    public static byte[] by;
    public static int selectedtime=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        try {
            myPref = getSharedPreferences(IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myPref.edit();
            editor.putString("COUNTRY"," ");
            editor.putString("LNG", "0.0");
            editor.putString("LAT", "0.0");
            editor.apply();
            requestCurrentLocation();
            imenumber = Secure.getString(getApplicationContext().getContentResolver(),
                    Secure.ANDROID_ID);
            pb = (ProgressBar) findViewById(R.id.progressBar);
            pb2 = (ProgressBar) findViewById(R.id.progressBar9);
            profile = getIntent().getExtras();
            pc = new PostClass();
            et_Location = (EditText) findViewById(R.id.post_location);
            iv_post_pic_user = (ImageView) findViewById(R.id.post_pic_user);
            iv_post_pic_venue = (ImageView) findViewById(R.id.post_pic_venue);
            // moodSpinner = (Spinner) findViewById(R.id.post_mood_spinner);
            timeSpinner = (Spinner) findViewById(R.id.post_time_spinner);
            action_View = (TextView) findViewById(R.id.post_btn_view);
            action_Post = (TextView) findViewById(R.id.post_btn_post);
            et_Location.clearFocus();
            // moodSpinner.setOnItemSelectedListener(this);
            thumbHeight = (int) getResources().getDimension(R.dimen.thumb_heigth);
            thumbWidth = (int) getResources().getDimension(R.dimen.thumb_width);
            //photoPath = BitmapUtil.getAlbumStorageDir(getString(R.string.album_name));
            le = (LinearLayout) findViewById(R.id.header);
            lik = (TextView) le.findViewById(R.id.likes);
            doll = (TextView) le.findViewById(R.id.dollars);
            doll.setText(myPref.getString("tokenss", " "));
            lik.setText(myPref.getString("likess", " "));

            if (savedInstanceState != null) {
                restoreData(savedInstanceState);
            }


            timeSpinner.setOnItemSelectedListener(this);
            ArrayAdapter<String> timeSpinnerAdapter = new ArrayAdapter<String>(this,
                    R.layout.spinner_item, timeItems) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) { // Disable the first item from Spinner
                        return false;   // First item will be use for hint
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) { // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            timeSpinnerAdapter.setDropDownViewResource(
                    android.R.layout.simple_spinner_dropdown_item);
            timeSpinner.setAdapter(timeSpinnerAdapter);

            action_Post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pb.setVisibility(View.VISIBLE);
                    try {
                        if (imagepathfront.length() > 1) {
                            imagebytesfront = uploadImage(imagepathfront);
                        }
                    }catch(Exception ab){}
                    try {
                        if (imagepathback.length() > 1) {
                            imagebytesback = uploadImage(imagepathback);
                        }
                    }catch(Exception pst){}

                    pc.setFrontpic(imagebytesfront);
                    pc.setBackpic(imagebytesback);


                    pc.setUsername(getIntent().getStringExtra("name").toString());
                    pc.setLang(String.valueOf(myPref.getString("LNG", "0")));
                    pc.setLat(String.valueOf(myPref.getString("LAT", "0")));
                    Calendar cal = Calendar.getInstance(); // creates calendar
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    String formattedDate = df.format(cal.getTime());
                    cal.setTime(new Date()); // sets calendar time/date
                    String timeStampNow = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
                    cal.add(Calendar.MINUTE, getMinutes(timeSpinner.getSelectedItemPosition()));
                    String timeStampLater = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
                    pc.setDate(String.valueOf(formattedDate));
                    pc.setToken("1"); ////////////////////////////////////////////////////////////////////////////////////////
                    pc.setTstart(timeStampNow);
                    pc.setTend(timeStampLater);
                    pc.setStatusmood(" ");
                    pc.setCity(myPref.getString("CITY", "Capital City"));
                    pc.setCountry(myPref.getString("COUNTRY", " "));
                    //  pc.setStatusmood(moodSpinner.getSelectedItem().toString());
                    pc.setMessage(et_Location.getText().toString());
                    pc.setEmail(myPref.getString("id", "0"));

                    String dataState = validateData();
                    if (!(dataState != "Please Add:") && isNotEmpty(et_Location)) {
                        // if (MainMenuActivity.lang != 0.0 && MainMenuActivity.lat != 0.0) {
                        action_Post.setVisibility(View.INVISIBLE);
                        if (ContextCompat.checkSelfPermission(PostActivity.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PostActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 123);
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
                        if(GetCurrentLocation.accuracy<=10.0){
                        if(myPref.getString("id"," ")!=" "&&myPref.getString("LNG","0.0")!="0.0"&&myPref.getString("LAT","0.0")!="0.0") {
                            AsyncCallWS as = new AsyncCallWS();
                            as.execute();
                        }else{
                            requestCurrentLocation();
                            Toast.makeText(getApplicationContext(),
                                    "Location cannot get,try again", Toast.LENGTH_LONG).show();
                        }
                        }else{
                            requestCurrentLocation();
                            pb.setVisibility(View.INVISIBLE);
                            action_Post.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),
                                    "Cannot post, location accuracy is "+GetCurrentLocation.accuracy, Toast.LENGTH_LONG).show();

                        }

                       /* } else {
                            Toast.makeText(getApplicationContext(),
                                    "Check internet connection and enable Location based services/ GPS", Toast.LENGTH_LONG).show();
                        }*/

                    } else {
                        pb.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),
                                "Please fill all fields", Toast.LENGTH_LONG).show();
                    }

                }
            });

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                iv_post_pic_user.setEnabled(false);
                iv_post_pic_venue.setEnabled(false);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 222);
            }

            iv_post_pic_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    takePicture(USER_PIC_REQUEST);
                }
            });
            iv_post_pic_venue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    takePicture(VENUE_PIC_REQUEST);
                }
            });

            action_View.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    action_View.setVisibility(View.INVISIBLE);
                    pb2.setVisibility(View.VISIBLE);
                    String dataState = validateData();
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                    if (mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if (!isGPSEnable) {
                            pb2.setVisibility(View.INVISIBLE);
                            action_View.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Check internet connection and enable Location based services/ GPS", Toast.LENGTH_LONG).show();
                        } else {
                            if (!(dataState != "Please Add:") && isNotEmpty(et_Location)) {
                                if (myPref.getString("id"," ")!=" ") {
                                Intent intentView = new Intent(PostActivity.this, PostStatus.class);
                                Calendar cal = Calendar.getInstance(); // creates calendar
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                    String formattedDate = df.format(cal.getTime());
                                cal.setTime(new Date()); // sets calendar time/date
                                String timeStampNow = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
                                selectedtime=getMinutes(timeSpinner.getSelectedItemPosition());
                                cal.add(Calendar.MINUTE, selectedtime);
                                String timeStampLater = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
                                intentView.putExtra("DATE", formattedDate);
                                intentView.putExtra("DURATION", timeStampNow + " - " + timeStampLater);
                                intentView.putExtra("MOOD", et_Location.getText().toString());
                                pc.setStatusmood(" ");
                                intentView.putExtra("longitude", profile.getDouble("longitude"));
                                intentView.putExtra("latitude", profile.getDouble("latitude"));
                                intentView.putExtra(USER_PIC, userPic);
                                intentView.putExtra(VENUE_PIC, venuePic);
                                intentView.putExtra("frontimagepath", imagepathfront);
                                intentView.putExtra("backimagepath", imagepathback);

                                ////////////////object creation for containing data////////////////////////////
                                pc.setUsername(getIntent().getStringExtra("name").toString());
                                pc.setLang(String.valueOf(myPref.getString("LNG", "0")));
                                pc.setLat(String.valueOf(myPref.getString("LAT", "0")));
                                // pc.setLang(String.valueOf(MainMenuActivity.lang));
                                // pc.setLat(String.valueOf(MainMenuActivity.lat));
                                pc.setEmail(myPref.getString("id", "0"));
                                pc.setDate(String.valueOf(formattedDate));
                                pc.setToken("1");
                                pc.setTstart(timeStampNow);
                                pc.setTend(timeStampLater);
                                pc.setMessage(et_Location.getText().toString());
                                pc.setImenumber(imenumber);
                                pc.setCity(myPref.getString("CITY", "Capital City"));
                                pc.setCountry(myPref.getString("COUNTRY", " "));
                                //////////////////////////////////////////////////////////////////////////////
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("postobject", pc);
                                intentView.putExtras(bundle);
                                startActivity(intentView);
                                PostActivity.this.finish();
                                //return;
                                } else {
                                    requestCurrentLocation();
                                    Toast.makeText(getApplicationContext(),
                                            "Location cannot get,try again", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                pb2.setVisibility(View.INVISIBLE);
                                action_View.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),
                                        "Please fill all fields", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        pb2.setVisibility(View.INVISIBLE);
                        action_View.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(),
                                "Check internet connection and enable Location based services/ GPS", Toast.LENGTH_LONG).show();
                    }

                }
            });

            et_Location.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    et_Location.setHint("");
                    return false;
                }
            });

            if(savedInstanceState != null) {
                Log.i(TAG, "onCreate:: Restoring State ");
                restoreData(savedInstanceState);
            }
        } catch (Exception ex) {
        }
    }

    //Listener for permission
    @Override
    public void onRequestPermissionsResult (int requestCode, String [] permissions, int [] grantResult){
        if(requestCode==222){
            int s=grantResult.length;
            for(int i=0;i<s;i++) {
                if (grantResult.length > 0 && grantResult[i] == PackageManager.PERMISSION_GRANTED
                        && grantResult[i] == PackageManager.PERMISSION_GRANTED) {
                    iv_post_pic_user.setEnabled(true);
                    iv_post_pic_venue.setEnabled(true);
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////
    public static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    ///////////////////////////////////////////////////////////
    private boolean isNotEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent,
                               View v, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void headerHome(View v) {
        Intent intentHome = new Intent(PostActivity.this, MainMenuActivity.class);
        startActivity(intentHome);
    }

    private String validateData() {
        String state = "Please Add:";
        Log.i(TAG, "Time Spinner:: " + timeSpinner.getSelectedItemPosition());
        if (timeSpinner.getSelectedItemPosition() == 0) {
            state = state + "\n Duration";
        }
        return state;
    }

    private int getMinutes(int index) {
        int minutes = 0;
        switch (index) {
            case 1:
                minutes = 120;
                break;
            case 2:
                minutes = 90;
                break;
            case 3:
                minutes = 60;
                pos = "3";
                break;
            case 4:
                minutes = 30;
                break;
            default:
                minutes = 0;
                break;
        }
        return minutes;
    }

    private void takePicture(int requestCode) {
        File f = null;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            f = BitmapUtil.createImageFile(getString(R.string.album_name));
            photoPath = f.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
            startActivityForResult(takePictureIntent, requestCode);

        } catch (IOException e) {
            e.printStackTrace();
            f = null;
            photoPath = null;
        }


    }


    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        Bitmap b = null;
        try {
            switch (requestCode) {
                case USER_PIC_REQUEST:
                    if (resultCode == RESULT_CANCELED) {
                        Log.i(TAG, "user canceled, deleting file...");
                        new File(photoPath).delete();
                    }
                    if (resultCode == RESULT_OK) {
                        try {
                            userPic = photoPath;
                            imagepathfront=photoPath;
                            String[] d = photoPath.split("/");
                            int psthsize=d.length;
                            pc.setFrontpicpath(d[psthsize-1]);
                            Log.i(TAG, "Taken User Pic " + userPic);
                            //user pic bitmap convert into string
                            b = BitmapUtil.getThumb(userPic, thumbHeight, thumbWidth);
                            //String upbitmapstring = BitMapToString(b);
                            String upbitmapstring = encodeToBase64(b, Bitmap.CompressFormat.JPEG, 100);
                           // pc.setFrontpic(upbitmapstring);

                            iv_post_pic_user.setImageBitmap(BitmapUtil.getThumb(userPic, thumbHeight, thumbWidth));
                            Log.i(TAG, "Done with user THUMB ");
                        } catch (Exception ex) {
                            if (b != null) {
                                b.recycle();
                                b = null;
                                b = BitmapUtil.getThumb(userPic, thumbHeight, thumbWidth);
                                //String upbitmapstring = BitMapToString(b);
                                String upbitmapstring = encodeToBase64(b, Bitmap.CompressFormat.JPEG, 100);
                                //pc.setFrontpic(upbitmapstring);

                                iv_post_pic_user.setImageBitmap(BitmapUtil.getThumb(userPic, thumbHeight, thumbWidth));

                            }
                        }
                    }
                    break;

                case VENUE_PIC_REQUEST:
                    if (resultCode == RESULT_OK) {
                        venuePic = photoPath;
                        imagepathback=photoPath;
                        String[] d = photoPath.split("/");
                        int psthsize=d.length;
                        pc.setBackpicpath(d[psthsize-1]);
                        Log.i(TAG, "Taken Venue Pic " + venuePic);
                        try {

                            //Venu pic bitmap convert into string
                            b = BitmapUtil.getThumb(venuePic, thumbHeight, thumbWidth);
                            // String vpbitmapstring = BitMapToString(b);
                            String vpbitmapstring = encodeToBase64(b, Bitmap.CompressFormat.JPEG, 100);
                            //pc.setBackpic(vpbitmapstring);

                            iv_post_pic_venue.setImageBitmap(BitmapUtil.getThumb(venuePic, thumbHeight, thumbWidth));
                            Log.i(TAG, "Done with Venue THUMB ");
                        } catch (Exception e4) {
                            if (b != null) {
                                b.recycle();
                                b = null;
                                b = BitmapUtil.getThumb(venuePic, thumbHeight, thumbWidth);
                                // String vpbitmapstring = BitMapToString(b);
                                String vpbitmapstring = encodeToBase64(b, Bitmap.CompressFormat.JPEG, 100);
                               // pc.setBackpic(vpbitmapstring);

                                iv_post_pic_venue.setImageBitmap(BitmapUtil.getThumb(venuePic, thumbHeight, thumbWidth));

                            }
                        }
                    }
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    private void galleryAddPic(String mCurrentPhotoPath) {
        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        saveData(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "onRestoreInstaecState -- Started");
        restoreData(savedInstanceState);
    }

    public void saveData(Bundle savedInstanceState) {
        savedInstanceState.putString(CURRENT_PIC, photoPath);
        if (!et_Location.getText().equals(null)) {
            Log.i(TAG, "onSaveInstaecState::Location " + et_Location.getText().toString());
            savedInstanceState.putString(LOCATION, et_Location.getText().toString());
        }
        Log.i(TAG, "onSaveInstaecState::User Pic " + userPic);
        savedInstanceState.putString(USER_PIC, userPic);
        Log.i(TAG, "onSaveInstaecState::Venue Pic " + venuePic);
        savedInstanceState.putString(VENUE_PIC, venuePic);
        //savedInstanceState.putInt("MOOD_KEY", moodSpinner.getSelectedItemPosition());
        savedInstanceState.putInt("TIME_KEY", timeSpinner.getSelectedItemPosition());
    }

    public void restoreData(Bundle savedInstanceState) {
        photoPath = savedInstanceState.getString(CURRENT_PIC);

        String temp = savedInstanceState.getString(LOCATION);
        if (!(temp.equals(null))) {
            et_Location.setText(temp);
            Log.i(TAG, "Restored Location::" + et_Location.getText());
        }
        temp = savedInstanceState.getString(USER_PIC);
        if (!(temp.equals("#"))) {
            userPic = temp;
            Log.i(TAG, "Restored User Pic::" + userPic);
            iv_post_pic_user.setImageBitmap(BitmapUtil.getThumb(userPic, thumbHeight, thumbWidth));
        }
        temp = savedInstanceState.getString(VENUE_PIC);
        if (!(temp.equals("#"))) {
            venuePic = temp;
            Log.i(TAG, "Restored Venue Pic::" + venuePic);
            iv_post_pic_venue.setImageBitmap(BitmapUtil.getThumb(venuePic, thumbHeight, thumbWidth));
        }
        int tempPos = savedInstanceState.getInt("MOOD_KEY");
        /*if (tempPos != 0) {
            Log.i(TAG, "Restored Mood Spinner ::" + tempPos);
            moodSpinner.setSelection(tempPos);
        }*/
        tempPos = savedInstanceState.getInt("TIME_KEY");
        if (tempPos != 0) {
            Log.i(TAG, "Restore Time Spinner ::" + tempPos);
            timeSpinner.setSelection(tempPos);
        }
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            display = PostWebService.postDataServer(pc.getUsername(), pc.getFrontpic(), pc.getBackpic(), pc.getMessage(), pc.getTstart(), pc.getTend(), pc.getDate(), pc.getLat(), pc.getLang(), pc.getStatusmood(), pc.getEmail(), imenumber, pc.getCity(), pc.getCountry(),pc.getFrontpicpath(),pc.getBackpicpath(), "insertPost"); //emplogin method name
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                pb.setVisibility(View.INVISIBLE);
                action_Post.setVisibility(View.VISIBLE);
                String[] dis = display.split(",");
                if (dis[0].equals("true")) {
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("tokenss", dis[1]);
                    editor.apply();
                    MainMenuActivity.ac.finish();
                    finish();
                    Toast.makeText(getApplicationContext(),
                            "Post submitted successfully", Toast.LENGTH_LONG).show();
                    Intent intentPost = new Intent(PostActivity.this, MainMenuActivity.class);
                    startActivity(intentPost);
                    PostActivity.this.finish();
                } else if (dis[0].equals("D:2")) {
                    Toast.makeText(getApplicationContext(),
                            "Please wait till your last post expires", Toast.LENGTH_LONG).show();
                } else if (dis[0].equals("not register")) {
                    Toast.makeText(getApplicationContext(),
                            ":Please create an account first:", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Post submission failed, check internet connection:  ", Toast.LENGTH_LONG).show();
                }
                //tv.setText(fahren + "° F");
            } catch (Exception ex) {
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

    public class CustomTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //((TextView) view).setTextColor(0xFFFFFFFF); // white
                    ((TextView) view).setTypeface(((TextView) view).getTypeface(), Typeface.BOLD_ITALIC);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    ((TextView) view).setTextColor(Color.parseColor("#4a4a4a")); // lightblack
                    ((TextView) view).setTypeface(((TextView) view).getTypeface(), Typeface.BOLD_ITALIC);
                    break;
            }
            return false;
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
        Bitmap scaled = Bitmap.createScaledBitmap(bm, 720, 480, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
///////////////////////////////////////////////////////////////////////////Location Receiver///////////////////////
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
        if(myPref.getString("COUNTRY", " ") != " "){
            // Toast.makeText(getApplicationContext(),"Get Location",Toast.LENGTH_LONG).show();
        }else{
            // Toast.makeText(getApplicationContext(),"Not Get Location",Toast.LENGTH_LONG).show();
            requestCurrentLocation();
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
        startService(new Intent(this, DataService.class).setAction(DataService.ACTION_GET_CURRENT_LOCATION));
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
