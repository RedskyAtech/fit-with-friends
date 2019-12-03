package com.fit_with_friends.fitWithFriends.ui.activities.notification

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IChallengeService
import com.fit_with_friends.common.contracts.services.IConnectionService
import com.fit_with_friends.common.contracts.services.INotificationService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.Page
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel
import com.fit_with_friends.fitWithFriends.impl.models.NotificationModel
import com.fit_with_friends.fitWithFriends.ui.activities.comments.CommentsActivity
import com.fit_with_friends.fitWithFriends.ui.adapters.notification.NotificationAdapter
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.android.synthetic.main.activity_notification.rLayout_back_arrow
import java.util.ArrayList
import javax.inject.Inject

class NotificationActivity : BaseAppCompactActivity() {

    @Inject
    lateinit var iNotificationService: INotificationService

    @Inject
    internal lateinit var iConnectionService: IConnectionService

    @Inject
    internal lateinit var iChallengeService: IChallengeService

    private lateinit var notificationAdapter: NotificationAdapter
    private var notificationModelList: MutableList<NotificationModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        listeners()
        setAdapter()
        clearNotificationCount()
        getNotificationListFromServer()
    }

    private fun clearNotificationCount() {
        startAnim()
        iNotificationService.clearNotificationCount(Constants.BASE_URL + Constants.CLEAR_NOTIFICATIONS_COUNT,
            object : AsyncResult<NotificationModel> {
                override fun success(data: NotificationModel?) {
                    runOnUiThread {
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopError(this@NotificationActivity, error)
                    }
                }

            })
    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            val intent = Intent()
            setResult(200, intent)
            finish()
        }

        layout_swipeRefresh.setOnRefreshListener {
            layout_swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
            getNotificationListFromServer()
        }
    }

    private fun setAdapter() {
        notificationAdapter = NotificationAdapter(
            this, notificationModelList, asyncResultAccept, asyncResultReject,
            asyncResultLike, asyncResultComment, asyncResultClearNotification
        )
        recyclerView_notification.layoutManager = LinearLayoutManager(this)
        recyclerView_notification.adapter = notificationAdapter
    }

    private var asyncResultAccept: AsyncResult<NotificationModel> = object : AsyncResult<NotificationModel> {
        override fun success(mNotificationModel: NotificationModel) {
            when {
                mNotificationModel.notification_type == "1" -> {
                    acceptRequest(mNotificationModel)
                }
                mNotificationModel.notification_type == "2" -> {
                }
                mNotificationModel.notification_type == "3" -> {
                    acceptChallengeRequest(mNotificationModel)
                }
            }
        }

        override fun error(error: String) {

        }
    }

    private var asyncResultReject: AsyncResult<NotificationModel> = object : AsyncResult<NotificationModel> {
        override fun success(mNotificationModel: NotificationModel) {
            when {
                mNotificationModel.notification_type == "1" -> {
                    declineRequest(mNotificationModel)
                }
                mNotificationModel.notification_type == "2" -> {
                }
                mNotificationModel.notification_type == "3" -> {
                    declineChallengeRequest(mNotificationModel)
                }
            }
        }

        override fun error(error: String) {

        }
    }

    private var asyncResultLike: AsyncResult<NotificationModel> = object : AsyncResult<NotificationModel> {
        override fun success(mNotificationModel: NotificationModel) {
            notificationLike(mNotificationModel)
        }

        override fun error(error: String) {

        }
    }

    private var asyncResultComment: AsyncResult<NotificationModel> = object : AsyncResult<NotificationModel> {
        override fun success(mNotificationModel: NotificationModel) {
            clearSingleNotificationCount(mNotificationModel)
            val intent = Intent(this@NotificationActivity, CommentsActivity::class.java)
            intent.putExtra(Constants.NOTIFICATION_MODEL, mNotificationModel)
            startActivityForResult(intent, 115)
        }

        override fun error(error: String) {

        }
    }

    private var asyncResultClearNotification: AsyncResult<NotificationModel> = object : AsyncResult<NotificationModel> {
        override fun success(mNotificationModel: NotificationModel) {
            clearSingleNotificationCount(mNotificationModel)
        }

        override fun error(error: String) {

        }
    }

    private fun getNotificationListFromServer() {
        startAnim()
        iNotificationService.getNotificationListFromServer(
            Constants.BASE_URL + Constants.GET_NOTIFICATION,
            null,
            PageInput(),
            object : AsyncResult<Page<NotificationModel>> {
                override fun success(notificationModelPage: Page<NotificationModel>) {
                    runOnUiThread {
                        stopAnim()
                        if (layout_swipeRefresh != null) {
                            layout_swipeRefresh.isRefreshing = false
                        }
                        if (notificationModelPage.body.size != 0) {
                            recyclerView_notification.visibility = View.VISIBLE
                            textView_notification.visibility = View.GONE
                            notificationModelList.clear()
                            notificationModelList.addAll(notificationModelPage.body)
                            notificationAdapter.notifyDataSetChanged()
                        } else {
                            recyclerView_notification.visibility = View.GONE
                            textView_notification.visibility = View.VISIBLE
                        }
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        if (layout_swipeRefresh != null) {
                            layout_swipeRefresh.isRefreshing = false
                        }
                        if (error == "java.lang.IllegalStateException: Expected BEGIN_ARRAY but was BEGIN_OBJECT at line 1 column 74 path \$.body"
                            || error == "Internet connection is slow"
                        ) {
                            //TODO logout
                            CommonMethods.startNextActivity(applicationContext)
                        }
                    }
                }
            })
    }


    private fun acceptRequest(mNotificationModel: NotificationModel) {
        startAnim()
        val connectionModel = ConnectionModel()
        connectionModel.request_id = mNotificationModel.request_id
        iConnectionService.acceptRequest(
            Constants.BASE_URL + Constants.ACCEPT_REQUEST,
            null,
            connectionModel,
            object : AsyncResult<ConnectionModel> {
                override fun success(connectionModel: ConnectionModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(this@NotificationActivity, "Successfully accepted")
                        val handler = Handler()
                        handler.postDelayed({
                            clearSingleNotificationCount(mNotificationModel)
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopError(this@NotificationActivity, error)
                    }
                }
            })
    }

    private fun declineRequest(mNotificationModel: NotificationModel) {
        startAnim()
        val connectionModel = ConnectionModel()
        connectionModel.request_id = mNotificationModel.request_id
        iConnectionService.declineRequest(
            Constants.BASE_URL + Constants.DECLINE_REQUEST,
            null,
            connectionModel,
            object : AsyncResult<ConnectionModel> {
                override fun success(connectionModel: ConnectionModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(this@NotificationActivity, "Successfully rejected")
                        val handler = Handler()
                        handler.postDelayed({
                            clearSingleNotificationCount(mNotificationModel)
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopError(this@NotificationActivity, error)
                    }
                }
            })
    }

    private fun acceptChallengeRequest(mNotificationModel: NotificationModel) {
        startAnim()
        val challengeModel = ChallengeModel()
        challengeModel.participant_id = mNotificationModel.participant_id
        challengeModel.request_status = 2
        iChallengeService.acceptRequest(
            Constants.BASE_URL + Constants.ACCEPT_DECLINE_CHALLENGE,
            null,
            challengeModel,
            object : AsyncResult<ChallengeModel> {
                override fun success(challengeModel: ChallengeModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(this@NotificationActivity, "Successfully accepted")
                        val handler = Handler()
                        handler.postDelayed({
                            clearSingleNotificationCount(mNotificationModel)
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopError(this@NotificationActivity, error)
                    }
                }
            })
    }

    private fun declineChallengeRequest(mNotificationModel: NotificationModel) {
        startAnim()
        val challengeModel = ChallengeModel()
        challengeModel.participant_id = mNotificationModel.participant_id
        challengeModel.request_status = 3
        iChallengeService.declineRequest(
            Constants.BASE_URL + Constants.ACCEPT_DECLINE_CHALLENGE,
            null,
            challengeModel,
            object : AsyncResult<ChallengeModel> {
                override fun success(challengeModel: ChallengeModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(this@NotificationActivity, "Successfully rejected")
                        val handler = Handler()
                        handler.postDelayed({
                            clearSingleNotificationCount(mNotificationModel)
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopError(this@NotificationActivity, error)
                    }
                }
            })
    }


    private fun notificationLike(mNotificationModel: NotificationModel) {
        startAnim()
        val notificationModel = NotificationModel()
        notificationModel.notification_id = mNotificationModel.id.toInt()
        iNotificationService.notificationLike(
            Constants.BASE_URL + Constants.NOTIFICATION_LIKE,
            null,
            notificationModel,
            object : AsyncResult<NotificationModel> {
                override fun success(notificationModel: NotificationModel) {
                    runOnUiThread {
                        stopAnim()
                        clearSingleNotificationCount(mNotificationModel)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopError(this@NotificationActivity, error)
                    }
                }
            })
    }

    private fun clearSingleNotificationCount(mNotificationModel: NotificationModel) {
        startAnim()
        val notificationModel = NotificationModel()
        notificationModel.notification_id = mNotificationModel.id.toInt()
        iNotificationService.clearSingleNotificationsCount(
            Constants.BASE_URL + Constants.CLEAR_SINGLE_NOTIFICATION_COUNT,
            null,
            notificationModel,
            object : AsyncResult<NotificationModel> {
                override fun success(notificationModel: NotificationModel) {
                    runOnUiThread {
                        getNotificationListFromServer()
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        getNotificationListFromServer()
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 115) {
            getNotificationListFromServer()
        }
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        if (avi_loader != null) {
            avi_loader.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader != null) {
            avi_loader.smoothToHide()
        }
    }
}
