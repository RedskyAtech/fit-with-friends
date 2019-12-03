package com.fit_with_friends.fitWithFriends.ui.activities.challenge

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.helpers.DateHelper
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.impl.models.ParticipantModel
import com.fit_with_friends.fitWithFriends.impl.models.WeightLogModel
import com.fit_with_friends.fitWithFriends.ui.adapters.challenge.ParticipantImagesAdapter
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.activity_challenge_details.rLayout_back_arrow
import kotlinx.android.synthetic.main.activity_participant_details.*
import kotlinx.android.synthetic.main.activity_participant_details.textView_challenge_name
import kotlinx.android.synthetic.main.activity_participant_details.textView_end_date
import kotlinx.android.synthetic.main.activity_participant_details.textView_start_date
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList

class ParticipantDetailsActivity : BaseAppCompactActivity() {

    private lateinit var participantImagesAdapter: ParticipantImagesAdapter
    private var imagesModelList: MutableList<WeightLogModel> = ArrayList()

    private lateinit var challengeModel: ChallengeModel
    private lateinit var mParticipantModel: ParticipantModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_participant_details)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        challengeModel = intent.getSerializableExtra(Constants.CHALLENGE_MODEL) as ChallengeModel
        mParticipantModel = intent.getSerializableExtra(Constants.PARTICIPANT_MODEL) as ParticipantModel
        setFonts()
        listeners()
        setAdapters()
        listeners()
        setData(mParticipantModel)
    }

    @SuppressLint("SetTextI18n")
    private fun setData(mParticipantModel: ParticipantModel) {

        if (mParticipantModel.name != null) {
            textView_header_name.text = capitalizeFirstWord.capitalizeWords(mParticipantModel.name)
        }

        if (mParticipantModel.image != null && mParticipantModel.image != "") {
            if (mParticipantModel.image.contains("public")) {
                Glide.with(this).load(Constants.BASE_URL_IMAGE + mParticipantModel.image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry)
                    ).into(imageView_participant)
            } else {
                Glide.with(this)
                    .load(Constants.BASE_URL_IMAGE + "public/" + mParticipantModel.image)
                    .apply(
                        RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry)
                    ).into(imageView_participant)
            }
        } else {
            imageView_participant.setImageResource(R.mipmap.harry)
        }

        if (challengeModel.challange_name != null) {
            textView_challenge_name.text = challengeModel.challange_name
        }

        if (challengeModel.start_date != null) {
            textView_start_date.text = CommonMethods.convertStringToData(challengeModel.start_date)
        }

        if (challengeModel.end_date != null) {
            textView_end_date.text = CommonMethods.convertStringToData(challengeModel.end_date)
        }

        if (mParticipantModel.rank == 0) {
            textView_ranking.text = "-"
        } else {
            textView_ranking.text = "-"
            textView_ranking.text = ordinal(mParticipantModel.rank)
        }

        if (mParticipantModel.weight_loss != null) {
            if (mParticipantModel.weight_loss.contains("-")) {
                textView_weight_loss.text = CommonMethods.trimString(mParticipantModel.weight_loss) + "% Gain"
            } else {
                textView_weight_loss.text = mParticipantModel.weight_loss + "% Lost"
            }
        }

        if (mParticipantModel.weight_logs != null && mParticipantModel.weight_logs.size != 0) {
            imagesModelList.clear()
            imagesModelList.addAll(mParticipantModel.weight_logs)
            participantImagesAdapter.notifyDataSetChanged()
            if (mParticipantModel.weight_logs.size > 2) {
                textView_see_all_images.visibility = View.GONE
            } else {
                textView_see_all_images.visibility = View.GONE
            }
        } else {
            textView_see_all_images.visibility = View.GONE
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

    private fun setFonts() {

    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }
    }

    private fun setAdapters() {
        participantImagesAdapter = ParticipantImagesAdapter(this, imagesModelList, asyncResult, true)
        recyclerView_participants_images.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView_participants_images.adapter = participantImagesAdapter
    }

    private var asyncResult: AsyncResult<WeightLogModel> = object : AsyncResult<WeightLogModel> {
        override fun success(weightLogModel: WeightLogModel) {

        }

        override fun error(error: String) {

        }
    }

    override fun setupActivityComponent() {
    }
}
