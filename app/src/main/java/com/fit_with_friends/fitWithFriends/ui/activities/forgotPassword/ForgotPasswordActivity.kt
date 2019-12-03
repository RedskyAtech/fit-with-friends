package com.fit_with_friends.fitWithFriends.ui.activities.forgotPassword

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.Window
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_forgot_password.rLayout_back_arrow
import kotlinx.android.synthetic.main.dialog_forgot_password.*
import java.util.regex.Pattern
import javax.inject.Inject

class ForgotPasswordActivity : BaseAppCompactActivity() {

    @Inject
    internal lateinit var iUserService: IUserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        window.setBackgroundDrawableResource(R.mipmap.bg_image)
        listeners()
    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }

        imageView_btn_send.setOnClickListener { view ->
            if (editText_email.text.toString().isEmpty()) {
                CommonMethods.alertTopError(this@ForgotPasswordActivity, "Please enter email")
            } else if (!isValidEmail(editText_email.text.toString().trim { it <= ' ' })) {
                CommonMethods.alertTopError(this@ForgotPasswordActivity, "Please enter valid email id")
            } else {
                val userModel = UserModel()
                userModel.email = editText_email.text.toString().trim()
                forgotPassword(userModel)
            }
        }
    }

    private fun forgotPassword(userModel: UserModel) {
        startAnim()
        iUserService.forgotPassword(
            Constants.BASE_URL + Constants.FORGOT_PASSWORD,
            null,
            userModel,
            object : AsyncResult<UserModel> {
                override fun success(userModel: UserModel) {
                    runOnUiThread {
                        stopAnim()
                        dialogForgotPassword()
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        CommonMethods.alertTopError(this@ForgotPasswordActivity, error)
                    }
                }
            })
    }

    private fun dialogForgotPassword() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_forgot_password)

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
            val intent = Intent(this@ForgotPasswordActivity, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        dialog.show()
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
        val pattern = Pattern.compile(emailPattern)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        avi_loader_forgot.smoothToShow()
    }

    internal fun stopAnim() {
        avi_loader_forgot.smoothToHide()
    }
}
