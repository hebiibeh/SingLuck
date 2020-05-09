package com.example.hebiibeh.singluck.common

import android.app.Application
import android.content.Context
import io.realm.Realm
import io.realm.RealmConfiguration

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder().build()
        Realm.setDefaultConfiguration(config)
    }
}