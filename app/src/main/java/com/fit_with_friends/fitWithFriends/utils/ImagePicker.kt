package com.fit_with_friends.fitWithFriends.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import com.fit_with_friends.BuildConfig
import com.fit_with_friends.R
import com.fit_with_friends.common.ui.BaseActivity
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

abstract class ImagePicker : BaseActivity() {
    private val SELECT_FILE = 200
    private val REQUEST_CAMERA = 201
    private val REQUEST_PERMISSIONS_CAMERA = 20

    lateinit var mImageFile: File

    fun checkPermissionCamera() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!cameraPermission(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            ) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ), REQUEST_PERMISSIONS_CAMERA
                )
                return
            } else {
                selectImage()
            }
        } else {
            selectImage()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionCheck = PackageManager.PERMISSION_GRANTED
        for (permission in grantResults) {
            permissionCheck += permission
        }
        if (grantResults.isNotEmpty() && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            selectImage()
        }
    }

    private fun cameraPermission(permissions: Array<String>): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permissions[0]
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            this,
            permissions[1]
        ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, permissions[2]) == PackageManager.PERMISSION_GRANTED
    }


    private fun selectImage() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_gallery)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.setGravity(Gravity.CENTER)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val ivCross = dialog.findViewById<ImageView>(R.id.iv_cross)

        val lyGallery = dialog.findViewById<LinearLayout>(R.id.ly_gallery)
        val lyCamera = dialog.findViewById<LinearLayout>(R.id.ly_camera)

        lyGallery.setOnClickListener {
            dialog.dismiss()
            galleryIntent()
        }

        lyCamera.setOnClickListener {
            dialog.dismiss()
            cameraIntent()

        }
        ivCross.setOnClickListener {

            dialog.dismiss()
        }
        dialog.show()
    }

    //TODO open camera
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
        val fileUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", mImageFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    //TODO open gallery
    private fun galleryIntent() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, SELECT_FILE)
    }

    @Throws(IOException::class)
    fun createImageFile(context: Context, name: String, extension: String) {
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        mImageFile = File.createTempFile(
            name,
            extension,
            storageDir
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                val uri = Uri.fromFile(mImageFile)
                val picturePath = CommonMethods.getAbsolutePath(this, uri)
                selectedImage(picturePath)
            } else if (requestCode == SELECT_FILE) {
                val uri = data?.data
                val picturePath = CommonMethods.getAbsolutePath(this, uri!!)
                selectedImage(picturePath)
            }
        }
    }

    abstract fun selectedImage(imagePath: String?)
}