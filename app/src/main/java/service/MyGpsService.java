package service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import mobile.instachat.android2practicalexam.MainActivity;

public class MyGpsService extends Service {

    String GPS_FILTER = "yev_GPS";
    Thread serviceThread;
    LocationManager lm;
    GPSListener myLocationListener;
    boolean isRunning = true;

    MainActivity activity;

    public void setMain(MainActivity main){
        activity = main;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("MyGpsService-onStart>>", "I am alive-GPS!");
        serviceThread = new Thread(new Runnable() {
            public void run() {
                getGPSFix_Version1(); // coarse: network based
                getGPSFix_Version2(); // fine: gps-chip based
            }// run
        });
        serviceThread.start(); // get the thread going
    }

    public void getGPSFix_Version1() {
// Get a location as soon as possible
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
// work with best available provider
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        checkLocationPermission();
        Location location = locationManager.getLastKnownLocation(provider);
        if ( location != null ){
// capture location data sent by current provider
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
// assemble data bundle to be broadcasted
            Intent intentFilteredResponse = new Intent(GPS_FILTER);
            intentFilteredResponse.putExtra("latitude", latitude);
            intentFilteredResponse.putExtra("longitude", longitude);
            intentFilteredResponse.putExtra("provider", provider);
            Log.e(">>GPS_Service<<", provider + " =>Lat:" + latitude
                    + " lon:" + longitude);
// send the location data out
            sendBroadcast(intentFilteredResponse);
        }
    }

    public void getGPSFix_Version2() {
        try {
// using: GPS_PROVIDER
// more accuracy but needs to see the sky for satellite fixing
            Looper.prepare();
            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
// This listener will catch and disseminate location updates
            myLocationListener = new GPSListener();
// define update frequency for GPS readings
            long minTime = 4000; // best time: 5*60*1000 (5min)
            float minDistance = 5; // 5 meters
// request GPS updates

            checkLocationPermission();
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    myLocationListener);
            Looper.loop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("MyGpsServonDestroy>>", "I am dead-GPS");
        try {
            lm.removeUpdates(myLocationListener);
            isRunning = false;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Intent myFilteredResponse = new Intent(GPS_FILTER);
            myFilteredResponse.putExtra("latitude", latitude);
            myFilteredResponse.putExtra("longitude", longitude);
            myFilteredResponse.putExtra("provider", location.getProvider());
            Log.e(">>GPS_Service<<", "Lat:" + latitude + " lon:" + longitude);

            sendBroadcast(myFilteredResponse);
        }
        public void onProviderDisabled(String provider) {
        }
        public void onProviderEnabled(String provider) {
        }
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    public void checkLocationPermission(){
        //FIRST RUN OF THE APP -- USER NEEDS TO GIVE PERMISSIONS TO ALLOW THE DEVICE TO OBTAIN LOCATION
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.;
            //
            Log.i("lat,lon", "NO PERMISSION");
            return;
        }
    }

}
