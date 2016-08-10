package net.iamavailable.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Context;
import android.widget.LinearLayout;
import android.content.SharedPreferences;
import android.content.Intent;
import android.view.View;
public class Help extends AppCompatActivity {
    TextView lik, doll;
    LinearLayout le;
    SharedPreferences myPref;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        myPref = getSharedPreferences(IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
        le=(LinearLayout) findViewById(R.id.header);
        lik=(TextView) le.findViewById(R.id.likes);
        doll=(TextView) le.findViewById(R.id.dollars);
        doll.setText(myPref.getString("tokenss", " "));
        lik.setText(myPref.getString("likess", " "));
    }

    public void headerHome(View v){
        Intent intentHome = new Intent(Help.this, MainMenuActivity.class);
        startActivity(intentHome);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_help, menu);
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
}
