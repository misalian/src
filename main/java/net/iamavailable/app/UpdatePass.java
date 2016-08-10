package net.iamavailable.app;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpdatePass extends AppCompatActivity {
    EditText op;
    EditText np;
    EditText rnp;
    TextView up;
    String newpass,renewpass,display,email,imenumber,oldpass;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    SharedPreferences myPref;
    ProgressBar pb;
    public static TextView lik, doll;
    LinearLayout le;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pass);
        try {
            myPref = getSharedPreferences(IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            email = myPref.getString("id", "0");
            pb = (ProgressBar) findViewById(R.id.progressBar33);
            op = (EditText) findViewById(R.id.oldpass);
            np = (EditText) findViewById(R.id.newpassword);
            rnp = (EditText) findViewById(R.id.renewpassword);

            le=(LinearLayout) findViewById(R.id.header);
            lik=(TextView) le.findViewById(R.id.likes);
            doll=(TextView) le.findViewById(R.id.dollars);
            doll.setText(myPref.getString("tokenss", " "));
            lik.setText(myPref.getString("likess", " "));

            up = (TextView) findViewById(R.id.updatepass);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    up.setTypeface(up.getTypeface(), Typeface.BOLD);
                    try {
                        oldpass=op.getText().toString();
                        newpass = np.getText().toString();
                        renewpass = rnp.getText().toString();
                        if(newpass.matches("")|| renewpass.matches("")||oldpass.matches("")){
                            Toast.makeText(getApplicationContext(), "Please fills all fields", Toast.LENGTH_LONG).show();
                        }else {
                            if (newpass.equals(renewpass)) {
                                imenumber = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                AsyncCallWS ac = new AsyncCallWS();
                                ac.execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Password mismatch", Toast.LENGTH_LONG).show();
                            }
                        }
                    }catch (Exception ex1){
                    }
                }
            });
        }catch (Exception ex){}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_pass, menu);
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
            display = PostWebService.updatePass(email, imenumber,newpass,oldpass,"updatePassword"); //emplogin method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                pb.setVisibility(View.INVISIBLE);
                if (display.equals("true")) {
                    Toast.makeText(getApplicationContext(),
                            "Password Successfuly Updated", Toast.LENGTH_LONG).show();
                    Intent intentPost = new Intent(UpdatePass.this, MainMenuActivity.class);
                    startActivity(intentPost);
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Password not Updated Successfuly", Toast.LENGTH_LONG).show();
                }
                //tv.setText(fahren + "° F");
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),
                        "Check Internet Connection", Toast.LENGTH_LONG).show();
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
        Intent intentHome = new Intent(UpdatePass.this, MainMenuActivity.class);
        startActivity(intentHome);
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        startActivity(new Intent(UpdatePass.this, profile.class));


    }
}
