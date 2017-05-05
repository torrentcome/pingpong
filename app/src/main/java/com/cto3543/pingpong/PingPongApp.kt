package com.cto3543.pingpong

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by cto3543 on 14/04/2017.
 */
class PingPongApp : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}