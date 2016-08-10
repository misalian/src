package net.iamavailable.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.*;
import java.io.*;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import android.util.Log;

import com.squareup.picasso.Picasso;

import net.iamavailable.app.data.DataService;

/**
 * Created by Arshad on 5/18/2016.
 */
public class ViewPagerAdapterHistory extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    int iterator;
    ViewPager viewPager;
    private TextView postStatus, tv_date, tv_duration, tv_status, tv_location,name,lik, likbtn;
    ImageView iv_user_pic, iv_venue_pic, group,place;
    String[] item;
    View itemView;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    SharedPreferences myPref;
    public static String id;
    TextView like_btn_like,help;
    Bitmap b;
    protected static final String TAG = DataService.TAG;
    public ViewPagerAdapterHistory(Context context) {
        this.context=context;
    }
    @Override
    public int getCount() {
        return ViewPagerAdapter.size;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        try {
            if(position==0) {
                iterator = position + 1;
            }else{
                iterator=(20*position)+1;
            }
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.like_items, container, false);
            item = HistoryLikeActivity.item;
            myPref = HistoryLikeActivity.myPref;
            //idrecord = item[iterator];

            // Toast.makeText(getApplicationContext(), "image " + position, Toast.LENGTH_SHORT).show();

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


            name.setText(item[iterator] + "\nLUCID");

            Log.i(TAG, "Path: " + item[iterator + 6].length());
               // if(item[iterator + 6].length()>1){
                if (ImageStorage.checkifImageExists(item[iterator + 6])) {
                    File file = ImageStorage.getImage(item[iterator + 6]);
                    String path = file.getAbsolutePath();
                    if (path != null) {
                        b = BitmapFactory.decodeFile(path);
                        iv_user_pic.setImageBitmap(b);
                    }
                } else {
                    Picasso.with(context)
                            .load("http://cust-fyp-projects.com/thumb/" + item[iterator + 6])
                            .into(iv_user_pic);
                    new GetImages("http://cust-fyp-projects.com/images/" + item[iterator + 6], iv_user_pic, item[iterator + 6]).execute();
                }
               // }



                //if(item[iterator + 7].length()>1){
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


            //CheckActivity.pb.setVisibility(View.INVISIBLE);
            //CheckActivity.action_Check.setVisibility(View.VISIBLE);

            tv_location.setText(item[iterator+17] + ", " + item[iterator+18]);
            lik.setText((position+1)+"/"+ViewPagerAdapter.size+" \nTHIS POST HAS "+item[iterator+15]+" LIKES");
            HistoryLikeActivity.likes.setText(item[iterator+1]);
            HistoryLikeActivity.dollars.setText(item[iterator+3]);
            // likestv.setText(CheckActivity.iterator+15);
            // iterator = iterator + 18;


            if(ViewPagerAdapter.flag==1){
                likbtn.setVisibility(View.GONE);
                group.setVisibility(View.GONE);
                place.setVisibility(View.GONE);
                help.setVisibility(View.GONE);
            }
            likbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HistoryLikeActivity lk=new HistoryLikeActivity();
                    lk.likeAction(HistoryLikeActivity.arr[position]);
                }
            });

            iv_user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int arrsize=0;
                    for(int i=5;i<HistoryLikeActivity.item.length;i++){
                        if((HistoryLikeActivity.item[i]).equals((HistoryLikeActivity.arr[position]).toString())){
                            String img1=HistoryLikeActivity.item[i+2];
                            String img2=HistoryLikeActivity.item[i+3];

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
            iv_venue_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int arrsize=0;
                    for(int i=5;i<HistoryLikeActivity.item.length;i++){
                        if((HistoryLikeActivity.item[i]).equals((HistoryLikeActivity.arr[position]).toString())){
                            String picuri=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
                            String img1=HistoryLikeActivity.item[i+3];
                            String img2=HistoryLikeActivity.item[i+2];
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

        }catch(Exception ex){}
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
    public void openInGallery(String imageId) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + imageId), "image/*");
        context.startActivity(intent);
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
}
