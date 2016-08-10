package net.iamavailable.app.data;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import net.iamavailable.app.MainMenuActivity;
import net.iamavailable.app.NotificationOne;
import net.iamavailable.app.NotificationTwo;
import net.iamavailable.app.PostWebService;
import net.iamavailable.app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Arshad on 3/29/2016.
 */
public class CheckUpdateService extends Service {
    private boolean isRunning;
    private Context context;
    String[] st;
    String[] s;
    String formattedDate;
    SharedPreferences myPref;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";

    private NotificationManager myNotificationManager;
    private int notificationIdOne = 111;
    private int numMessagesOne = 0;
    int count=0;

    @Override
    public void onCreate() {
        super.onCreate();
        Notification noti = new Notification();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            noti.priority = Notification.PRIORITY_MIN;
        }
      startForeground(R.string.app_name, noti);
    }
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    private class AsyncCallWS extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {

            st= PostWebService.checkUpdates(MainMenuActivity.IMENUMBER,formattedDate,"checkNotification");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                if(st[0].equals("true")){
                    displayNotificationOne();
                    st=null;
                }else{}
            }catch (Exception ex){}

        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class AsyncCallWSRes extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... params) {

            s= PostWebService.checkResponse(MainMenuActivity.IMENUMBER,formattedDate,"checkResponseNotification");
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            try {
                if (s[0].equals("true")) {
                    displayNotificationTwo();
                    s=null;
                } else {

                }
            }catch(Exception ex){}


        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        try {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                myPref = getSharedPreferences(IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
                Calendar cal = Calendar.getInstance(); // creates calendar
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                formattedDate = df.format(cal.getTime());
                AsyncCallWS ac = new AsyncCallWS();
                ac.execute();

                AsyncCallWSRes re = new AsyncCallWSRes();
                re.execute();
                handler.postDelayed(this, 30000);
            }
        }, 1500);
         }catch (Exception ex){
        }
        return START_STICKY;
    }



    protected void displayNotificationOne() {
        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setContentTitle("" + st[2] + " saluted " + myPref.getString("NAME", " ") + "");
        float f=Float.parseFloat(st[15]);
        mBuilder.setContentText("with "+Math.round(f)+"/5 stars");
        mBuilder.setTicker("IamAvailable: Post Saluted Message!");
        mBuilder.setSmallIcon(R.drawable.notification_logo);
        mBuilder.setWhen(0);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
       // mBuilder.setSmallIcon(R.drawable.arrow);

        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(numMessagesOne);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationOne.class);
        resultIntent.putExtra("notificationId", notificationIdOne);
        resultIntent.putExtra("postrecord2", st);

        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent
        stackBuilder.addParentStack(NotificationOne.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT //can only be used once
                );
        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSound(soundUri);

        Intent view = new Intent();
        view.setAction("view");
        view.putExtra("notifydata", st);  ///////////// -----
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 12345, view, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.view1, "View", pendingIntentYes);

        Intent like = new Intent();
        like.setAction("like");
        like.putExtra("notifydata", st);  ///////////// -----
        like.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        PendingIntent pendingIntentLike = PendingIntent.getBroadcast(this, 12345, like, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.mipmap.img_like, "Like", pendingIntentLike);

        // PendingIntent.FLAG_ONE_SHOT //can only be used once
        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system

        mBuilder.setAutoCancel(true);
        myNotificationManager.notify(notificationIdOne, mBuilder.build());
    }


    protected void displayNotificationTwo() {

        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
//Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setContentTitle("Post salute back message");
        mBuilder.setContentText("By " + s[2]);
        mBuilder.setTicker("IamAvailable: Post salute back message!");
        mBuilder.setSmallIcon(R.drawable.notification_logo);
        mBuilder.setWhen(0);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
       // mBuilder.setSmallIcon(R.drawable.arrow);

        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(numMessagesOne);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationTwo.class);
        resultIntent.putExtra("notificationId", notificationIdOne);
        resultIntent.putExtra("name", s);

        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent
        stackBuilder.addParentStack(NotificationTwo.class);

        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_ONE_SHOT //can only be used once
                );
        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSound(soundUri);
        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system

        mBuilder.setAutoCancel(true);
        myNotificationManager.notify(notificationIdOne, mBuilder.build());

    }


}
