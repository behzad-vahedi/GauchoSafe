package app.mma.locationlistenerapp.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.sdsmdg.tastytoast.TastyToast;

import org.jetbrains.annotations.NotNull;

import app.mma.locationlistenerapp.R;
import app.mma.locationlistenerapp.config.Config;
import app.mma.locationlistenerapp.config.MyBaseActivity;
import app.mma.locationlistenerapp.locationservice.LocationService;
import app.mma.locationlistenerapp.shakeutils.ShakerService;
import app.mma.locationlistenerapp.utils.ConnectionUtils;
import app.mma.locationlistenerapp.utils.GpsUtils;
import app.mma.locationlistenerapp.utils.SessionManager;
import app.mma.locationlistenerapp.views.IntervalValueSelector;
import app.mma.locationlistenerapp.views.SettingsDialog;
import app.mma.locationlistenerapp.views.ValueSelector;
import io.ghyeok.stickyswitch.widget.StickySwitch;

public class MainActivity extends MyBaseActivity implements StickySwitch.OnSelectedChangeListener {

    Button btnLogOut;
    StickySwitch sw;
    AppCompatTextView tvServiceStatus;
    AppCompatImageView btnShake;

    SharedPreferences prefs;
    ConnectivityReceiver receiver = new ConnectivityReceiver();
    ProviderChangeReceiver providerChangeReceiver = new ProviderChangeReceiver();
    SettingsDialog settingsDialog;

