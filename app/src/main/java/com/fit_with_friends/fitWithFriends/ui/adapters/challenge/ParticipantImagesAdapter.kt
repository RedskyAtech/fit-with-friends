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
import com.fit_with_friends.fitWithFriends.impl.models.WeightLogModel
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.list_images.view.*
import java.text.ParseException
import java.text.SimpleDateFormat

class ParticipantImagesAdapter(
    var activity: Activity?,
    private var imagesModelList: MutableList<WeightLogModel>,
    var asyncResult: AsyncResult<WeightLogModel>,
    var isFromDetailPage: Boolean
) : RecyclerView.Adapter<ParticipantImagesAdapter.ViewHolder>() {

    init {

    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_images, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        /*return if (isFromDetailPage && imagesModelList.size > 2) {
            2
        } else {
            imagesModelList.size
        }*/
        return imagesModelList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (imagesModelList[position].image != null && imagesModelList[position].image != "") {
            if (imagesModelList[position].image.contains("public")) {
                Glide.with(activity).load(Constants.BASE_URL_IMAGE + imagesModelList[position].image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.wight_machine).error(R.mipmap.wight_machine).dontAnimate()
                    ).into(holder.itemView.imageView_myFav)
            } else {
                Glide.with(activity)
                    .load(Constants.BASE_URL_IMAGE + "public/" + imagesModelList[position].image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.wight_machine).error(R.mipmap.wight_machine).dontAnimate()
                    ).into(holder.itemView.imageView_myFav)
            }
        } else {
            holder.itemView.imageView_myFav.setImageResource(R.mipmap.wight_machine)
        }

        if (imagesModelList[position].created_at != null) {
            holder.itemView.textView_date.text = convertStringToData(imagesModelList[position].created_at)
        }

        if (imagesModelList[position].weight_type == 1) {
            val weight: Int = imagesModelList[position].weight.toInt()
            val weightValue: String = weight.toString()
            holder.itemView.textView_weight.text = "$weightValue kg"
        } else {
            val weight: Int = imagesModelList[position].weight.toInt()
            val weightValue: String = weight.toString()
            holder.itemView.textView_weight.text = "$weightValue lbs"
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