package net.iamavailable.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class HistoryDataCurrentUser extends AppCompatActivity {
    public static TextView likes,dollars;
    public static SharedPreferences myPref;
    public static String[] item;
    public static String[] arr;
    Context context;
    public static String id;
    public static int locid;
    public static int counter=0,numpost=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_data_current_user);
        try {
            final View vi = findViewById(R.id.fragment_place);
            counter = getIntent().getIntExtra("counter", 0);
            item = HistoryList.display;
            arr = new String[HistoryList.size];
            likes = (TextView) findViewById(R.id.likes);
            dollars = (TextView) findViewById(R.id.dollars);

            FragmentOne fr = new FragmentOne();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            //fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
            fragmentTransaction.replace(R.id.fragment_place, fr);
            fragmentTransaction.commit();
            vi.setOnTouchListener(new OnSwipeTouchListener(HistoryDataCurrentUser.this) {
                public void onSwipeTop() {
                }

                public void onSwipeRight() {
                    counter = counter - 20;
                    if (counter < 0) {
                        counter = counter + 20;
                    } else {
                        numpost--;
                        FragmentOne fr = new FragmentOne();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        //fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left);
                        fragmentTransaction.replace(R.id.fragment_place, fr);
                        fragmentTransaction.commit();
                    }

                }

                public void onSwipeLeft() {
                    counter = counter + 20;
                    if (counter >= item.length) {
                        counter = counter - 20;
                    } else {
                        numpost++;
                        FragmentOne fr = new FragmentOne();
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        //fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
                        fragmentTransaction.replace(R.id.fragment_place, fr);
                        fragmentTransaction.commit();
                    }
                }

                public void onSwipeBottom() {
                }

            });
        }catch (Exception pe){}
    }
    public void headerHome(View v){
        Intent intentHome = new Intent(HistoryDataCurrentUser.this, MainMenuActivity.class);
        startActivity(intentHome);
    }

}
