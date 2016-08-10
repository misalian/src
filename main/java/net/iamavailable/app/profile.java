package net.iamavailable.app;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class profile extends AppCompatActivity {

    TextView logout,history,changepass;
    ProgressBar pb;
    public static SharedPreferences myPref;
    Activity activity;
    public static String[] display;
    public static int size;
    String formattedDate;
    public static int size2;
    public static int flag=0;
    TextView lik, doll;
    LinearLayout le;
    ListView lv;
    ProgressDialog pd;
    String display2;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = df.format(cal.getTime());
            myPref = getSharedPreferences(Splash.IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            le=(LinearLayout) findViewById(R.id.include);
            lik=(TextView) le.findViewById(R.id.likes);
            doll=(TextView) le.findViewById(R.id.dollars);
            doll.setText(myPref.getString("tokenss", " "));
            lik.setText(myPref.getString("likess", " "));


            activity=this;
            logout = (TextView) findViewById(R.id.logout);
            history = (TextView) findViewById(R.id.history);
            changepass = (TextView) findViewById(R.id.change);
            pb = (ProgressBar) findViewById(R.id.progressBar7);
           // pb = (ProgressBar) findViewById(R.id.progressBar77);

            lv=(ListView) findViewById(R.id.listView);
            String[] values = new String[] { "  Log out","  History"," Change Password","  Help"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);
            //ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listView, values);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //String selectedFromList = (lv.getItemAtPosition(i).get);
                    if(i==0){
                        count=1;
                        logout.setTypeface(logout.getTypeface(), Typeface.BOLD);
                        showAlert();

                    }else if(i==1){
                        count=2;
                    history.setTypeface(history.getTypeface(), Typeface.BOLD);
                    history.setVisibility(View.INVISIBLE);
                    if (ContextCompat.checkSelfPermission(profile.this, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions(profile.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 123);
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

                        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                        NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                        if(mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                            AsyncCallWS ac = new AsyncCallWS();
                            ac.execute();
                        }else{
                            Toast.makeText(getApplicationContext(),"Check internet connection.",Toast.LENGTH_LONG).show();

                        }

                }else if(i==2){
                    changepass.setTypeface(changepass.getTypeface(), Typeface.BOLD);
                    Intent intent = new Intent(profile.this, UpdatePass.class);
                    startActivity(intent);
                }else{
                        Intent intent = new Intent(profile.this, Help.class);
                        startActivity(intent);
                    }
            }}
            );
        }catch (Exception ex){}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
                display2 = PostWebService.delCurrentPost(profile.myPref.getString("id"," "), formattedDate, "deleteCurrentPost"); //emplogin method name
            }
            if(count==2){
                display = PostWebService.postHistory(myPref.getString("id", " "),formattedDate, "getPostHistory1"); //emplogin method name
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if(count==1){
                count=0;
                pd.dismiss();
                try {
                    if (display2.equals("true")) {
                        Toast.makeText(getApplicationContext(),
                                "Logged out Successfully",
                                Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = profile.myPref.edit();
                        editor.remove("id").commit();
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else if (display2.equals("false")) {
                        Toast.makeText(getApplicationContext(),
                                "Logged out Successfully",
                                Toast.LENGTH_LONG).show();
                        SharedPreferences.Editor editor = profile.myPref.edit();
                        editor.remove("id").commit();
                        Intent i = new Intent(getApplicationContext(), Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Logout failed, try again",
                                Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ep){
                    Toast.makeText(getApplicationContext(),
                            "Logout failed, try again",
                            Toast.LENGTH_LONG).show();
                }
            }
            if(count==2) {
                count=0;
                try {
                    pb.setVisibility(View.INVISIBLE);
                    if (display.length != 1) {
                        Intent intentCheck = new Intent(profile.this, HistoryList.class);
                        //intentCheck.putExtra("displayarray", display);
                        startActivity(intentCheck);

                    }else if(display[0].equals("false")){
                        Toast.makeText(getApplicationContext(),
                                "There are no posts in the history", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Check internet connection.", Toast.LENGTH_LONG).show();
                    }

                    //tv.setText(fahren + "° F");
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), "Check internet connection.", Toast.LENGTH_LONG).show();
                }
            }


        }

        @Override
        protected void onPreExecute() {
            if(count==1){
                pd.show();
            }
            if(count==2) {
                pb.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    public void headerHome(View v){
        MainMenuActivity.ac.finish();
        finish();
        startActivity(new Intent(profile.this, MainMenuActivity.class));
    }

    public void showAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Logout Message");

        // Setting Dialog Message
        alertDialog.setMessage("Your active post will be deleted, if you logout.\n");

        // On pressing Settings button
        alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                pd = new ProgressDialog(profile.this);
                pd.setMessage("Logout process, please wait..");
                AsyncCallWS ac=new AsyncCallWS();
                ac.execute();
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
    public void onRequestPermissionsResult (int requestCode, String [] permissions, int [] grantResult){
        if(requestCode==229){
            if(grantResult.length>0 && grantResult[0] == PackageManager.PERMISSION_GRANTED){

            }
        }
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        MainMenuActivity.ac.finish();
        finish();
        startActivity(new Intent(profile.this, MainMenuActivity.class));
    }

}
