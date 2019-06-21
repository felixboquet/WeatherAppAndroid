package com.example.fboq.weatherappandroid.services.network

import com.example.fboq.weatherappandroid.model.Weather
import retrofit.http.Path
import retrofit.http.GET
import rx.Observable


interface WeatherNetworkService {
    @GET("{lat},{long}")
    fun getWeather(@Path("lat") lat: String, @Path("long") long: String): Observable<Weather>
}