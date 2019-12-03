package com.fit_with_friends.fitWithFriends.ui.fragments.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.webkit.MimeTypeMap
import android.widget.*
import com.bumptech.glide.Glide
import com.fit_with_friends.App
import com.fit_with_friends.BuildConfig
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IChallengeService
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.contracts.tool.PagePagination
import com.fit_with_friends.common.helpers.DateHelper
import com.fit_with_friends.common.ui.BaseV4Fragment
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.ui.activities.challenge.ChallengeDetailsActivity
import com.fit_with_friends.fitWithFriends.ui.activities.notification.NotificationActivity
import com.fit_with_friends.fitWithFriends.ui.adapters.challenge.ChallengeAdapter
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.listeners.OnImagePickedListener
import com.fit_with_friends.fitWithFriends.ui.activities.home.DashboardActivity
import com.fit_with_friends.fitWithFriends.utils.CapitalizeFirstWord
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import com.fit_with_friends.fitWithFriends.utils.image.ToastUtils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.layout_swipeRefresh
import kotlinx.android.synthetic.main.fragment_home.rLayout_weight
import kotlinx.android.synthetic.main.fragment_home.textView_name
import kotlinx.android.synthetic.main.fragment_home.textView_weight

import java.io.File
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class HomeFragment : BaseV4Fragment(), View.OnClickListener, OnImagePickedListener {

    @Inject
    internal lateinit var iUserService: IUserService

    @Inject
    internal lateinit var iChallengeService: IChallengeService

    private lateinit var mFile: File
    private val mRequestPermission = 20
    private val mRequestCamera = 201
    private val mSelectFile = 200
    private var fileExtensionString = ""
    private var mFilePath: String = ""

    private lateinit var imageViewWeight: ImageView

    private lateinit var challengeAdapter: ChallengeAdapter
    private var challengesModelList: MutableList<ChallengeModel> = ArrayList()

    private val capitalizeFirstWord: CapitalizeFirstWord = CapitalizeFirstWord()

    private lateinit var mUserModel: UserModel

    private var weightType: String = ""
    private var mYear: String = ""
    private var mMonth: String = ""
    private var mDay: String = ""
    private var date: String = ""

    private lateinit var mContext: DashboardActivity

    private var mPermissionAll = 1
    private var mPermissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun setupActivityComponent() {
        App.get(activity).component().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
        setFonts()
        setAdapters()
        listeners()
        getProfileFromServer()
        getAllChallengesFromServer()
    }

    private fun initView() {
        mContext = activity as DashboardActivity
        weightType = "2"
    }

    private fun getPermissions() {
        if (!hasPermissions(activity, *mPermissions)) {
            ActivityCompat.requestPermissions(mContext, mPermissions, mPermissionAll)
        } else {
            selectImageAlert()
        }
    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
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
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        if (layout_swipeRefresh != null) {
                            layout_swipeRefresh.isRefreshing = false
                        }
                        mUserModel = userModel
                        PreferenceHandler.writeString(activity, Constants.USER_ID, userModel.user_detail.id)
                        setData(userModel)
                    }
                }

                override fun error(error: String) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        if (layout_swipeRefresh != null) {
                            layout_swipeRefresh.isRefreshing = false
                        }
                    }
                }
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setData(userModel: UserModel) {
        if (userModel.user_detail != null) {
            if (userModel.user_detail.name != null) {
                if (textView_name != null) {
                    textView_name.text = capitalizeFirstWord.capitalizeWords("Hey " + userModel.user_detail.name + "!")
                    textView_user_name.text =
                        capitalizeFirstWord.capitalizeWords("Hey " + userModel.user_detail.name + "!")
                }
            }

            if (userModel.user_detail.today_weight != null) {
                if (userModel.user_detail.today_weight.weight != null && userModel.user_detail.today_weight.weight != "") {
                    if (rLayout_add_weight != null) {
                        rLayout_add_weight.visibility = View.GONE
                    }
                    if (rLayout_weight != null) {
                        rLayout_weight.visibility = View.VISIBLE
                    }
                    if (textView_date != null) {
                        textView_date.text =
                            "Date: " + convertStringToData(userModel.user_detail.today_weight.created_at)
                    }

                    if (userModel.user_detail.today_weight.weight_type == 1) {
                        if (textView_weight != null) {
                            textView_weight.text = userModel.user_detail.today_weight.weight.toString() + " kg"
                        }
                    } else {
                        if (textView_weight != null) {
                            textView_weight.text = userModel.user_detail.today_weight.weight.toString() + " lbs"
                        }
                    }
                } else {
                    if (rLayout_add_weight != null) {
                        rLayout_add_weight.visibility = View.VISIBLE
                    }
                    if (rLayout_weight != null) {
                        rLayout_weight.visibility = View.GONE
                    }
                }
            } else {
                if (rLayout_add_weight != null) {
                    rLayout_add_weight.visibility = View.VISIBLE
                }
                if (rLayout_weight != null) {
                    rLayout_weight.visibility = View.GONE
                }
            }

            if (userModel.user_detail.notification_count != 0) {
                if (rLayout_notification_badge != null) {
                    rLayout_notification_badge.visibility = View.VISIBLE
                }
                if (textView_count_notification != null) {
                    textView_count_notification.text = userModel.user_detail.notification_count.toString()
                }
            } else {
                if (rLayout_notification_badge != null) {
                    rLayout_notification_badge.visibility = View.GONE
                }
            }
        }
    }

    @Throws(ParseException::class)
    fun convertStringToData(stringData: String): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val data = sdf.parse(stringData)
        return DateHelper.stringify(data, "dd-MM-yyyy")
    }

    private fun getAllChallengesFromServer() {
        startAnim()
        iChallengeService.getAllChallengesFromServer(
            Constants.BASE_URL + Constants.GET_CHALLENGES,
            null,
            PageInput(),
            object : AsyncResult<PagePagination<ChallengeModel>> {
                override fun success(challengeModelPage: PagePagination<ChallengeModel>) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        if (layout_swipeRefresh != null) {
                            layout_swipeRefresh.isRefreshing = false
                        }
                        if (challengeModelPage.body.data.size != 0) {
                            if (recyclerView_challenges != null) {
                                recyclerView_challenges.visibility = View.VISIBLE
                            }
                            if (layout_no_challenges != null) {
                                layout_no_challenges.visibility = View.GONE
                            }
                            challengesModelList.clear()
                            challengesModelList.addAll(challengeModelPage.body.data)
                            challengeAdapter.notifyDataSetChanged()
                        } else {
                            if (recyclerView_challenges != null) {
                                recyclerView_challenges.visibility = View.GONE
                            }
                            if (layout_no_challenges != null) {
                                layout_no_challenges.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                override fun error(error: String) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        if (layout_swipeRefresh != null) {
                            layout_swipeRefresh.isRefreshing = false
                        }
                    }
                }
            })
    }

    private fun setFonts() {

    }

    private fun setAdapters() {
        challengeAdapter = ChallengeAdapter(activity, challengesModelList, async, asyncResult)
        recyclerView_challenges.layoutManager = LinearLayoutManager(activity)
        recyclerView_challenges.adapter = challengeAdapter
    }

    private var async: AsyncResult<ChallengeModel> = object : AsyncResult<ChallengeModel> {
        override fun success(challengeModel: ChallengeModel) {
            share(challengeModel)
        }

        override fun error(error: String) {

        }
    }

    private var asyncResult: AsyncResult<ChallengeModel> = object : AsyncResult<ChallengeModel> {
        override fun success(challengeModel: ChallengeModel) {
            val intent = Intent(activity, ChallengeDetailsActivity::class.java)
            intent.putExtra(Constants.CHALLENGE_MODEL, challengeModel)
            startActivity(intent)
        }

        override fun error(error: String) {

        }
    }

    private fun share(challengeModel: ChallengeModel) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(
            Intent.EXTRA_TEXT, "http://202.164.42.227/fitwithfriends/deeplink.php?challenge_id=" + challengeModel.id
        )
        startActivity(Intent.createChooser(sharingIntent, "Share Ceed External"))
    }

    private fun listeners() {
        rLayout_add_button.setOnClickListener {
            dialogAddWeight("Add")
        }

        rLayout_change_button.setOnClickListener {
            dialogAddWeight("Change")
        }

        rLayout_notification.setOnClickListener {
            val intent = Intent(activity, NotificationActivity::class.java)
            startActivityForResult(intent, 200)
        }

        layout_swipeRefresh.setOnRefreshListener {
            layout_swipeRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
            )
            getProfileFromServer()
            getAllChallengesFromServer()
        }
    }

    private fun selectImageAlert() {
        val items = arrayOf<CharSequence>("Camera", "Gallery")
        val builder = AlertDialog.Builder(activity)
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
                    mContext,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    mContext, arrayOf(Manifest.permission.CAMERA),
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
            createImageFile(mContext, imageFileName, ".jpg")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", mFile)
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

    private fun dialogAddWeight(status: String) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_weight)

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

        imageViewWeight = dialog.findViewById(R.id.imageView_weight)
        val rLayoutImage: RelativeLayout = dialog.findViewById(R.id.rLayout_image)
        val textViewKg: TextView = dialog.findViewById(R.id.textView_kg)
        val textViewLbs: TextView = dialog.findViewById(R.id.textView_lbs)
        //val layoutOutline: LinearLayout = dialog.findViewById(R.id.layout_outline)
        val textViewCancel: TextView = dialog.findViewById(R.id.textView_cancel)
        val textViewSave: TextView = dialog.findViewById(R.id.textView_save)
        val editTextWeight: EditText = dialog.findViewById(R.id.editText_weight)

        rLayoutImage.setOnClickListener {
            getPermissions()
        }

        textViewLbs.setOnClickListener {
            textViewKg.background = resources.getDrawable(R.color.white)
            textViewLbs.setBackgroundDrawable(resources.getDrawable(R.drawable.layout_outline))
            textViewKg.setBackgroundDrawable(resources.getDrawable(R.drawable.text_drawable_right_outline))
            textViewLbs.setTextColor(resources.getColor(R.color.white))
            textViewKg.setTextColor(resources.getColor(R.color.color_purple))
            weightType = "2"
        }

        textViewKg.setOnClickListener {
            textViewLbs.background = resources.getDrawable(R.color.white)
            textViewLbs.setBackgroundDrawable(resources.getDrawable(R.drawable.text_drawable_left_outline))
            textViewKg.setBackgroundDrawable(resources.getDrawable(R.drawable.text_drawable_outline))
            textViewKg.setTextColor(resources.getColor(R.color.white))
            textViewLbs.setTextColor(resources.getColor(R.color.color_purple))
            weightType = "1"
        }

        textViewCancel.setOnClickListener {
            dialog.dismiss()
        }

        textViewSave.setOnClickListener {
            if (editTextWeight.text.toString().trim().isEmpty()) {
                //CommonMethods.alertTopError(activity!!, "Please add weight")
                Toast.makeText(activity, "Please add weight", Toast.LENGTH_LONG).show()
            } else {
                if (status == "Change") {
                    dialog.dismiss()
                    deleteWeight(editTextWeight.text.toString().trim())
                } else {
                    dialog.dismiss()
                    addWeight(editTextWeight.text.toString().trim())
                }
            }
        }

        dialog.show()
    }

    private fun deleteWeight(weight: String) {
        val userModel = UserModel()
        userModel.weight_id = mUserModel.user_detail.today_weight.id
        iUserService.deleteWeight(
            Constants.BASE_URL + Constants.DELETE_WEIGHT,
            userModel,
            object : AsyncResult<UserModel> {
                override fun success(userModel: UserModel) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        addWeight(weight)
                    }
                }

                override fun error(error: String) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                    }
                }
            })
    }

    private fun addWeight(weight: String) {
        startAnim()
        val c = Calendar.getInstance()
        val day = c.get(Calendar.DAY_OF_MONTH)
        val month = c.get(Calendar.MONTH) + 1
        val year = c.get(Calendar.YEAR)
        mYear = year.toString()
        mMonth = if (month < 10) {
            "-0$month"
        } else {
            "-$month"
        }
        mDay = if (day < 10) {
            "-0$day"
        } else {
            "-$day"
        }
        date = mYear + mMonth + mDay
        val userModel = UserModel()
        userModel.weight = weight
        userModel.weight_type = weightType
        userModel.date = date
        userModel.security_key = Constants.SECURITY_KEY_VALUE
        if (mFilePath == "") {
            iUserService.addWeight(
                Constants.BASE_URL + Constants.ADD_WEIGHT,
                null,
                userModel,
                object : AsyncResult<UserModel> {
                    override fun success(userModel: UserModel) {
                        if (activity == null) {
                            return
                        }
                        activity!!.runOnUiThread {
                            stopAnim()
                            getProfileFromServer()
                            getAllChallengesFromServer()
                            Toast.makeText(activity, "Weight added successfully", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun error(error: String) {
                        if (activity == null) {
                            return
                        }
                        activity!!.runOnUiThread {
                            stopAnim()
                        }
                    }
                })
        } else {
            iUserService.editProfile(
                Constants.BASE_URL + Constants.ADD_WEIGHT,
                userModel,
                Constants.ADD_WEIGHT, PageInput(), mFilePath,
                object : AsyncResult<UserModel> {
                    override fun success(userModel: UserModel) {
                        if (activity == null) {
                            return
                        }
                        activity!!.runOnUiThread {
                            stopAnim()
                            getProfileFromServer()
                            getAllChallengesFromServer()
                            Toast.makeText(activity, "Weight added successfully", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun error(error: String) {
                        if (activity == null) {
                            return
                        }
                        activity!!.runOnUiThread {
                            stopAnim()
                        }
                    }
                })
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //imagePickHelper.pickAnImage(this, ImageUtils.IMAGE_REQUEST_CODE)
        } else {
            ToastUtils.longToast("Required permissions are not granted")
        }
    }

    override fun onImagePicked(requestCode: Int, file: File) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onImagePickError(requestCode: Int, e: Exception) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onImagePickClosed(requestCode: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == mRequestCamera) run {
                val uri = Uri.fromFile(mFile)
                val imagePath = CommonMethods.getAbsolutePath(mContext, uri)
                mFile = File(imagePath)
                mFilePath = mFile.absolutePath
                Glide.with(activity).load(imagePath).into(imageViewWeight)
            } else if (requestCode == mSelectFile) run {
                val uri = data?.data
                if (uri != null) {
                    fileExtensionString = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                    val imagePath = getAbsolutePath(activity!!, uri)
                    mFile = File(imagePath)
                    mFilePath = mFile.absolutePath
                    Glide.with(activity).load(imagePath).into(imageViewWeight)
                }
            }
        } else if (requestCode == 200) {
            getProfileFromServer()
            getAllChallengesFromServer()
        }
    }

    @SuppressLint("Recycle")
    fun getAbsolutePath(activity: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme!!, ignoreCase = true)) {
            val projection = arrayOf("_data")
            val cursor: Cursor?
            try {
                cursor = activity.contentResolver.query(uri, projection, null, null, null)
                assert(cursor != null)
                val columnIndex = cursor!!.getColumnIndexOrThrow("_data")
                if (cursor.moveToFirst()) {
                    return cursor.getString(columnIndex)
                }
            } catch (e: Exception) {
                // Eat it
                e.printStackTrace()
            }

        } else if ("file".equals(uri.scheme!!, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    private fun startAnim() {
        if (avi_loader_home != null) {
            avi_loader_home.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader_home != null) {
            avi_loader_home.smoothToHide()
        }
    }
}
