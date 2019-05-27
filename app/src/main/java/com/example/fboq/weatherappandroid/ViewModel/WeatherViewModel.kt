package com.example.fboq.weatherappandroid.ViewModel

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.example.fboq.weatherappandroid.Model.Weather
import com.example.fboq.weatherappandroid.Services.Network.WeatherNetworkService
import retrofit.http.Path
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class WeatherViewModel: ViewModel() {

    /*fun getWeather(@Path("lat") lat: String, @Path("long") long: String): Observable<Weather> {
        val weatherNetworkService: WeatherNetworkService = retrofit.create(
            WeatherNetworkService::class.java)

        return weatherNetworkService.getWeather("43.600000", "1.433333")
    }*/

}