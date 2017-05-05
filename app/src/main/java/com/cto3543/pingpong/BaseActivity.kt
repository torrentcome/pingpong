package com.cto3543.pingpong

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

abstract class BaseActivity : AppCompatActivity() {

    // object
    companion object {
        const val USER: String = "user"
        const val MATCH: String = "match"
    }

    // firebase
    var databaseRefUser: DatabaseReference = FirebaseDatabase.getInstance().getReference(USER)
    var databaseRefMatch: DatabaseReference = FirebaseDatabase.getInstance().getReference(MATCH)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
