package com.example.fboq.weatherappandroid.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*


@RealmClass
open class Currently: RealmObject() {

    @PrimaryKey
    var id: Long = UUID.randomUUID().mostSignificantBits

    @SerializedName("icon")
    @Expose
    var image: String = ""

    @SerializedName("temperature")
    @Expose
    var temperature: Double = 0.0

    @SerializedName("summary")
    @Expose
    var summary: String? = null

}