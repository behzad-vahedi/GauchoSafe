package app.mma.locationlistenerapp.utils;


import android.content.Context;
import android.location.LocationManager;

public class GpsUtils {

   

    public static boolean isGpsEnabled(Context context){
        LocationManager locman = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return (locman != null &&
                (locman.isProviderEnabled(LocationManager.GPS_PROVIDER) || locman.isProviderEnabled(LocationManager.NETWORK_PROVIDER)));
    }


}
