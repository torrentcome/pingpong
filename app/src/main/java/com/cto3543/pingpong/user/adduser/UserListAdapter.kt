package com.cto3543.pingpong.user.adduser

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cto3543.pingpong.R
import com.cto3543.pingpong.user.User

class UserListAdapter(val context: Context, val list: ArrayList<User>, val handler: UserSelectionAdapter?) : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {

    interface UserSelectionAdapter {
        fun onClick(user: User? = null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindForecast(context, list[position])
        holder.setOnClick(handler, list[position])
    }

    override fun getItemCount() = list.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var name: TextView? = null
        var email: TextView? = null
        var score: TextView? = null
        var icon: FloatingActionButton? = null

        fun bindForecast(context: Context, user: User?) {

            score = view.findViewById<TextView>(R.id.score)
            email = view.findViewById<TextView>(R.id.email)
            name = view.findViewById<TextView>(R.id.name)
            icon = view.findViewById<FloatingActionButton>(R.id.icon)

            with(user) {
                email?.text = user?.email
                name?.text = user?.surname
                score?.text = user?.score.toString()
                val drawable: Drawable
                if (user?.sexe as Boolean)
                    drawable = context.resources.getDrawable(R.drawable.avatar1)
                else drawable = context.resources.getDrawable(R.drawable.avatar2)
                icon?.setImageDrawable(drawable)
            }
        }

        fun setOnClick(handler: UserSelectionAdapter?, user: User?) {
            itemView.setOnClickListener { handler?.onClick(user) }
        }
    }

    fun addUser(user: User) {
        list.add(user)
        notifyDataSetChanged()
    }

    fun clean() {
        list.clear()
    }
}