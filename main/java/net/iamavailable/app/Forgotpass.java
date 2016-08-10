package net.iamavailable.app;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import android.content.Intent;
public class Forgotpass extends AppCompatActivity {
    TextView send;
    String email,displayText;
    EditText e;
    ProgressBar pb;
    int check;
    String newpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        pb=(ProgressBar) findViewById(R.id.progressBar6);
        send=(TextView) findViewById(R.id.send);
        e=(EditText) findViewById(R.id.email);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=e.getText().toString().trim();
                if (isEmailValid(email)) {
                    Random r = new Random();

                    int newp=r.nextInt();
                    if(newp<0)
                    {
                        newp=-1*newp;
                    }
                    newpass=Integer.toString(newp);
                    AsyncCallWS ac=new AsyncCallWS();
                    ac.execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Email Address cannot be verified", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgotpass, menu);
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
            displayText=PostWebService.forgetEmails(email,newpass, "forgotPass");
            if("true".equals(displayText)){
                try {
                    GMailSender sender = new GMailSender("iamavailable24@gmail.com", "iamavailable123");
                    check=sender.sendMail("Your New Password: ","Your New Password: "+newpass,"iamavailable24@gmail.com",email);

                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            pb.setVisibility(View.INVISIBLE);
            if(check==1){
                Toast.makeText(Forgotpass.this, "An email has been sent successfully.", Toast.LENGTH_LONG).show();
            }else if(displayText.equals("false")){
                Toast.makeText(Forgotpass.this, "Your record is not found.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(Forgotpass.this, "Email cannot be sent. Address cannot be verified.", Toast.LENGTH_LONG).show();
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
    public void headerHome(View v){
        Intent intentHome = new Intent(Forgotpass.this, Login.class);
        startActivity(intentHome);
    }
}

