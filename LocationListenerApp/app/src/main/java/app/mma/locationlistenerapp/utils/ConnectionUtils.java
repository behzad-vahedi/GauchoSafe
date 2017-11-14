package app.mma.locationlistenerapp.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtils {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NONE = 0;

    public static int getConnectionStatus(Context context) {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager conman = (ConnectivityManager) context.getSystemService(cs);

        NetworkInfo netInfo = conman.getActiveNetworkInfo();
        if (netInfo != null) {
            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            }
        }
        return TYPE_NONE;
    }

    public static boolean haveNetwork(Context context){
        int status = getConnectionStatus(context);
        return ( status == TYPE_WIFI || status == TYPE_MOBILE );
    }


}
