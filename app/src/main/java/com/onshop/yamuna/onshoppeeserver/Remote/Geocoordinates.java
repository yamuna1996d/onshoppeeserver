package com.onshop.yamuna.onshoppeeserver.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Geocoordinates {
    @GET("maps/api/geocode/json")
    Call<String>getGeocode(@Query("address")String address);

    @GET("maps/api/directions/json")
    Call<String>getDirections(@Query("origin")String origin,@Query("destination")String destination);

}
