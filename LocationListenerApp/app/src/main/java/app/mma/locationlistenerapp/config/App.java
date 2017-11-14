package app.mma.locationlistenerapp.config;

import android.app.Application;


import app.mma.locationlistenerapp.R;
import app.mma.locationlistenerapp.net.Client;
import app.mma.locationlistenerapp.net.ApiService;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class App extends Application {


    private static ApiService apiService;
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/UbuntuMono-R.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


    // retrofit
    public static synchronized ApiService apiService(){
        if(apiService == null){
            apiService = Client.getRetrofit().create(ApiService.class);
        }
        return apiService;
    }

}
