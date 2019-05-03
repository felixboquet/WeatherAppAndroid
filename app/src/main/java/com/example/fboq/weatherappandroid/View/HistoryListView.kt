package com.example.fboq.weatherappandroid.View

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.fboq.weatherappandroid.R

class HistoryListView : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById<ListView>(R.id.history_list_view)

        val weather: Array<String> = arrayOf("15° ciel bleu", "10° ciel nuageux")

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, weather)

        listView.adapter = adapter
    }
}
