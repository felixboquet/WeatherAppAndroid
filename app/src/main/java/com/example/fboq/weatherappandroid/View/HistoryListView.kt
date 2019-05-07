package com.example.fboq.weatherappandroid.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.fboq.weatherappandroid.Model.Weather
import com.example.fboq.weatherappandroid.R
import com.example.fboq.weatherappandroid.Services.Network.WeatherNetworkService
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.GsonBuilder
import io.realm.Realm
import io.realm.RealmObject
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Felix Boquet on 03/05/19
 *
 */

class HistoryListView : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        /*val weather: Array<String> = arrayOf("15° ciel bleu", "10° ciel nuageux")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weather)

        listView.adapter = adapter*/

        //val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
          //  R.layout.activity_main)

        val gson = GsonBuilder().setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.declaringClass == RealmObject::class.java
            }

            override fun shouldSkipClass(clazz: Class<*>): Boolean {
                return false
            }
        }).create()

        val retrofit: Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.darksky.net/forecast/")
            .build()

        Realm.init(applicationContext)
        val realm = Realm.getDefaultInstance()

        val weatherNetworkService: WeatherNetworkService = retrofit.create(
            WeatherNetworkService::class.java)

        weatherNetworkService.getWeather("43.600000", "1.433333")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { weather ->
                    realm.beginTransaction()
                    realm.copyToRealmOrUpdate(weather)
                    realm.commitTransaction()
                    updateViews(weather)
                },
                { error ->
                    Log.e("Error", error.message)
                }
            )

    }

    private fun updateViews(weather: Weather) {
        //Glide.with(this).load(savedUser?.avatarUrl).into(binding.userImage)
        //binding.userName.text = savedUser?.name
        //binding.publicRepos.text = "Public Repos: "+ savedUser?.publicRepos
        //    .toString()

        listView = findViewById<ListView>(R.id.history_list_view)
        val weatherArray: Array<String> = arrayOf(
            weather.id.toString(),
            weather.temperature.toString(),
            weather.summary.toString()
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weatherArray)

        listView.adapter = adapter
    }
}
