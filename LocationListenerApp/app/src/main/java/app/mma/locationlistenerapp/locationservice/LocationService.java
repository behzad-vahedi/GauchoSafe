package app.mma.locationlistenerapp.locationservice;


import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import app.mma.locationlistenerapp.config.App;
import app.mma.locationlistenerapp.config.Config;
import app.mma.locationlistenerapp.net.ApiRes;
import app.mma.locationlistenerapp.utils.ConnectionUtils;
import app.mma.locationlistenerapp.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service implements LocationListener {
    private static final String TAG = "LOCATION_SERVICE";
    private AlarmManager alarmManager;
    private PendingIntent alarmIntent;
    private SharedPreferences prefs;
    private LocationManager locationManager;
    private String provider;
    private static final int ALARM_REQ_CODE = 1112;



    private Handler handler;
    private Runnable checkTimeRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // pending intent to repeat service
        alarmIntent = PendingIntent.getBroadcast(this,
                ALARM_REQ_CODE,
                new Intent(StartServiceReceiver.ACTION),
                0);
        // location manager : listen to location changes
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        HandlerThread t = new HandlerThread("location_service", Process.THREAD_PRIORITY_BACKGROUND);
        t.start();
        handler = new Handler(t.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        int interval = prefs.getInt(Config.INTERVAL_SECONDS, Config.DEFAULT_INT) * 1000;
        boolean taskRunning = prefs.getBoolean(Config.SERVICE_EN, false);

        if (taskRunning && ConnectionUtils.haveNetwork(this)) {
            alarmManager.setInexactRepeating(
                    AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + interval,
                    interval,
                    alarmIntent);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ?
                    LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;
                checkTimeRunnable = new Runnable(){
                    @Override
                    public void run() {
                        Location location = locationManager.getLastKnownLocation(provider);
                        sendLocationToApi(location);
                        stop();
                    }
                };

                locationManager.requestLocationUpdates(provider, interval, 0, this);

                handler.postDelayed(checkTimeRunnable, (interval + 2000));
            } else {
                stop();
            }
        } else {
            alarmManager.cancel(alarmIntent);
            deleteOngoingNotification();
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if(handler != null){
            handler.removeCallbacks(checkTimeRunnable);
        }
        locationManager.removeUpdates(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        sendLocationToApi(location);
        stop();
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    private void deleteOngoingNotification(){
        NotificationManager man = (NotificationManager)
                getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        man.cancel(Config.NOTIF_ID);
        prefs.edit().putBoolean(Config.SERVICE_EN, false).apply();
    }


    private void sendLocationToApi(Location location){
        if(location != null &&
                (location.getLatitude() != 0 ||
                        location.getLongitude() != 0)
                ){

            String email = new SessionManager(getApplicationContext()).getEmail();
            App.apiService()
                    .addLocation(email, location.getLatitude(),
                            location.getLongitude())
                    .enqueue(new Callback<ApiRes<Void>>() {
                        @Override
                        public void onResponse(Call<ApiRes<Void>> call, Response<ApiRes<Void>> response) {
                            if(response.body().isError()){
                                Log.e(TAG, "error");
                            } else {
                                Log.i(TAG, "ok");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiRes<Void>> call, Throwable t) {
                            Log.e(TAG, "error in connection");
                        }
                    });
        }
    }

    private void stop(){
        if(handler!= null){
            handler.removeCallbacks(checkTimeRunnable);
            handler = null;
        }
        stopSelf();
    }
}
