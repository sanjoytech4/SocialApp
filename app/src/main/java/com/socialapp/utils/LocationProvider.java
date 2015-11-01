package com.socialapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.socialapp.R;


public class LocationProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener
{
    private GoogleApiClient mGoogleApiClient;
    private static LocationProvider provider;
    private Context context;
    private AppLocation slaLocation;
    private  double UndefinedCoordinate=0.0;
    private  double lat = UndefinedCoordinate, lng = UndefinedCoordinate;
    private String TAG="LocationProvider";

    public interface AppLocation
    {
        public void getLocation(double lat, double lng);
    }

    private LocationProvider() {
    }

    public static LocationProvider getInstance()
    {
        if(provider==null)
        {
            provider=new LocationProvider();
        }
        return provider;
    }

    public void doConnectLocationClient(Context context)
    {
        this.context=context;
        slaLocation= (AppLocation) context;
        if(checkPlayServices())
        {
            buildGoogleApiClient();
        }
        else{
            getLocation();
        }
    }

    public void doConnectLocationClient(Fragment fragment)
    {
        this.context=fragment.getActivity();
        slaLocation= (AppLocation) fragment;
        if(checkPlayServices())
        {
            buildGoogleApiClient();
        }
        else
        {
            getLocation();
        }
    }


    /**
     * Method initialises the Google Services API
     */
    protected synchronized void buildGoogleApiClient()
    {
        if(mGoogleApiClient==null )
        {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        else if(mGoogleApiClient.isConnected())
        {
            slaLocation.getLocation(lat, lng);
            return;
        }
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        LocationAvailability locationAvailability= LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(!locationAvailability.isLocationAvailable())
        {
            showEnableLocationServiceDialog();
        }
        if (mLastLocation != null)
        {
            lat = mLastLocation.getLatitude();
            lng = mLastLocation.getLongitude();
        }
        else
        {
            getLocation();
        }
        mGoogleApiClient.disconnect();
        slaLocation.getLocation(lat, lng);
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }
    private void showEnableLocationServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.enable_location_message));
        builder.setPositiveButton(context.getResources().getString(R.string.app_close_dialog_button_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                openLocationSettings();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.app_close_dialog_button_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {

    }

    private void openLocationSettings()
    {
        Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(viewIntent);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            return false;
        }
        return true;
    }

    public void getLocation()
    {
        double lat = UndefinedCoordinate, lng = UndefinedCoordinate;
        try
        {
            Location location=null;
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled == false && isNetworkEnabled == false)
            {
                slaLocation.getLocation(lat, lng);
            }
            else
            {
                if (isGPSEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null)
                        {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                        locationManager.removeUpdates(this);
                        slaLocation.getLocation(lat, lng);
                    }
                }
                if (isNetworkEnabled)
                {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 1, this);
                    if (locationManager != null && location==null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                        }
                        locationManager.removeUpdates(this);
                        slaLocation.getLocation(lat, lng);
                    }
                }

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            slaLocation.getLocation(lat, lng);
        }
    }

    @Override
    public void onLocationChanged(Location location)
    {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle)
    {

    }

    @Override
    public void onProviderEnabled(String s)
    {

    }

    @Override
    public void onProviderDisabled(String s)
    {

    }

}
