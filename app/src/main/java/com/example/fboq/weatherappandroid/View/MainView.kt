package com.example.fboq.weatherappandroid.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.fboq.weatherappandroid.model.Weather
import com.example.fboq.weatherappandroid.R
import com.example.fboq.weatherappandroid.services.network.WeatherNetworkService
import com.example.fboq.weatherappandroid.utils.BASE_URL
import com.example.fboq.weatherappandroid.utils.LOCATION_PERMISSION_REQUEST_CODE
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
import java.util.*
import kotlin.math.round

class MainView : AppCompatActivity() {

    private lateinit var temperatureTextView: TextView
    private lateinit var summaryTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var historyButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var weatherNetworkService: WeatherNetworkService
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
          //  R.layout.activity_main)

        temperatureTextView = findViewById(R.id.temperature_text_view)
        summaryTextView = findViewById(R.id.summary_text_view)
        cityTextView = findViewById(R.id.city_text_view)
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
        realm = Realm.getInstance(config)

        weatherNetworkService = retrofit.create(WeatherNetworkService::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }else {
            getLastLocation()
        }

        historyButton.setOnClickListener {
            val intent = Intent(applicationContext, HistoryListView::class.java)
            startActivity(intent)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission enabled, we get the last location
                    getLastLocation()
                } else {
                    // permission denied, we display a toast
                    Toast.makeText(this, "Permission denied for getting location", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

                weatherNetworkService.getWeather(location?.latitude.toString(), location?.longitude.toString())
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

                getCityFromLocation(location?.latitude, location?.longitude)
            }
    }

    private fun getCityFromLocation(lat: Double?, long: Double?) {
        val geocoder = Geocoder(this.applicationContext, Locale.getDefault())
        var addresses: List<Address>

        if (lat != null && long != null) {
            addresses = geocoder.getFromLocation(lat, long, 1)
            cityTextView.text = addresses.get(0).locality
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
