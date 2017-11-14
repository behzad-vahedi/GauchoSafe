package app.mma.locationlistenerapp.utils;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v4.content.ContextCompat;

public class CameraUtils {


    public static void turnLEDon(Context context){
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
                Camera camera = Camera.open();
                Camera.Parameters p = camera.getParameters();
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(p);
                camera.startPreview();
            }
    }

    public static void turnLEDoff(Context context){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            Camera camera = Camera.open();
            Camera.Parameters p = camera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(p);
            camera.stopPreview();
            camera.release();
        }
    }


    public static void ledOnOff(final Context context, long duration){
        turnLEDon(context);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                turnLEDoff(context);
            }
        }, duration);
    }
}
