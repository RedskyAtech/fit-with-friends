package com.fit_with_friends.fitWithFriends.ui.adapters.notification

import android.annotation.SuppressLint
import android.app.Activity
import android.support.v4.content.ContextCompat
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
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.list_notifications.view.*
import kotlinx.android.synthetic.main.list_notifications.view.imageView_user_new
import kotlinx.android.synthetic.main.list_notifications.view.textView_user_name
import java.text.ParseException
import java.text.SimpleDateFormat

class NotificationAdapter(
    var activity: Activity?,
    private var notificationModelList: MutableList<NotificationModel>,
    private var asyncResultAccept: AsyncResult<NotificationModel>,
    private var asyncResultReject: AsyncResult<NotificationModel>,
    private var asyncResultLike: AsyncResult<NotificationModel>,
    private var asyncResultComment: AsyncResult<NotificationModel>,
    private var asyncResultClearNotification: AsyncResult<NotificationModel>
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_notifications, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notificationModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (notificationModelList[position].notification_type != null && notificationModelList[position].notification_type != "") {
            when {
                notificationModelList[position].notification_type == "1" -> when {
                    notificationModelList[position].message.contains("You have a new friend request from") -> {
                        holder.itemView.rLayout_accept_reject.visibility = View.VISIBLE
                        holder.itemView.rLayout_like_comment.visibility = View.GONE
                    }
                    notificationModelList[position].message.contains("You have a new invitation from") -> {
                        holder.itemView.rLayout_accept_reject.visibility = View.VISIBLE
                        holder.itemView.rLayout_like_comment.visibility = View.GONE
                    }
                    else -> {
                        setLikeComments(holder, position)
                    }
                }
                notificationModelList[position].notification_type == "2" -> {
                    holder.itemView.rLayout_like_comment.visibility = View.GONE
                    holder.itemView.rLayout_accept_reject.visibility = View.GONE
                }
                notificationModelList[position].notification_type == "3" -> when {
                    notificationModelList[position].message.contains("Your challenge request has been accepted by") -> {
                        setLikeComments(holder, position)
                    }
                    notificationModelList[position].message.contains("You have accepted a challenge request from") -> {
                        setLikeComments(holder, position)
                    }
                    else -> {
                        holder.itemView.rLayout_accept_reject.visibility = View.VISIBLE
                        holder.itemView.rLayout_like_comment.visibility = View.GONE
                    }
                }
                notificationModelList[position].notification_type == "4" -> {
                    setLikeComments(holder, position)
                }
                notificationModelList[position].notification_type == "5" -> {
                    setLikeComments(holder, position)
                }
                notificationModelList[position].notification_type == "6" -> {
                    setLikeComments(holder, position)
                }
                else -> {
                    holder.itemView.rLayout_accept_reject.visibility = View.GONE
                    holder.itemView.rLayout_like_comment.visibility = View.GONE
                }
            }
        }

        if (notificationModelList[position].user.image != null && notificationModelList[position].user.image != "") {
            if (notificationModelList[position].user.image.contains("public")) {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + Constants.BASE_URL_IMAGE + notificationModelList[position].user.image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_user_new)
            } else {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + "public/" + notificationModelList[position].user.image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_user_new)
            }
        } else {
            holder.itemView.imageView_user_new.setImageResource(R.mipmap.harry)
        }

        if (notificationModelList[position].user.name != null) {
            holder.itemView.textView_user_name.text = notificationModelList[position].user.name
        }

        if (notificationModelList[position].message != null) {
            holder.itemView.textView_message.text = notificationModelList[position].message
        }

        if (notificationModelList[position].created_at != null) {
            holder.itemView.textView_time.text = convertStringToData(notificationModelList[position].created_at)
        }

        holder.itemView.textView_accept.setOnClickListener {
            asyncResultAccept.success(notificationModelList[position])
        }

        holder.itemView.textView_reject.setOnClickListener {
            asyncResultReject.success(notificationModelList[position])
        }

        holder.itemView.rLayout_like.setOnClickListener {
            asyncResultLike.success(notificationModelList[position])
        }

        holder.itemView.rLayout_comment.setOnClickListener {
            asyncResultComment.success(notificationModelList[position])
        }

        if (notificationModelList[position].notification_counts != null) {
            if (notificationModelList[position].notification_counts != "0") {
                holder.itemView.rLayout_upper.background = ContextCompat.getDrawable(activity!!, R.color.light_sky)
                //holder.itemView.rLayout_bottom_line.background = ContextCompat.getDrawable(activity!!, R.color.white)
            } else {
                holder.itemView.rLayout_upper.background = ContextCompat.getDrawable(activity!!, R.color.white)
                //holder.itemView.rLayout_bottom_line.background = ContextCompat.getDrawable(activity!!, R.color.gray)
            }
        }

        holder.itemView.rLayout_upper.setOnClickListener {
            asyncResultClearNotification.success(notificationModelList[position])
        }
    }

    private fun setLikeComments(holder: ViewHolder, position: Int) {
        holder.itemView.rLayout_like_comment.visibility = View.VISIBLE
        holder.itemView.rLayout_accept_reject.visibility = View.GONE

        if (notificationModelList[position].total_likes != 0) {
            holder.itemView.textView_like_count.visibility = View.VISIBLE
            holder.itemView.textView_like_count.text = notificationModelList[position].total_likes.toString()
            //holder.itemView.imageView_like.setImageResource(R.mipmap.like_filled)
        } else {
            holder.itemView.textView_like_count.visibility = View.GONE
            //holder.itemView.imageView_like.setImageResource(R.mipmap.like)
        }

        if (notificationModelList[position].like_status != null && notificationModelList[position].like_status == "0") {
            holder.itemView.imageView_like.setImageResource(R.mipmap.heart_gray)
        } else {
            holder.itemView.imageView_like.setImageResource(R.mipmap.heart_large)
        }

        if (notificationModelList[position].total_comments != 0) {
            holder.itemView.textView_comment_count.visibility = View.VISIBLE
            holder.itemView.textView_comment_count.text = notificationModelList[position].total_comments.toString()
        } else {
            holder.itemView.textView_comment_count.visibility = View.GONE
        }
    }

    @Throws(ParseException::class)
    fun convertStringToData(stringData: String): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val data = sdf.parse(stringData)
        return DateHelper.stringify(data, "dd-MM-yyyy HH:mm a")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}