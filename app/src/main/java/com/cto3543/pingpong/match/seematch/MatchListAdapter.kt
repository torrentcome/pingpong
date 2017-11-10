package com.cto3543.pingpong.match.seematch

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

class MatchListAdapter(val context: Context, private val list: ArrayList<Match>, private val handler: MatchSelectionAdapter?) : RecyclerView.Adapter<MatchListAdapter.ViewHolder>() {

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
        var formatter = SimpleDateFormat("dd/MM/yyyy")

        var name_1: TextView? = null
        var name_2: TextView? = null
        var score_1: TextView? = null
        var score_2: TextView? = null
        var wheny: TextView? = null

        fun bindForecast(match: Match?) {
            name_1 = view.findViewById(R.id.name_1) as TextView?
            name_2 = view.findViewById(R.id.name_2) as TextView?
            score_1 = view.findViewById(R.id.score_1) as TextView?
            score_1?.setTextColor(Color.WHITE)
            score_2 = view.findViewById(R.id.score_2) as TextView?
            score_2?.setTextColor(Color.WHITE)
            wheny = view.findViewById(R.id.wheny) as TextView?

            if (match != null) {
                with(match) {
                    name_1?.text = match.mref1
                    name_2?.text = match.mref2
                    score_1?.text = match.score1.toString()
                    score_2?.text = match.score2.toString()
                    val d = Date(match.timestamp)
                    wheny?.text = formatter.format(d)

                }

                if (match.score1 == match.score2) {
                    score_1?.setTextColor(Color.WHITE)
                    score_2?.setTextColor(Color.WHITE)
                } else if (match.score1 > match.score2) {
                    score_1?.setTextColor(Color.RED)
                    score_2?.setTextColor(Color.WHITE)
                } else {
                    score_1?.setTextColor(Color.WHITE)
                    score_2?.setTextColor(Color.RED)
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