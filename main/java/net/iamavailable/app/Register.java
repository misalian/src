package net.iamavailable.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
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

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {
    String username,email,password,repassword,test,display,imenumber;
    TextView register;
    EditText u,e,p,rp,t;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        try {

            Random r=new Random();
            final int ran = r.nextInt(10);
            t = (EditText) findViewById(R.id.test);
            t.setHint("What is 2 * "+ran);

            pb=(ProgressBar) findViewById(R.id.progressBar3);
            u = (EditText) findViewById(R.id.username);
            e = (EditText) findViewById(R.id.email);
            p = (EditText) findViewById(R.id.password);
            rp = (EditText) findViewById(R.id.repassword);
            t = (EditText) findViewById(R.id.test);
            register = (TextView) findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        username = u.getText().toString();
                        email = e.getText().toString().trim();
                        password = p.getText().toString();
                        repassword = rp.getText().toString();
                        test = t.getText().toString();
                            if (Double.parseDouble(test) == (2*ran)) {
                                if (password.equals(repassword)) {
                                    if (isEmailValid(email)) {
                                        imenumber = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                                Settings.Secure.ANDROID_ID);
                                        AsyncCallWS ac = new AsyncCallWS();
                                        ac.execute();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Email Address cannot be verified", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid Captcha Please Re-Enter", Toast.LENGTH_LONG).show();
                            }

                    }catch (Exception ex){
                        Toast.makeText(getApplicationContext(), "Please fills all fields", Toast.LENGTH_LONG).show();
                    }
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
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
            display = PostWebService.registerU(username,email,password,imenumber,"registerUser"); //emplogin method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                pb.setVisibility(View.INVISIBLE);
                if (display.equals("true")) {
                    Toast.makeText(getApplicationContext(),
                            "Registration successful", Toast.LENGTH_LONG).show();
                    Intent intentPost = new Intent(Register.this, Login.class);
                    startActivity(intentPost);
                    finish();
                }else if(display.equals("allreadyregistered")){
                    Toast.makeText(getApplicationContext(),
                            "Email address already registered", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(),
                            "Registration failed, check internet connection", Toast.LENGTH_LONG).show();
                }
                //tv.setText(fahren + "° F");
           }catch (Exception ex){
                Toast.makeText(getApplicationContext(),
                        "Failed, check internet connection", Toast.LENGTH_LONG).show();
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
    public void headerHome(View v){
        Intent intentHome = new Intent(Register.this, Login.class);
        startActivity(intentHome);
    }
}
