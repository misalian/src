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

public class HistoryLikeActivity extends AppCompatActivity {
    public static TextView likes,dollars;
    public static SharedPreferences myPref;
    public static String[] item;
    public static String[] arr;
    public static Activity likactivity;
    ViewPager viewPager;
    PagerAdapter adapter;
    Context context;
    public static String id,em;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_like);
        try {
            likactivity=this;
            context = HistoryLikeActivity.this;
            likes=(TextView) findViewById(R.id.likes);
            dollars=(TextView) findViewById(R.id.dollars);
            // item = getIntent().getStringArrayExtra("displayarray");
            item=ViewPagerAdapter.display;


            arr=new String[ViewPagerAdapter.size];
            int k=0;
            for(int i=5;i<item.length;i++){
                if(((item.length)-i)!=12) {
                    arr[k] = item[i];
                }
                i=i+19;
                k++;
            }
            myPref = getSharedPreferences(Splash.IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);

            viewPager = (ViewPager) findViewById(R.id.like_page);
            adapter = new ViewPagerAdapterHistory(HistoryLikeActivity.this);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(1);
        }catch(Exception ex){}
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public void likeAction(String id1)
    {
        id=id1;
        CustomDialogClass cdd = new CustomDialogClass(likactivity);
        cdd.show();
        // Intent intentLike = new Intent(LikeActivity.this, MessageActivity.class);
        //startActivity(intentLike);
    }

    public void headerHome(View v){
        Intent intentHome = new Intent(HistoryLikeActivity.this, MainMenuActivity.class);
        startActivity(intentHome);
    }
}
