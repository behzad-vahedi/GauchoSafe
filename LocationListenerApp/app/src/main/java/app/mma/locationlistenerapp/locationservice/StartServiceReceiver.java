package app.mma.locationlistenerapp.locationservice;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartServiceReceiver extends BroadcastReceiver {

    public static final String ACTION = "app.mma.locationlistener.ACTION_START_SERVICE";


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, LocationService.class);
        context.startService(serviceIntent);
    }
}
