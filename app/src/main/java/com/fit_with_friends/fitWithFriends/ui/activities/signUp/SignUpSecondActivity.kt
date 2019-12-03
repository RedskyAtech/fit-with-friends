package com.fit_with_friends.fitWithFriends.ui.activities.signUp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.ui.activities.home.DashboardActivity
import com.fit_with_friends.fitWithFriends.ui.activities.policyTypes.PolicyTypeActivity
import com.fit_with_friends.fitWithFriends.ui.activities.signIn.SignInActivity
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.InputFilterMinMax
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import com.fit_with_friends.fitWithFriends.utils.image.KeyboardUtils
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_up_second.*
import kotlinx.android.synthetic.main.activity_sign_up_second.checkbox_terms
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_cm
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_date_of_birth
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_feet
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_gender
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_height
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_inch
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_m
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_weight
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_cm
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_feet
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_female
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_inches
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_kg
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_lbs
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_m
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_male
import kotlinx.android.synthetic.main.activity_sign_up_second.layout_gender_dialog
import kotlinx.android.synthetic.main.activity_sign_up_second.layout_height_dialog
import kotlinx.android.synthetic.main.activity_sign_up_second.layout_weight_dialog
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_back_arrow
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_cm
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_feet
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_feet_main
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_female
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_gender
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_height
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_inches
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_kg
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_lbs
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_m
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_male
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_meter_main
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_weight
import kotlinx.android.synthetic.main.activity_sign_up_second.textView_sign_in
import kotlinx.android.synthetic.main.activity_sign_up_second.textView_weight_type
import java.util.*
import javax.inject.Inject

class SignUpSecondActivity : BaseAppCompactActivity() {

    @Inject
    internal lateinit var iUserService: IUserService

    private lateinit var mUserModel: UserModel

