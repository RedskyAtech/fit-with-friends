package com.fit_with_friends.fitWithFriends.ui.activities.createChallenge

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
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.MimeTypeMap
import com.fit_with_friends.App
import com.fit_with_friends.BuildConfig
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IChallengeService
import com.fit_with_friends.common.contracts.services.IConnectionService
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.Page
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.helpers.DateHelper
import com.fit_with_friends.common.helpers.ImagePickHelper
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.impl.models.ConnectionModel
import com.fit_with_friends.fitWithFriends.impl.models.FriendDetailModel
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.ui.adapters.participants.ParticipantsListAdapter
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import com.fit_with_friends.fitWithFriends.utils.image.ToastUtils
import com.fit_with_friends.fitWithFriends.listeners.OnImagePickedListener
import com.fit_with_friends.fitWithFriends.ui.adapters.participants.AddParticipantsListAdapter
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.image.KeyboardUtils
import kotlinx.android.synthetic.main.activity_create_challenge.*
import kotlinx.android.synthetic.main.activity_create_challenge.textView_weight_type
import kotlinx.android.synthetic.main.activity_sign_up_second.rLayout_back_arrow
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class CreateChallengeActivity : BaseAppCompactActivity(), View.OnClickListener, OnImagePickedListener {

    @Inject
    internal lateinit var iUserService: IUserService

    @Inject
    internal lateinit var iChallengeService: IChallengeService

    @Inject
    internal lateinit var iConnectionService: IConnectionService

    private lateinit var imagePickHelper: ImagePickHelper
    private lateinit var mFile: File
    private val mRequestPermission = 20
    private val mRequestCamera = 201
    private val mSelectFile = 200
    private var fileExtensionString = ""
    private var mFilePath: String = ""

    private lateinit var addParticipantsListAdapter: AddParticipantsListAdapter
    private lateinit var participantsListAdapter: ParticipantsListAdapter
    private var participantsModelList: MutableList<ConnectionModel> = ArrayList()
    private var participantsList: MutableList<ConnectionModel> = ArrayList()
    private var connectionModelList: MutableList<ConnectionModel> = ArrayList()

    private lateinit var startDate: String
    private lateinit var endDate: String
    private var isParticipantsShow: Boolean = true
    private var isBaseWeightShow: Boolean = true
    private var isUpdate: Boolean = false
    private var weightType: String = ""
    private lateinit var mChallengeModel: ChallengeModel
    private var fromActivity: String = ""

    private val calendar: Calendar = Calendar.getInstance()
    private var startDay = calendar.get(Calendar.DAY_OF_MONTH)
    private var startMonth = calendar.get(Calendar.MONTH)
    private var startYear = calendar.get(Calendar.YEAR)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_challenge)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        fromActivity = intent.getStringExtra(Constants.FROM_ACTIVITY)
        mChallengeModel = intent.getSerializableExtra(Constants.CHALLENGE_MODEL) as ChallengeModel
        initView()
        listeners()
        setAdapters()
        addWatcher()
        if (fromActivity == "ChallengeDetailsActivity") {
            textView_create.text = "Update"
            setChallengeData(mChallengeModel)
            isUpdate = true
            setAdapters()
            getProfileFromServer()
        } else {
            textView_create.text = "Create"
            isUpdate = false
            setAdapters()
            getProfileFromServer()
            rLayout_lbs_base.performClick()
        }
    }

    private fun addWatcher() {
        editText_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                if (editable.isEmpty() && editable.toString() == "") {
                    participantsModelList.clear()
                    participantsModelList.addAll(connectionModelList)
                    addParticipantsListAdapter.notifyDataSetChanged()
                } else {
                    if (connectionModelList.size != 0) {
                        participantsModelList.clear()
                        for (i in connectionModelList.indices) {
                            if (connectionModelList[i].friend_detail.name.contains(editable.toString())) {
                                participantsModelList.add(connectionModelList[i])
                                addParticipantsListAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setChallengeData(challengeModel: ChallengeModel) {
        if (challengeModel.challange_name != null) {
            editText_challenge_name.setText(challengeModel.challange_name)
        }

        if (challengeModel.base_weight != 0) {
            if (editText_base_weight != null) {
                editText_base_weight.isEnabled = false
                editText_base_weight.setText(challengeModel.base_weight.toString())
            }
            if (rLayout_base_weight != null) {
                rLayout_base_weight.isEnabled = false
            }
            if (imageView_base_weight != null) {
                imageView_base_weight.visibility = View.GONE
            }
        }

        if (challengeModel.weight_type != null) {
            if (challengeModel.weight_type == "1") {
                textView_weight_type.text = "kg"
                weightType = "1"
            } else {
                textView_weight_type.text = "lbs"
                weightType = "2"
            }
        }

        if (challengeModel.start_date != null) {
            startDate = convertStringToData(challengeModel.start_date)
            textView_start_date_value.text = CommonMethods.convertStringToData(challengeModel.start_date)
        }

        if (challengeModel.end_date != null) {
            endDate = convertStringToData(challengeModel.end_date)
            textView_end_date_value.text = CommonMethods.convertStringToData(challengeModel.end_date)
        }

        if (mChallengeModel.challange_image != null) {
            editText_add_photo.setText("Image Uploaded")
            editText_add_photo.setTextColor(ContextCompat.getColor(this, R.color.color_purple))
        }

        if (challengeModel.participant != null && challengeModel.participant.size != 0) {
            participantsList.clear()
            for (i in 0 until challengeModel.participant.size) {
                if (i > 0) {
                    val connectionModel = ConnectionModel()
                    val friendModel = FriendDetailModel()
                    friendModel.name = challengeModel.participant[i].name
                    friendModel.id = challengeModel.participant[i].id
                    connectionModel.friend_detail = friendModel
                    participantsList.add(connectionModel)
                }
            }
            participantsListAdapter.notifyDataSetChanged()
        }

        if (challengeModel.description != null) {
            editText_description.setText(challengeModel.description)
        }
    }

    @Throws(ParseException::class)
    fun convertStringToData(stringData: String): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val data = sdf.parse(stringData)
        return DateHelper.stringify(data, "yyyy-MM-dd")
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
                        getAllConnectionsFromServer()
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
        if (userModel.user_detail.today_weight != null) {
            if (userModel.user_detail.today_weight.weight != null && userModel.user_detail.today_weight.weight != "") {
                if (editText_base_weight != null) {
                    editText_base_weight.isEnabled = false
                    editText_base_weight.setText(userModel.user_detail.today_weight.weight.toString())
                }
                if (rLayout_base_weight != null) {
                    rLayout_base_weight.isEnabled = false
                }
                if (imageView_base_weight != null) {
                    imageView_base_weight.visibility = View.GONE
                }

                if (userModel.user_detail.today_weight.weight_type == 1) {
                    if (textView_weight_type != null) {
                        textView_weight_type.text = "kg"
                        weightType = "1"
                    }
                } else {
                    if (textView_weight_type != null) {
                        textView_weight_type.text = "lbs"
                        weightType = "2"
                    }
                }
            } else {
                if (editText_base_weight != null) {
                    editText_base_weight.isEnabled = true
                }
                if (rLayout_base_weight != null) {
                    rLayout_base_weight.isEnabled = true
                }
                if (imageView_base_weight != null) {
                    imageView_base_weight.visibility = View.VISIBLE
                }
            }
        } else {
            if (rLayout_add_weight != null) {
                editText_base_weight.isEnabled = true
            }
            if (rLayout_base_weight != null) {
                rLayout_base_weight.isEnabled = true
            }
        }
    }


    private fun getAllConnectionsFromServer() {
        startAnim()
        iConnectionService.getAllConnectionsFromServer(
            Constants.BASE_URL + Constants.GET_ALL_CONNECTION,
            null,
            PageInput(),
            object : AsyncResult<Page<ConnectionModel>> {
                override fun success(connectionModelPage: Page<ConnectionModel>) {
                    runOnUiThread {
                        stopAnim()
                        if (connectionModelPage.body.size != 0) {
                            connectionModelList.clear()
                            connectionModelList.addAll(connectionModelPage.body)
                            recyclerView_participants_list.visibility = View.VISIBLE
                            participantsModelList.clear()
                            participantsModelList.addAll(connectionModelPage.body)
                            addParticipantsListAdapter.notifyDataSetChanged()
                        } else {
                            recyclerView_participants_list.visibility = View.GONE
                        }
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
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

    private fun initView() {
        imagePickHelper = ImagePickHelper()
        weightType = "2"
    }

    @SuppressLint("SetTextI18n")
    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            if (fromActivity == Constants.DASHBOARD) {
                val intent = Intent()
                intent.putExtra(Constants.FROM_ACTIVITY, Constants.CREATE_CHALLENGE)
                setResult(115, intent)
                finish()
            } else {
                finish()
            }
        }

        rLayout_scroll_view.setOnClickListener {
            hideDialog()
        }

        rLayout_start_date.setOnClickListener {
            hideDialog()
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
                    startDay = dayOfMonth
                    startMonth = monthOfYear
                    startYear = currentYear
                    startDate = if (day < 10) {
                        "$year-0$month-0$day"
                    } else {
                        "$year-0$month-$day"
                    }
                    textView_start_date_value.text = "$day - $month - $year"

                }, year, month, day
            )

            //dpd.datePicker.maxDate = System.currentTimeMillis()
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000

            dpd.show()
        }

        rLayout_end_date.setOnClickListener {
            hideDialog()
            if (textView_start_date_value.text != "") {
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
                        endDate = if (day < 10) {
                            "$year-0$month-0$day"
                        } else {
                            "$year-0$month-$day"
                        }
                        textView_end_date_value.text = "$day - $month - $year"

                    }, year, month, day
                )

                calendar.set(Calendar.DAY_OF_MONTH, startDay)
                calendar.set(Calendar.MONTH, startMonth)
                calendar.set(Calendar.YEAR, startYear)

                dpd.datePicker.minDate = calendar.timeInMillis - 1000

                dpd.show()
            } else {
                CommonMethods.alertTopError(this, "Please select start date first")
            }
        }

        editText_base_weight.setOnClickListener {
            hideDialog()
        }

        editText_challenge_name.setOnClickListener {
            hideDialog()
        }

        editText_add_photo.setOnClickListener(this)
        rLayout_add_photo.setOnClickListener(this)

        editText_add_participants.setOnClickListener {
            hideDialog()
        }

        editText_description.setOnClickListener {
            hideDialog()
        }

        layout_description.setOnClickListener {
            hideDialog()
        }

        rLayout_base_weight.setOnClickListener {
            if (isBaseWeightShow) {
                isBaseWeightShow = false
                layout_base_weight_dialog.bringToFront()
                layout_base_weight_dialog.visibility = View.VISIBLE
                layout_add_participants_dialog.visibility = View.GONE
                setBaseWeightValue()
            } else {
                isBaseWeightShow = true
                hideDialog()
                setBaseWeightValue()
            }

        }

        rLayout_kg_base.setOnClickListener {
            layout_base_weight_dialog.visibility = View.GONE
            imageView_radio_kg_base.setImageResource(R.mipmap.radio_selected)
            imageView_radio_lbs_base.setImageResource(R.mipmap.radio_unselected)
            PreferenceHandler.writeBoolean(this@CreateChallengeActivity, Constants.IS_WEIGHT_SELECTED, true)
            weightType = "1"
            textView_weight_type.text = "kg"
        }

        rLayout_lbs_base.setOnClickListener {
            layout_base_weight_dialog.visibility = View.GONE
            imageView_radio_lbs_base.setImageResource(R.mipmap.radio_selected)
            imageView_radio_kg_base.setImageResource(R.mipmap.radio_unselected)
            PreferenceHandler.writeBoolean(this@CreateChallengeActivity, Constants.IS_WEIGHT_SELECTED, false)
            weightType = "2"
            textView_weight_type.text = "lbs"
        }

        recyclerView_add_participants.setOnClickListener {
            openParticipantsDialog()
        }

        rLayout_add_participants.setOnClickListener {
            openParticipantsDialog()
        }

        editText_add_participants.setOnClickListener {
            openParticipantsDialog()
        }

        layout_create.setOnClickListener { view ->
            when {
                editText_challenge_name.text.toString().isEmpty() ->
                    CommonMethods.alertTopError(this@CreateChallengeActivity, "Please enter challenge name")
                textView_start_date_value.text.toString().isEmpty() ->
                    CommonMethods.alertTopError(this@CreateChallengeActivity, "Please enter start date")
                textView_end_date_value.text.toString().isEmpty() ->
                    CommonMethods.alertTopError(this@CreateChallengeActivity, "Please enter end date")
                /*editText_description.text.toString().isEmpty() ->
                    CommonMethods.alertTopError(this@CreateChallengeActivity, "Please add description")*/
                else -> {
                    createChallengeApi()
                }
            }
        }
    }

    private fun openParticipantsDialog() {
        KeyboardUtils.hideKeyboard(this)
        if (participantsModelList.size == 0) {
            CommonMethods.alertTopError(this, "No participant in your list")
        } else {
            editText_description.requestFocus()
            if (isParticipantsShow) {
                isParticipantsShow = false
                layout_add_participants_dialog.bringToFront()
                layout_base_weight_dialog.visibility = View.GONE
                layout_add_participants_dialog.visibility = View.VISIBLE
            } else {
                isParticipantsShow = true
                hideDialog()
            }
        }
    }

    private fun hideDialog() {
        layout_base_weight_dialog.visibility = View.GONE
        layout_add_participants_dialog.visibility = View.GONE
    }

    private fun createChallengeApi() {
        layout_create.isClickable = false
        layout_create.isEnabled = false
        val selectedParticipantsList: MutableList<String> = ArrayList()
        selectedParticipantsList.clear()
        if (participantsList.size != 0) {
            for (i in 0 until participantsList.size) {
                if (participantsList[i].isChecked) {
                    selectedParticipantsList.add(participantsList[i].friend_detail.id)
                }
            }
        }
        startAnim()
        val challengeModel = ChallengeModel()
        challengeModel.name = editText_challenge_name.text.toString().trim()
        challengeModel.weight = editText_base_weight.text.toString().trim()
        challengeModel.start_date = startDate
        challengeModel.end_date = endDate
        challengeModel.add_friend = participants(selectedParticipantsList.toString())
        challengeModel.description = editText_description.text.toString().trim()
        challengeModel.security_key = Constants.SECURITY_KEY_VALUE
        challengeModel.weight_type = weightType
        val action: String
        if (fromActivity == "ChallengeDetailsActivity") {
            action = Constants.EDIT_CHALLENGE
            challengeModel.challenge_id = mChallengeModel.id
        } else {
            action = Constants.CREATE_CHALLENGE
        }
        if (mFilePath == "") {
            iChallengeService.createChallenge(Constants.BASE_URL + action,
                null, challengeModel,
                object : AsyncResult<ChallengeModel> {
                    override fun success(challengeModel: ChallengeModel) {
                        runOnUiThread {
                            stopAnim()
                            if (fromActivity == "ChallengeDetailsActivity") {
                                CommonMethods.alertTopSuccess(this@CreateChallengeActivity, "Successfully updated")
                            } else {
                                CommonMethods.alertTopSuccess(this@CreateChallengeActivity, "Successfully created")
                            }
                            //Toast.makeText(this@CreateChallengeActivity, "Successfully created", Toast.LENGTH_LONG).show()
                            val handler = Handler()
                            handler.postDelayed({
                                if (fromActivity == Constants.DASHBOARD) {
                                    val intent = Intent()
                                    intent.putExtra(Constants.FROM_ACTIVITY, "")
                                    setResult(115, intent)
                                    finish()
                                } else {
                                    val intent = Intent()
                                    setResult(115, intent)
                                    finish()
                                }
                            }, Constants.ALERT_DURATION)
                        }
                    }

                    override fun error(error: String) {
                        runOnUiThread {
                            stopAnim()
                            layout_create.isClickable = true
                            layout_create.isEnabled = true
                            CommonMethods.alertTopError(this@CreateChallengeActivity, error)
                            //Toast.makeText(this@CreateChallengeActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        } else {
            iChallengeService.createChallenge(Constants.BASE_URL + action,
                challengeModel,
                action, PageInput(), mFilePath,
                object : AsyncResult<ChallengeModel> {
                    override fun success(challengeModel: ChallengeModel) {
                        runOnUiThread {
                            stopAnim()
                            if (fromActivity == "ChallengeDetailsActivity") {
                                CommonMethods.alertTopSuccess(this@CreateChallengeActivity, "Successfully updated")
                            } else {
                                CommonMethods.alertTopSuccess(this@CreateChallengeActivity, "Successfully created")
                            }
                            //Toast.makeText(this@CreateChallengeActivity, "Successfully created", Toast.LENGTH_LONG).show()
                            val handler = Handler()
                            handler.postDelayed({
                                if (fromActivity == Constants.DASHBOARD) {
                                    val intent = Intent()
                                    intent.putExtra(Constants.FROM_ACTIVITY, "")
                                    setResult(115, intent)
                                    finish()
                                } else {
                                    val intent = Intent()
                                    setResult(115, intent)
                                    finish()
                                }
                            }, Constants.ALERT_DURATION)
                        }
                    }

                    override fun error(error: String) {
                        runOnUiThread {
                            stopAnim()
                            layout_create.isClickable = true
                            layout_create.isEnabled = true
                            CommonMethods.alertTopError(this@CreateChallengeActivity, error)
                            //Toast.makeText(this@CreateChallengeActivity, error, Toast.LENGTH_LONG).show()
                        }
                    }
                })
        }
    }

    private fun participants(toString: String): String {
        return toString
            .replace("[", "")  //remove the right bracket
            .replace("]", "")  //remove the left bracket
            .trim()
    }

    private fun setBaseWeightValue() {
        val isWeightSelected =
            PreferenceHandler.readBoolean(this@CreateChallengeActivity, Constants.IS_WEIGHT_SELECTED, true)
        if (isWeightSelected) {
            imageView_radio_kg_base.setImageResource(R.mipmap.radio_selected)
            imageView_radio_lbs_base.setImageResource(R.mipmap.radio_unselected)
        } else {
            imageView_radio_lbs_base.setImageResource(R.mipmap.radio_selected)
            imageView_radio_kg_base.setImageResource(R.mipmap.radio_unselected)
        }
    }

    private fun setAdapters() {
        addParticipantsListAdapter =
            AddParticipantsListAdapter(
                this,
                participantsModelList,
                participantsList,
                asyncResult,
                isUpdate
            )
        recyclerView_participants_list.layoutManager = LinearLayoutManager(this)
        recyclerView_participants_list.adapter = addParticipantsListAdapter

        participantsListAdapter = ParticipantsListAdapter(this, participantsList, asyncResult)
        recyclerView_add_participants.layoutManager =
            GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView_add_participants.adapter = participantsListAdapter
    }

    private var asyncResult: AsyncResult<ConnectionModel> = object : AsyncResult<ConnectionModel> {
        override fun success(connectionModel: ConnectionModel) {
            if (participantsList.size != 0) {
                for (i in 0 until participantsList.size) {
                    if (participantsList[i].friend_detail.name == connectionModel.friend_detail.name) {
                        participantsList.removeAt(i)
                        participantsListAdapter.notifyDataSetChanged()
                        if (participantsList.size == 0) {
                            editText_add_participants.hint = "Select"
                        }
                        return
                    }
                }
                participantsList.add(connectionModel)
                participantsListAdapter.notifyDataSetChanged()
                editText_add_participants.hint = ""
            } else {
                participantsList.add(connectionModel)
                participantsListAdapter.notifyDataSetChanged()
                editText_add_participants.hint = ""
            }
        }

        override fun error(error: String) {

        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.editText_add_photo -> {
                    imagePicker()
                }
                R.id.rLayout_add_photo -> {
                    imagePicker()
                }
            }
        }
    }

    private fun imagePicker() {
        layout_base_weight_dialog.visibility = View.GONE
        layout_add_participants_dialog.visibility = View.GONE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            return
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
            return
        }
        //imagePickHelper.pickAnImage(this, ImageUtils.IMAGE_REQUEST_CODE)
        selectImageAlert()
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

    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == mRequestCamera) run {
                val uri = Uri.fromFile(mFile)
                val imagePath = CommonMethods.getAbsolutePath(this, uri)
                mFile = File(imagePath)
                mFilePath = mFile.absolutePath
                editText_add_photo.setText("Image Uploaded")
                editText_add_photo.setTextColor(ContextCompat.getColor(this, R.color.color_purple))
                //Glide.with(this).load(imagePath).into(imageView_profile)
            } else if (requestCode == mSelectFile) run {
                val uri = data?.data
                if (uri != null) {
                    fileExtensionString = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                    val imagePath = CommonMethods.getAbsolutePath(this, uri)
                    mFile = File(imagePath)
                    mFilePath = mFile.absolutePath
                    editText_add_photo.setText("Image Uploaded")
                    editText_add_photo.setTextColor(ContextCompat.getColor(this, R.color.color_purple))
                    //Glide.with(this).load(imagePath).into(imageView_profile)
                }
            }
        }
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        loader.smoothToShow()
    }

    internal fun stopAnim() {
        loader.smoothToHide()
    }
}
