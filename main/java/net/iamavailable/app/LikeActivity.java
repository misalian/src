package net.iamavailable.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class LikeActivity extends Activity {

   public static SharedPreferences myPref;
    private final String TAG = "PostScreen1";
    double latitude = 0, longitude = 0;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    ImageView iv_user_pic, iv_venue_pic;
    private GoogleMap googleMap;
    private MarkerOptions marker;
    private TextView postStatus, tv_date, tv_duration, tv_status, tv_location,name;
    Bundle profile1;
    ViewPager viewPager;
    PagerAdapter adapter;
    Context context;
    public static Activity likactivity;
    TextView like_btn_like;
    public static TextView likes,dollars;
   public static String[] item;
    public static String[] arr;
    public static String id,em;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_like);
        try {
            likactivity=this;
            context = LikeActivity.this;
            likes=(TextView) findViewById(R.id.likes);
            dollars=(TextView) findViewById(R.id.dollars);
           // item = getIntent().getStringArrayExtra("displayarray");
            item=CheckActivity.display;


            arr=new String[CheckActivity.size];
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
            adapter = new ViewPagerAdapter(LikeActivity.this);
            viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(10);
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
        Intent intentHome = new Intent(LikeActivity.this, MainMenuActivity.class);
        startActivity(intentHome);
    }
}
