package com.fit_with_friends.fitWithFriends.ui.adapters.participants

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel
import kotlinx.android.synthetic.main.list_add_participants.view.*
import kotlinx.android.synthetic.main.list_challenges_participants.view.*

class ParticipantsListAdapter(
    var activity: Activity?,
    private var participantsList: MutableList<ConnectionModel>,
    var asyncResult: AsyncResult<ConnectionModel>
) : RecyclerView.Adapter<ParticipantsListAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_add_participants, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return participantsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemView.textView_part_name.text = participantsList[position].friend_detail.name
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}