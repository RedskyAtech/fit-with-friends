package com.fit_with_friends.fitWithFriends.ui.adapters.comments

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
import com.fit_with_friends.common.helpers.DateHelper
import com.fit_with_friends.fitWithFriends.impl.models.NotificationModel
import com.fit_with_friends.fitWithFriends.utils.CapitalizeFirstWord
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import kotlinx.android.synthetic.main.list_comments.view.*
import kotlinx.android.synthetic.main.list_notifications.view.imageView_comment
import java.text.ParseException
import java.text.SimpleDateFormat

class CommentsAdapter(
    var activity: Activity?,
    var notificationModelList: MutableList<NotificationModel>,
    var asyncResultEdit: AsyncResult<NotificationModel>,
    var asyncResultDelete: AsyncResult<NotificationModel>
) : RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    private val capitalizeFirstWord: CapitalizeFirstWord = CapitalizeFirstWord()

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_comments, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notificationModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (notificationModelList[position].user.image != null && notificationModelList[position].user.image != "") {
            if (notificationModelList[position].user.image.contains("public")) {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + Constants.BASE_URL_IMAGE + notificationModelList[position].user.image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_comment)
            } else {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + "public/" + notificationModelList[position].user.image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_comment)
            }
        } else {
            holder.itemView.imageView_comment.setImageResource(R.mipmap.harry)
        }

        if (notificationModelList[position].user.name != null) {
            holder.itemView.textView_comment_user.text =
                capitalizeFirstWord.capitalizeWords(notificationModelList[position].user.name)
        }

        if (notificationModelList[position].comment != null) {
            holder.itemView.textView_comment.text = notificationModelList[position].comment
        }

        if (notificationModelList[position].created_at != null) {
            holder.itemView.textView_comment_date.text = convertStringToData(notificationModelList[position].created_at)
        }

        if (notificationModelList[position].user_id != null) {
            if (notificationModelList[position].user_id == PreferenceHandler.readString(
                    activity,
                    Constants.USER_ID,
                    ""
                )
            ) {
                holder.itemView.textView_edit.visibility = View.VISIBLE
                holder.itemView.textView_delete.visibility = View.VISIBLE
            } else {
                holder.itemView.textView_edit.visibility = View.GONE
                holder.itemView.textView_delete.visibility = View.VISIBLE
            }
        }

        holder.itemView.textView_edit.setOnClickListener {
            notificationModelList[position].message = "edit"
            asyncResultEdit.success(notificationModelList[position])
        }

        holder.itemView.textView_delete.setOnClickListener {
            asyncResultDelete.success(notificationModelList[position])
        }
    }

    @Throws(ParseException::class)
    fun convertStringToData(stringData: String): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val data = sdf.parse(stringData)
        return DateHelper.stringify(data, "dd-MM-yyyy")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}