package net.iamavailable.app;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

public class NotificationActivity extends Activity {
    private NotificationManager myNotificationManager;
    private int notificationIdOne = 111;
    private int numMessagesOne = 0;
    String[] item;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        try {
            item = getIntent().getStringArrayExtra("postrecord");
            displayNotificationOne();
        }catch (Exception ex){}

    }

    protected void displayNotificationOne() {

        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setContentTitle("Your post has been liked");
        mBuilder.setContentText("By " + item[1]);
        mBuilder.setTicker("IamAvailable: Post Liked Message!");
        mBuilder.setSmallIcon(R.drawable.arrow);

        // Increase notification number every time a new notification arrives
        mBuilder.setNumber(numMessagesOne);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, NotificationOne.class);
        resultIntent.putExtra("notificationId", notificationIdOne);
        resultIntent.putExtra("postrecord2", item);

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
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(soundUri);

        Intent view = new Intent();
        view.setAction("view");
        view.putExtra("notifydata",item);  ///////////// -----
        PendingIntent pendingIntentYes = PendingIntent.getBroadcast(this, 12345, view, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.addAction(R.drawable.images, "View", pendingIntentYes);

       // PendingIntent.FLAG_ONE_SHOT //can only be used once
        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system
        myNotificationManager.notify(notificationIdOne, mBuilder.build());
    }


}