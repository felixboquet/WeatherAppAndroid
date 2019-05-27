package com.example.fboq.weatherappandroid.view

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.example.fboq.weatherappandroid.Model.Weather
import com.example.fboq.weatherappandroid.R
import com.example.fboq.weatherappandroid.Services.Network.WeatherNetworkService
import com.example.fboq.weatherappandroid.Utils.BASE_URL
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
import kotlin.math.round

class MainView : AppCompatActivity() {

    private lateinit var temperatureTextView: TextView
    private lateinit var summaryTextView: TextView
    private lateinit var historyButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
          //  R.layout.activity_main)

        temperatureTextView = findViewById(R.id.temperature_text_view)
        summaryTextView = findViewById(R.id.summary_text_view)
        historyButton = findViewById(R.id.history_button)

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
            .baseUrl(BASE_URL)
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

        historyButton.setOnClickListener {
            val intent = Intent(applicationContext, HistoryListView::class.java)
            startActivity(intent)
        }

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

                temperatureTextView.text = round(temp).toString().plus("°C")
                summaryTextView.text = weather.currently?.summary.toString()
            }
        }

    }
}
