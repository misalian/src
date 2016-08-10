package net.iamavailable.app;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;
import java.util.Locale;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.*;
import java.io.*;
import android.net.Uri;
/**
 * Created by Ansar on 12/7/2015.
 */
public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    String formattedDate,postid;
    public ViewPagerAdapter(Context context) {
        this.context=context;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(cal.getTime());
    }
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    SharedPreferences myPref;
    private final String TAG = "PostScreen1";
    double latitude = 0, longitude = 0;
    ImageView iv_user_pic, iv_venue_pic, group,place;
    private TextView postStatus, tv_date, tv_duration, tv_status, tv_location,name,lik, likbtn;
    TextView like_btn_like,help;
    String[] item;
    public static String[] display;
    View itemView;
    int iterator;
    public static String id;
    LocationManager locationManager;
    boolean isGPSEnable=false;
    public static int flag,size;
    ProgressDialog pd;
    Bitmap b;

    @Override
    public int getCount() {
        return CheckActivity.size;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
       // try {
            if(position==0) {
                iterator = position + 1;
            }else{
                iterator=(20*position)+1;
            }
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.like_items, container, false);
            item = LikeActivity.item;
            myPref = LikeActivity.myPref;
            name = (TextView) itemView.findViewById(R.id.like_intricate);
            tv_date = (TextView) itemView.findViewById(R.id.like_date);
            tv_duration = (TextView) itemView.findViewById(R.id.like_session);
            tv_status = (TextView) itemView.findViewById(R.id.like_alias);
            postStatus = (TextView) itemView.findViewById(R.id.post2_btn_post);
            tv_location = (TextView) itemView.findViewById(R.id.like_address);
            iv_user_pic = (ImageView) itemView.findViewById(R.id.like_pic_user);
            iv_venue_pic = (ImageView) itemView.findViewById(R.id.like_pic_venue);
            lik=(TextView) itemView.findViewById(R.id.like_post_likes);
            likbtn=(TextView) itemView.findViewById(R.id.like_btn_like);
            help=(TextView) itemView.findViewById(R.id.like_help);
            group=(ImageView) itemView.findViewById(R.id.like_group);
            place=(ImageView) itemView.findViewById(R.id.like_place);
            name.setText(item[iterator].toUpperCase() + "\nLUCID");

       // if(item[iterator + 6].length()>1){
            if(ImageStorage.checkifImageExists(item[iterator + 6]))
            {
                File file = ImageStorage.getImage(item[iterator + 6]);
                String path = file.getAbsolutePath();
                if (path != null){
                    b = BitmapFactory.decodeFile(path);
                    iv_user_pic.setImageBitmap(b);
                }
            } else {
                Picasso.with(context)
                        .load("http://cust-fyp-projects.com/thumb/" + item[iterator + 6])
                        .into(iv_user_pic);
                new GetImages("http://cust-fyp-projects.com/images/" + item[iterator + 6], iv_user_pic, item[iterator + 6]).execute() ;
            }
       // }
           // if(item[iterator + 7].length()>1){
                if(ImageStorage.checkifImageExists(item[iterator + 7]))
                {
                    File file = ImageStorage.getImage(item[iterator + 7]);
                    String path = file.getAbsolutePath();
                    if (path != null){
                        b = BitmapFactory.decodeFile(path);
                        iv_venue_pic.setImageBitmap(b);
                    }
                } else {
                    Picasso.with(context)
                            .load("http://cust-fyp-projects.com/thumb/" + item[iterator + 7])
                            .into(iv_venue_pic);
                    new GetImages("http://cust-fyp-projects.com/images/" + item[iterator + 7], iv_venue_pic, item[iterator + 7]).execute() ;
                }
           // }
            tv_date.setText(item[iterator + 11]);
            tv_duration.setText(item[iterator + 9] + " - " + item[iterator + 10]);
            tv_status.setText(item[iterator + 14]+ item[iterator + 8]);
            CheckActivity.pb.setVisibility(View.INVISIBLE);
            CheckActivity.action_Check.setVisibility(View.VISIBLE);
            tv_location.setText(item[iterator+17] + ", " + item[iterator+18]);
            lik.setText((position+1)+"/"+CheckActivity.size+" \nTHIS POST HAS "+item[iterator+15]+" LIKES");
            LikeActivity.likes.setText(myPref.getString("likess", " "));
            LikeActivity.dollars.setText(myPref.getString("tokenss", " "));


            if(profile.flag==1){
                likbtn.setVisibility(View.GONE);
                group.setVisibility(View.GONE);
                place.setVisibility(View.GONE);
                help.setVisibility(View.GONE);
            }
            likbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LikeActivity lk=new LikeActivity();
                    lk.likeAction(LikeActivity.arr[position]);
                }
            });
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postid=LikeActivity.arr[position];
                    pd = new ProgressDialog(context);
                    pd.setMessage("Loading, please wait..");
                   AsyncCallWS ac=new AsyncCallWS();
                    ac.execute();
                    //Toast.makeText(context, "Hello:  "+LikeActivity.arr[position]+"   " + formattedDate, Toast.LENGTH_SHORT).show();
                }
            });

            group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Only for Intricate Members", Toast.LENGTH_SHORT).show();
                }
            });
            
            place.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                    if (mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
                        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if (!isGPSEnable) {
                            Toast.makeText(context, "Please turn on your Location", Toast.LENGTH_LONG).show();
                        } else {
                            for (int i = 5; i < LikeActivity.item.length; i++) {
                                if ((LikeActivity.item[i]).equals(LikeActivity.arr[position])) {
                                    latitude = Double.parseDouble(LikeActivity.item[i + 8]);
                                    longitude = Double.parseDouble(LikeActivity.item[i + 9]);
                                }
                                i = i + 19;
                            }
                            /*
                            String label = "ABC Label";
                            String uriBegin = "geo:" + latitude + "," + longitude;
                            String query = latitude + "," + longitude;
                            String encodedQuery = Uri.encode(query);
                            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                            Uri uri = Uri.parse(uriString);
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);*/
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("geo:0,0?q=" + (""+latitude+", "+longitude+"")));
                            try {
                                context.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        Toast.makeText(context,
                                "Check internet connection", Toast.LENGTH_LONG).show();
                    }
                }
            });

            iv_user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int arrsize=0;
                    for(int i=5;i<LikeActivity.item.length;i++){
                        if((LikeActivity.item[i]).equals((LikeActivity.arr[position]).toString())){
                            String picuri=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
                            String img1=LikeActivity.item[i+2];
                            String img2=LikeActivity.item[i+3];
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
                        }
                        i=i+19;
                    }
                }
            });
            iv_venue_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int arrsize=0;
                    for(int i=5;i<LikeActivity.item.length;i++){
                        if((LikeActivity.item[i]).equals((LikeActivity.arr[position]).toString())){
                            String img1=LikeActivity.item[i+3];
                            String img2=LikeActivity.item[i+2];
                            String picuri=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
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
                        }
                        i=i+19;
                    }
                }
            });
            container.addView(itemView);

       // }catch(Exception ex){}
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);

    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            //Toast.makeText(getApplicationContext(),"successfully!",Toast.LENGTH_LONG).show();
            display = PostWebService.postHistory(postid, formattedDate, "getPostHistoryAgainstUsername"); //emplogin method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                pd.dismiss();
                if(display.length!=1) {
                    if (display.length > 5) {
                        ViewPagerAdapter.flag=1;
                        size = (display.length) / 20;     // This size is for showing number of record on slides in LikeActivity
                        Intent intentCheck = new Intent(context, HistoryLikeActivity.class);
                        //intentCheck.putExtra("displayarray", display);
                        context.startActivity(intentCheck);
                    } else {
                        Toast.makeText(context, "There are no posts in the history", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,
                            "There are no posts in the history", Toast.LENGTH_LONG).show();
                }

                //tv.setText(fahren + "° F");
            }catch(Exception ex){
                Toast.makeText(context,"Check internet connection.",Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private static final float BLUR_RADIUS = 10f;

    public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createBitmap(image);
        final RenderScript renderScript = RenderScript.create(context);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
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
        context.startActivity(intent);
    }
}
