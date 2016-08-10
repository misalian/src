package net.iamavailable.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MeetingConfirmDialogBox extends Dialog implements
        android.view.View.OnClickListener  {

    TextView meet,ok;
    Activity c;
    SharedPreferences myPref;
    public MeetingConfirmDialogBox() {
    super(null);
    }
    public MeetingConfirmDialogBox(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        this.c = a;
    }
    /*public static MeetingConfirmDialogBox newInstance(Activity a) {
        super(a);
        // TODO Auto-generated constructor stub
        c = a;
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_meeting_confirm_dialog_box);
        try{
            myPref = NotificationTwo.myPref;
            meet=(TextView) findViewById(R.id.meet);
            ok=(TextView) findViewById(R.id.ok);
           // meet.setText("Meeting has been confirmed between YOU and "+NotificationTwo.name1+"");
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   c.finish();
                }
            });

        }catch (Exception exp){}
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }
}
