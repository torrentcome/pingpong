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
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.cto3543.pingpong.addmatch.AddMatchActivity
import com.cto3543.pingpong.addmatch.Match
import com.cto3543.pingpong.adduser.AddUserActivity
import com.cto3543.pingpong.adduser.User
import com.cto3543.pingpong.adduser.UserListAdapter
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.pchmn.materialchips.ChipView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, UserListAdapter.UserSelectionAdapter {
    private var mRecycler: RecyclerView? = null
    var chips_input1: ChipView? = null
    var chips_input2: ChipView? = null
    var vs: FloatingActionButton? = null

    private var mUser1: User? = null
    private var mUser2: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        mRecycler = findViewById(R.id.recycler_view) as RecyclerView
        chips_input1 = findViewById(R.id.chips_input1) as ChipView
        chips_input2 = findViewById(R.id.chips_input2) as ChipView
        vs = findViewById(R.id.vs) as FloatingActionButton

        val layoutManager = GridLayoutManager(this, 1)
        mRecycler?.layoutManager = layoutManager as RecyclerView.LayoutManager?
        mRecycler?.adapter = UserListAdapter(this, ArrayList<User>(), object : UserListAdapter.UserSelectionAdapter {
            override fun onClick(user: User?) {
                // initialize
                if (mUser1 != null && mUser2 != null) {
                    bindForecast(chips_input1, null)
                    bindForecast(chips_input2, null)
                    mUser1 = null
                    mUser2 = null
                }

                // logic
                if (mUser1 == null) {
                    mUser1 = user
                    bindForecast(chips_input1, mUser1)
                } else {
                    mUser2 = user
                    bindForecast(chips_input2, mUser2)
                }
            }
        })
        setData()
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
                    user.key = p0.key
                    (mRecycler?.adapter as UserListAdapter).addUser(user)
                }
            }
        })

        databaseRefMatch.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                println(p0)
                val listMatch = p0?.children
                listMatch?.forEach {
                    val match: Match? = it.getValue(Match::class.java)
                    if (match?.score1 as Int > match.score2) {
                        val user = databaseRefUser.orderByChild(match.mRef1).addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onCancelled(p0: DatabaseError?) {
                            }

                            override fun onDataChange(p0: DataSnapshot?) {
                            }

                        })

                    } else {
                        val user = databaseRefUser.orderByChild("mRef2").equalTo(match.mRef2)
                    }
                }
            }
        })

        vs?.setOnClickListener {
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
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when (id) {
            R.id.nav_add_user -> startActivity(Intent(this, AddUserActivity::class.java))
            else -> Unit
        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onClick(user: User?) {

    }
}
