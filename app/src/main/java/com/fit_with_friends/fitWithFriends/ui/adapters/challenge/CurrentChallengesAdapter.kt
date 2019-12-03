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
import com.fit_with_friends.fitWithFriends.utils.CapitalizeFirstWord
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.list_challenges_past_current.view.*
import kotlinx.android.synthetic.main.list_challenges_past_current.view.imageView_user_new
import kotlinx.android.synthetic.main.list_challenges_past_current.view.textView_description
import kotlinx.android.synthetic.main.list_challenges_past_current.view.textView_user_name
import java.text.ParseException
import java.text.SimpleDateFormat

import org.joda.time.format.DateTimeFormat.forPattern
import org.joda.time.Duration
import java.util.*


class CurrentChallengesAdapter(
    var activity: Activity?,
    private var currentChallengesModelList: MutableList<ChallengeModel>,
    var asyncResult: AsyncResult<ChallengeModel>
) : RecyclerView.Adapter<CurrentChallengesAdapter.ViewHolder>() {

    private val capitalizeFirstWord: CapitalizeFirstWord = CapitalizeFirstWord()

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_challenges_past_current, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return currentChallengesModelList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (currentChallengesModelList[position].challange_image != null && currentChallengesModelList[position].challange_image != "") {
            if (currentChallengesModelList[position].challange_image.contains("public")) {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + currentChallengesModelList[position].challange_image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_user_new)
            } else {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + "public/" + currentChallengesModelList[position].challange_image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_user_new)
            }
        } else {
            holder.itemView.imageView_user_new.setImageResource(R.mipmap.harry)
        }

        if (currentChallengesModelList[position].user.name != null) {
            holder.itemView.textView_user_name.text =
                capitalizeFirstWord.capitalizeWords(currentChallengesModelList[position].user.name)
        }

        if (currentChallengesModelList[position].challange_name != null) {
            holder.itemView.textView_description.text =
                capitalizeFirstWord.capitalizeWords(currentChallengesModelList[position].challange_name)
        }

        if (currentChallengesModelList[position].start_date != null) {
            try {
                val startDate = convertStringToData(currentChallengesModelList[position].start_date)
                val endDate = convertStringToData(currentChallengesModelList[position].end_date)
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                val formatter = forPattern("yyyy-MM-dd")
                val d1 = formatter.parseDateTime(currentDate)
                val d2 = formatter.parseDateTime(endDate)
                //val diffInMillis = d2.millis - d1.millis
                val duration = Duration(d1, d2)
                val days = duration.standardDays
                //val hours = duration.standardHours
                //val minutes = duration.standardMinutes
                if (currentDate >= startDate) {
                    holder.itemView.textView_time.text = "$days Days Left"
                } else {
                    holder.itemView.textView_time.text = "About to start"
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        holder.itemView.rLayout_upper_main.setOnClickListener {
            asyncResult.success(currentChallengesModelList[position])
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