package com.fit_with_friends.fitWithFriends.ui.adapters.participants

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.fitWithFriends.impl.models.ParticipantModel
import com.fit_with_friends.fitWithFriends.utils.CapitalizeFirstWord
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.list_participants.view.*
import kotlinx.android.synthetic.main.list_participants.view.imageView_user_new

class ParticipantsAdapter(
    var activity: Activity?,
    private var participantsModelList: MutableList<ParticipantModel>,
    var asyncResult: AsyncResult<ParticipantModel>,
    var isFromDetailPage: Boolean
) : RecyclerView.Adapter<ParticipantsAdapter.ViewHolder>() {

    val capitalizeFirstWord: CapitalizeFirstWord = CapitalizeFirstWord()

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_participants, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        /*return if (isFromDetailPage && participantsModelList.size > 2) {
            2
        } else {
            participantsModelList.size
        }*/
        return participantsModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (participantsModelList[position].image != null && participantsModelList[position].image != "") {
            if (participantsModelList[position].image.contains("public")) {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + participantsModelList[position].image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_user_new)
            } else {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + "public/" + participantsModelList[position].image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_user_new)
            }
        } else {
            holder.itemView.imageView_user_new.setImageResource(R.mipmap.harry)
        }

        if (participantsModelList[position].name != null) {
            holder.itemView.textView_name.text =
                capitalizeFirstWord.capitalizeWords(participantsModelList[position].name)
        }

        if (participantsModelList[position].rank == 0) {
            holder.itemView.textView_pos.text = "-"
        } else {
            holder.itemView.textView_pos.text = ordinal(participantsModelList[position].rank)
        }

        holder.itemView.rLayout_participant.setOnClickListener {
            asyncResult.success(participantsModelList[position])
        }
    }

    fun ordinal(i: Int): String {
        val sufixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
        return when (i % 100) {
            11, 12, 13 -> i.toString() + "th"
            else -> i.toString() + sufixes[i % 10]
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}