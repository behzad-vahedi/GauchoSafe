package app.mma.locationlistenerapp.shakeutils;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import app.mma.locationlistenerapp.R;
import app.mma.locationlistenerapp.config.App;
import app.mma.locationlistenerapp.net.ApiRes;
import app.mma.locationlistenerapp.utils.CameraUtils;
import app.mma.locationlistenerapp.utils.ConnectionUtils;
import app.mma.locationlistenerapp.utils.GpsUtils;
import app.mma.locationlistenerapp.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShakerService extends Service implements Shaker.OnShakeListener, LocationListener {


    private Shaker mShaker;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    LocationManager locationManager;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        this.mSensorManager = ((SensorManager)getSystemService("sensor"));
        this.mAccelerometer = this.mSensorManager.getDefaultSensor(1);
        mShaker = new Shaker(this);
        mShaker.setOnShakeListener(this);
    }


    @Override
    public void onShake() {

        Log.e("ShakerService", "shake detected");
        if(!GpsUtils.isGpsEnabled(this)) {
            TastyToast.makeText(this, getString(R.string.gps_off), TastyToast.DEFAULT, TastyToast.WARNING);
        } else if(! ConnectionUtils.haveNetwork(this)) {
            TastyToast.makeText(this, getString(R.string.netword_not_available), TastyToast.DEFAULT, TastyToast.WARNING);
        } else {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            String provider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ?
                    LocationManager.GPS_PROVIDER : LocationManager.NETWORK_PROVIDER;

            Location location = locationManager.getLastKnownLocation(provider);
//            Location location = locationManager.getLastLocation(provider);
            if(location != null){
                sendLocationEmsToApi(location);
            } else {
                locationManager.requestLocationUpdates(provider, 0, 0, this);
            }

            Toast toast = Toast.makeText(this,"play sound",Toast.LENGTH_SHORT);
            toast.show();
            // play sound
            MediaPlayer player = MediaPlayer.create(this, R.raw.twingy);
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });

            // led on / off
            CameraUtils.ledOnOff(this,1000);

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onLocationChanged(Location location) {
        Toast toast3 = Toast.makeText(this,"location changed",Toast.LENGTH_LONG);
        toast3.show();
        if(location != null){
            sendLocationEmsToApi(location);
            locationManager.removeUpdates(this);
        }
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


    private void sendLocationEmsToApi(Location location){
        Toast toast2 = Toast.makeText(this,"shake location sent",Toast.LENGTH_LONG);
        toast2.show();

        if(location == null)
            return;
        if(location.getAltitude() == 0 && location.getLongitude() == 0)
            return;

        String email = new SessionManager(this).getEmail();
        String phoneNumber = new SessionManager(this).getPhoneNumber();
        App.apiService().addLocationEms(email, location.getLatitude(), location.getLongitude(), phoneNumber)
                .enqueue(new Callback<ApiRes<Void>>() {
                    @Override
                    public void onResponse(Call<ApiRes<Void>> call, Response<ApiRes<Void>> response) {
                        if(response.body().isError()){
                            Log.e("Shaker Service", "error");
                        } else {
                            Log.i("Shaker Service", "ok");
                            TastyToast.makeText(getApplicationContext(), "Location has been sent.",
                                    TastyToast.LENGTH_SHORT, TastyToast.DEFAULT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiRes<Void>> call, Throwable t) {
                        Log.e("Shaker Service", "error in connection");
                    }
                });
    }


}