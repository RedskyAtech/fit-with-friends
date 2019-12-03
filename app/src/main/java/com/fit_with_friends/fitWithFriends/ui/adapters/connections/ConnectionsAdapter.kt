package com.fit_with_friends.fitWithFriends.ui.adapters.connections

import android.annotation.SuppressLint
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
import kotlinx.android.synthetic.main.list_connections.view.*
import kotlinx.android.synthetic.main.list_connections.view.imageView_user_new
import kotlinx.android.synthetic.main.list_connections.view.textView_user_name

class ConnectionsAdapter(
    var activity: Activity?,
    var connectionsModelList: MutableList<ConnectionModel>,
    var asyncResult: AsyncResult<ConnectionModel>
) : RecyclerView.Adapter<ConnectionsAdapter.ViewHolder>() {

    private val capitalizeFirstWord: CapitalizeFirstWord = CapitalizeFirstWord()

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_connections, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return connectionsModelList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (connectionsModelList[position].friend_detail != null) {
            holder.itemView.textView_add.visibility = View.GONE
            if (connectionsModelList[position].friend_detail.image != null && connectionsModelList[position].friend_detail.image != "") {
                if (connectionsModelList[position].friend_detail.image.contains("public")) {
                    Glide.with(activity)
                        .load(Constants.BASE_URL_IMAGE + connectionsModelList[position].friend_detail.image)
                        .apply(
                            RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                        ).into(holder.itemView.imageView_user_new)
                } else {
                    Glide.with(activity)
                        .load(Constants.BASE_URL_IMAGE + "public/" + connectionsModelList[position].friend_detail.image)
                        .apply(
                            RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                        ).into(holder.itemView.imageView_user_new)
                }
            } else {
                holder.itemView.imageView_user_new.setImageResource(R.mipmap.harry)
            }

            if (connectionsModelList[position].friend_detail != null && connectionsModelList[position].friend_detail.name != null) {
                holder.itemView.textView_user_name.text =
                    capitalizeFirstWord.capitalizeWords(connectionsModelList[position].friend_detail.name)
            }

            if (connectionsModelList[position].friend_detail != null && connectionsModelList[position].friend_detail.email != null) {
                holder.itemView.textView_email.visibility = View.VISIBLE
                holder.itemView.textView_email.text = connectionsModelList[position].friend_detail.email
            }
        } else {
            holder.itemView.textView_email.visibility = View.GONE
            holder.itemView.textView_add.visibility = View.VISIBLE
            if (connectionsModelList[position].image != null && connectionsModelList[position].image != "") {
                if (connectionsModelList[position].image.contains("public")) {
                    Glide.with(activity)
                        .load(Constants.BASE_URL_IMAGE + connectionsModelList[position].image)
                        .into(holder.itemView.imageView_user_new)
                } else {
                    Glide.with(activity)
                        .load(Constants.BASE_URL_IMAGE + "public/" + connectionsModelList[position].image)
                        .into(holder.itemView.imageView_user_new)
                }
            } else {
                holder.itemView.imageView_user_new.setImageResource(R.mipmap.harry)
            }

            if (connectionsModelList[position].name != null) {
                holder.itemView.textView_user_name.text =
                    capitalizeFirstWord.capitalizeWords(connectionsModelList[position].name)
            }

            if (connectionsModelList[position].request_status != null) {
                when {
                    connectionsModelList[position].request_status == "0" -> {
                        holder.itemView.textView_add.visibility = View.VISIBLE
                        holder.itemView.textView_sent.visibility = View.GONE
                        holder.itemView.textView_friends.visibility = View.GONE
                    }
                    connectionsModelList[position].request_status == "1" -> {
                        holder.itemView.textView_add.visibility = View.GONE
                        holder.itemView.textView_sent.visibility = View.VISIBLE
                        holder.itemView.textView_friends.visibility = View.GONE
                        //holder.itemView.textView_add.isClickable = false
                        //holder.itemView.textView_add.isEnabled = false
                    }
                    else -> {
                        holder.itemView.textView_add.visibility = View.GONE
                        holder.itemView.textView_sent.visibility = View.GONE
                        holder.itemView.textView_friends.visibility = View.VISIBLE
                        //holder.itemView.textView_add.isClickable = false
                        //holder.itemView.textView_add.isEnabled = false
                    }
                }
            }
        }

        holder.itemView.textView_add.setOnClickListener {
            asyncResult.success(connectionsModelList[position])
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}