package com.fit_with_friends.fitWithFriends.ui.activities.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.fit_with_friends.R
import com.fit_with_friends.common.ui.BaseActivity
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.ui.activities.home.DashboardActivity
import com.fit_with_friends.fitWithFriends.ui.activities.signIn.SignInActivity
import android.net.Uri
import android.view.View
import android.widget.VideoView
import android.view.WindowManager
import android.view.Window
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler

class SplashActivity : BaseActivity() {

    private val mSplashLength = 1000
    private var mInfoFilled = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_splash)
        mInfoFilled = PreferenceHandler.readString(applicationContext, Constants.INFO_FILLED, "")
        val videoHolder = findViewById<View>(R.id.videoView) as VideoView
        try {
            val video = "android.resource://" + packageName + "/" + R.raw.fit_with_friend
            val uri = Uri.parse(video)
            videoHolder.setVideoURI(uri)
            videoHolder.setOnCompletionListener { jump() }
            videoHolder.start()
        } catch (ex: Exception) {
            jump()
        }
    }

    private fun jump() {
        setSplash()
    }

    private fun setSplash() {
        Handler().postDelayed({
            if (mInfoFilled == Constants.DASHBOARD) {
                val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this@SplashActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, mSplashLength.toLong())
    }

    override fun setupActivityComponent() {}
}