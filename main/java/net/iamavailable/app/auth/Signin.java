package net.iamavailable.app.auth;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import net.iamavailable.app.R;
import net.iamavailable.app.data.DataService;

public class Signin extends ActionBarActivity {


    BroadcastReceiver receiverReady = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startService(new Intent(DataService.ACTION_GET_DISPLAY_NAME));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_signin);
    }



    public void onSignoutClicked(View v) {
    }


}

