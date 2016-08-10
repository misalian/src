package net.iamavailable.app;

/**
 * Created by Arshad on 7/1/2016.
 */
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.content.Intent;
import org.kobjects.base64.Base64;
import java.io.File;
import android.net.Uri;
import android.os.AsyncTask;

public class FragmentOne extends Fragment {
    String picuri;
    View itemView;
    int iterator=0;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    SharedPreferences myPref;
    private final String TAG = "PostScreen1";
    double latitude = 0, longitude = 0;
    ImageView iv_user_pic, iv_venue_pic, group,place;
    private TextView postStatus, tv_date, tv_duration, tv_status, tv_location,name,lik, likbtn;
    TextView like_btn_like,help;
    String[] item;
    Bitmap b,vp;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        itemView=inflater.inflate(R.layout.fragment_one, container, false);
        try {
            iterator = (HistoryDataCurrentUser.counter) + 1;
            item = HistoryList.display;
            myPref = HistoryList.myPref;

            AsyncCallWS ac=new AsyncCallWS();
            ac.execute();

            name = (TextView) itemView.findViewById(R.id.like_intricate);
            tv_date = (TextView) itemView.findViewById(R.id.like_date);
            tv_duration = (TextView) itemView.findViewById(R.id.like_session);
            tv_status = (TextView) itemView.findViewById(R.id.like_alias);
            postStatus = (TextView) itemView.findViewById(R.id.post2_btn_post);
            tv_location = (TextView) itemView.findViewById(R.id.like_address);
            iv_user_pic = (ImageView) itemView.findViewById(R.id.like_pic_user);
            iv_venue_pic = (ImageView) itemView.findViewById(R.id.like_pic_venue);
            lik = (TextView) itemView.findViewById(R.id.like_post_likes);
            likbtn = (TextView) itemView.findViewById(R.id.like_btn_like);
            help = (TextView) itemView.findViewById(R.id.like_help);
            group = (ImageView) itemView.findViewById(R.id.like_group);
            place = (ImageView) itemView.findViewById(R.id.like_place);
            name.setText(item[iterator].toUpperCase() + "\nLUCID");

            tv_date.setText(item[iterator + 11]);
            tv_duration.setText(item[iterator + 9] + " - " + item[iterator + 10]);
            tv_status.setText(item[iterator + 14] + item[iterator + 8]);
            tv_location.setText(item[iterator + 17] + ", " + item[iterator + 18]);
            lik.setText(HistoryDataCurrentUser.numpost + "/" + HistoryList.size + " \nTHIS POST HAS " + item[iterator + 15] + " LIKES");
            HistoryDataCurrentUser.likes.setText(myPref.getString("likess", " "));
            HistoryDataCurrentUser.dollars.setText(myPref.getString("tokenss", " "));


            if (profile.flag == 1) {
                likbtn.setVisibility(View.GONE);
                group.setVisibility(View.GONE);
                place.setVisibility(View.GONE);
                help.setVisibility(View.GONE);
            }
            iv_user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int arrsize=0;
                    String img1=item[iterator + 6];
                    String img2=item[iterator + 7];
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
            });
            iv_venue_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int arrsize=0;
                    String img1=item[iterator + 7];
                    String img2=item[iterator + 6];
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
            });
        }catch (Exception ep){}
        return itemView;
    }
    public void openInGallery(String imageId) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + imageId), "image/*");
        startActivity(intent);
    }
    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            picuri=android.os.Environment.getExternalStorageDirectory() + "/" + (android.os.Environment.DIRECTORY_DCIM).toLowerCase() + "/IamAvailable/";
             b = BitmapFactory.decodeFile((new  File(picuri + item[iterator + 6])).getAbsolutePath());
             vp = BitmapFactory.decodeFile((new File(picuri + item[iterator + 7])).getAbsolutePath());
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {

                iv_user_pic.setImageBitmap(b);
                iv_venue_pic.setImageBitmap(vp);
            }catch (Exception ex){
            }


        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}