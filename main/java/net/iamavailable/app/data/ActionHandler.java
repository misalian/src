package net.iamavailable.app.data;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

public class ActionHandler {
    private Context context;
    GoogleApiClient googleApiClient;
    Intent intent;

    public Intent getIntent() {
        return intent;
    }

    public ActionHandler setIntent(Intent intent) {
        this.intent = intent;
        return this;
    }

    public ActionHandler() {
    }

    public Context getContext() {
        return context;
    }

    public DataService getDataService() {
        return (DataService) context;
    }

    public GoogleApiClient getGoogleApiClient() {
        return googleApiClient;
    }

    public ActionHandler setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
        return this;
    }

    public ActionHandler setContext(Context context) {
        this.context = context;
        return this;
    }

    void waitUntilConnected() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Method must not be called on the UI thread");
        }

        if (!getGoogleApiClient().isConnected() && !getGoogleApiClient().isConnecting()) {
            getGoogleApiClient().connect();
        }

        while (!getGoogleApiClient().isConnected()) {
            Log.i(DataService.class.getName(), "Waiting for client to connect.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    boolean sendLocalBroadcast(Intent intent) {
        return LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    }

    public void performAction() {
    }

}
