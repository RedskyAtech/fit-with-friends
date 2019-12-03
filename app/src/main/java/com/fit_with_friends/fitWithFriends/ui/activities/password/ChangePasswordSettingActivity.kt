package com.fit_with_friends.fitWithFriends.ui.activities.password

import android.os.Bundle
import android.os.Handler
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.activity_change_password.editText_confirm_password
import kotlinx.android.synthetic.main.activity_change_password.editText_new_password
import kotlinx.android.synthetic.main.activity_change_password_setting.*
import kotlinx.android.synthetic.main.activity_change_password_setting.imageView_update
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_back_arrow
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class ChangePasswordSettingActivity : BaseAppCompactActivity() {

    @Inject
    internal lateinit var iUserService: IUserService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password_setting)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        listeners()
    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }

        imageView_update.setOnClickListener { view ->
            val oldPassword = editText_old_password.text.toString().trim()
            if (oldPassword.isEmpty()) {
                CommonMethods.alertTopError(this@ChangePasswordSettingActivity, "Please enter old password")
            } else if (editText_new_password.text.toString().isEmpty()) {
                CommonMethods.alertTopError(this@ChangePasswordSettingActivity, "Please enter new password")
            } else if (!isValidPassword(editText_new_password.text.toString().trim { it <= ' ' })) {
                CommonMethods.alertTopError(
                    this@ChangePasswordSettingActivity,
                    "Password Should Contains 1 Cap Word 1 Numeric, 1 Character or 1 Special Character"
                )
            } else if (editText_confirm_password.text.toString().isEmpty()) {
                CommonMethods.alertTopError(this@ChangePasswordSettingActivity, "Please enter confirm password")
            } else if (editText_new_password.text.toString().trim() != editText_confirm_password.text.toString().trim()) {
                CommonMethods.alertTopError(this@ChangePasswordSettingActivity, "Password not matched")
            } else {
                val userModel = UserModel()
                userModel.old_password = oldPassword
                userModel.new_password = editText_new_password.text.toString().trim()
                userModel.comfirm_password = editText_confirm_password.text.toString().trim()
                changePassword(userModel)
            }
        }
    }

    private fun changePassword(userModel: UserModel) {
        imageView_update.isEnabled = false
        imageView_update.isClickable = false
        startAnim()
        iUserService.changePassword(
            Constants.BASE_URL + Constants.CHANGE_PASSWORD,
            null,
            userModel,
            object : AsyncResult<UserModel> {
                override fun success(userModel: UserModel) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopSuccess(
                            this@ChangePasswordSettingActivity,
                            "Password changed successfully"
                        )
                        //Toast.makeText(this@ChangePasswordSettingActivity, "Password changed successfully", Toast.LENGTH_LONG).show()
                        val handler = Handler()
                        handler.postDelayed({
                            finish()
                        }, Constants.ALERT_DURATION)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        imageView_update.isEnabled = true
                        imageView_update.isClickable = true
                        CommonMethods.alertTopError(this@ChangePasswordSettingActivity, error)
                        //Toast.makeText(this@ChangePasswordSettingActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(passwordPattern)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        avi_loader_password.smoothToShow()
    }

    internal fun stopAnim() {
        avi_loader_password.smoothToHide()
    }
}
