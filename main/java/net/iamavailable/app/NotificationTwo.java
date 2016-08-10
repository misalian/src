package net.iamavailable.app;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;
import android.app.NotificationManager;
import android.widget.Toast;

import net.iamavailable.app.PushNotificationClassess.MyFirebaseMessagingService;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.net.Uri;

import com.squareup.picasso.Picasso;
import java.io.File;
import android.graphics.Bitmap;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.*;
import java.io.*;

public class NotificationTwo extends AppCompatActivity {

    public static SharedPreferences myPref;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    private final String TAG = "PostScreen1";
    private TextView postStatus, tv_date, tv_duration,lik, tv_status, tv_location,name, dollars,likes;
    TextView ebutton;
    public static String idrecord2;
    String[] item,pic;
    String display;
    Activity a;
    ImageView iv_user_pic, iv_venue_pic,place,group;
    ProgressBar pb;
    EditText done;
    String name1;
    PostClass pc;
    ProgressDialog pd;
    LocationManager locationManager;
    boolean isGPSEnable=false;
    int picfound=0;
    Bitmap b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            a=this;
           // pc= MyFirebaseMessagingService.ps;
            setContentView(R.layout.activity_notification_two);
            pc=(PostClass)getIntent().getSerializableExtra("postdata");
            int nid=Integer.parseInt(getIntent().getStringExtra("nid"));
            cancelNotification(nid);
            pb = (ProgressBar) findViewById(R.id.progressBar5);
            myPref = getSharedPreferences(IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            done=(EditText) findViewById(R.id.done);
            name = (TextView) findViewById(R.id.like_intricate);
            tv_date = (TextView) findViewById(R.id.like_date);
            tv_duration = (TextView) findViewById(R.id.like_session);
            tv_status = (TextView) findViewById(R.id.like_alias);
            postStatus = (TextView) findViewById(R.id.post2_btn_post);
            tv_location = (TextView) findViewById(R.id.like_address);
            iv_user_pic = (ImageView) findViewById(R.id.like_pic_user);
            iv_venue_pic = (ImageView) findViewById(R.id.like_pic_venue);
            lik=(TextView) findViewById(R.id.like_post_likes);
            likes=(TextView) findViewById(R.id.likes);
            dollars=(TextView) findViewById(R.id.dollars);
            place = (ImageView) findViewById(R.id.like_place);
            group=(ImageView) findViewById(R.id.like_group);

            name1=pc.getUsername();
            name.setText((pc.getUsername()).toUpperCase() + "\nLUCID");
            try {
                if(ImageStorage.checkifImageExists(pc.getFrontpic()))
                {
                    File file = ImageStorage.getImage(pc.getFrontpic());
                    String path = file.getAbsolutePath();
                    if (path != null){
                        b = BitmapFactory.decodeFile(path);
                        iv_user_pic.setImageBitmap(b);
                    }
                } else {
                    Picasso.with(NotificationTwo.this)
                            .load("http://cust-fyp-projects.com/thumb/" + pc.getFrontpic())
                            .into(iv_user_pic);
                    new GetImages("http://cust-fyp-projects.com/images/" + pc.getFrontpic(), iv_user_pic, pc.getFrontpic()).execute() ;
                }


                if(ImageStorage.checkifImageExists(pc.getBackpic()))
                {
                    File file = ImageStorage.getImage(pc.getBackpic());
                    String path = file.getAbsolutePath();
                    if (path != null){
                        b = BitmapFactory.decodeFile(path);
                        iv_venue_pic.setImageBitmap(b);
                    }
                } else {
                    Picasso.with(NotificationTwo.this)
                            .load("http://cust-fyp-projects.com/thumb/" + pc.getBackpic())
                            .into(iv_venue_pic);
                    new GetImages("http://cust-fyp-projects.com/images/" + pc.getBackpic(), iv_venue_pic, pc.getBackpic()).execute() ;
                }
            }catch (Exception pt){picfound=1;}
            tv_date.setText(pc.getDate());
            tv_duration.setText(pc.getTstart() + " - " + pc.getTend());
            tv_status.setText(pc.getMessage());
            tv_location.setText(pc.getCity() + ", " + pc.getCountry());

            lik.setText("THIS POST HAS "+pc.getNumlikes()+" LIKES");
            likes.setText(pc.getLikes());
            dollars.setText(pc.getDollars());

            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NotificationTwo.this, "Only for Intricate Members", Toast.LENGTH_SHORT).show();
                }
            });
            place.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                    if (mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if (!isGPSEnable) {
                            Toast.makeText(getApplicationContext(), "Please turn on your Location", Toast.LENGTH_LONG).show();
                        } else {
                            /*
                            String label = "ABC Label";
                            String uriBegin = "geo:" + pc.getLat() + "," + pc.getLang();
                            String query = pc.getLat() + "," + pc.getLang();
                            String encodedQuery = Uri.encode(query);
                            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                            Uri uri = Uri.parse(uriString);
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                            startActivity(intent);*/
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("geo:0,0?q=" + (""+pc.getLat()+", "+pc.getLang()+"")));
                            try {
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),
                                "Check internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            });

            iv_user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(picfound==0) {

                        String picuri=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
                        String img1=pc.getFrontpic();
                        String img2=pc.getBackpic();
                       int arrsize=0;
                        int check=0;
                        if(img1.length() >20){
                            check=1;
                            arrsize++;
                        }
                        if(img2.length() >20){
                            arrsize++;
                        }
                        if(arrsize>0&&check==1){
                            if(ImageStorage.checkifImageExists(img1))
                            {
                                openInGallery(picuri+img1);
                            }
                        }
                    }else{
                        Toast.makeText(NotificationTwo.this, "Picture are not reterive successfully", Toast.LENGTH_LONG).show();
                    }
                }
            });
            iv_venue_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(picfound==0) {
                        String picuri=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
                        String img1=pc.getBackpic();
                        String img2=pc.getFrontpic();
                        int arrsize=0;
                        int check=0;
                        if(img1.length() >20){
                            check=1;
                            arrsize++;
                        }
                        if(img2.length() >20){
                            arrsize++;
                        }
                        if(arrsize>0&&check==1){
                            if(ImageStorage.checkifImageExists(img1))
                            {
                                openInGallery(picuri+img1);
                            }
                        }
                    }else{
                        Toast.makeText(NotificationTwo.this, "Picture are not reterive successfully", Toast.LENGTH_LONG).show();
                    }
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSettingsAlert();
                    //MeetingConfirmDialogBox m=new MeetingConfirmDialogBox(a);
                    //m.show();
                    //finish();
                }
            });
            getIntent().putExtra("used", true);
        }catch(Exception ex){}
    }

    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notification_two, menu);
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

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirmation Message");

        // Setting Dialog Message
        alertDialog.setMessage("You and " + name1+" are available to meet\n");

        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        // Showing Alert Message
        alertDialog.show();

    }
    private class GetImages extends AsyncTask<Object, Object, Object> {
        private String requestUrl, imagename_;
        private ImageView view;
        private Bitmap bitmap ;
        private FileOutputStream fos;
        private GetImages(String requestUrl, ImageView view, String _imagename_) {
            this.requestUrl = requestUrl;
            this.view = view;
            this.imagename_ = _imagename_ ;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {
                URL url = new URL(requestUrl);
                URLConnection conn = url.openConnection();
                bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception ex) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(!ImageStorage.checkifImageExists(imagename_))
            {
                view.setImageBitmap(bitmap);
                String s=ImageStorage.saveToSdCard(bitmap, imagename_);
            }
        }
    }
    public void openInGallery(String imageId) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + imageId), "image/*");
        startActivity(intent);
    }
    public void cancelNotification(int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) getSystemService(ns);
        nMgr.cancel(notifyId);
    }
}
