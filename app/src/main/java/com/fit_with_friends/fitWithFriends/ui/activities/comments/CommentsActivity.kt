package com.fit_with_friends.fitWithFriends.ui.activities.comments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.INotificationService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.Page
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.NotificationModel
import com.fit_with_friends.fitWithFriends.ui.adapters.comments.CommentsAdapter
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.image.KeyboardUtils
import kotlinx.android.synthetic.main.activity_comments.*
import kotlinx.android.synthetic.main.activity_notification.rLayout_back_arrow
import java.util.ArrayList
import javax.inject.Inject

class CommentsActivity : BaseAppCompactActivity() {

    @Inject
    lateinit var iNotificationService: INotificationService

    private lateinit var commentsAdapter: CommentsAdapter
    private var notificationModelList: MutableList<NotificationModel> = ArrayList()

    var mNotificationModel: NotificationModel = NotificationModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comments)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        mNotificationModel = intent.getSerializableExtra(Constants.NOTIFICATION_MODEL) as NotificationModel
        listeners()
        setAdapter()
        getNotificationCommentListFromServer()
    }

    private fun getNotificationCommentListFromServer() {
        startAnim()
        val input = PageInput()
        input.query.add("notification_id", mNotificationModel.id)
        iNotificationService.getNotificationCommentListFromServer(
            Constants.BASE_URL + Constants.GET_NOTIFICATION_COMMENT_LIST,
            null,
            input,
            object : AsyncResult<Page<NotificationModel>> {
                override fun success(notificationModelPage: Page<NotificationModel>) {
                    runOnUiThread {
                        stopAnim()
                        if (notificationModelPage.body.size != 0) {
                            if (recyclerView_comments != null) {
                                recyclerView_comments.visibility = View.VISIBLE
                            }
                            if (textView_comments != null) {
                                textView_comments.visibility = View.GONE
                            }
                            notificationModelList.clear()
                            notificationModelList.addAll(notificationModelPage.body)
                            commentsAdapter.notifyDataSetChanged()
                        } else {
                            if (recyclerView_comments != null) {
                                recyclerView_comments.visibility = View.GONE
                            }
                            if (textView_comments != null) {
                                textView_comments.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }


    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }

        imageView_send.setOnClickListener {
            if (mNotificationModel.message != null && mNotificationModel.message == "edit") {
                editComment(mNotificationModel)
            } else {
                if (editText_comment.text.toString().trim().isEmpty()) {
                    Toast.makeText(this@CommentsActivity, "Please enter comment", Toast.LENGTH_LONG).show()
                } else {
                    notificationComment(mNotificationModel)
                }
            }
        }
    }

    private fun notificationComment(mNotificationModel: NotificationModel) {
        startAnim()
        val notificationModel = NotificationModel()
        notificationModel.notification_id = mNotificationModel.id.toInt()
        notificationModel.comment = editText_comment.text.toString().trim()
        iNotificationService.notificationComment(
            Constants.BASE_URL + Constants.NOTIFICATION_COMMENT,
            null,
            notificationModel,
            object : AsyncResult<NotificationModel> {
                override fun success(notificationModel: NotificationModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(this@CommentsActivity, "Successfully commented")
                        val handler = Handler()
                        handler.postDelayed({
                            val intent = Intent()
                            setResult(115, intent)
                            finish()
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    private fun setAdapter() {
        commentsAdapter = CommentsAdapter(this, notificationModelList, asyncResultEdit, asyncResultDelete)
        recyclerView_comments.layoutManager = LinearLayoutManager(this)
        recyclerView_comments.adapter = commentsAdapter
    }

    private var asyncResultEdit: AsyncResult<NotificationModel> = object : AsyncResult<NotificationModel> {
        override fun success(notificationModel: NotificationModel) {
            mNotificationModel = notificationModel
            editText_comment.requestFocus()
            editText_comment.setText(notificationModel.comment)
            editText_comment.setSelection(editText_comment.text.length)
            KeyboardUtils.showKeyboard(this@CommentsActivity)
        }

        override fun error(error: String) {

        }
    }

    private fun editComment(mNotificationModel: NotificationModel) {
        startAnim()
        val notificationModel = NotificationModel()
        notificationModel.comment_id = mNotificationModel.id
        notificationModel.comment = editText_comment.text.toString().trim()
        iNotificationService.notificationComment(
            Constants.BASE_URL + Constants.EDIT_COMMENT,
            null,
            notificationModel,
            object : AsyncResult<NotificationModel> {
                override fun success(notificationModel: NotificationModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(this@CommentsActivity, "Successfully edited")
                        val handler = Handler()
                        handler.postDelayed({
                            val intent = Intent()
                            setResult(115, intent)
                            finish()
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    private var asyncResultDelete: AsyncResult<NotificationModel> = object : AsyncResult<NotificationModel> {
        override fun success(mNotificationModel: NotificationModel) {
            deleteComment(mNotificationModel)
        }

        override fun error(error: String) {

        }
    }

    private fun deleteComment(mNotificationModel: NotificationModel) {
        startAnim()
        val notificationModel = NotificationModel()
        notificationModel.comment_id = mNotificationModel.id
        iNotificationService.notificationComment(
            Constants.BASE_URL + Constants.DELETE_COMMENT,
            null,
            notificationModel,
            object : AsyncResult<NotificationModel> {
                override fun success(notificationModel: NotificationModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(this@CommentsActivity, "Successfully deleted")
                        val handler = Handler()
                        handler.postDelayed({
                            val intent = Intent()
                            setResult(115, intent)
                            finish()
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        if (avi_loader_comments != null) {
            avi_loader_comments.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader_comments != null) {
            avi_loader_comments.smoothToHide()
        }
    }
}
