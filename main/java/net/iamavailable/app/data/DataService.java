package net.iamavailable.app.data;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;

import java.util.HashMap;


public class DataService extends IntentService implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = "IAA";
    public static final String ACTION_GET_DISPLAY_NAME = "net.iamavailable.app.ggl.action.get-display-name";
    public static final String ACTION_GET_CURRENT_LOCATION = "net.iamavailable.app.ggl.action.get-current-location";
    public static final String BROADCAST_READY = "net.iamavailable.app.ggl.broadcast.ready";
    public static final String BROADCAST_SIGN_IN_REQUIRED = "net.iamavailable.app.ggl.broadcast.sign-in-required";
    public static final String BROADCAST_CONNECTION_FAILED = "net.iamavailable.app.ggl.broadcast.connection-failed";
    public static final String BROADCAST_GET_DISPLAY_NAME = "net.iamavailable.app.ggl.broadcast.get-display-name";
    public static final String BROADCAST_CURRENT_LOCATION = "net.iamavailable.app.ggl.broadcast.current-location";

    private HashMap<String, ActionHandler> handlers = new HashMap<>();

    private GoogleApiClient googleApiClient;

    public DataService() {
        super("DataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        switch (intent.getAction()) {
            case ACTION_GET_DISPLAY_NAME:
                new GetDisplayName().setContext(this).setGoogleApiClient(getGoogleApiClient()).setIntent(intent).performAction();
                break;
            case ACTION_GET_CURRENT_LOCATION:
                Log.i(TAG, "Instantiating ACTION GET CURRENT LOCATION");
                if (handlers.get(ACTION_GET_CURRENT_LOCATION) == null) {
                    handlers.put(ACTION_GET_CURRENT_LOCATION,
                            new GetCurrentLocation().setContext(this).setGoogleApiClient(getGoogleApiClient()).setIntent(intent));
                }
                Log.i(TAG," Performing LOCATION ACTION");
                handlers.get(ACTION_GET_CURRENT_LOCATION).performAction();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (googleApiClient != null && googleApiClient.isConnected()) {
            getGoogleApiClient().disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(DataService.class.getName(), "Google API Client connected");
        sendLocalBroadcast(new Intent(BROADCAST_READY));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        switch (result.getErrorCode()) {
            case ConnectionResult.SIGN_IN_REQUIRED:
                Intent s = new Intent(BROADCAST_SIGN_IN_REQUIRED);
                if (result.hasResolution()) {
                    s.putExtra("resolution", result.getResolution());
                }
                sendLocalBroadcast(s);
                break;
            default:
                Intent e = new Intent(BROADCAST_CONNECTION_FAILED)
                        .putExtra("result", result)
                        .putExtra("error", result.getErrorMessage());
                sendLocalBroadcast(e);
        }
    }

    private GoogleApiClient getGoogleApiClient() {
        if (this.googleApiClient == null) {
            this.googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(new Scope(Scopes.PROFILE))
                    .addScope(new Scope(Scopes.EMAIL))
                    .build();

        }
        return this.googleApiClient;
    }

    private boolean sendLocalBroadcast(Intent intent) {
        return LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}

