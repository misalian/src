package net.iamavailable.app;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.NotificationManager;
import net.iamavailable.app.PushNotificationClassess.MyFirebaseMessagingService;
import net.iamavailable.app.data.CheckUpdateService;

/**
 * Created by Arshad on 4/27/2016.
 */
public class ReceiverNotfiButton extends BroadcastReceiver {
    String display,nid2;
    Context c;
    String formattedDate;
    PostClass pc;
    @Override
    public void onReceive(final Context context, Intent intent) {
        c=context;
        //pc= MyFirebaseMessagingService.ps;
        Calendar cal = Calendar.getInstance(); // creates calendar
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(cal.getTime());
        String action = intent.getAction();

       // Toast.makeText(context, "Intent Detected.  "+action, Toast.LENGTH_LONG).show();
        if(action.equals("view")) {
            PostClass ps=(PostClass)intent.getSerializableExtra("class");
            String nid=intent.getStringExtra("notifiid");
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);

            Intent i = new Intent();
           // i.setClass(context, MainMenuActivity.class);
            i.putExtra("postdata",ps);
            i.putExtra("nid",nid);  // notification id
            i.setClass(context, NotificationOne.class);
            i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |
                    Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS); //////////////////////////////////////////////
            context.startActivity(i);
        }
        if(action.equals("view2")) {
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);

            Intent i = new Intent();
            // i.setClass(context, MainMenuActivity.class);
            i.setClass(context, NotificationTwo.class);
            i.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |
                    Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            context.startActivity(i);
        }

        if(action.equals("like")) {
            pc=(PostClass)intent.getSerializableExtra("class");
            nid2=intent.getStringExtra("notifiid");
            AsyncCallWS ac=new AsyncCallWS();
            ac.execute();
        }

    }
    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {
            //Toast.makeText(getApplicationContext(),"successfully!",Toast.LENGTH_LONG).show();
            display = PostWebService.postCommetment(MyFirebaseMessagingService.email,pc.getId(),formattedDate,"confirmCommetmentFinal"); //emplogin method name
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
               // pb.setVisibility(View.INVISIBLE);
                String[] array=display.split(",");
                if (array[0].equals("true")) {
                    cancelNotification(c,Integer.parseInt(nid2));
                    if(array[2].equals("LikeAgain")){
                        Toast.makeText(c,"You saluted back to "+pc.getUsername()+" again. You have lost 1 curio.",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(c,"You saluted back to "+pc.getUsername()+". You have lost 1 curio.",Toast.LENGTH_LONG).show();
                    }

                } else if (array[0].equals("false")) {
                    Toast.makeText(c,
                            "Salute posting is in process, please wait", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(c,
                            "Check internet connection", Toast.LENGTH_LONG).show();
                }
                //tv.setText(fahren + "° F");
            }catch (Exception ex){
                Toast.makeText(c,
                        "Check internet connection", Toast.LENGTH_LONG).show();
            }


        }

        @Override
        protected void onPreExecute() {
            //pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);

        // Setting Dialog Title
        alertDialog.setTitle("Confirmation Message");

        // Setting Dialog Message
        alertDialog.setMessage("Your post has been liked by " + pc.getUsername() + " available at " + pc.getLat() + ", " + pc.getLang() + "\n");

        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(c, MainMenuActivity.class);
                c.startActivity(intent);
            }
        });


        // Showing Alert Message
        alertDialog.show();

    }
    public static void cancelNotification(Context ctx, int notifyId) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(notifyId);
    }
}
