package com.cto3543.pingpong.match.seematch

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cto3543.pingpong.R
import com.cto3543.pingpong.match.Match
import java.text.SimpleDateFormat
import java.util.*

class MatchListAdapter(val context: Context?, private val list: ArrayList<Match>, private val handler: MatchSelectionAdapter?) : RecyclerView.Adapter<MatchListAdapter.ViewHolder>() {

    interface MatchSelectionAdapter {
        fun onClick(match: Match? = null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_match, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(list[position])
        holder.setOnClick(handler, list[position])
    }

    override fun getItemCount() = list.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        @SuppressLint("SimpleDateFormat")
        private var formatter = SimpleDateFormat("dd/MM/yyyy")

        private var name1: TextView? = null
        private var name2: TextView? = null
        private var scoreTv1: TextView? = null
        private var scoreTv2: TextView? = null
        private var wheny: TextView? = null

        fun bindForecast(match: Match?) {
            name1 = view.findViewById<TextView>(R.id.name_1)
            name2 = view.findViewById<TextView>(R.id.name_2)
            scoreTv1 = view.findViewById<TextView>(R.id.score_1)
            scoreTv1?.setTextColor(Color.WHITE)
            scoreTv2 = view.findViewById<TextView>(R.id.score_2)
            scoreTv2?.setTextColor(Color.WHITE)
            wheny = view.findViewById<TextView>(R.id.wheny)

            if (match != null) {
                with(match) {
                    name1?.text = match.mref1
                    name2?.text = match.mref2
                    scoreTv1?.text = match.score1.toString()
                    scoreTv2?.text = match.score2.toString()
                    val d = Date(match.timestamp)
                    wheny?.text = formatter.format(d)

                }

                if (match.score1 == match.score2) {
                    scoreTv1?.setTextColor(Color.WHITE)
                    scoreTv2?.setTextColor(Color.WHITE)
                } else if (match.score1 > match.score2) {
                    scoreTv1?.setTextColor(Color.RED)
                    scoreTv2?.setTextColor(Color.WHITE)
                } else {
                    scoreTv1?.setTextColor(Color.WHITE)
                    scoreTv2?.setTextColor(Color.RED)
                }
            }
        }

        fun setOnClick(handler: MatchSelectionAdapter?, match: Match?) {
            itemView.setOnClickListener { handler?.onClick(match) }
        }
    }

    fun addMatch(match: Match) {
        list.add(match)
        notifyDataSetChanged()
    }
}