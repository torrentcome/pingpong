package com.cto3543.pingpong.user.adduser

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import com.cto3543.pingpong.BaseActivity
import com.cto3543.pingpong.R
import com.cto3543.pingpong.user.User
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

/**
 * A login screen that offers login via email/surname.
 */
class AddUserActivity : BaseActivity() {

    // UI references
    private var mEmailView: TextView? = null
    private var mSurnameView: TextView? = null
    private var mProgressView: View? = null
    private var mLoginFormView: View? = null
    private var mIconGuy: ImageView? = null
    private var mIconGirl: ImageView? = null
    private var mRecycler: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // Set up the login form.
        mEmailView = findViewById<TextView>(R.id.email)
        mSurnameView = findViewById<TextView>(R.id.surname)
        mIconGuy = findViewById<ImageView>(R.id.icon_guy)
        mIconGirl = findViewById<ImageView>(R.id.icon_girl)
        mLoginFormView = findViewById(R.id.login_form)
        mProgressView = findViewById(R.id.login_progress)

        mRecycler = findViewById<RecyclerView>(R.id.recycler_view)

        setUI()
        setData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_user, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.add_user -> attemptAddUser()
        }
        return super.onOptionsItemSelected(item)
    }

    var isAGuy: Boolean = true

    fun setUI() {
        mIconGirl?.visibility = GONE

        mIconGuy?.setOnClickListener {
            isAGuy = false
            mIconGuy?.visibility = GONE
            mIconGirl?.visibility = VISIBLE
        }

        mIconGirl?.setOnClickListener {
            isAGuy = false
            mIconGuy?.visibility = VISIBLE
            mIconGirl?.visibility = GONE
        }

        // recyclerview
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mRecycler?.layoutManager = layoutManager
        mRecycler?.adapter = UserListAdapter(this, ArrayList<User>(), null)
    }


    fun setData() {
        databaseRefUser.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.e("AddUserActivity", p0.toString())
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val user: User? = p0?.getValue(User::class.java)
                if (user != null) {
                    (mRecycler?.adapter as UserListAdapter).addUser(user)
                }
            }
        })
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptAddUser() {
        // Reset errors.
        mEmailView!!.error = null
        mSurnameView!!.error = null

        // Store values at the time of the login attempt.
        val email = mEmailView!!.text.toString()
        val surname = mSurnameView!!.text.toString()
        val isHoF = isAGuy
        var cancel = false
        var focusView: View? = null

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView!!.error = getString(R.string.error_field_required)
            focusView = mEmailView
            cancel = true
        } else if (!isEmailValid(email)) {
            mEmailView!!.error = getString(R.string.error_invalid_email)
            focusView = mEmailView
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView!!.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            pushUserTask(isHoF, email, surname)
        }
    }

    fun pushUserTask(isHoF: Boolean, email: String, surname: String?) {
        databaseRefUser.push().setValue(User(isHoF, email, surname))
        // clean
        mEmailView?.setText("")
        mSurnameView?.setText("")
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}

