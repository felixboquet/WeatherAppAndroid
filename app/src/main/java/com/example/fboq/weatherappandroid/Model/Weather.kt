package com.example.fboq.weatherappandroid.Model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass
import java.util.*


@RealmClass
open class Weather : RealmObject() {

    @PrimaryKey
    var id: Long = UUID.randomUUID().mostSignificantBits

    @SerializedName("timezone")
    @Expose
    open var adress: String = ""

    var date: String? = null

    @SerializedName("currently")
    @Expose
    open var currently: Currently? = null

}