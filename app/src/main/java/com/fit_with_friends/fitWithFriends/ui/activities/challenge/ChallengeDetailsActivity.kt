package com.fit_with_friends.fitWithFriends.ui.activities.challenge

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IChallengeService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.helpers.DateHelper
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.impl.models.ParticipantModel
import com.fit_with_friends.fitWithFriends.ui.activities.createChallenge.CreateChallengeActivity
import com.fit_with_friends.fitWithFriends.ui.adapters.participants.ParticipantsAdapter
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.assist.FailReason
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener
import kotlinx.android.synthetic.main.activity_challenge_details.*
import kotlinx.android.synthetic.main.activity_challenge_details.rLayout_back_arrow
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChallengeDetailsActivity : BaseAppCompactActivity() {

    @Inject
    internal lateinit var iChallengeService: IChallengeService

    private lateinit var participantsAdapter: ParticipantsAdapter
    private var participantsModelList: MutableList<ParticipantModel> = ArrayList()

    private lateinit var mChallengeModel: ChallengeModel
    private var activityType: String? = ""
    private lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_details)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        mChallengeModel = intent.getSerializableExtra(Constants.CHALLENGE_MODEL) as ChallengeModel
        activityType = intent.getStringExtra(Constants.ACTIVITY_TYPE)
        initView()
        setFonts()
        listeners()
        setAdapters()
        challengeDetails()
    }

    private fun initView() {
        imageLoader = ImageLoader.getInstance()
    }

    private fun challengeDetails() {
        startAnim()
        val challengeModel = ChallengeModel()
        challengeModel.challenge_id = mChallengeModel.id
        iChallengeService.challengeDetail(
            Constants.BASE_URL + Constants.CHALLENGE_DETAIL,
            null,
            challengeModel,
            object : AsyncResult<ChallengeModel> {
                override fun success(challengeModel: ChallengeModel) {
                    runOnUiThread {
                        stopAnim()
                        mChallengeModel = challengeModel
                        val userId = PreferenceHandler.readString(this@ChallengeDetailsActivity, Constants.USER_ID, "")
                        if (userId != null && userId == challengeModel.user_id) {
                            if (activityType != "past") {
                                textView_edit.visibility = View.VISIBLE
                            } else {
                                textView_edit.visibility = View.GONE
                            }
                        } else {
                            textView_edit.visibility = View.GONE
                        }
                        setData(challengeModel)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun setData(challengeModel: ChallengeModel) {

        if (challengeModel.start_date != null) {
            try {
                val startDate = convertStringToData(challengeModel.start_date)
                val endDate = convertStringToData(challengeModel.end_date)
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
                val d1 = formatter.parseDateTime(currentDate)
                val d2 = formatter.parseDateTime(endDate)
                //val diffInMillis = d2.millis - d1.millis
                val duration = Duration(d1, d2)
                //val hours = duration.standardHours
                //val minutes = duration.standardMinutes
                if (activityType != null && activityType.equals("past")) {
                    val days = duration.standardDays.toString()
                    if (days.contains("-")) {
                        textView_days_left.text = "Ended " + CommonMethods.trimString(days) + " days ago"
                    } else {
                        textView_days_left.text = "Ended $days days ago"
                    }
                } else {
                    val days = duration.standardDays.toString()
                    if (days.contains("-")) {
                        textView_days_left.text = "Ended " + CommonMethods.trimString(days) + " days ago"
                    } else {
                        if (currentDate >= startDate) {
                            textView_days_left.text = "$days Days Left"
                        } else {
                            textView_days_left.text = "About to start"
                        }
                    }
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }

        if (challengeModel.challange_image != null && challengeModel.challange_image != "") {
            if (challengeModel.challange_image.contains("public")) {
                if (imageView_post != null) {
                    displayImage(
                        imageView_post,
                        Constants.BASE_URL_IMAGE + Constants.BASE_URL_IMAGE + challengeModel.challange_image
                    )
                    /*Glide.with(this)
                        .load(Constants.BASE_URL_IMAGE + Constants.BASE_URL_IMAGE + challengeModel.challange_image)
                        .apply(
                            RequestOptions().placeholder(R.mipmap.pic_demo).error(R.mipmap.pic_demo).dontAnimate()
                        ).into(imageView_post)*/
                }
            } else {
                if (imageView_post != null) {
                    displayImage(imageView_post, Constants.BASE_URL_IMAGE + "public/" + challengeModel.challange_image)
                    /*Glide.with(this)
                        .load(Constants.BASE_URL_IMAGE + "public/" + challengeModel.challange_image)
                        .apply(
                            RequestOptions().placeholder(R.mipmap.pic_demo).error(R.mipmap.pic_demo).dontAnimate()
                        ).into(imageView_post)*/
                }
            }
        } else {
            if (imageView_post != null) {
                imageView_post.setImageResource(R.mipmap.pic_demo)
            }
        }

        if (challengeModel.challange_name != null) {
            textView_challenge_name.text = challengeModel.challange_name
        }

        if (challengeModel.user != null) {
            if (challengeModel.user.name != null) {
                textView_created_by.text = "Created by " + challengeModel.user.name
            }
        }

        if (challengeModel.start_date != null) {
            textView_start_date.text = CommonMethods.convertStringToData(challengeModel.start_date)
        }

        if (challengeModel.end_date != null) {
            textView_end_date.text = CommonMethods.convertStringToData(challengeModel.end_date)
        }

        if (challengeModel.your_ranking != null && challengeModel.your_ranking == "0") {
            textView_your_ranking.text = "-"
        } else {
            textView_your_ranking.text = CommonMethods.ordinal(challengeModel.your_ranking.toInt())
        }

        if (challengeModel.body_weight != null) {
            if (challengeModel.body_weight.contains("-")) {
                textView_body_weight.text = CommonMethods.trimString(challengeModel.body_weight) + "% Gain"
            } else {
                textView_body_weight.text = challengeModel.body_weight + "% Lost"
            }
        }

        if (challengeModel.collective_body_weight != null) {
            if (challengeModel.collective_body_weight.contains("-")) {
                textView_collective_body_weight.text =
                    CommonMethods.trimString(challengeModel.collective_body_weight) + "% Gain"
            } else {
                textView_collective_body_weight.text = challengeModel.collective_body_weight + "% Lost"
            }
        }

        textView_no_participants.text = challengeModel.participant_count.toString()

        if (challengeModel.participant != null && challengeModel.participant.size != 0) {
            participantsModelList.clear()
            participantsModelList.addAll(challengeModel.participant)
            participantsAdapter.notifyDataSetChanged()
            if (challengeModel.participant.size > 2) {
                textView_see_all.visibility = View.GONE
            } else {
                textView_see_all.visibility = View.GONE
            }
        } else {
            textView_see_all.visibility = View.GONE
        }

        if (challengeModel.description != null) {
            textView_description.text = challengeModel.description
        }
    }

    private fun displayImage(imageView: ImageView, url: String) {
        imageLoader.displayImage(url, imageView, object : ImageLoadingListener {
            override fun onLoadingStarted(s: String, view: View) {

            }

            override fun onLoadingFailed(s: String, view: View, failReason: FailReason) {
                if (imageView_post != null) {
                    imageView_post.setImageResource(R.mipmap.pic_demo)
                }
            }

            override fun onLoadingComplete(s: String, view: View, bitmap: Bitmap) {

            }

            override fun onLoadingCancelled(s: String, view: View) {
                if (imageView_post != null) {
                    imageView_post.setImageResource(R.mipmap.pic_demo)
                }
            }
        })
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

        rLayout_accept.setOnClickListener {
            acceptRequest()
        }

        rLayout_decline.setOnClickListener {
            declineRequest()
        }

        imageView_share.setOnClickListener {
            share()
        }

        textView_edit.setOnClickListener {
            val intent = Intent(this@ChallengeDetailsActivity, CreateChallengeActivity::class.java)
            intent.putExtra(Constants.FROM_ACTIVITY, "ChallengeDetailsActivity")
            intent.putExtra(Constants.CHALLENGE_MODEL, mChallengeModel)
            startActivity(intent)
        }
    }

    private fun share() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(
            Intent.EXTRA_TEXT, "http://202.164.42.227/fitwithfriends/deeplink.php?challenge_id=" + mChallengeModel.id
        )
        startActivity(Intent.createChooser(sharingIntent, "Share Ceed External"))
    }

    private fun acceptRequest() {
        startAnim()
        val challengeModel = ChallengeModel()
        iChallengeService.acceptRequest(
            Constants.BASE_URL + Constants.ACCEPT_REQUEST,
            null,
            challengeModel,
            object : AsyncResult<ChallengeModel> {
                override fun success(challengeModel: ChallengeModel) {
                    runOnUiThread {
                        stopAnim()
                        Toast.makeText(this@ChallengeDetailsActivity, "Success", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    private fun declineRequest() {
        startAnim()
        val challengeModel = ChallengeModel()
        iChallengeService.declineRequest(
            Constants.BASE_URL + Constants.DECLINE_REQUEST,
            null,
            challengeModel,
            object : AsyncResult<ChallengeModel> {
                override fun success(challengeModel: ChallengeModel) {
                    runOnUiThread {
                        stopAnim()
                        Toast.makeText(this@ChallengeDetailsActivity, "Success", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    private fun setAdapters() {
        participantsAdapter = ParticipantsAdapter(this, participantsModelList, asyncResult, true)
        recyclerView_participants.layoutManager = LinearLayoutManager(this)
        recyclerView_participants.adapter = participantsAdapter
    }

    private var asyncResult: AsyncResult<ParticipantModel> = object : AsyncResult<ParticipantModel> {
        override fun success(participantModel: ParticipantModel) {
            val intent = Intent(this@ChallengeDetailsActivity, ParticipantDetailsActivity::class.java)
            intent.putExtra(Constants.CHALLENGE_MODEL, mChallengeModel)
            intent.putExtra(Constants.PARTICIPANT_MODEL, participantModel)
            startActivity(intent)
        }

        override fun error(error: String) {

        }
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        loader_challenge_detail.smoothToShow()
    }

    internal fun stopAnim() {
        loader_challenge_detail.smoothToHide()
    }
}
