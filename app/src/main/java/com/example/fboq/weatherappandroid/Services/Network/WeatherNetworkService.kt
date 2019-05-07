package com.example.fboq.weatherappandroid.Services.Network

import com.example.fboq.weatherappandroid.Model.Weather
import retrofit.http.Path
import retrofit.http.GET
import rx.Observable

/**
 * Created by Felix Boquet on 07/05/19
 *
 */

interface WeatherNetworkService {
    @GET("{lat},{long}")
    fun getWeather(@Path("lat") lat: String, @Path("long") long: String): Observable<Weather>
}