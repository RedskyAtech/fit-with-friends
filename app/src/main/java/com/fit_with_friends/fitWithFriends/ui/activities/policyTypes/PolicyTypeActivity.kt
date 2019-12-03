package com.fit_with_friends.fitWithFriends.ui.activities.policyTypes

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.utils.Constants
import kotlinx.android.synthetic.main.activity_notification.rLayout_back_arrow
import kotlinx.android.synthetic.main.activity_policy_type.*
import javax.inject.Inject

class PolicyTypeActivity : BaseAppCompactActivity() {

    @Inject
    internal lateinit var iUserService: IUserService

    private var activityType: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_policy_type)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        activityType = intent.getStringExtra(Constants.ACTIVITY_TYPE)
        listeners()
        setHeader()
    }

    @SuppressLint("SetTextI18n")
    private fun setHeader() {
        if (activityType != null) {
            when (activityType) {
                "aboutUs" -> {
                    textView_header.text = "About Us"
                    getPolicyFromServer(Constants.ABOUT_US)
                }
                "privacyPolicy" -> {
                    textView_header.text = "Privacy Policy"
                    getPolicyFromServer(Constants.PRIVACY_LINK)
                }
                "terms&conditions" -> {
                    textView_header.text = "Terms & Conditions"
                    getPolicyFromServer(Constants.TERM_CONDITION)
                }
            }
        }
    }

    private fun getPolicyFromServer(action: String) {
        val input = PageInput()
        iUserService.policyType(
            Constants.BASE_URL + action,
            null,
            input,
            object : AsyncResult<UserModel> {
                override fun success(userModel: UserModel) {
                    runOnUiThread {
                        if (userModel.about_us != null && userModel.about_us != "") {
                            setText(userModel.about_us)
                        } else if (userModel.privacy_policy != null && userModel.privacy_policy != "") {
                            setText(userModel.privacy_policy)
                        } else if (userModel.terms_and_conditions != null && userModel.terms_and_conditions != "") {
                            setText(userModel.terms_and_conditions)
                        }
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                    }
                }
            })
    }

    private fun setText(policyType: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView_policy.text = Html.fromHtml(policyType)
        } else {
            textView_policy.text = Html.fromHtml(policyType)
        }
    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }
}
