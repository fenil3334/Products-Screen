package com.example.products_screen_usinggetx;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.gsatechworld.fuelboy.MainActivity;
import com.gsatechworld.fuelboy.R;
import com.gsatechworld.fuelboy.data.NetworkAPI;
import com.gsatechworld.fuelboy.data.NetworkService;
import com.gsatechworld.fuelboy.data.pojo.ForgotResponse;
import com.gsatechworld.fuelboy.data.pojo.UpdateLocationStatus;
import com.gsatechworld.fuelboy.ui.reg.ActivityOtpScreen;
import com.gsatechworld.fuelboy.ui.reg.ActivityResetPassword;
import com.gsatechworld.fuelboy.util.CommonUtils;
import com.gsatechworld.fuelboy.util.DriverSharedPreferences;
import com.gsatechworld.fuelboy.util.SessionManager;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service implements
        /*   GoogleApiClient.ConnectionCallbacks,
           GoogleApiClient.OnConnectionFailedListener,*/
        LocationListener {

    private static final String TAG = LocationService.class.getSimpleName();

    // the notification id for the foreground notification
    public static final int GPS_NOTIFICATION = 1;

    // the interval in seconds that gps updates are requested
    private static final int UPDATE_INTERVAL_IN_SECONDS = 10;


    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1;


    // is this service currently running in the foreground?
    private boolean isForeground = false;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    protected LocationManager locationManager;

    // the google api client
   /* private GoogleApiClient googleApiClient;

    // the wakelock used to keep the app alive while the screen is off
    private PowerManager.WakeLock wakeLock;
    Location location; // location
    double latitude; // latitude
    double longitude;
    @Inject
    SessionManager sessionManager;
    DriverSharedPreferences driverSharedPreferences;
    String token,driver_id;

*/

    @Override
    public void onCreate() {
        super.onCreate();


    /*    driverSharedPreferences = DriverSharedPreferences.getInstance(this);
        token = driverSharedPreferences.getData("token");
        driver_id=driverSharedPreferences.getData("DRIVER_ID");
        // get a wakelock from the power manager
        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);*/
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d("aaa","--==----"+isGPSEnabled);
        if (!isNetworkEnabled) {
           // showNetworkAlert();
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }else if (!isGPSEnabled){
           // showSettingsAlert();
            Toast.makeText(this, "please location enable", Toast.LENGTH_SHORT).show();
        }else {
            if (!isForeground) {
                Log.v(TAG, "Starting the " + this.getClass().getSimpleName());

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForeground(-1,getnotification());
                } else {
                    Notification notification = new Notification();
                    startForeground(-1,notification);
                }


                isForeground = true;

                // connect to google api client
                //  googleApiClient.connect();
                if (isNetworkEnabled){
                  /*  location = null;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if (locationManager!=null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location!=null){
                            Log.e("latitude",location.getLatitude()+"");
                            Log.e("longitude",location.getLongitude()+"");

                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }*/

                }


              /*  if (isGPSEnabled){
                    location = null;
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if (locationManager!=null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location!=null){
                            Log.e("latitude",location.getLatitude()+"");
                            Log.e("longitude",location.getLongitude()+"");
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                // acquire wakelock
                wakeLock.acquire();*/
            }
        }

        return START_REDELIVER_INTENT;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    @Override
    public void onDestroy() {

        Log.v(TAG, "Stopping the " + this.getClass().getSimpleName());

        stopForeground(true);
        isForeground = false;


       /* // release wakelock if it is held
        if (null != wakeLock && wakeLock.isHeld()) {
            wakeLock.release();
        }
*/
        super.onDestroy();
    }

/*

    private LocationRequest getLocationRequest() {

        LocationRequest locationRequest = LocationRequest.create();

        // we always want the highest accuracy
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // we want to make sure that we get an updated location at the specified interval
        locationRequest.setInterval(TimeUnit.SECONDS.toMillis(0));

        // this sets the fastest interval that the app can receive updates from other apps accessing
        // the location service. for example, if Google Maps is running in the background
        // we can update our location from what it sees every five seconds
        locationRequest.setFastestInterval(TimeUnit.SECONDS.toMillis(0));
        locationRequest.setMaxWaitTime(TimeUnit.SECONDS.toMillis(UPDATE_INTERVAL_IN_SECONDS));

        return locationRequest;
    }
*/


    private Notification getnotification() {

        String NOTIFICATION_CHANNEL_ID = "Fuel Driver";
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle("Fuel Driver")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(notificationPendingIntent)
                .setWhen(System.currentTimeMillis());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }
      //  builder.setVisibility(NotificationCompat.VISIBILITY_SECRET);

        return builder.build();
    }



    @Override
    public void onLocationChanged(Location location) {
        // Log.e(TAG, "onLocationChanged: " + location.toString());
        Log.e("aaa", "   onLocationChanged: ");
        Log.e("aaa", "----lat-------"+location.getLatitude());
        Log.e("aaa", "-----lng--------"+location.getLongitude());
        update_driver_location(location.getLatitude(),location.getLongitude());
    }



    public  void update_driver_location(double latitude,double longitude) {

        if (CommonUtils.isConnectingToInternet(this)) {
            NetworkAPI networkAPI = NetworkService.getAPI().create(NetworkAPI.class);
            Call<UpdateLocationStatus> call1 = networkAPI.update_driver_location(token,driver_id,String.valueOf(latitude),String.valueOf(longitude));
            call1.enqueue(new Callback<UpdateLocationStatus>() {
                @Override
                public void onResponse(Call<UpdateLocationStatus> call, Response<UpdateLocationStatus> response) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            Log.d("aaa","------api response--------"+response.body().getStatus());
                        }
                    }
                }

                @Override
                public void onFailure(Call<UpdateLocationStatus> call, Throwable t) {
                }
            });
        } else {
            CommonUtils.showerrormsg("Internet", getString(R.string.no_internet), this);
        }

    }


    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.e("onStatusChanged()",s+" ss");

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e("onProviderEnabled()",s+" ss");
        startService(new Intent(LocationService.this, LocationService.class));
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e("onProviderDisabled()",s+" ss");

    }


    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }





    public void showNetworkAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("No Internet Connection");
        alertDialog.setMessage("Please Check Internet Connection.");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startActivity(new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY));
                }else {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                    startActivity(intent);
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
