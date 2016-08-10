package net.iamavailable.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class HistoryList extends AppCompatActivity {
    TextView lik, doll,up1,deletebtn;
    LinearLayout le;
    ListView lv;
    public static String[] ids;
    ArrayList<String> list;
    public static String id;
    ProgressDialog pd;
    public static String[] display,postData;
    NetworkInfo mMobile,mWifi;
    ConnectivityManager connManager;
    public static SharedPreferences myPref;
    public static String formattedDate;
    public static int size;
    public static int flag=0;
    int count=0;
    String deletecheck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
    try {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(cal.getTime());

        list = new ArrayList<String>();
        myPref = getSharedPreferences(Splash.IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);

        le = (LinearLayout) findViewById(R.id.include);
        lik = (TextView) le.findViewById(R.id.likes);
        doll = (TextView) le.findViewById(R.id.dollars);
        doll.setText(myPref.getString("tokenss", " "));
        lik.setText(myPref.getString("likess", " "));
        up1 = (TextView) findViewById(R.id.up);
        up1.setText(myPref.getString("NAME", " ").toUpperCase() + " \n HISTORY");
        lv = (ListView) findViewById(R.id.listView);
        int size1 = ((profile.display).length) / 2;
        postData = new String[size1];
        ids = new String[size1];
        int k = 0, j = 0;
        Integer[] imageId = new Integer[size1];
        for (int i = 0; i < (profile.display).length; i++) {
            if (i % 2 != 0) {
                postData[k] = profile.display[i];
                imageId[k] = R.mipmap.img_one_line;
                k++;
            } else {
                ids[j] = profile.display[i];
                j++;
            }
        }

        deletebtn = (TextView) findViewById(R.id.delete);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteAlert();
            }
        });

        TextView[] arrTxtView = new TextView[size1]; // declare and assign array length of text views.

        for (int i = 0; i < size1; i++) { // iterate over all array items and assign them text.
            TextView txtCnt = new TextView(this);
            txtCnt.setText("DELETE");
            arrTxtView[i] = txtCnt;
        }


        CustomList adapter = new
                CustomList(HistoryList.this, postData, imageId, arrTxtView);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, t);
        //ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listView, values);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                          //String selectedFromList = (lv.getItemAtPosition(i).get);
                                          connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                          mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                                          mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                                          if(mWifi.isConnected() || mMobile.isConnectedOrConnecting()) {
                                              id = ids[i];
                                              HistoryDataCurrentUser.numpost=i+1;
                                              pd = new ProgressDialog(HistoryList.this);
                                              pd.setMessage("Loading, please wait..");
                                              AsyncCallWS ac = new AsyncCallWS();
                                              ac.execute();
                                          }else{
                                              Toast.makeText(getApplicationContext(),"Check internet connection.",Toast.LENGTH_LONG).show();
                                          }
                                      }
                                  }
        );
    }catch (Exception ep){}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history_list, menu);
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
            //Toast.makeText(getApplicationContext(),"successfully!",Toast.LENGTH_LONG).show();
            display = PostWebService.postHistory(myPref.getString("id", " "),formattedDate, "getPostHistory"); //emplogin method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                int k=0;
                for(int i=5;i<display.length;i++){
                    if(display[i].equals(id)){
                        count=i-5;
                    }
                    i=i+19;
                    k++;
                }
                pd.dismiss();
                if(display.length!=1) {
                    if ((display.length)%20==0) {
                        HistoryList.flag=1;
                        size = (display.length) / 20;     // This size is for showing number of record on slides in LikeActivity
                        Intent intentCheck = new Intent(HistoryList.this, HistoryDataCurrentUser.class);
                        intentCheck.putExtra("counter", count);
                        startActivity(intentCheck);
                    } else {
                        Toast.makeText(getApplicationContext(), "Check internet connection.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            "There are no posts in the history", Toast.LENGTH_LONG).show();
                }

                //tv.setText(fahren + "° F");
            }catch(Exception ex){
                Toast.makeText(getApplicationContext(),"Check internet connection.",Toast.LENGTH_LONG).show();
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

    private class AsyncCallWS2 extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            //Toast.makeText(getApplicationContext(),"successfully!",Toast.LENGTH_LONG).show();
            deletecheck = PostWebService.deleteAllPostHistory(myPref.getString("id", " "), "deleteAllHistory"); //deleteAllHistory method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                pd.dismiss();
                if(deletecheck.equals("true")){
                    SharedPreferences.Editor editor = myPref.edit();
                    editor.putString("EndTime","");
                    editor.apply();
                    finish();
                    Toast.makeText(getApplicationContext(),"History deleted successfuly.",Toast.LENGTH_LONG).show();
                    Intent intentCheck = new Intent(HistoryList.this, profile.class);
                    startActivity(intentCheck);
                }else if(deletecheck.equals("false")){
                    Toast.makeText(getApplicationContext(),"History not found.",Toast.LENGTH_LONG).show();
                }else if(deletecheck.equals("Error occured")){
                    Toast.makeText(getApplicationContext(),"Check internet connection.",Toast.LENGTH_LONG).show();
                }else{}
            }catch(Exception ex){
                Toast.makeText(getApplicationContext(),"Check internet connection.",Toast.LENGTH_LONG).show();
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
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        //make static values to null
        ids=null;
        display=null;
        HistoryDataCurrentUser.counter=0;
        HistoryDataCurrentUser.item=null;
        HistoryDataCurrentUser.arr=null;
        HistoryDataCurrentUser.id=null;
        HistoryDataCurrentUser.numpost=1;
        startActivity(new Intent(HistoryList.this, MainMenuActivity.class));
        finish();

    }
    public void headerHome(View v){
        Intent intentHome = new Intent(HistoryList.this, MainMenuActivity.class);
        startActivity(intentHome);
    }
    public void showDeleteAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HistoryList.this);

        // Setting Dialog Title
        alertDialog.setTitle("Delete Post");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure to delete all posts?\n");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                pd = new ProgressDialog(HistoryList.this);
                pd.setMessage("Delete process, please wait..");
                AsyncCallWS2 ac=new AsyncCallWS2();
                ac.execute();
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
}
