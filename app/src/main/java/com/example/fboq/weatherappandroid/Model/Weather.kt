package com.example.fboq.weatherappandroid.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*

/**
 * Created by Felix Boquet on 03/05/19
 *
 */

@RealmClass
open class Weather() : RealmObject() {

    @PrimaryKey
    var id: Long = UUID.randomUUID().mostSignificantBits

    @SerializedName("timezone")
    @Expose
    open var adress: String = ""

    var date: String? = null

    @SerializedName("currently/icon")
    @Expose
    var image: String = ""

    @SerializedName("currently/temperature")
    @Expose
    var temperature: Double = 0.0

    @SerializedName("currently/summary")
    @Expose
    var summary: String? = null

}