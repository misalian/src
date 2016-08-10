package net.iamavailable.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends Activity {
    public static SharedPreferences myPref;
    private final String TAG = "PostScreen1";
    TextView un,pn;
    String name;
    TextView lik, doll;
    LinearLayout le;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        try {
            LikeActivity.likactivity.finish();
            for(int i=5;i<LikeActivity.item.length;i++){
                if((LikeActivity.item[i]).equals(LikeActivity.id)){
                    name=LikeActivity.item[i-4];
                }
                i=i+19;
            }
            myPref = getSharedPreferences(Splash.IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            le=(LinearLayout) findViewById(R.id.header);
            lik=(TextView) le.findViewById(R.id.likes);
            doll=(TextView) le.findViewById(R.id.dollars);
            doll.setText(myPref.getString("tokenss", " "));
            lik.setText(myPref.getString("likess", " "));


            un = (TextView) findViewById(R.id.message_intricate);
            un.setText(myPref.getString("NAME", " ").toUpperCase() + "\n LUCID");

            pn = (TextView) findViewById(R.id.message_liked);

            pn.setText("YOU HAVE LIKED \n" + name.toUpperCase());
        }catch (Exception ex){}
    }

    public void headerHome(View v){
        MainMenuActivity.ac.finish();
        finish();
        Intent intentHome = new Intent(MessageActivity.this, MainMenuActivity.class);
        startActivity(intentHome);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        finish();
        startActivity(new Intent(MessageActivity.this, CheckActivity.class));

    }
}
