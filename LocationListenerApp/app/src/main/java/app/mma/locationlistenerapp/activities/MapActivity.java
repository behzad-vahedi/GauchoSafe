package app.mma.locationlistenerapp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;

import app.mma.locationlistenerapp.R;
import app.mma.locationlistenerapp.config.App;
import app.mma.locationlistenerapp.config.MyBaseActivity;
import app.mma.locationlistenerapp.net.ApiRes;
import app.mma.locationlistenerapp.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends MyBaseActivity implements OnMapReadyCallback {

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        loadData();
    }

    private void loadData(){
        final ProgressDialog pdialog = new ProgressDialog(this);
        pdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pdialog.setMessage(getString(R.string.loading));
        pdialog.setCancelable(false);
        pdialog.show();

        App.apiService().getLastLocations(new SessionManager(this).getEmail()).enqueue(
                new Callback<ApiRes<List<LatLng>>>() {
                    @Override
                    public void onResponse(Call<ApiRes<List<LatLng>>> call, Response<ApiRes<List<LatLng>>> response) {
                        pdialog.cancel();
                        if(response == null){
                            showToast("Error in connection", TastyToast.ERROR);
                            finish();
                            return;
                        }

                        if(response.body().isError()){
                            showToast(response.body().getMsg(), TastyToast.ERROR);
                            finish();
                        } else {
//                            map.clear();
                            List<LatLng> list = response.body().getData();
                            if(list != null && !list.isEmpty()){
                                HeatmapTileProvider provider = new HeatmapTileProvider.Builder()
                                        .data(list)
                                        .build();
                                TileOverlay overlay = map.addTileOverlay(
                                        new TileOverlayOptions().tileProvider(provider));
                                moveCamera(list);
                                list.clear();
                                showToast(response.body().getMsg(), TastyToast.SUCCESS);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiRes<List<LatLng>>> call, Throwable t) {
                        pdialog.cancel();
                        showToast("Failed", TastyToast.ERROR);
                        finish();
                    }
                }
        );
    }

    private void moveCamera(List<LatLng> list) {
        double lat = 0;
        double lng = 0;
        if(!list.isEmpty()){
            for(int i = 0; i < list.size() ; i++){
                lat += list.get(i).latitude;
                lng += list.get(i).longitude;
            }
            lat /= list.size();
            lng /= list.size();
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12));
    }

    private void showToast(String msg, int type){
        Toast toast = TastyToast.makeText(this, msg, TastyToast.LENGTH_SHORT, type);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

}
