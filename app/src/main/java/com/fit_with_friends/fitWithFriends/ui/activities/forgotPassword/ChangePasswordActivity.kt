package com.fit_with_friends.fitWithFriends.ui.activities.forgotPassword

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.Window
import android.widget.Toast
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.ui.activities.home.DashboardActivity
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.dialog_forgot_password.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

class ChangePasswordActivity : BaseAppCompactActivity() {

    @Inject
    internal lateinit var iUserService: IUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        window.setBackgroundDrawableResource(R.mipmap.bg_image)
        listeners()
    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }

        imageView_btn_submit.setOnClickListener { view ->
            val password = editText_new_password.text.toString().trim()
            if (password.isEmpty()) {
                CommonMethods.alertTopError(this@ChangePasswordActivity, "Please enter new password")
            } else if (!isValidPassword(editText_new_password.text.toString().trim { it <= ' ' })) {
                CommonMethods.alertTopError(
                    this@ChangePasswordActivity,
                    "Password Should Contains 1 Cap Word 1 Numeric, 1 Character or 1 Special Character"
                )
            } else if (editText_confirm_password.text.toString().isEmpty()) {
                CommonMethods.alertTopError(this@ChangePasswordActivity, "Please enter confirm password")
            } else if (editText_confirm_password.text.toString().trim() != password) {
                CommonMethods.alertTopError(this@ChangePasswordActivity, "Password not matched")
            } else {
                changePasswordDialog()
            }
        }
    }

    private fun changePassword(userModel: UserModel) {
        iUserService.changePassword(
            Constants.BASE_URL + "/changePassword",
            null,
            userModel,
            object : AsyncResult<UserModel> {
                override fun success(userModel: UserModel) {
                    runOnUiThread {

                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                    }
                }
            })
    }

    private fun changePasswordDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_change_password)

        val window = dialog.window
        if (window != null) {
            val wlp = window.attributes
            window.setLayout(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
            window.setBackgroundDrawableResource(android.R.color.transparent)
            wlp.gravity = Gravity.CENTER
            //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.attributes = wlp
        }
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialog.imageView_cross.setOnClickListener {
            dialog.dismiss()
        }

        dialog.imageView_ok.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, DashboardActivity::class.java)
            PreferenceHandler.writeString(applicationContext, Constants.INFO_FILLED, Constants.DASHBOARD)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        dialog.show()
    }

    private fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(passwordPattern)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }
}
