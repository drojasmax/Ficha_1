package com.example.ficha_project;

import Modelo.Direction;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
/**
 * Interfaz hecha para interactuar con el servidor
 *
 */
public interface GMapsDirectionsAPI {
    @GET("directions/json")
    Call<Direction> getDirection(@Query("origin") String origin, @Query("destination") String destination, @Query("key") String key);
}
