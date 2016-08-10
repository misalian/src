package net.iamavailable.app.PushNotificationClassess;

/**
 * Created by Arshad on 6/23/2016.
 */
import android.app.TaskStackBuilder;
import android.content.SharedPreferences;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.app.Notification;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import org.json.JSONObject;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.iamavailable.app.MainMenuActivity;
import net.iamavailable.app.NotificationOne;
import net.iamavailable.app.NotificationTwo;
import net.iamavailable.app.PostClass;
import net.iamavailable.app.R;
import net.iamavailable.app.ReceiverNotfiButton;

/**
 * Created by Belal on 5/27/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    PostClass ps;
    SharedPreferences myPref;
    public static final String IAA_SHAREDPREFERENCES = "IamAvailable_prefs";
    public static String email;
    private NotificationManager myNotificationManager;
    public static int notificationIdOne = 1;
    private int numMessagesOne = 0;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        //Calling method to generate notification
        try {
            Log.d(TAG, "From1: " + "From 1");
            myPref = getSharedPreferences(IAA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
            Log.d(TAG, "From2: " + "From 1");
            email=myPref.getString("id","0");
            JSONObject obj = new JSONObject(remoteMessage.getData().get("body"));
            Log.d(TAG, "From31: " + obj.getString("username"));
            ps=new PostClass();
            ps.setUsername(obj.getString("username"));
            ps.setId(obj.getString("id"));
            ps.setFrontpic(obj.getString("frontpic"));
            ps.setBackpic(obj.getString("backpic"));
            ps.setMessage(obj.getString("message"));
            ps.setTstart(obj.getString("tstart"));
            ps.setTend(obj.getString("tend"));
            ps.setDate(obj.getString("date"));
            ps.setLat(obj.getString("lat"));
            ps.setLang(obj.getString("lang"));
            Log.d(TAG, "From4: " + "From 1");
            ps.setStatusmood(obj.getString("statusmood"));
            ps.setNumlikes(obj.getString("numlikes"));
            ps.setNumstars(obj.getString("numstars"));
            ps.setCity(obj.getString("city"));
            ps.setCountry(obj.getString("country"));
            Log.d(TAG, "From5: " + "From 1");
            ps.setLikes(obj.getString("likes"));
            ps.setDollars(obj.getString("dollars"));
           // sendNotification(ps.getUsername()+"  "+ps.getCountry());
            if((obj.getString("check")).equals("likepost")){
                displayNotificationOne(ps);
            }
            if((obj.getString("check")).equals("likebackpost")){
                Log.d(TAG, "From7: " + remoteMessage.getData().get("body"));
                Log.d(TAG, "From77: " + ps.getUsername());
                displayNotificationTwo(ps);
            }
        }catch (Exception ep){}
    }
    protected void displayNotificationOne(PostClass ps) {
        notificationIdOne++;

        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Log.d(TAG, "From6: " + "From 1");
        mBuilder.setContentTitle("" + ps.getUsername() + " saluted " + myPref.getString("NAME", " ") + "");
        float f=Float.parseFloat(ps.getStatusmood());
        mBuilder.setContentText("with "+Math.round(f)+"/5 stars");
        mBuilder.setTicker("IamAvailable: Post Saluted Message!");
        mBuilder.setSmallIcon(R.drawable.notification_logo);
        mBuilder.setWhen(0);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        // mBuilder.setSmallIcon(R.drawable.arrow);

        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(numMessagesOne);

        // Creates an explicit intent for an Activity in your app
        /*Intent resultIntent = new Intent(this, NotificationOne.class);
        resultIntent.putExtra("postdata", ps);
        resultIntent.putExtra("nid",""+notificationIdOne);*/

        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent
        stackBuilder.addParentStack(NotificationOne.class);

        // Adds the Intent that starts the Activity to the top of the stack
       /* stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT //can only be used once
                );
        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);*/
        mBuilder.setSound(soundUri);

        Intent view = new Intent();
        view.setAction("view");
        view.putExtra("class", ps);
        view.putExtra("notifiid",""+notificationIdOne);
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, notificationIdOne, view, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.view1, "View", pendingIntentYes);

        Intent like = new Intent();
        like.setAction("like");
        like.putExtra("class", ps);
        like.putExtra("notifiid",""+notificationIdOne);
        like.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        PendingIntent pendingIntentLike = PendingIntent.getBroadcast(this, notificationIdOne, like, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.mipmap.img_like, "Like", pendingIntentLike);

        // PendingIntent.FLAG_ONE_SHOT //can only be used once
        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system

        mBuilder.setAutoCancel(false);
        myNotificationManager.notify(notificationIdOne, mBuilder.build());
    }


    protected void displayNotificationTwo(PostClass ps) {
        notificationIdOne++;


        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setContentTitle("" + ps.getUsername() + " saluted " + myPref.getString("NAME", " ") + "");
        mBuilder.setTicker("IamAvailable: Post Saluted Message!");
        mBuilder.setSmallIcon(R.drawable.notification_logo);
        mBuilder.setWhen(0);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        // mBuilder.setSmallIcon(R.drawable.arrow);

        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(numMessagesOne);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationTwo.class);
        resultIntent.putExtra("postdata", ps);
        resultIntent.putExtra("nid",""+notificationIdOne);

        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Adds the back stack for the Intent
        stackBuilder.addParentStack(NotificationTwo.class);

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

        // PendingIntent.FLAG_ONE_SHOT //can only be used once
        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system

        mBuilder.setAutoCancel(false);
        myNotificationManager.notify(notificationIdOne, mBuilder.build());
    }
}