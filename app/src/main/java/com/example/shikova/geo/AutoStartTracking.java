package com.example.shikova.geo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by shikova on 23/01/17.
 */

public class AutoStartTracking extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        if ((isGpsEnabled(context)||isLocationByNetworkEnabled(context)) && isDataConnectionAvailable(context))
            startService(context);
        else
            stopService(context);


    }

    private void stopService(Context context) {

        context.stopService(new Intent(context,TrackService.class));
        Toast.makeText(context, "Service Stoped :(", Toast.LENGTH_LONG).show();

    }


    private void startService(Context context) {
        context.startService(new Intent(context, TrackService.class));
        Toast.makeText(context, "Service Started :D", Toast.LENGTH_LONG).show();
    }

    private boolean isDataConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }


    private boolean isGpsEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isLocationByNetworkEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}
