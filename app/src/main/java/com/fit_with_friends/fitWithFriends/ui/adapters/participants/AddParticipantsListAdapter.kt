package com.fit_with_friends.fitWithFriends.ui.adapters.participants

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel
import kotlinx.android.synthetic.main.list_challenges_participants.view.*

class AddParticipantsListAdapter(
    var activity: Activity?,
    private var participantsModelList: MutableList<ConnectionModel>,
    private var participantsList: MutableList<ConnectionModel>,
    var asyncResult: AsyncResult<ConnectionModel>,
    var isUpdate: Boolean
) : RecyclerView.Adapter<AddParticipantsListAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_challenges_participants, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return participantsModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.itemView.textView_participant_name.text = participantsModelList[position].friend_detail.name
        holder.itemView.checkbox.setOnClickListener {
            if (participantsModelList.size != 0) {
                participantsModelList[position].isChecked = true
                asyncResult.success(participantsModelList[position])
            }
        }

        if (!isUpdate) {
            if (participantsList.size != 0) {
                for (i in 0 until participantsList.size) {
                    if (participantsList[i].friend_detail.id == participantsModelList[position].friend_detail.id) {
                        holder.itemView.checkbox.isChecked = true
                        Log.d("", "true")
                        return
                    } else {
                        holder.itemView.checkbox.isChecked = false
                        Log.d("", "true")
                    }
                }
            }
        }

        if (isUpdate) {
            if (participantsList.size != 0) {
                for (i in 0 until participantsList.size) {
                    if (participantsList[i].friend_detail.id == participantsModelList[position].friend_detail.id) {
                        holder.itemView.checkbox.isChecked = true
                        holder.itemView.checkbox.isClickable = false
                        return
                    } else {
                        holder.itemView.checkbox.isChecked = false
                        holder.itemView.checkbox.isClickable = true
                    }
                }
            }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}