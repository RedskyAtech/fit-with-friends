package com.fit_with_friends.fitWithFriends.ui.activities.signUp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.View
import android.webkit.MimeTypeMap
import com.bumptech.glide.Glide
import com.fit_with_friends.App
import com.fit_with_friends.BuildConfig
import com.fit_with_friends.R
import com.fit_with_friends.common.helpers.ImagePickHelper
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.image.ImageUtils
import com.fit_with_friends.fitWithFriends.utils.image.ToastUtils
import com.fit_with_friends.fitWithFriends.listeners.OnImagePickedListener
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.editText_email
import kotlinx.android.synthetic.main.activity_sign_up.editText_name
import kotlinx.android.synthetic.main.activity_sign_up.rLayout_back_arrow
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class SignUpActivity : BaseAppCompactActivity(), View.OnClickListener, OnImagePickedListener {

    private lateinit var imagePickHelper: ImagePickHelper
    private lateinit var mFile: File
    private val mRequestPermission = 20
    private val mRequestCamera = 201
    private val mSelectFile = 200
    private var fileExtensionString = ""
    private var mFilePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        window.setBackgroundDrawableResource(R.mipmap.bg_image)
        initView()
        listeners()
    }

    private fun initView() {
        imagePickHelper = ImagePickHelper()
    }

    private fun listeners() {
        rLayout_back_arrow.setOnClickListener {
            finish()
        }

        rLayout_logo.setOnClickListener(this)

        imageView_btn_continue.setOnClickListener { view ->
            val name = editText_name.text.toString().trim()
            val email = editText_email.text.toString().trim()
            val password = editText_password.text.toString().trim()
            if (name.isEmpty()) {
                CommonMethods.alertTopError(this@SignUpActivity, "Please enter name")
            } else if (email.isEmpty()) {
                CommonMethods.alertTopError(this@SignUpActivity, "Please enter email")
            } else if (!isValidEmail(editText_email.text.toString().trim { it <= ' ' })) {
                CommonMethods.alertTopError(this@SignUpActivity, "Please enter a valid email address")
            } else if (password.isEmpty()) {
                CommonMethods.alertTopError(this@SignUpActivity, "Please enter a valid password")
            }/* else if (!isValidPassword(editText_password.text.toString().trim { it <= ' ' })) {

            }*/ else if (!passwordValidation(editText_password.text.toString().trim { it <= ' ' })) {
                CommonMethods.alertTopError(
                    this@SignUpActivity,
                    "Password should contain one capital letter, one number and one special character."
                )
            } else if (editText_confirm_password.text.toString().isEmpty()) {
                CommonMethods.alertTopError(this@SignUpActivity, "Please enter confirm password")
            } else if (editText_confirm_password.text.toString().trim() != password) {
                CommonMethods.alertTopError(this@SignUpActivity, "Password not matched")
            } else {
                val intent = Intent(this@SignUpActivity, SignUpSecondActivity::class.java)
                val userModel = UserModel()
                userModel.name = editText_name.text.toString().trim()
                userModel.email = editText_email.text.toString().trim()
                userModel.image = mFilePath
                userModel.password = editText_password.text.toString().trim()
                userModel.confirmPassword = editText_confirm_password.text.toString().trim()
                intent.putExtra(Constants.USER_MODEL, userModel)
                startActivity(intent)
            }
        }
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

    private fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(passwordPattern)
        matcher = pattern.matcher(password)
        return matcher.matches()
    }

    private fun passwordValidation(password: String): Boolean {
        return if (password.length >= 8) {
            val letter = Pattern.compile("[a-zA-z]")
            val digit = Pattern.compile("[0-9]")
            val special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]")
            //Pattern eight = Pattern.compile (".{8}");
            val hasLetter = letter.matcher(password)
            val hasDigit = digit.matcher(password)
            val hasSpecial = special.matcher(password)
            hasLetter.find() && hasDigit.find() && hasSpecial.find()
        } else
            false
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.rLayout_logo -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                        return
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
                        return
                    }
                    imagePickHelper.pickAnImage(this, ImageUtils.IMAGE_REQUEST_CODE)
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
        Glide.with(this@SignUpActivity).load(imageUri)
            .into(imageView_user)
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
                val imagePath = CommonMethods.getAbsolutePath(this, uri)
                mFile = File(imagePath)
                mFilePath = mFile.absolutePath
                Glide.with(this).load(imagePath).into(imageView_profile)
            } else if (requestCode == mSelectFile) run {
                val uri = data?.data
                if (uri != null) {
                    fileExtensionString = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
                    val imagePath = CommonMethods.getAbsolutePath(this, uri)
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
}
