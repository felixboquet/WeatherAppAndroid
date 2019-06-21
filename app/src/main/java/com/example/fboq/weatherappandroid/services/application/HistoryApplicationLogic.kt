package com.example.fboq.weatherappandroid.services.application

import com.example.fboq.weatherappandroid.model.Currently
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where

class HistoryApplicationLogic {

    fun getAllCurrently(realm: Realm): RealmResults<Currently> {
        return realm.where<Currently>().findAllAsync()
    }

}