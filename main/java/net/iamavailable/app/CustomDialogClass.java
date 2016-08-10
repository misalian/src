package net.iamavailable.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.Drawable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;

/**
 * Created by Arshad on 3/28/2016.
 */
public class CustomDialogClass extends Dialog implements
        android.view.View.OnClickListener {
    public Activity c;
    public Dialog d;
    TextView likbtn;
    double ns=0.0;
    String display,formattedDate,name;
    RatingBar rb;
    ProgressBar pb;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    SharedPreferences myPref;
    public static String iid;

    public CustomDialogClass(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rating_like);
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            formattedDate = df.format(cal.getTime());

            iid=LikeActivity.id;
            myPref = LikeActivity.myPref;
            pb = (ProgressBar) findViewById(R.id.progressBar4);
            rb = (RatingBar) findViewById(R.id.ratingBar);

            LayerDrawable stars = (LayerDrawable) rb.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.parseColor("#808080"),
                    PorterDuff.Mode.SRC_ATOP); // for filled stars
            stars.getDrawable(0).setColorFilter(Color.parseColor("#D3D3D3"),
                    PorterDuff.Mode.SRC_ATOP);

            rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    ns = rating;
                }
            });

            for(int i=5;i<LikeActivity.item.length;i++){
                if((LikeActivity.item[i]).equals(LikeActivity.id)){
                    name=LikeActivity.item[i-4];
                }
                i=i+19;
            }

            likbtn = (TextView) findViewById(R.id.textView3);
            likbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likbtn.setTypeface(likbtn.getTypeface(), Typeface.BOLD);
                    // Toast.makeText(getContext(),"id= "+myPref.getString("itemid","0")+"  "+ns,Toast.LENGTH_LONG).show();
                    if (ns != 0.0) {
                        likbtn.setVisibility(View.GONE);
                        AsyncCallWS ac = new AsyncCallWS();
                        ac.execute();
                    } else {
                        Toast.makeText(getContext(), "Send a salute with stars", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch(Exception ex){}
    }

    @Override
    public void onClick(View v) {

        dismiss();
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
          display = PostWebService.postRating(String.valueOf(ns),LikeActivity.id,CheckActivity.email1,formattedDate,"SendNotification"); //emplogin method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                String[] array=display.split(",");
                pb.setVisibility(View.INVISIBLE);
                likbtn.setVisibility(View.VISIBLE);
                if (array[0].equals("true")) {
                    if(array[2].equals("LikeAgain")){
                        Toast.makeText(getContext(),
                                "" + myPref.getString("NAME", " ") + " is saluting " + name + " again. " + myPref.getString("NAME", " ") + " lost 1 curio.", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getContext(),
                                "" + myPref.getString("NAME", " ") + " saluted " + name + ". " + myPref.getString("NAME", " ") + " lost 1 curio.", Toast.LENGTH_LONG).show();
                    }
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("tokenss", array[1]);
                    //editor.putString("LNG", Double.toString(longitude));
                    // editor.putString("LAT", Double.toString(lattitude));
                    editor.apply();
                    Intent intentLike = new Intent(c, MessageActivity.class);
                    c.startActivity(intentLike);
                }else if(array[0].equals("FirstEnterPost")){
                    Toast.makeText(getContext(),
                            "Please make a post before saluting a post", Toast.LENGTH_LONG).show();

                } else if(array[0].equals("AllReadyExist")){
                    Toast.makeText(getContext(),
                            ""+myPref.getString("NAME", " ")+" has saluted "+name+" already. Wait for a reply.", Toast.LENGTH_LONG).show();
                }else if(array[0].equals("NotLikeOwnPost")){
                    Toast.makeText(getContext(),
                            "Users cannot salute themselves.", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(),
                            "Your rated salute cannot be posted, please check internet connection", Toast.LENGTH_LONG).show();
                }
                //tv.setText(fahren + "° F");
            }catch(Exception ex){
                Toast.makeText(getContext(),
                        "Your rated salute cannot be posted, please check internet connection", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}