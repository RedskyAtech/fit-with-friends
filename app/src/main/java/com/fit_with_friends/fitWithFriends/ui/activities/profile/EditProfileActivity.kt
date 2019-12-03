package com.fit_with_friends.fitWithFriends.ui.activities.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.View
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fit_with_friends.App
import com.fit_with_friends.BuildConfig
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.helpers.ImagePickHelper
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import com.fit_with_friends.fitWithFriends.utils.image.ToastUtils
import com.fit_with_friends.fitWithFriends.listeners.OnImagePickedListener
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.CommonMethods.Companion.getAbsolutePath
import com.fit_with_friends.fitWithFriends.utils.InputFilterMinMax
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.editText_feet
import kotlinx.android.synthetic.main.activity_edit_profile.editText_inch
import kotlinx.android.synthetic.main.activity_edit_profile.imageView_radio_feet
import kotlinx.android.synthetic.main.activity_edit_profile.imageView_radio_inches
import kotlinx.android.synthetic.main.activity_edit_profile.rLayout_edit_profile
import kotlinx.android.synthetic.main.activity_edit_profile.rLayout_feet
import kotlinx.android.synthetic.main.activity_edit_profile.rLayout_feet_main
import kotlinx.android.synthetic.main.activity_edit_profile.rLayout_inches
import kotlinx.android.synthetic.main.activity_edit_profile.textView_weight_type
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_date_of_birth
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_gender
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_height
import kotlinx.android.synthetic.main.activity_sign_up_second.editText_weight
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_cm
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_female
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_kg
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_lbs
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_m
import kotlinx.android.synthetic.main.activity_sign_up_second.imageView_radio_male
import kotlinx.android.synthetic.main.activity_sign_up_second.layout_gender_dialog
import kotlinx.android.synthetic.main.activity_sign_up_second.layout_height_dialog
import kotlinx.android.synthetic.main.activity_sign_up_second.layout_weight_dialog
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_back_arrow
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_cm
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_female
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_gender
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_height
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_kg
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_lbs
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_m
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_male
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_weight
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class EditProfileActivity : BaseAppCompactActivity(), View.OnClickListener, OnImagePickedListener {

    @Inject
    internal lateinit var iUserService: IUserService

    private lateinit var imagePickHelper: ImagePickHelper
    private lateinit var mFile: File
    private val mRequestPermission = 20
    private val mRequestCamera = 201
    private val mSelectFile = 200
    private var fileExtensionString = ""
    private var mFilePath: String = ""

    private var isWeight: Boolean = true
    private var isHeight: Boolean = true
    private var isGender: Boolean = true
    private var dateOfBirth: String = ""
    private var gender: String = ""
    private var weightType: String = ""
    private var heightType: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        initView()
        listeners()
        setTypes()
        rLayout_feet.performClick()
        getProfileFromServer()
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
    private fun setTypes() {
        val isWeightSelected =
            PreferenceHandler.readBoolean(this@EditProfileActivity, Constants.IS_WEIGHT_SELECTED, true)
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

        when (PreferenceHandler.readString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "")) {
            "1" -> {
                imageView_radio_cm.setImageResource(R.mipmap.radio_selected)
                imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
                imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
                heightType = "1"
                textView_height_type_edit.text = "cm"
                if (editText_height != null) {
                    editText_height.visibility = View.GONE
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
                textView_height_type_edit.text = "meters"
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
                textView_height_type_edit.text = "feet"
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
                textView_height_type_edit.text = "inches"
            }
        }
    }

    private fun initView() {
        imagePickHelper = ImagePickHelper()
        gender = "m"
        weightType = "2"
        heightType = "3"
        addWatcher()
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
                        setData(userModel)
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setData(userModel: UserModel) {
        if (userModel.user_detail != null) {
            if (userModel.user_detail.image != null && userModel.user_detail.image != "") {
                if (userModel.user_detail.image.contains("public")) {
                    if (imageView_profile != null) {
                        Glide.with(this).load(Constants.BASE_URL_IMAGE + userModel.user_detail.image)
                            .apply(
                                RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                            ).into(imageView_profile)
                    }
                } else {
                    if (imageView_profile != null) {
                        Glide.with(this)
                            .load(Constants.BASE_URL_IMAGE + "public/" + userModel.user_detail.image)
                            .apply(
                                RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                            ).into(imageView_profile)
                    }
                }
            } else {
                imageView_profile.setImageResource(R.mipmap.harry)
            }

            if (userModel.user_detail.name != null) {
                editText_name.setText(capitalizeFirstWord.capitalizeWords(userModel.user_detail.name))
            }

            if (userModel.user_detail.email != null) {
                editText_email.setText(userModel.user_detail.email)
            }

            if (userModel.user_detail.dob != null) {
                dateOfBirth = userModel.user_detail.dob
                editText_date_of_birth.setText(userModel.user_detail.dob)
            }

            if (userModel.user_detail.weight != null) {
                editText_weight.setText(userModel.user_detail.weight)
            }

            if (userModel.user_detail.height != null) {
                if (userModel.user_detail.height_type != null && userModel.user_detail.height_type == "2") {
                    rLayout_meter_main.visibility = View.VISIBLE
                    rLayout_feet_main.visibility = View.GONE
                    val m: String
                    val cm: String
                    if (userModel.user_detail.height.contains(".")) {
                        m =
                            userModel.user_detail.height.substring(userModel.user_detail.height.lastIndexOf(".") + -1)
                        cm =
                            userModel.user_detail.height.substring(userModel.user_detail.height.lastIndexOf(".") + +1)
                    } else {
                        m = userModel.user_detail.height
                        cm = "0"
                    }
                    if (editText_m != null) {
                        editText_m.setText(m)
                    }
                    if (editText_cm != null) {
                        editText_cm.setText(cm)
                    }
                } else if (userModel.user_detail.height_type != null && userModel.user_detail.height_type == "3") {
                    rLayout_meter_main.visibility = View.GONE
                    rLayout_feet_main.visibility = View.VISIBLE
                    val feet: String
                    val inch: String
                    if (userModel.user_detail.height.contains(".")) {
                        feet =
                            userModel.user_detail.height.substring(userModel.user_detail.height.lastIndexOf(".") + -1)
                        inch =
                            userModel.user_detail.height.substring(userModel.user_detail.height.lastIndexOf(".") + +1)
                    } else {
                        feet = userModel.user_detail.height
                        inch = "0"
                    }
                    if (editText_feet != null) {
                        editText_feet.setText(feet)
                    }
                    if (editText_inch != null) {
                        editText_inch.setText(inch)
                    }
                } else {
                    rLayout_meter_main.visibility = View.VISIBLE
                    rLayout_feet_main.visibility = View.GONE
                    //editText_height.setText(userModel.user_detail.height)
                }
            } else {
                rLayout_meter_main.visibility = View.VISIBLE
                rLayout_feet_main.visibility = View.GONE
            }

            if (userModel.user_detail.gender != null) {
                if (userModel.user_detail.gender == "m") {
                    editText_gender.setText("Male")
                    gender = "m"
                    userModel.user_detail.gender
                    PreferenceHandler.writeBoolean(this@EditProfileActivity, Constants.IS_MALE_SELECTED, true)
                } else {
                    editText_gender.setText("Female")
                    gender = "f"
                    userModel.user_detail.gender
                    PreferenceHandler.writeBoolean(this@EditProfileActivity, Constants.IS_MALE_SELECTED, false)
                }
            }

            if (userModel.user_detail.weight_type != null) {
                if (userModel.user_detail.weight_type == "1") {
                    textView_weight_type.text = "kg"
                    weightType = "1"
                    PreferenceHandler.writeBoolean(this@EditProfileActivity, Constants.IS_WEIGHT_SELECTED, true)
                } else {
                    textView_weight_type.text = "lbs"
                    weightType = "2"
                    PreferenceHandler.writeBoolean(this@EditProfileActivity, Constants.IS_WEIGHT_SELECTED, false)
                }
            }

            if (userModel.user_detail.height_type != null) {
                when {
                    userModel.user_detail.height_type == "1" -> {
                        textView_height_type_edit.text = "cm"
                        heightType = "1"
                        PreferenceHandler.writeString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "1")
                    }
                    userModel.user_detail.height_type == "2" -> {
                        textView_height_type_edit.text = "meters"
                        heightType = "2"
                        PreferenceHandler.writeString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "2")
                    }
                    userModel.user_detail.height_type == "3" -> {
                        textView_height_type_edit.text = "feet"
                        heightType = "3"
                        PreferenceHandler.writeString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "3")
                    }
                    userModel.user_detail.height_type == "4" -> {
                        textView_height_type_edit.text = "inches"
                        heightType = "4"
                        PreferenceHandler.writeString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "4")
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }

        rLayout_edit_profile.setOnClickListener(this)

        rLayout_outside.setOnClickListener {
            dialogHide()
        }

        editText_weight.setOnClickListener {
            dialogHide()
        }

        editText_height.setOnClickListener {
            dialogHide()
        }

        editText_gender.setOnClickListener {
            dialogHide()
        }

        editText_date_of_birth.setOnClickListener {
            dialogHide()
            val c = Calendar.getInstance()
            var day = c.get(Calendar.DAY_OF_MONTH)
            var month = c.get(Calendar.MONTH)
            var year = c.get(Calendar.YEAR)

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

            dpd.show()
        }

        rLayout_weight.setOnClickListener {
            if (isWeight) {
                isWeight = false
                layout_weight_dialog.bringToFront()
                layout_weight_dialog.visibility = View.VISIBLE
                layout_height_dialog.visibility = View.GONE
                layout_gender_dialog.visibility = View.GONE
                val isWeightSelected =
                    PreferenceHandler.readBoolean(this@EditProfileActivity, Constants.IS_WEIGHT_SELECTED, true)
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
                when (PreferenceHandler.readString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "")) {
                    "1" -> {
                        imageView_radio_cm.setImageResource(R.mipmap.radio_selected)
                        imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
                        imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
                        heightType = "1"
                        textView_height_type_edit.text = "cm"
                        if (editText_height != null) {
                            editText_height.visibility = View.GONE
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
                        textView_height_type_edit.text = "meters"
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
                        textView_height_type_edit.text = "feet"
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
                        textView_height_type_edit.text = "inch"
                    }
                }
            } else {
                isHeight = true
                dialogHide()
            }
        }

        editText_gender.setOnClickListener {
            genderDialog()
        }

        rLayout_gender.setOnClickListener {
            genderDialog()
        }

        rLayout_kg.setOnClickListener {
            layout_weight_dialog.visibility = View.GONE
            imageView_radio_kg.setImageResource(R.mipmap.radio_selected)
            imageView_radio_lbs.setImageResource(R.mipmap.radio_unselected)
            PreferenceHandler.writeBoolean(this@EditProfileActivity, Constants.IS_WEIGHT_SELECTED, true)
            weightType = "1"
            textView_weight_type.text = "kg"
        }

        rLayout_lbs.setOnClickListener {
            layout_weight_dialog.visibility = View.GONE
            imageView_radio_lbs.setImageResource(R.mipmap.radio_selected)
            imageView_radio_kg.setImageResource(R.mipmap.radio_unselected)
            PreferenceHandler.writeBoolean(this@EditProfileActivity, Constants.IS_WEIGHT_SELECTED, false)
            weightType = "2"
            textView_weight_type.text = "lbs"
        }

        rLayout_cm.setOnClickListener {
            layout_height_dialog.visibility = View.GONE
            imageView_radio_cm.setImageResource(R.mipmap.radio_selected)
            imageView_radio_m.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_feet.setImageResource(R.mipmap.radio_unselected)
            imageView_radio_inches.setImageResource(R.mipmap.radio_unselected)
            //conversion(heightType, "1")
            PreferenceHandler.writeString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "1")
            heightType = "1"
            textView_height_type_edit.text = "cm"
            if (editText_height != null) {
                editText_height.visibility = View.GONE
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
            //conversion(heightType, "2")
            PreferenceHandler.writeString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "2")
            heightType = "2"
            textView_height_type_edit.text = "meters"
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
            //conversion(heightType, "3")
            PreferenceHandler.writeString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "3")
            heightType = "3"
            textView_height_type_edit.text = "feet"
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
            PreferenceHandler.writeString(this@EditProfileActivity, Constants.HEIGHT_SELECTED, "4")
            heightType = "4"
            textView_height_type_edit.text = "inch"
            if (editText_height != null) {
                editText_height.visibility = View.GONE
            }
            if (rLayout_feet_main != null) {
                rLayout_feet_main.visibility = View.GONE
            }
        }

        rLayout_male.setOnClickListener {
            layout_gender_dialog.visibility = View.GONE
            imageView_radio_male.setImageResource(R.mipmap.radio_selected)
            imageView_radio_female.setImageResource(R.mipmap.radio_unselected)
            editText_gender.setText("Male")
            gender = "m"
            PreferenceHandler.writeBoolean(this@EditProfileActivity, Constants.IS_MALE_SELECTED, true)
        }

        rLayout_female.setOnClickListener {
            layout_gender_dialog.visibility = View.GONE
            imageView_radio_female.setImageResource(R.mipmap.radio_selected)
            imageView_radio_male.setImageResource(R.mipmap.radio_unselected)
            editText_gender.setText("Female")
            gender = "f"
            PreferenceHandler.writeBoolean(this@EditProfileActivity, Constants.IS_MALE_SELECTED, false)
        }

        imageView_update.setOnClickListener {
            dialogHide()
            editProfile()
        }

    }

    private fun conversion(heightType: String, currentHeight: String) {
        if (currentHeight == "1") {
            if (heightType == "2") {

            } else if (heightType == "3") {
                val feet = editText_feet.text.toString().trim()
                val inch = editText_inch.text.toString().trim()
                editText_height.setText(CommonMethods.feetToCentimeter(feet, inch))
            }
        } else if (currentHeight == "2") {
            if (heightType == "1") {

            } else if (heightType == "3") {

            }
        } else if (currentHeight == "3") {
            if (heightType == "1") {
                val feet =
                    CommonMethods.convertToFeetInches(editText_height.text.toString().trim())
                        .substring(
                            CommonMethods.convertToFeetInches(editText_height.text.toString().trim()).lastIndexOf(
                                "."
                            ) + -1
                        )
                val inch =
                    CommonMethods.convertToFeetInches(editText_height.text.toString().trim())
                        .substring(
                            CommonMethods.convertToFeetInches(editText_height.text.toString().trim()).lastIndexOf(
                                "."
                            ) + +1
                        )
                if (editText_feet != null) {
                    editText_feet.setText(feet)
                }
                if (editText_inch != null) {
                    editText_inch.setText(inch)
                }
            } else if (heightType == "2") {
                CommonMethods.convertToFeetInches(editText_height.text.toString().trim())
            }
        }
    }

    private fun genderDialog() {
        if (isGender) {
            isGender = false
            layout_gender_dialog.bringToFront()
            layout_weight_dialog.visibility = View.GONE
            layout_height_dialog.visibility = View.GONE
            layout_gender_dialog.visibility = View.VISIBLE
            val isGenderSelected =
                PreferenceHandler.readBoolean(this@EditProfileActivity, Constants.IS_MALE_SELECTED, true)
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

    private fun dialogHide() {
        layout_weight_dialog.visibility = View.GONE
        layout_height_dialog.visibility = View.GONE
        layout_gender_dialog.visibility = View.GONE
    }

    private fun editProfile() {
        imageView_update.isEnabled = false
        imageView_update.isClickable = false
        startAnim()
        val userModel = UserModel()
        userModel.dob = dateOfBirth
        userModel.name = editText_name.text.toString().trim()
        userModel.email = editText_email.text.toString().trim()
        userModel.weight = editText_weight.text.toString().trim()
        when (heightType) {
            "2" -> {
                val m = editText_m.text.toString().trim()
                val cm = editText_cm.text.toString().trim()
                val height = "$m.$cm"
                userModel.height = height
            }
            "3" -> {
                val feet = editText_feet.text.toString().trim()
                val inch = editText_inch.text.toString().trim()
                val height = "$feet.$inch"
                userModel.height = height
            }
            else -> userModel.height = editText_height.text.toString().trim()
        }
        userModel.gender = gender
        userModel.weight_type = weightType
        userModel.height_type = heightType
        userModel.security_key = Constants.SECURITY_KEY_VALUE
        if (mFilePath == "") {
            iUserService.editProfile(
                Constants.BASE_URL + Constants.EDIT_PROFILE,
                null, userModel,
                object : AsyncResult<UserModel> {
                    override fun success(userModel: UserModel) {
                        runOnUiThread {
                            stopAnim()
                            CommonMethods.alertTopSuccess(this@EditProfileActivity, "Successfully updated")
                            //Toast.makeText(this@EditProfileActivity, "Successfully updated", Toast.LENGTH_LONG).show()
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
                            CommonMethods.alertTopError(this@EditProfileActivity, error)
                            //Toast.makeText(this@EditProfileActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        } else {
            iUserService.editProfile(
                Constants.BASE_URL + Constants.EDIT_PROFILE,
                userModel,
                Constants.EDIT_PROFILE, PageInput(), mFilePath,
                object : AsyncResult<UserModel> {
                    override fun success(userModel: UserModel) {
                        runOnUiThread {
                            stopAnim()
                            CommonMethods.alertTopSuccess(this@EditProfileActivity, "Successfully updated")
                            //Toast.makeText(this@EditProfileActivity, "Successfully updated", Toast.LENGTH_LONG).show()
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
                            CommonMethods.alertTopError(this@EditProfileActivity, error)
                            //Toast.makeText(this@EditProfileActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.rLayout_edit_profile -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                        return
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
                        return
                    }
                    //imagePickHelper.pickAnImage(this, ImageUtils.IMAGE_REQUEST_CODE)
                    selectImageAlert()
                    dialogHide()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //imagePickHelper.pickAnImage(this, ImageUtils.IMAGE_REQUEST_CODE)
        } else {
            ToastUtils.longToast("Required permissions are not granted")
        }
    }

    override fun onImagePicked(requestCode: Int, file: File) {
        mFilePath = file.absolutePath
        loadUrl("file://" + file.absolutePath)
    }

    private fun loadUrl(imageUri: String) {
        Glide.with(this@EditProfileActivity).load(imageUri)
            .into(imageView_profile)
    }

    override fun onImagePickError(requestCode: Int, e: Exception) {

    }

    override fun onImagePickClosed(requestCode: Int) {

    }

    private fun selectImageAlert() {
        val items = arrayOf<CharSequence>("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select")
        builder.setItems(items) { dialog, item ->
            if (item == 0) {
                cameraPickImage()
            } else if (item == 1) {
                galleryIntent()
            }
            dialog.dismiss()
        }
        builder.show()
    }

    private fun galleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, mSelectFile)
    }

    private fun cameraPickImage() {
        val currentApiVersion = Build.VERSION.SDK_INT
        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.CAMERA),
                    mRequestPermission
                )
            } else {
                cameraIntent()
            }
        } else {
            cameraIntent()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun cameraIntent() {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        try {
            createImageFile(this, imageFileName, ".jpg")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", mFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(intent, mRequestCamera)
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context, name: String, extension: String) {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        mFile = File.createTempFile(
            name,
            extension,
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == mRequestCamera) run {
                val uri = Uri.fromFile(mFile)
                val imagePath = getAbsolutePath(this, uri)
                mFile = File(imagePath)
                mFilePath = mFile.absolutePath
                Glide.with(this).load(imagePath).into(imageView_profile)
            } else if (requestCode == mSelectFile) run {
                val uri = data?.data
                if (uri != null) {
                    fileExtensionString = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                    val imagePath = getAbsolutePath(this, uri)
                    mFile = File(imagePath)
                    mFilePath = mFile.absolutePath
                    Glide.with(this).load(imagePath).into(imageView_profile)
                }
            }
        }
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        if (loader_edit_profile != null) {
            loader_edit_profile.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (loader_edit_profile != null) {
            loader_edit_profile.smoothToHide()
        }
    }
}