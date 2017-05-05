package com.cto3543.pingpong.addmatch

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.cto3543.pingpong.BaseActivity
import com.cto3543.pingpong.R
import com.cto3543.pingpong.adduser.User

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

            // clean
            mEditText1?.setText("")
            mEditText2?.setText("")
            finish()
        }
    }
}
