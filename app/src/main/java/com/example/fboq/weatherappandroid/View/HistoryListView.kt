package com.example.fboq.weatherappandroid.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.fboq.weatherappandroid.R
import com.example.fboq.weatherappandroid.services.application.HistoryApplicationLogic
import io.realm.Realm
import io.realm.RealmConfiguration

class HistoryListView: AppCompatActivity() {

    private lateinit var historyListView: ListView
    private lateinit var realm: Realm
    private val applicationLogic = HistoryApplicationLogic()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        Realm.init(applicationContext)
        val config = RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build()
        realm = Realm.getInstance(config)

        historyListView = findViewById(R.id.history_list_view)

        val weather: Array<String> = arrayOf("15° ciel bleu", "10° ciel nuageux")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weather)

        historyListView.adapter = adapter
    }
}