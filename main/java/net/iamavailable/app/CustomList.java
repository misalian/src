package net.iamavailable.app;

/**
 * Created by Arshad on 5/23/2016.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.Settings;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.util.Log;
import java.text.DateFormat;
import java.util.Date;
import android.content.Context;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import net.iamavailable.app.data.DataService;


public class CustomList extends ArrayAdapter<String>{
    int position2;
    SharedPreferences myPref;
    ProgressDialog pd;
    String displayText,deletepostid;
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    protected static final String TAG = DataService.TAG;
    private final TextView[] tv;
    public CustomList(Activity context,String[] web, Integer[] imageId, TextView[] tv) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
        this.tv=tv;
    }
    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        myPref = HistoryList.myPref;
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);

        TextView del = (TextView) rowView.findViewById(R.id.delete);
        del.setText("DELETE");
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position2=position;
                deletepostid=HistoryList.ids[position];
                showDeleteAlert();
            }
        });

        imageView.setImageResource(imageId[position]);
        return rowView;
    }

    public void showDeleteAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle("Delete Post");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure to delete this post?\n");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                pd = new ProgressDialog(context);
                pd.setMessage("Delete process, please wait..");
                AsyncCallWS ac=new AsyncCallWS();
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

    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            displayText=PostWebService.deletePostHistory(deletepostid, HistoryList.formattedDate ,"deletePost");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            pd.dismiss();
            String[] response = displayText.split(",");
            if(response[0].equals("true")){
                if(position2==0){
                    String postdata=HistoryList.postData[position2];
                    String[] d = postdata.split("\n");
                    String[] d2 = d[1].split("-");
                    String[] d3 = d2[1].split(" ");


                    try {
                        String datetime = d[0] + " " + d3[1];
                        Calendar cal = Calendar.getInstance(); // creates calendar
                        SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
                        Date dat = df.parse(datetime);
                        String endtime = myPref.getString("EndTime", "");
                        SimpleDateFormat df2 = new SimpleDateFormat("yyMMddHHmmssZ");
                        Date dat2 = df2.parse(endtime);
                        Log.i(TAG, " PERFORM ACTION CALLED 1= "+dat.toString()+"|"+dat2.toString());
                        if (dat.equals(dat2)) {
                            SharedPreferences.Editor editor = myPref.edit();
                            editor.putString("EndTime", "");
                            editor.apply();
                        }
                    }catch(Exception rt){}
                }

                SharedPreferences.Editor editor = HistoryList.myPref.edit();
                editor.putString("tokenss", response[1]);
                editor.apply();

                Toast.makeText(context, "Successfuly deleted", Toast.LENGTH_SHORT).show();
                context.finish();
                Intent i=new Intent(context,profile.class);
                context.startActivity(i);
            }else if(response[0].equals("false")){
                Toast.makeText(context, "Post history not deleted, please check internet connection", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Check internet connection", Toast.LENGTH_SHORT).show();
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
}