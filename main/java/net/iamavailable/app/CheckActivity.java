package net.iamavailable.app;

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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import net.iamavailable.app.PushNotificationClassess.MyFirebaseInstanceIDService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckActivity extends Activity implements AdapterView.OnItemSelectedListener{

   public static TextView action_Check;
    SharedPreferences myPref;
    Spinner moodSpinner;
    Spinner locSpinner;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    String loc,mood,email,formattedDate,stime,chkemail;
    public static String[] display;
    double lat,lang;
    public static  int size;
    public  static String email1, country, city;
    private static final String[] moodItems ={"Select Mood   (Optional)", "Anything and Everything", "Frisky", "Bored", "Thirsty", "Zonked", "Newbie"};
    private static final String[] timeItems ={"Select a Duration  (Optional)", "Available for 2.0 Hour", "Available for 1.5 Hour", "Available for 1.0 Hour", "Available for 0.5 Hour"};
   // private static final String[] locations ={"Select a Location  ", "50m radius", "100m radius", "1000m radius", "5km radius", "10km radius", "50km radius"};
    public static ProgressBar pb;
    TextView lik, doll;
    LinearLayout le;
    EditText checkemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    try{
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(cal.getTime());
        stime = new SimpleDateFormat("HH:mm:ss").format(cal.getTime());
        setContentView(R.layout.activity_check);
        myPref = getSharedPreferences(IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
        email1 = myPref.getString("id", "0");
            locSpinner = (Spinner) findViewById(R.id.check_location);
        lat = Double.parseDouble(myPref.getString("LAT", "0"));
        lang = Double.parseDouble(myPref.getString("LNG", "0"));
        try {
                String[] locations={"Select a Location  ", "10m radius", "50m radius", "100m radius", "1000m radius", "5km radius", "10km radius", "50km radius",myPref.getString("CITY","Capital City"),myPref.getString("COUNTRY"," ")};
                locSpinner.setOnItemSelectedListener(this);
                ArrayAdapter<String> timeSpinnerAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item2, locations);
                timeSpinnerAdapter.setDropDownViewResource(
                        android.R.layout.simple_spinner_dropdown_item);
                locSpinner.setAdapter(timeSpinnerAdapter);
            }catch (Exception ep){}

        pb = (ProgressBar) findViewById(R.id.progressBar3);
        action_Check = (TextView) findViewById(R.id.check_btn_check);
        checkemail=(EditText) findViewById(R.id.check_email);

            le=(LinearLayout) findViewById(R.id.header);
            lik=(TextView) le.findViewById(R.id.likes);
            doll=(TextView) le.findViewById(R.id.dollars);
            doll.setText(myPref.getString("tokenss", " "));
            lik.setText(myPref.getString("likess", " "));

            email = myPref.getString("id", "0");

        action_Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // action_Check.setOnTouchListener(new CustomTouchListener());
                loc = locSpinner.getSelectedItem().toString();
                chkemail=checkemail.getText().toString().trim();
                if(loc!="Select a Location  " || chkemail.length()!=0) {
                    int count=0;
                        if (loc != "Select a Location  " && chkemail.length()==0) {
                            action_Check.setVisibility(View.GONE);
                            if (ContextCompat.checkSelfPermission(CheckActivity.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(CheckActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 123);
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
                            if (loc.equals("10m radius")) {
                                loc = "10.0";
                            }
                            if (loc.equals("50m radius")) {
                                loc = "50.0";
                            }
                            if (loc.equals("100m radius")) {
                                loc = "100.0";
                            }
                            if (loc.equals("1000m radius")) {
                                loc = "1000.0";
                            }
                            if (loc.equals("5km radius")) {
                                loc = "5000.0";
                            }
                            if (loc.equals("10km radius")) {
                                loc = "10000.0";
                            }
                            if (loc.equals("50km radius")) {
                                loc = "50000.0";
                            }
                            if (loc.equals(myPref.getString("CITY", "Capital City"))) {
                                loc = "city";
                            }
                            if (loc.equals(myPref.getString("COUNTRY", " "))) {
                                loc = "country";
                            }
                            AsyncCallWS ac = new AsyncCallWS();
                            ac.execute();
                        } /*else {
                            Toast.makeText(getApplicationContext(), "Please pick a radius around your location", Toast.LENGTH_LONG).show();
                        }*/

                        if (chkemail.length() != 0) {
                            if (isEmailValid(chkemail)) {
                                action_Check.setVisibility(View.GONE);
                                if (ContextCompat.checkSelfPermission(CheckActivity.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(CheckActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 123);
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
                                                "Signals strength weak,it take sometime", Toast.LENGTH_LONG).show();
                                    }
                                }
                                loc = chkemail;
                                AsyncCallWS ac = new AsyncCallWS();
                                ac.execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Email Address cannot be verified", Toast.LENGTH_LONG).show();
                            }
                        }

                }else{
                    Toast.makeText(getApplicationContext(), "Please fill one of the field", Toast.LENGTH_LONG).show();
                }
            }
        });

    }catch(Exception ex){}
    }

    public void onItemSelected(AdapterView<?> parent,
                               View v, int position, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void headerHome(View v){
        Intent intentHome = new Intent(CheckActivity.this, MainMenuActivity.class);
        startActivity(intentHome);
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            try {
                display = PostWebService.getSelectedPostServer(loc, email, formattedDate, stime, "getSelectedPost"); //emplogin method name
            }catch(Exception ep){}
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
               // pb.setVisibility(View.INVISIBLE);
               // action_Check.setVisibility(View.VISIBLE);
                if(display.length!=1) {
                    if (display.length > 5) {
                        profile.flag=0;
                        size = (display.length) / 20;     // This size is for showing number of record on slides in LikeActivity
                        Intent intentCheck = new Intent(CheckActivity.this, LikeActivity.class);
                       // intentCheck.putExtra("displayarray", display);
                        startActivity(intentCheck);
                        CheckActivity.this.finish();
                    } else {
                        pb.setVisibility(View.INVISIBLE);
                        action_Check.setVisibility(View.VISIBLE);
                        Toast.makeText(getApplicationContext(), "No one is available near your location at this time" , Toast.LENGTH_LONG).show();
                    }
                }else if(display[0].equals("TokenFinish")){
                    pb.setVisibility(View.INVISIBLE);
                    action_Check.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),
                            "Tokens finish, Make a post to earn tokens", Toast.LENGTH_LONG).show();

                }else if(display[0].equals("FirstEnterPost")){
                    pb.setVisibility(View.INVISIBLE);
                    action_Check.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),
                            "Please make a post before checking available posts", Toast.LENGTH_LONG).show();
                }else{
                    pb.setVisibility(View.INVISIBLE);
                    action_Check.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_LONG).show();
                }

                //tv.setText(fahren + "° F");
            }catch(Exception ex){
                pb.setVisibility(View.INVISIBLE);
                action_Check.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Please check internet connection",Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onPreExecute() {
              pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }


    public class CustomTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ((TextView) view).setTextColor(0xFFFFFFFF); // white
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    ((TextView) view).setTextColor(Color.parseColor("#4a4a4a")); // lightblack
                    break;
            }
            return false;
        }
    }


    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        MainMenuActivity.ac.finish();
        finish();
        startActivity(new Intent(CheckActivity.this, MainMenuActivity.class));


    }
    @Override
    public void onRequestPermissionsResult (int requestCode, String [] permissions, int [] grantResult){
        if(requestCode==229){
            if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){

            }
        }
    }
}
