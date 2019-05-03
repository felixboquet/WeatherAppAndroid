package com.example.fboq.weatherappandroid.Model

import io.realm.annotations.PrimaryKey

open class Weather(

    @PrimaryKey var id: Long = 0,
    var adress: String = "",
    var date: String?,
    var image: String = "",
    var temperature: Double = 0.0

) {}