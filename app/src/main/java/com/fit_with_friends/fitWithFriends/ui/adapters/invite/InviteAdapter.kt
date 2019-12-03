package com.fit_with_friends.fitWithFriends.ui.adapters.invite

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel
import com.fit_with_friends.fitWithFriends.utils.CapitalizeFirstWord
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.list_connections.view.imageView_user_new
import kotlinx.android.synthetic.main.list_connections.view.textView_email
import kotlinx.android.synthetic.main.list_connections.view.textView_user_name
import kotlinx.android.synthetic.main.list_invites.view.*

class InviteAdapter(
    private var activity: Activity?,
    private var inviteModelList: MutableList<ConnectionModel>,
    private var asyncResultReject: AsyncResult<ConnectionModel>,
    private var asyncResultResend: AsyncResult<ConnectionModel>
) : RecyclerView.Adapter<InviteAdapter.ViewHolder>() {

    private val capitalizeFirstWord: CapitalizeFirstWord = CapitalizeFirstWord()

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_invites, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return inviteModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (inviteModelList[position].friend_detail != null) {
            if (inviteModelList[position].friend_detail.image != null && inviteModelList[position].friend_detail.image != "") {
                if (inviteModelList[position].friend_detail.image.contains("public")) {
                    Glide.with(activity)
                        .load(Constants.BASE_URL_IMAGE + inviteModelList[position].friend_detail.image)
                        .apply(
                            RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                        ).into(holder.itemView.imageView_user_new)
                } else {
                    Glide.with(activity)
                        .load(Constants.BASE_URL_IMAGE + "public/" + inviteModelList[position].friend_detail.image)
                        .apply(
                            RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                        ).into(holder.itemView.imageView_user_new)
                }
            } else {
                holder.itemView.imageView_user_new.setImageResource(R.mipmap.harry)
            }

            if (inviteModelList[position].friend_detail != null && inviteModelList[position].friend_detail.name != null) {
                holder.itemView.textView_user_name.text =
                    capitalizeFirstWord.capitalizeWords(inviteModelList[position].friend_detail.name)
            }

            if (inviteModelList[position].friend_detail != null && inviteModelList[position].friend_detail.email != null) {
                holder.itemView.textView_email.visibility = View.VISIBLE
                holder.itemView.textView_email.text = inviteModelList[position].friend_detail.email
            }
        }

        if (inviteModelList[position].request_status == "3") {
            holder.itemView.rLayout_resend.visibility = View.VISIBLE
        } else {
            holder.itemView.rLayout_resend.visibility = View.GONE
        }

        holder.itemView.rLayout_cancel.setOnClickListener {
            asyncResultReject.success(inviteModelList[position])
        }

        holder.itemView.rLayout_resend.setOnClickListener {
            asyncResultResend.success(inviteModelList[position])
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}