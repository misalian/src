package net.iamavailable.app.data;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ansar on 2/19/2016.
 */
public class GetCurrentLocation extends ActionHandler implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    protected static final String TAG = DataService.TAG;
    public static Location mCurrentLocation;
    public static double accuracy=0.0;
    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;

    public void performAction() {
        Log.i(TAG, " PERFORM ACTION CALLED ");
        buildGoogleApiClient();
        mGoogleApiClient.connect();
    }

    void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient in GetCurrentLocation");
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        Log.i(TAG, "Building GoogleApiClient in GetCurrentLocation-- DONE ");
    }

    protected void createLocationRequest() {
        Log.i(TAG, "Creating Location Request ");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        Log.i(TAG, "Creating Location Request -- DONE ");
    }

    protected void startLocationUpdates() {
        Log.i(TAG, "Starting Location Updates ");
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "OnConnected:: Starting Location Updates ");
        try {
            createLocationRequest();
            startLocationUpdates();
        }catch (Exception es){}
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged:: Location Update received ");
        mCurrentLocation = location;
        String cityName=null, countryName=null;
        mCurrentLocation = location;

        double lat = mCurrentLocation.getLatitude();
        double lng = mCurrentLocation.getLongitude();
        accuracy=mCurrentLocation.getAccuracy();

        Log.i(DataService.TAG, "Got Data, Long:"+mCurrentLocation.getLongitude()+", Lat:"+mCurrentLocation.getLatitude()+" Accuracy: "+mCurrentLocation.getAccuracy());

        Geocoder geoCoder = new Geocoder(getContext(), Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int i = 0;
            for (Address ad : address) {
                Log.i(DataService.TAG, "count : " + i++);
                Log.i(DataService.TAG, "ADRESSS: " + ad.toString());
            }
            countryName = address.get(0).getCountryName();

            Log.i(DataService.TAG, "subAdminArea:"+address.get(0).getSubAdminArea());
            Log.i(DataService.TAG, "AdminArea:"+address.get(0).getAdminArea());
            Log.i(DataService.TAG, "FeatureName:"+address.get(0).getFeatureName());
            Log.i(DataService.TAG, "Locality:"+address.get(0).getLocality());


            String subAd=address.get(0).getSubAdminArea();
            String Ad=address.get(0).getAdminArea();
            String fea=address.get(0).getFeatureName();
            String lo=address.get(0).getLocality();

           // Toast.makeText(getContext(),"subAd: "+subAd+" Ad: "+Ad+" fea: "+fea+" lo: "+lo,Toast.LENGTH_LONG).show();
            if (lo != null && !lo.isEmpty()) {
                // doSomething
                cityName=lo;
            }else{
                if (Ad != null && !Ad.isEmpty()) {
                    // doSomething
                    cityName=Ad;
                }else{
                    if (subAd != null && !subAd.isEmpty()) {
                        // doSomething
                        cityName=subAd;
                    }else{
                        if (fea != null && !fea.isEmpty()) {
                            // doSomething
                            cityName=fea;
                        }else{
                            cityName="Not Found";
                        }
                    }
                }
            }

            Log.i(DataService.TAG, " Country Name: " + countryName);
            Log.i(DataService.TAG, " City Name: " + cityName);

        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }

        Intent i = new Intent(DataService.BROADCAST_CURRENT_LOCATION);
        Log.i(DataService.TAG, "Got Data, Processing Intent");
        i.putExtra("latitude", mCurrentLocation.getLatitude());
        i.putExtra("longitude", mCurrentLocation.getLongitude());
        i.putExtra("country", countryName);
        i.putExtra("city", cityName);
        Log.i(DataService.TAG, "Inserted location in Intent, Lat: " + i.getDoubleExtra("latitude", 0.0) + ", Long: " + i.getDoubleExtra("longitude", 0.0));
        stopLocationUpdates();
        sendLocalBroadcast(i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }
}
