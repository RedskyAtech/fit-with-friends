package com.fit_with_friends.fitWithFriends.ui.adapters.challenge

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
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.list_challenges.view.*
import kotlinx.android.synthetic.main.list_challenges.view.imageView_user_new
import kotlinx.android.synthetic.main.list_challenges.view.textView_description
import kotlinx.android.synthetic.main.list_challenges.view.textView_user_name
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChallengeAdapter(
    var activity: Activity?,
    private var challengesModelList: MutableList<ChallengeModel>,
    private var async: AsyncResult<ChallengeModel>,
    private var asyncResult: AsyncResult<ChallengeModel>
) : RecyclerView.Adapter<ChallengeAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_challenges, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return challengesModelList.size
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val pos = holder.adapterPosition + 1
        holder.itemView.textView_count.text = pos.toString()

        if (challengesModelList[position].challange_image != null && challengesModelList[position].challange_image != "") {
            if (challengesModelList[position].challange_image.contains("public")) {
                Glide.with(activity).load(Constants.BASE_URL_IMAGE + challengesModelList[position].challange_image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_user_new)
            } else {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + "public/" + challengesModelList[position].challange_image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_user_new)
            }
        } else {
            holder.itemView.imageView_user_new.setImageResource(R.mipmap.harry)
        }

        if (challengesModelList[position].name != null) {
            holder.itemView.textView_user_name.text = challengesModelList[position].name
        }

        if (challengesModelList[position].challange_name != null) {
            holder.itemView.textView_challenge_name.text = challengesModelList[position].challange_name
        }

        if (challengesModelList[position].body_weight != null) {
            if (challengesModelList[position].body_weight.contains("-")) {
                holder.itemView.textView_description.text =
                    "You've gained " + CommonMethods.trimString(challengesModelList[position].body_weight) + "% body weight"
            } else {
                holder.itemView.textView_description.text =
                    "You've lost " + challengesModelList[position].body_weight + "% body weight"
            }
        }

        if (challengesModelList[position].user != null) {
            if (challengesModelList[position].user.name != null) {
                holder.itemView.textView_user_name.text = challengesModelList[position].user.name
            }
        }

        if (challengesModelList[position].start_date != null) {
            try {
                val startDate = convertStringToData(challengesModelList[position].start_date)
                val endDate = convertStringToData(challengesModelList[position].end_date)
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
                val d1 = formatter.parseDateTime(currentDate)
                val d2 = formatter.parseDateTime(endDate)
                //val diffInMillis = d2.millis - d1.millis
                val duration = Duration(d1, d2)
                val days = duration.standardDays.toString()
                //val hours = duration.standardHours
                //val minutes = duration.standardMinutes
                if (currentDate >= startDate) {
                    if (currentDate > endDate) {
                        if (days.contains("-")) {
                            holder.itemView.textView_days_count.text =
                                "Ended " + CommonMethods.trimString(days) + " days ago"
                        }
                    } else {
                        holder.itemView.textView_days_count.text = days
                        holder.itemView.textView_days_left.text = "Days Left"
                    }
                } else {
                    holder.itemView.textView_days_count.text = "-"
                    holder.itemView.textView_days_left.text = "ABOUT TO START"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        if (challengesModelList[position].your_ranking != null && challengesModelList[position].your_ranking != "0") {
            holder.itemView.textView_rank.text = ordinal(challengesModelList[position].your_ranking.toInt())
        } else {
            holder.itemView.textView_rank.text = "-"
        }

        holder.itemView.rLayout_user_info.setOnClickListener {
            asyncResult.success(challengesModelList[position])
        }

        holder.itemView.layout_challenge_detail.setOnClickListener {
            asyncResult.success(challengesModelList[position])
        }

        holder.itemView.imageView_share.setOnClickListener {
            async.success(challengesModelList[position])
        }
    }

    private fun ordinal(i: Int): String {
        val sufixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
        return when (i % 100) {
            11, 12, 13 -> i.toString() + "th"
            else -> i.toString() + sufixes[i % 10]
        }
    }

    @Throws(ParseException::class)
    fun convertStringToData(stringData: String): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val data = sdf.parse(stringData)
        return DateHelper.stringify(data, "yyyy-MM-dd")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}