    private var isWeight: Boolean = true
    private var isHeight: Boolean = true
    private var isGender: Boolean = true
    private var dateOfBirth: String = ""
    private var gender: String = ""
    private var weightType: String = ""
    private var heightType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_second)
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        window.setBackgroundDrawableResource(R.mipmap.bg_image)
        mUserModel = intent.getSerializableExtra(Constants.USER_MODEL) as UserModel
        initView()
        listeners()
        setTypes()
        rLayout_feet.performClick()
    }

    @SuppressLint("SetTextI18n")
    private fun setTypes() {
        val isWeightSelected =
            PreferenceHandler.readBoolean(this@SignUpSecondActivity, Constants.IS_WEIGHT_SELECTED, true)
        if (isWeightSelected) {
            imageView_radio_kg.setImageResource(R.mipmap.radio_selected)
            imageView_radio_lbs.setImageResource(R.mipmap.radio_unselected)
            weightType = "1"
            textView_weight_type.text = "kg"
        } else {
            imageView_radio_lbs.setImageResource(R.mipmap.radio_selected)
            imageView_radio_kg.setImageResource(R.mipmap.radio_unselected)
            weightType = "2"
            textView_weight_type.text = "lbs"
        }

        when (PreferenceHandler.readString(this@SignUpSecondActivity, Constants.HEIGHT_SELECTED, "")) {
            "1" -> {
                imageView_radio_cm.setImageResource(R.mipmap.radio_selected)
                imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
                heightType = "1"
                textView_height_type.text = "cm"
                if (textInputLayout != null) {
                    textInputLayout.visibility = View.VISIBLE
                }
                if (rLayout_feet_main != null) {
                    rLayout_feet_main.visibility = View.GONE
                }
            }
            "2" -> {
                imageView_radio_m.setImageResource(R.mipmap.radio_selected)
                imageView_radio_cm.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
                heightType = "2"
                textView_height_type.text = "meters"
                if (rLayout_meter_main != null) {
                    rLayout_meter_main.visibility = View.VISIBLE
                }
                if (rLayout_feet_main != null) {
                    rLayout_feet_main.visibility = View.GONE
                }
            }
            "3" -> {
                imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_cm.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_feet.setImageResource(R.mipmap.radio_selected)
                imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
                heightType = "3"
                textView_height_type.text = "feet"
                if (rLayout_meter_main != null) {
                    rLayout_meter_main.visibility = View.GONE
                }
                if (rLayout_feet_main != null) {
                    rLayout_feet_main.visibility = View.VISIBLE
                }
            }
            "4" -> {
                imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_cm.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_inches.setImageResource(R.mipmap.radio_selected)
                heightType = "4"
                textView_height_type.text = "inches"
            }
        }
    }

    private fun initView() {
        gender = "m"
        weightType = "2"
        heightType = "3"
        if (rLayout_meter_main != null) {
            rLayout_meter_main.visibility = View.GONE
        }
        if (rLayout_feet_main != null) {
            rLayout_feet_main.visibility = View.VISIBLE
        }
        addWatcher()
    }

    private fun addWatcher() {
        editText_inch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.toString().contains("0")) {
                    if (editText_inch != null) {
                        editText_inch.filters = arrayOf(InputFilter.LengthFilter(2))
                    }
                } else {
                    if (editable.length <= 2) {
                        if (editText_inch != null) {
                            editText_inch.filters = arrayOf(InputFilterMinMax("0", "11"))
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun listeners() {

        rLayout_back_arrow.setOnClickListener {
            finish()
        }

        textView_terms.setOnClickListener {
            KeyboardUtils.hideKeyboard(this)
            val intent = Intent(this@SignUpSecondActivity, PolicyTypeActivity::class.java)
            intent.putExtra(Constants.ACTIVITY_TYPE, "terms&conditions")
            startActivity(intent)
        }

        rLayout_top.setOnClickListener {
            dialogHide()
        }

        layout_outside.setOnClickListener {
            dialogHide()
        }

        rLayout_logo.setOnClickListener {
            dialogHide()
        }

        layout_terms.setOnClickListener {
            dialogHide()
        }

        checkbox_terms.setOnClickListener {
            KeyboardUtils.hideKeyboard(this)
            dialogHide()
        }

        editText_date_of_birth.setOnClickListener {
            KeyboardUtils.hideKeyboard(this)
            dialogHide()
            val calendar = Calendar.getInstance()
            var day = calendar.get(Calendar.DAY_OF_MONTH)
            var month = calendar.get(Calendar.MONTH)
            var year = calendar.get(Calendar.YEAR)

            val dpd = DatePickerDialog(
                this,
                /*android.R.style.Theme_Holo_Dialog,*/
                DatePickerDialog.OnDateSetListener { _, currentYear, monthOfYear, dayOfMonth ->
                    day = dayOfMonth
                    month = monthOfYear + 1
                    year = currentYear
                    dateOfBirth = "$day-$month-$year"
                    editText_date_of_birth.setText("$day - $month - $year")
                }, year, month, day
            )

            dpd.datePicker.maxDate = System.currentTimeMillis()

            dpd.show()
        }

        editText_weight.setOnClickListener {
            dialogHide()
        }

        editText_height.setOnClickListener {
            dialogHide()
        }

        editText_referral_code.setOnClickListener {
            dialogHide()
        }

        editText_gender.setOnClickListener {
            KeyboardUtils.hideKeyboard(this)
            genderDialog()
        }

        rLayout_weight.setOnClickListener {
            if (isWeight) {
                isWeight = false
                layout_weight_dialog.bringToFront()
                layout_weight_dialog.visibility = View.VISIBLE
                layout_height_dialog.visibility = View.GONE
                layout_gender_dialog.visibility = View.GONE
                val isWeightSelected =
                    PreferenceHandler.readBoolean(this@SignUpSecondActivity, Constants.IS_WEIGHT_SELECTED, true)
                if (isWeightSelected) {
                    imageView_radio_kg.setImageResource(R.mipmap.radio_selected)
                    imageView_radio_lbs.setImageResource(R.mipmap.radio_unselected)
                    weightType = "1"
                    textView_weight_type.text = "kg"
                } else {
                    imageView_radio_lbs.setImageResource(R.mipmap.radio_selected)
                    imageView_radio_kg.setImageResource(R.mipmap.radio_unselected)
                    weightType = "2"
                    textView_weight_type.text = "lbs"
                }
            } else {
                isWeight = true
                dialogHide()
            }
        }

        rLayout_height.setOnClickListener {
            if (isHeight) {
                isHeight = false
                layout_height_dialog.bringToFront()
                layout_weight_dialog.visibility = View.GONE
                layout_height_dialog.visibility = View.VISIBLE
                layout_gender_dialog.visibility = View.GONE
                when (PreferenceHandler.readString(this@SignUpSecondActivity, Constants.HEIGHT_SELECTED, "")) {
                    "1" -> {
                        imageView_radio_cm.setImageResource(R.mipmap.radio_selected)
                        imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
                        heightType = "1"
                        textView_height_type.text = "cm"
                        if (textInputLayout != null) {
                            textInputLayout.visibility = View.VISIBLE
                        }
                        if (rLayout_feet_main != null) {
                            rLayout_feet_main.visibility = View.GONE
                        }
                    }
                    "2" -> {
                        imageView_radio_m.setImageResource(R.mipmap.radio_selected)
                        imageView_radio_cm.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
                        heightType = "2"
                        textView_height_type.text = "meters"
                        if (rLayout_meter_main != null) {
                            rLayout_meter_main.visibility = View.VISIBLE
                        }
                        if (rLayout_feet_main != null) {
                            rLayout_feet_main.visibility = View.GONE
                        }
                    }
                    "3" -> {
                        imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_cm.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_feet.setImageResource(R.mipmap.radio_selected)
                        imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
                        heightType = "3"
                        textView_height_type.text = "feet"
                        if (rLayout_meter_main != null) {
                            rLayout_meter_main.visibility = View.GONE
                        }
                        if (rLayout_feet_main != null) {
                            rLayout_feet_main.visibility = View.VISIBLE
                        }
                    }
                    "4" -> {
                        imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_cm.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_inches.setImageResource(R.mipmap.radio_selected)
                        heightType = "4"
                        textView_height_type.text = "inch"
                    }
                }
            } else {
                isHeight = true
                dialogHide()
            }
        }

        rLayout_gender.setOnClickListener {
            KeyboardUtils.hideKeyboard(this)
            genderDialog()
        }

        rLayout_kg.setOnClickListener {
            layout_weight_dialog.visibility = View.GONE
            imageView_radio_kg.setImageResource(R.mipmap.radio_selected)
            imageView_radio_lbs.setImageResource(R.mipmap.radio_unselected)
            PreferenceHandler.writeBoolean(this@SignUpSecondActivity, Constants.IS_WEIGHT_SELECTED, true)
            weightType = "1"
            textView_weight_type.text = "kg"
        }

        rLayout_lbs.setOnClickListener {
            layout_weight_dialog.visibility = View.GONE
            imageView_radio_lbs.setImageResource(R.mipmap.radio_selected)
            imageView_radio_kg.setImageResource(R.mipmap.radio_unselected)
            PreferenceHandler.writeBoolean(this@SignUpSecondActivity, Constants.IS_WEIGHT_SELECTED, false)
            weightType = "2"
            textView_weight_type.text = "lbs"
        }

        rLayout_cm.setOnClickListener {
            layout_height_dialog.visibility = View.GONE
            imageView_radio_cm.setImageResource(R.mipmap.radio_selected)
            imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
            PreferenceHandler.writeString(this@SignUpSecondActivity, Constants.HEIGHT_SELECTED, "1")
            heightType = "1"
            textView_height_type.text = "cm"
            if (textInputLayout != null) {
                textInputLayout.visibility = View.VISIBLE
            }
            if (rLayout_feet_main != null) {
                rLayout_feet_main.visibility = View.GONE
            }
        }

        rLayout_m.setOnClickListener {
            layout_height_dialog.visibility = View.GONE
            imageView_radio_m.setImageResource(R.mipmap.radio_selected)
            imageView_radio_cm.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
            PreferenceHandler.writeString(this@SignUpSecondActivity, Constants.HEIGHT_SELECTED, "2")
            heightType = "2"
            textView_height_type.text = "meters"
            if (rLayout_meter_main != null) {
                rLayout_meter_main.visibility = View.VISIBLE
            }
            if (rLayout_feet_main != null) {
                rLayout_feet_main.visibility = View.GONE
            }
        }

        rLayout_feet.setOnClickListener {
            layout_height_dialog.visibility = View.GONE
            imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_cm.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_feet.setImageResource(R.mipmap.radio_selected)
            imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
            PreferenceHandler.writeString(this@SignUpSecondActivity, Constants.HEIGHT_SELECTED, "3")
            heightType = "3"
            textView_height_type.text = "feet"
            if (rLayout_meter_main != null) {
                rLayout_meter_main.visibility = View.GONE
            }
            if (rLayout_feet_main != null) {
                rLayout_feet_main.visibility = View.VISIBLE
            }
        }

        rLayout_inches.setOnClickListener {
            layout_height_dialog.visibility = View.GONE
            imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_cm.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_inches.setImageResource(R.mipmap.radio_selected)
            PreferenceHandler.writeString(this@SignUpSecondActivity, Constants.HEIGHT_SELECTED, "4")
            heightType = "4"
            textView_height_type.text = "inch"
        }

        rLayout_male.setOnClickListener {
            layout_gender_dialog.visibility = View.GONE
            imageView_radio_male.setImageResource(R.mipmap.radio_selected)
            imageView_radio_female.setImageResource(R.mipmap.radio_unselected)
            editText_gender.setText("Male")
            gender = "m"
            PreferenceHandler.writeBoolean(this@SignUpSecondActivity, Constants.IS_MALE_SELECTED, true)
        }

        rLayout_female.setOnClickListener {
            layout_gender_dialog.visibility = View.GONE
            imageView_radio_female.setImageResource(R.mipmap.radio_selected)
            imageView_radio_male.setImageResource(R.mipmap.radio_unselected)
            editText_gender.setText("Female")
            gender = "f"
            PreferenceHandler.writeBoolean(this@SignUpSecondActivity, Constants.IS_MALE_SELECTED, false)
        }

        imageView_btn_sign_up.setOnClickListener {
            validations()
        }

        textView_sign_in.setOnClickListener {
            val intent = Intent(this@SignUpSecondActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun genderDialog() {
        editText_referral_code.requestFocus()
        if (isGender) {
            isGender = false
            layout_gender_dialog.bringToFront()
            layout_weight_dialog.visibility = View.GONE
            layout_height_dialog.visibility = View.GONE
            layout_gender_dialog.visibility = View.VISIBLE
            val isGenderSelected =
                PreferenceHandler.readBoolean(this@SignUpSecondActivity, Constants.IS_MALE_SELECTED, true)
            if (isGenderSelected) {
                imageView_radio_male.setImageResource(R.mipmap.radio_selected)
                imageView_radio_female.setImageResource(R.mipmap.radio_unselected)
            } else {
                imageView_radio_female.setImageResource(R.mipmap.radio_selected)
                imageView_radio_male.setImageResource(R.mipmap.radio_unselected)
            }
        } else {
            isGender = true
            dialogHide()
        }
    }

    private fun validations() {
        val dob = editText_date_of_birth.text.toString().trim()
        val weight = editText_weight.text.toString().trim()
        val height: String
        height = when (heightType) {
            "2" -> {
                val m = editText_m.text.toString().trim()
                val cm = editText_cm.text.toString().trim()
                "$m.$cm"
            }
            "3" -> {
                val feet = editText_feet.text.toString().trim()
                val inch = editText_inch.text.toString().trim()
                "$feet.$inch"
            }
            else -> editText_height.text.toString().trim()
        }
        val mGender = editText_gender.text.toString().trim()
        val invitationCode = editText_referral_code.text.toString().trim()
        if (dob.isEmpty()) {
            CommonMethods.alertTopError(this@SignUpSecondActivity, "Please enter date of birth")
        } else if (weight.isEmpty()) {
            CommonMethods.alertTopError(this@SignUpSecondActivity, "Please enter weight")
        } else if (height.isEmpty()) {
            CommonMethods.alertTopError(this@SignUpSecondActivity, "Please enter height")
        } else if (mGender.isEmpty()) {
            CommonMethods.alertTopError(this@SignUpSecondActivity, "Please select gender")
        } else if (!checkbox_terms.isChecked) run {
            CommonMethods.alertTopError(this@SignUpSecondActivity, "Please Tick On Terms and Conditions")
        } else {
            mUserModel.dob = dateOfBirth
            mUserModel.weight = weight
            mUserModel.height = height
            mUserModel.gender = gender
            mUserModel.invitation_code = invitationCode
            mUserModel.weight_type = weightType
            mUserModel.height_type = heightType
            mUserModel.device_type = "1"
            mUserModel.device_token = FirebaseInstanceId.getInstance().token
            mUserModel.security_key = Constants.SECURITY_KEY_VALUE
            createUser(mUserModel)
        }
    }

    private fun dialogHide() {
        layout_weight_dialog.visibility = View.GONE
        layout_height_dialog.visibility = View.GONE
        layout_gender_dialog.visibility = View.GONE
    }

    private fun createUser(userModel: UserModel) {
        startAnim()
        var filePath = ""
        if (userModel.image != null) {
            filePath = userModel.image
        }
        if (filePath == "") {
            iUserService.createUser(
                Constants.BASE_URL + Constants.REGISTER,
                null, userModel,
                object : AsyncResult<UserModel> {
                    override fun success(userModel: UserModel) {
                        runOnUiThread {
                            stopAnim()
                            val intent = Intent(this@SignUpSecondActivity, DashboardActivity::class.java)
                            PreferenceHandler.writeString(
                                this@SignUpSecondActivity,
                                Constants.TOKEN,
                                userModel.access_token
                            )
                            PreferenceHandler.writeString(
                                this@SignUpSecondActivity,
                                Constants.NOTIFICATION_STATUS,
                                userModel.notification_status
                            )
                            PreferenceHandler.writeString(
                                this@SignUpSecondActivity,
                                Constants.MOTIVATION_NOTIFICATION_STATUS,
                                userModel.motivation_notification_status
                            )
                            PreferenceHandler.writeString(
                                this@SignUpSecondActivity,
                                Constants.INFO_FILLED,
                                Constants.DASHBOARD
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    override fun error(error: String) {
                        runOnUiThread {
                            stopAnim()
                            CommonMethods.alertTopError(this@SignUpSecondActivity, error)
                            //Toast.makeText(this@SignUpSecondActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        } else {
            iUserService.createUser(
                Constants.BASE_URL + Constants.REGISTER,
                userModel,
                Constants.REGISTER, PageInput(), filePath,
                object : AsyncResult<UserModel> {
                    override fun success(userModel: UserModel) {
                        runOnUiThread {
                            stopAnim()
                            val intent = Intent(this@SignUpSecondActivity, DashboardActivity::class.java)
                            PreferenceHandler.writeString(
                                this@SignUpSecondActivity,
                                Constants.TOKEN,
                                userModel.access_token
                            )
                            PreferenceHandler.writeString(
                                this@SignUpSecondActivity,
                                Constants.NOTIFICATION_STATUS,
                                userModel.notification_status
                            )
                            PreferenceHandler.writeString(
                                this@SignUpSecondActivity,
                                Constants.MOTIVATION_NOTIFICATION_STATUS,
                                userModel.motivation_notification_status
                            )
                            PreferenceHandler.writeString(
                                this@SignUpSecondActivity,
                                Constants.INFO_FILLED,
                                Constants.DASHBOARD
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }

                    override fun error(error: String) {
                        runOnUiThread {
                            stopAnim()
                            Toast.makeText(this@SignUpSecondActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        avi_loader_signup.smoothToShow()
    }

    internal fun stopAnim() {
        avi_loader_signup.smoothToHide()
    }
}
