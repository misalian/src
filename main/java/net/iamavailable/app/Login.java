package net.iamavailable.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import net.iamavailable.app.PushNotificationClassess.MyFirebaseInstanceIDService;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Random.*;

public class Login extends AppCompatActivity {
    String email,password,test,display;
    TextView register,login,forgotpass;
    EditText e,p,t;
    ProgressBar pb;
    int random;
    LocationManager locationManager;
    boolean isGPSEnable=false;
    Handler mHandler = new Handler();
    private static double longitude = 0.0, lattitude = 0.0;
    private static String userName = null, cityName = null, countryName = null, id=null;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    public static final String TAG = "IAA";
    SharedPreferences myPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            Random r = new Random();
            final int ran = r.nextInt(10);
            t = (EditText) findViewById(R.id.test);
            t.setHint("What is 2 * " + ran);

            locationManager = locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            myPref = getSharedPreferences(Splash.IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            pb = (ProgressBar) findViewById(R.id.progressBar4);
            e = (EditText) findViewById(R.id.email);
            p = (EditText) findViewById(R.id.password);

            register = (TextView) findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Login.this, Register.class);
                    startActivity(i);
                    finish();
                }
            });
            login = (TextView) findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        email = e.getText().toString().trim();
                        password = p.getText().toString();
                        test = t.getText().toString();
                        if (Double.parseDouble(test) == (2 * ran)) {
                            isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            if (isEmailValid(email)) {
                                if(!isGPSEnable){
                                    Toast.makeText(getApplicationContext(), "Enabled your Location based services/ GPS", Toast.LENGTH_LONG).show();

                                }else {
                                    if(myPref.getString("id"," ").equals(" ")){
                                        String reg=MyFirebaseInstanceIDService.refreshedToken;
                                        if(reg!=null){
                                            SharedPreferences.Editor editor = myPref.edit();
                                            editor.putString("pushtoken",reg);
                                            editor.apply();
                                        }else{
                                            reg=myPref.getString("pushtoken"," ");
                                        }
                                        try{
                                            int len=reg.length();
                                            AsyncCallWS ac = new AsyncCallWS();
                                            ac.execute();
                                        }catch(Exception pt){
                                            Toast.makeText(getApplicationContext(), "Check internet connection and try again", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
                                        AsyncCallWS ac = new AsyncCallWS();
                                        ac.execute();
                                    }
                                }

                            } else {
                                Toast.makeText(getApplicationContext(),  "Email Address cannot be verified", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid Captcha Please Re-Enter", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception ex) {
                        Toast.makeText(getApplicationContext(), "Please fills all fields", Toast.LENGTH_LONG).show();
                    }
                }
            });
            forgotpass = (TextView) findViewById(R.id.frogetpass);
            forgotpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Login.this, Forgotpass.class);
                    startActivity(i);
                }
            });
        }catch (Exception ex){}

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            display = PostWebService.checkLogin(email, password, myPref.getString("pushtoken"," ") ,"userLogin"); //emplogin method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                pb.setVisibility(View.INVISIBLE);
                String[] d = display.split(",");
                if (d[0].equals("true")) {
                    Intent intentPost = new Intent(Login.this, MainMenuActivity.class);
                    intentPost.putExtra("name", d[1]);
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("id",email);
                    editor.putString("password",password);
                    editor.putString("NAME", d[1]);
                    editor.apply();
                    startActivity(intentPost);
                    finish();
                } else if(d[0].equals("false")){
                    Toast.makeText(getApplicationContext(),
                            "Login failed, invalid email address or password", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(),
                            "Login faild, please check internet connection", Toast.LENGTH_LONG).show();
                }
                //tv.setText(fahren + "° F");
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),
                        "Login faild, please check internet connection", Toast.LENGTH_LONG).show();
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
    public void headerHome(View v){

    }
}
