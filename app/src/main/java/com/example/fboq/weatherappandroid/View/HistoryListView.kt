package com.example.fboq.weatherappandroid.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
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
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import io.realm.RealmConfiguration


/**
 * Created by Felix Boquet on 03/05/19
 *
 */

class HistoryListView : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var temperatureTextView: TextView
    private lateinit var summaryTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val weather: Array<String> = arrayOf("15° ciel bleu", "10° ciel nuageux")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weather)

        listView.adapter = adapter*/

        //val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
          //  R.layout.activity_main)

        listView = findViewById(R.id.history_list_view)
        temperatureTextView = findViewById(R.id.temperature_text_view)
        summaryTextView = findViewById(R.id.summary_text_view)

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
            .baseUrl("https://api.darksky.net/forecast/f7d5a88bf098e518d6c69bf1e64dc52f/")
            .build()

        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        val realm = Realm.getInstance(config)

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

        if (weather.currently?.temperature != null) {
            var temp = weather.currently?.temperature
            if (temp != null) {
                temp -= 32
                temp/=1.8
               /* val weatherArray: Array<String> = arrayOf(
                    weather.id.toString(),
                    weather.currently?.summary.toString(),
                    temp.toString().plus("°C"),
                    weather.adress
                )

                val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weatherArray)

                listView.adapter = adapter */

                temperatureTextView.text = temp.toString().plus("°C")
                summaryTextView.text = weather.currently?.summary.toString()
            }
        }

    }
}
