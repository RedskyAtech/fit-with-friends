package com.fit_with_friends.fitWithFriends.ui.adapters.motivation

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
import com.fit_with_friends.fitWithFriends.impl.models.MotivationTypeModel
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.list_motivation.view.*
import java.text.ParseException
import java.text.SimpleDateFormat

class MotivationTypeAdapter(
    var activity: Activity?,
    var motivationModelList: MutableList<MotivationTypeModel>,
    var asyncResult: AsyncResult<String>
) : RecyclerView.Adapter<MotivationTypeAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_motivation, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return motivationModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (motivationModelList[position].image != null && motivationModelList[position].image != "") {
            if (motivationModelList[position].image.contains("public")) {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + motivationModelList[position].image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_motivation)
            } else {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + "public/" + motivationModelList[position].image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                    ).into(holder.itemView.imageView_motivation)
            }
        } else {
            holder.itemView.imageView_motivation.setImageResource(R.mipmap.harry)
        }

        if (motivationModelList[position].publish_at != null) {
            holder.itemView.textView_date.text = convertStringToData(motivationModelList[position].publish_at)
        }

        if (motivationModelList[position].motivation != null) {
            holder.itemView.textView_description.text = motivationModelList[position].motivation
        }


    }

    @Throws(ParseException::class)
    fun convertStringToData(stringData: String): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val data = sdf.parse(stringData)
        return DateHelper.stringify(data, "dd-MM-yyyy")
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}