    private static final int PERM_REQ_CODE = 11;
    boolean isGpsOn = false;
    boolean shakeEn = false;
    Snackbar snackGps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SessionManager session = new SessionManager(this);
        if(! session.isLoggedIn()){
            startActivity(new Intent(this, LoginRegisterActivity.class));
            finish();
            return;
        }
        initToolbar();
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        tvServiceStatus = (AppCompatTextView) findViewById(R.id.tv_enable_disable);
        sw = (StickySwitch) findViewById(R.id.switch_activate);
        btnShake = (AppCompatImageView) findViewById(R.id.btn_shake);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logout();
                startActivity(new Intent(MainActivity.this, LoginRegisterActivity.class));
                finish();
            }
        });


        sw.setOnSelectedChangeListener(this);

        btnShake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shakeEn = prefs.getBoolean(Config.SHAKE_EN, false);
                shakeEn = !shakeEn;
                prefs.edit().putBoolean(Config.SHAKE_EN, shakeEn).apply();
                btnShake.setBackgroundResource(shakeEn ? R.drawable.shake_btn_bg_enable :
                        R.drawable.shake_btn_bg_disable);
                TastyToast.makeText(MainActivity.this,
                        getString(shakeEn ? R.string.shake_service_enabled : R.string.shake_service_disabled),
                        TastyToast.LENGTH_SHORT, shakeEn ? TastyToast.SUCCESS : TastyToast.WARNING);
                if(shakeEn){
                    startService(new Intent(MainActivity.this, ShakerService.class));
                } else {
                    stopService(new Intent(MainActivity.this, ShakerService.class));
                }
            }
        });

        findViewById(R.id.btn_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapActivity.class));
            }
        });
    }

    private void initToolbar() {
        changeStatusBarWithColorResId(R.color.colorPrimaryDark);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.findViewById(R.id.setting_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsDialog();
            }
        });
    }

    private void showSettingsDialog() {
        if(settingsDialog == null){
            settingsDialog = new SettingsDialog(this);
        }
        if(!settingsDialog.isShowing()){
            settingsDialog.show();
        }
    }

    // enable or disable service
    @Override
    public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String s) {
        // enable or disable service
        if(direction == StickySwitch.Direction.LEFT){
            tvServiceStatus.setText(getString(R.string.service_disabled));
            prefs.edit().putBoolean(Config.SERVICE_EN, false).apply();
            deleteOngoingNotification();
        } else {
            if(! ConnectionUtils.haveNetwork(this)){
                sw.setDirection(StickySwitch.Direction.LEFT);
                tvServiceStatus.setText(getString(R.string.service_disabled));
            } else {
                prefs.edit().putBoolean(Config.SERVICE_EN, true).apply();
                tvServiceStatus.setText(getString(R.string.service_enabled));
                startLocationService();
            }
        }
    }


    private void startLocationService(){
        // start service
        setupSnackGps();
        if(isGpsOn){
            createOngoingNotification();
            Intent serviceIntent = new Intent(this, LocationService.class);
            startService(serviceIntent);
        }
    }


    @Override
    protected void onResume() {
        //register receiver for network status and location provider change
        registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(providerChangeReceiver, new IntentFilter(ProviderChangeReceiver.ACTION_PROVIDER_CHANGE));


        // load preference from last session
        boolean serviceEnable = prefs.getBoolean(Config.SERVICE_EN, false);
        tvServiceStatus.setText(serviceEnable ? R.string.service_enabled : R.string.service_disabled);
        sw.setDirection(serviceEnable ? StickySwitch.Direction.RIGHT : StickySwitch.Direction.LEFT);
        shakeEn = prefs.getBoolean(Config.SHAKE_EN, false);
        btnShake.setBackgroundResource(shakeEn ? R.drawable.shake_btn_bg_enable :
                R.drawable.shake_btn_bg_disable);
        if(shakeEn){
            startService(new Intent(MainActivity.this, ShakerService.class));
        } else {
            stopService(new Intent(MainActivity.this, ShakerService.class));
        }
        // check location access permission and request from user
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA},
                        PERM_REQ_CODE);
            }
        }

        // show snack bar if gps is off
        setupSnackGps();
        startService(new Intent(this, ShakerService.class));
        super.onResume();
    }

    private void setupSnackGps() {
        isGpsOn = GpsUtils.isGpsEnabled(this);
        if(isGpsOn){
            if(snackGps != null){
                snackGps.dismiss();
                snackGps = null;
            }
        } else {
            snackGps = Snackbar.make(findViewById(R.id.root), "GPS is off!", Snackbar.LENGTH_INDEFINITE);
            snackGps.setAction("Turn On", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            snackGps.show();
            prefs.edit().putBoolean(Config.SERVICE_EN, false).apply();
            sw.setDirection(StickySwitch.Direction.LEFT);
            tvServiceStatus.setText(R.string.service_disabled);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERM_REQ_CODE){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                TastyToast.makeText(this, "Permission denied !", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(providerChangeReceiver);
        unregisterReceiver(receiver);
        super.onPause();
    }

    // Create the notification to notify the user that the service is running
    public void createOngoingNotification() {

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.notification_title))
                .setSmallIcon(R.drawable.location_mark)
                .setContentIntent(pendingIntent)
                .build();

        notification.flags |= Notification.FLAG_ONGOING_EVENT;


        NotificationManager notifman = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifman.notify(Config.NOTIF_ID, notification);
    }

    // Delete the notification
    public void deleteOngoingNotification() {
        NotificationManager man = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        man.cancel(Config.NOTIF_ID);
    }

    public class ConnectivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(ConnectionUtils.haveNetwork(context)){
                sw.setEnabled(true);
            } else {
                if(prefs.getBoolean(Config.SERVICE_EN, false)){
                    deleteOngoingNotification();
                }
                sw.setDirection(StickySwitch.Direction.LEFT);
                sw.setEnabled(false);
                prefs.edit().putBoolean(Config.SERVICE_EN, false).apply();
                deleteOngoingNotification();
            }
        }
    }

    public class ProviderChangeReceiver extends BroadcastReceiver{
        public static final String ACTION_PROVIDER_CHANGE = "android.location.PROVIDERS_CHANGED";
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().matches(ACTION_PROVIDER_CHANGE)){
                setupSnackGps();
            }
        }
    }

}
