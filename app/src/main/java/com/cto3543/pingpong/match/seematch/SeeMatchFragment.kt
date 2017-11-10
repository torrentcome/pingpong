package com.cto3543.pingpong.match.seematch

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cto3543.pingpong.BaseActivity
import com.cto3543.pingpong.R
import com.cto3543.pingpong.match.Match
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.content_main.view.*

class SeeMatchFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_see_match, container, false)

    private var adapter: MatchListAdapter? = null

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view != null) {
            val layoutManager = GridLayoutManager(activity as Context?, 1)
            view.recycler_view.layoutManager = layoutManager
            adapter = MatchListAdapter(activity as Context?, ArrayList(), object : MatchListAdapter.MatchSelectionAdapter {
                override fun onClick(match: Match?) {

                }
            })
            view.recycler_view.adapter = adapter
            callMatch()
        }
    }

    companion object {
        private val ARG_SECTION_NUMBER = "section_number"
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int): SeeMatchFragment {
            val fragment = SeeMatchFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

    fun callMatch() {
        FirebaseDatabase.getInstance().getReference(BaseActivity.MATCH).addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
            }

            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val match: Match? = p0?.getValue(Match::class.java)
                if (match != null) {
                    (adapter)?.addMatch(match)
                }
            }
        })
    }
}