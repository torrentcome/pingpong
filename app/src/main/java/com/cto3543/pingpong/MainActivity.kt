package com.cto3543.pingpong

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.cto3543.pingpong.match.addmatch.AddMatchActivity
import com.cto3543.pingpong.match.seematch.SeeMatchActivity
import com.cto3543.pingpong.user.User
import com.cto3543.pingpong.user.adduser.AddUserActivity
import com.cto3543.pingpong.user.adduser.UserListAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.pchmn.materialchips.ChipView


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, UserListAdapter.UserSelectionAdapter {
    private var mRecycler: RecyclerView? = null
    var chipsInput1: ChipView? = null
    var chipsInput2: ChipView? = null
    var vs: FloatingActionButton? = null

    private var mUser1: User? = null
    private var mUser2: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        mRecycler = findViewById<RecyclerView>(R.id.recycler_view)
        chipsInput1 = findViewById<ChipView>(R.id.chips_input1)
        chipsInput2 = findViewById<ChipView>(R.id.chips_input2)
        vs = findViewById<FloatingActionButton>(R.id.vs)

        val layoutManager = GridLayoutManager(this, 1)
        mRecycler?.layoutManager = layoutManager
        mRecycler?.adapter = UserListAdapter(this, ArrayList<User>(), object : UserListAdapter.UserSelectionAdapter {
            override fun onClick(user: User?) {
                // initialize
                if (mUser1 != null && mUser2 != null) {
                    bindForecast(chipsInput1, null)
                    bindForecast(chipsInput2, null)
                    mUser1 = null
                    mUser2 = null
                }

                // logic
                if (mUser1 == null) {
                    mUser1 = user
                    bindForecast(chipsInput1, mUser1)
                } else {
                    mUser2 = user
                    bindForecast(chipsInput2, mUser2)
                }
            }
        })
        vs?.setOnClickListener {
            startMatch()
        }
        callUser()
    }

    fun bindForecast(view: ChipView?, user: User?) {
        with(user) {
            when (user) {
                null -> {
                    view?.label = ""
                    view?.setHasAvatarIcon(false)
                }
                else -> {
                    val split = user.email.split("@")
                    view?.label = split.get(0)
                    val drawable: Drawable
                    view?.setHasAvatarIcon(true)
                    if (user.sexe)
                        drawable = resources.getDrawable(R.drawable.avatar1)
                    else
                        drawable = resources.getDrawable(R.drawable.avatar2)
                    view?.setAvatarIcon(drawable)
                }
            }
        }
    }

    private fun startMatch() {
        if (mUser1?.key.equals(mUser2?.key)) {
            Snackbar.make(mRecycler as View, "Wrong selection", Snackbar.LENGTH_LONG).show()
        } else if (mUser1 != null && mUser2 != null) {
            val bundle: Bundle = Bundle()
            bundle.putParcelable("mUser1", mUser1)
            bundle.putParcelable("mUser2", mUser2)
            val intent = Intent(this, AddMatchActivity::class.java)
            intent.putExtra("bundle", bundle)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_start) {
            startMatch()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        callUser()
    }

    fun callUser() {
        (mRecycler?.adapter as UserListAdapter).clean()
        databaseRefUser.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                println("call user :" + p0)
                val user: User? = p0?.getValue(User::class.java)
                if (user != null) {
                    user.key = p0.key
                    (mRecycler?.adapter as UserListAdapter).addUser(user)
                }
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.nav_add_user -> startActivity(Intent(this, AddUserActivity::class.java))
            R.id.nav_see_match -> startActivity(Intent(this, SeeMatchActivity::class.java))
            else -> Unit
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(user: User?) {

    }
}
