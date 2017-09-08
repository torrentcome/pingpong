package com.cto3543.pingpong.addmatch

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.cto3543.pingpong.BaseActivity
import com.cto3543.pingpong.R
import com.cto3543.pingpong.adduser.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.MutableData
import com.google.firebase.database.Transaction

class AddMatchActivity : BaseActivity() {

    private var mUser1: User? = null
    private var mUser2: User? = null

    private var mEditText1: EditText? = null
    private var mEditText2: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_match)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add Match"

        val bundle = intent.getBundleExtra("bundle")
        mUser1 = bundle.getParcelable("mUser1")
        mUser2 = bundle.getParcelable("mUser2")

        (findViewById(R.id.name1) as TextView).text = mUser1?.email
        (findViewById(R.id.name2) as TextView).text = mUser2?.email

        mEditText1 = findViewById(R.id.edit1) as EditText
        mEditText2 = findViewById(R.id.edit2) as EditText

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            attemptMatch()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun attemptMatch() {
        mEditText1!!.error = null
        mEditText2!!.error = null

        val edit1 = mEditText1!!.text.toString()
        val edit2 = mEditText2!!.text.toString()
        var focusView: View? = null
        var cancel = false

        if (TextUtils.isEmpty(edit1)) {
            mEditText1!!.error = getString(R.string.error_field_required)
            focusView = mEditText1
            cancel = true
        } else if (TextUtils.isEmpty(edit2)) {
            mEditText2!!.error = getString(R.string.error_field_required)
            focusView = mEditText2
            cancel = true
        }

        if (cancel) {
            focusView!!.requestFocus()
        } else {
            databaseRefMatch.push().setValue(Match(mUser1?.key, mUser2?.key, edit1.toInt(), edit2.toInt(), System.currentTimeMillis()))

            if (edit1.toInt() == edit2.toInt()) {
                Log.i("", "Firebase no increment")
            } else if (edit1.toInt() > edit2.toInt()) {
                setScore(mUser1)
            } else {
                setScore(mUser2)
            }

            // clean
            mEditText1?.setText("")
            mEditText2?.setText("")
            finish()
        }
    }

    private fun setScore(mUser: User?) {
        databaseRefUser.ref.child(mUser?.key).child("score").runTransaction(object : Transaction.Handler {
            override fun onComplete(p0: DatabaseError?, p1: Boolean, p2: DataSnapshot?) {
                if (p0 != null) {
                    Log.i("Firebase", "Firebase increment failed p0.code =" + p0.code)
                    Log.i("Firebase", "Firebase increment failed details=" + p0.details)
                    Log.i("Firebase", "Firebase increment failed message=" + p0.message)
                } else {
                    Log.i("Firebase", "Firebase increment sucess")
                }
            }

            override fun doTransaction(p0: MutableData?): Transaction.Result? {
                if (p0 != null) {
                    if (p0.value == null) {
                        p0.value = 0
                    } else {
                        val value: Long = p0.value as Long
                        p0.value = value + 1
                    }
                    return Transaction.success(p0)
                } else {
                    return Transaction.abort()
                }
            }
        })
    }
}
