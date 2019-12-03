package com.fit_with_friends.fitWithFriends.ui.activities.notification

import android.os.Bundle
import android.widget.Toast
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IMotivationService
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.MotivationModel
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_notification.rLayout_back_arrow
import kotlinx.android.synthetic.main.activity_notification_settings.*
import javax.inject.Inject

class NotificationSettingsActivity : BaseAppCompactActivity() {

    @Inject
    internal lateinit var iMotivationService: IMotivationService

    @Inject
    internal lateinit var iUserService: IUserService

    private var isAllNotificationSelected: Boolean = false
    private var isMotivationOnlySelected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        listeners()
        getProfileFromServer()
    }

    private fun getProfileFromServer() {
        startAnim()
        val input = PageInput()
        iUserService.getProfile(
            Constants.BASE_URL + Constants.GET_PROFILE,
            null,
            input,
            object : AsyncResult<UserModel> {
                override fun success(userModel: UserModel) {
                    runOnUiThread {
                        stopAnim()
                        if (userModel.user_detail != null) {
                            if (userModel.user_detail.notification_status == "1") {
                                PreferenceHandler.writeBoolean(
                                    this@NotificationSettingsActivity,
                                    Constants.IS_ALL_NOTIFICATION_SELECTED,
                                    true
                                )
                            } else {
                                PreferenceHandler.writeBoolean(
                                    this@NotificationSettingsActivity,
                                    Constants.IS_ALL_NOTIFICATION_SELECTED,
                                    false
                                )
                            }
                            if (userModel.user_detail.motivation_notification_status == "1") {
                                PreferenceHandler.writeBoolean(
                                    this@NotificationSettingsActivity,
                                    Constants.IS_MOTIVATION_ONLY_SELECTED,
                                    true
                                )
                            } else {
                                PreferenceHandler.writeBoolean(
                                    this@NotificationSettingsActivity,
                                    Constants.IS_MOTIVATION_ONLY_SELECTED,
                                    false
                                )
                            }
                            setToggle()
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

    private fun setToggle() {
        isAllNotificationSelected = PreferenceHandler.readBoolean(
            this@NotificationSettingsActivity,
            Constants.IS_ALL_NOTIFICATION_SELECTED,
            true
        )
        isMotivationOnlySelected = PreferenceHandler.readBoolean(
            this@NotificationSettingsActivity,
            Constants.IS_MOTIVATION_ONLY_SELECTED,
            true
        )
        if (isAllNotificationSelected) {
            PreferenceHandler.writeBoolean(
                this@NotificationSettingsActivity,
                Constants.IS_ALL_NOTIFICATION_SELECTED,
                true
            )
            toggle_all_notification.setBackgroundDrawable(resources.getDrawable(R.drawable.on_switch))
        } else {
            PreferenceHandler.writeBoolean(
                this@NotificationSettingsActivity,
                Constants.IS_ALL_NOTIFICATION_SELECTED,
                false
            )
            toggle_all_notification.setBackgroundDrawable(resources.getDrawable(R.drawable.off_switch))
        }

        if (isMotivationOnlySelected) {
            PreferenceHandler.writeBoolean(
                this@NotificationSettingsActivity,
                Constants.IS_MOTIVATION_ONLY_SELECTED,
                true
            )
            toggle_daily_motivation.setBackgroundDrawable(resources.getDrawable(R.drawable.on_switch))
        } else {
            PreferenceHandler.writeBoolean(
                this@NotificationSettingsActivity,
                Constants.IS_MOTIVATION_ONLY_SELECTED,
                false
            )
            toggle_daily_motivation.setBackgroundDrawable(resources.getDrawable(R.drawable.off_switch))
        }
    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }

        toggle_all_notification.setOnClickListener {
            isAllNotificationSelected = PreferenceHandler.readBoolean(
                this@NotificationSettingsActivity,
                Constants.IS_ALL_NOTIFICATION_SELECTED,
                true
            )
            if (isAllNotificationSelected) {
                PreferenceHandler.writeBoolean(
                    this@NotificationSettingsActivity,
                    Constants.IS_ALL_NOTIFICATION_SELECTED,
                    false
                )
                toggle_all_notification.setBackgroundDrawable(resources.getDrawable(R.drawable.off_switch))
                onOffNotification()
            } else {
                PreferenceHandler.writeBoolean(
                    this@NotificationSettingsActivity,
                    Constants.IS_ALL_NOTIFICATION_SELECTED,
                    true
                )
                toggle_all_notification.setBackgroundDrawable(resources.getDrawable(R.drawable.on_switch))
                onOffNotification()
            }
        }

        toggle_daily_motivation.setOnClickListener {
            isMotivationOnlySelected = PreferenceHandler.readBoolean(
                this@NotificationSettingsActivity,
                Constants.IS_MOTIVATION_ONLY_SELECTED,
                true
            )
            if (isMotivationOnlySelected) {
                PreferenceHandler.writeBoolean(
                    this@NotificationSettingsActivity,
                    Constants.IS_MOTIVATION_ONLY_SELECTED,
                    false
                )
                toggle_daily_motivation.setBackgroundDrawable(resources.getDrawable(R.drawable.off_switch))
                onOffMotivation()
            } else {
                PreferenceHandler.writeBoolean(
                    this@NotificationSettingsActivity,
                    Constants.IS_MOTIVATION_ONLY_SELECTED,
                    true
                )
                toggle_daily_motivation.setBackgroundDrawable(resources.getDrawable(R.drawable.on_switch))
                onOffMotivation()
            }
        }
    }

    private fun onOffMotivation() {
        startAnim()
        val motivationModel = MotivationModel()
        iMotivationService.motivationNotiOnOff(
            Constants.BASE_URL + Constants.MOTIVATION_ON_OFF,
            null,
            motivationModel,
            object : AsyncResult<MotivationModel> {
                override fun success(motivationModel: MotivationModel) {
                    runOnUiThread {
                        stopAnim()
                        //Toast.makeText(this@NotificationSettingsActivity, "", Toast.LENGTH_LONG).show()
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    private fun onOffNotification() {
        startAnim()
        val motivationModel = MotivationModel()
        iMotivationService.motivationNotiOnOff(
            Constants.BASE_URL + Constants.NOTIFICATION_ON_OFF,
            null,
            motivationModel,
            object : AsyncResult<MotivationModel> {
                override fun success(motivationModel: MotivationModel) {
                    runOnUiThread {
                        stopAnim()
                        //Toast.makeText(this@NotificationSettingsActivity, "", Toast.LENGTH_LONG).show()
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
        avi_loader_noti.smoothToShow()
    }

    internal fun stopAnim() {
        avi_loader_noti.smoothToHide()
    }
}
