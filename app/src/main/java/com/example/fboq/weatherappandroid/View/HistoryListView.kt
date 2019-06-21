package com.example.fboq.weatherappandroid.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.fboq.weatherappandroid.R
import com.example.fboq.weatherappandroid.services.application.HistoryApplicationLogic
import io.realm.Realm

class HistoryListView: AppCompatActivity() {

    private lateinit var historyListView: ListView
    private val realm = Realm.getDefaultInstance()
    private val applicationLogic = HistoryApplicationLogic()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        historyListView = findViewById(R.id.history_list_view)

        val weather: Array<String> = arrayOf("15° ciel bleu", "10° ciel nuageux")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weather)

        historyListView.adapter = adapter
    }
}