package app.mma.locationlistenerapp.net;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("register.php")
    Call<ApiRes<UserInfo>> register(
            @Field("firstname") String firstname,
            @Field("lastname") String lastname,
            @Field("phoneNumber") String phoneNumber,
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("login.php")
    Call<ApiRes<UserInfo>> login(
            @Query("email") String email,
            @Query("password") String password
    );

    @FormUrlEncoded
    @POST("addlocs.php")
    Call<ApiRes<Void>> addLocation(@Field("email") String email,
                                   @Field("lat") double lat,
                                   @Field("lng") double lng
                        );

    @FormUrlEncoded
    @POST("addlocsems.php")
    Call<ApiRes<Void>> addLocationEms(@Field("email") String email,
                                   @Field("lat") double lat,
                                   @Field("lng") double lng,
                                   @Field("phoneNumber") String phoneNumber
    );

    @GET("getlastlocs.php")
    Call<ApiRes<List<LatLng>>> getLastLocations(@Query("email") String email);

}
