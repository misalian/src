package net.iamavailable.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.view.View;
import com.squareup.picasso.Picasso;

import android.os.AsyncTask;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.content.Intent;
import android.net.Uri;
public class ShowDownlaodImage extends AppCompatActivity {

    public static Bitmap bm;
    Bitmap theBitmap1;
    ImageView img;
    String imgname;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_downlaod_image);
        imgname=getIntent().getStringExtra("up1").toString();
       Picasso.with(ShowDownlaodImage.this)
                .load("http://cust-fyp-projects.com/images/" + imgname)
                .into(img);


       // pb=(ProgressBar) findViewById(R.id.progressBar3);
       // AsyncCallWS ac=new AsyncCallWS();
        //ac.execute();

        /*Glide.with(ShowDownlaodImage.this).load("http://cust-fyp-projects.com/images/AbC.jpg")
                .bitmapTransform(new BlurTransformation(ShowDownlaodImage.this))
                .into(img);*/

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_downlaod_image, menu);
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

    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            Picasso.with(ShowDownlaodImage.this)
                    .load("http://cust-fyp-projects.com/images/" + imgname)
                    .into(img);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            pb.setVisibility(View.INVISIBLE);
            img=(ImageView) findViewById(R.id.image);

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
