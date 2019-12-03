package com.fit_with_friends.fitWithFriends.ui.activities.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.Toast
import com.fit_with_friends.R
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.enums.FragmentTag
import com.fit_with_friends.fitWithFriends.impl.models.ChallengeModel
import com.fit_with_friends.fitWithFriends.ui.activities.challenge.ChallengeDetailsActivity
import com.fit_with_friends.fitWithFriends.ui.fragments.findChallengers.FindChallengersFragment
import com.fit_with_friends.fitWithFriends.ui.fragments.competition.CompetitionFragment
import com.fit_with_friends.fitWithFriends.ui.fragments.home.HomeFragment
import com.fit_with_friends.fitWithFriends.ui.fragments.motivation.MotivationFragment
import com.fit_with_friends.fitWithFriends.ui.fragments.settings.SettingsFragment
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseAppCompactActivity(),
    HomeFragment.OnFragmentInteractionListener,
    CompetitionFragment.OnFragmentInteractionListener,
    MotivationFragment.OnFragmentInteractionListener,
    FindChallengersFragment.OnFragmentInteractionListener,
    SettingsFragment.OnFragmentInteractionListener, View.OnClickListener {

    private lateinit var selectedFragment: Fragment
    private var currentFragment: String = ""
    private var isExit: Boolean = false
    private var isHome: Boolean = true
    private var isCompetition: Boolean = true
    private var isMotivation: Boolean = true
    private var isChallenge: Boolean = true
    private var isUser: Boolean = true
    //private var isClicked: Boolean = false
    private var isFirstTime: Boolean = true
    private var count: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        isFirstTime = PreferenceHandler.readBoolean(this@DashboardActivity, Constants.IS_FIRST_TIME, true)
        listeners()
        rLayout_home.performClick()
        setWalkThrough()

        try {
            deepLinking()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun deepLinking() {
        try {
            var intent = intent
            val data = intent.data
            if (data != null) {
                Log.e("data_deep_linking", data.toString())
                val challengeId = data.toString().substring(data.toString().lastIndexOf("=") + 1)
                val id = challengeId.replace("[^0-9]".toRegex(), "")
                if (id != "") {
                    intent = Intent(this@DashboardActivity, ChallengeDetailsActivity::class.java)
                    val challengeModel = ChallengeModel()
                    challengeModel.id = id
                    intent.putExtra(Constants.CHALLENGE_MODEL, challengeModel)
                    startActivity(intent)
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun setWalkThrough() {
        if (isFirstTime) {
            if (rLayout_background != null) {
                rLayout_background.visibility = View.VISIBLE
            }
            if (frame_layout_dashboard != null) {
                frame_layout_dashboard.visibility = View.GONE
            }
            if (bottom_layout != null) {
                bottom_layout.visibility = View.GONE
            }
            PreferenceHandler.writeBoolean(this@DashboardActivity, Constants.IS_FIRST_TIME, false)
        } else {
            if (rLayout_background != null) {
                rLayout_background.visibility = View.GONE
            }
            if (frame_layout_dashboard != null) {
                frame_layout_dashboard.visibility = View.VISIBLE
            }
            if (bottom_layout != null) {
                bottom_layout.visibility = View.VISIBLE
            }
        }
    }

    private fun listeners() {
        rLayout_home.setOnClickListener(this)
        rLayout_competition.setOnClickListener(this)
        rLayout_motivation.setOnClickListener(this)
        rLayout_challenge.setOnClickListener(this)
        rLayout_user.setOnClickListener(this)

        rLayout_background.setOnClickListener {
            /*if (isClicked) {
                isClicked = false
                val intent = Intent(this@DashboardActivity, CreateChallengeActivity::class.java)
                intent.putExtra(Constants.FROM_ACTIVITY, Constants.DASHBOARD)
                startActivityForResult(intent, 115)
            } else {
                isClicked = true
            }*/

            when (count) {
                0 -> {
                    count = 1
                    rLayout_background.background =
                        ContextCompat.getDrawable(this@DashboardActivity, R.drawable.home_walkthrough_two)
                }
                1 -> {
                    count = 2
                    rLayout_background.background =
                        ContextCompat.getDrawable(this@DashboardActivity, R.drawable.home_walkthrough_three)
                }
                2 -> {
                    if (rLayout_background != null) {
                        rLayout_background.visibility = View.GONE
                    }
                    if (frame_layout_dashboard != null) {
                        frame_layout_dashboard.visibility = View.VISIBLE
                    }
                    if (bottom_layout != null) {
                        bottom_layout.visibility = View.VISIBLE
                    }
                    openFragment(FragmentTag.HOME.tag)
                }
            }
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.rLayout_home ->
                    if (isHome) {
                        setHomeValidation()
                        openFragment(FragmentTag.HOME.tag)
                    }
                R.id.rLayout_competition ->
                    if (isCompetition) {
                        setCompetitionValidation()
                        openFragment(FragmentTag.COMPETITION.tag)
                    }
                R.id.rLayout_motivation ->
                    if (isMotivation) {
                        setMotivationValidation()
                        openFragment(FragmentTag.MOTIVATION.tag)
                    }
                R.id.rLayout_challenge ->
                    if (isChallenge) {
                        setChallengeValidation()
                        openFragment(FragmentTag.CHALLENGE.tag)
                    }
                R.id.rLayout_user ->
                    if (isUser) {
                        setUserValidation()
                        openFragment(FragmentTag.SETTINGS.tag)
                    }
            }
        }
    }

    fun openFragment(fragment: String) {
        currentFragment = fragment
        when (fragment) {
            FragmentTag.HOME.tag -> {
                setHomeValidation()
                selectedFragment = HomeFragment.newInstance()
                imageView_home.setBackgroundResource(R.mipmap.dashboard)
                imageView_competition.setBackgroundResource(R.mipmap.competition_white)
                imageView_motivation.setBackgroundResource(R.mipmap.motivation_white)
                imageView_challenge.setBackgroundResource(R.mipmap.challenge_white)
                imageView_user.setBackgroundResource(R.mipmap.user_white)
            }
            FragmentTag.COMPETITION.tag -> {
                setCompetitionValidation()
                selectedFragment = CompetitionFragment.newInstance()
                imageView_home.setBackgroundResource(R.mipmap.dashboard_white)
                imageView_competition.setBackgroundResource(R.mipmap.competition)
                imageView_motivation.setBackgroundResource(R.mipmap.motivation_white)
                imageView_challenge.setBackgroundResource(R.mipmap.challenge_white)
                imageView_user.setBackgroundResource(R.mipmap.user_white)
            }
            FragmentTag.MOTIVATION.tag -> {
                setMotivationValidation()
                selectedFragment = MotivationFragment.newInstance()
                imageView_home.setBackgroundResource(R.mipmap.dashboard_white)
                imageView_competition.setBackgroundResource(R.mipmap.competition_white)
                imageView_motivation.setBackgroundResource(R.mipmap.motivation)
                imageView_challenge.setBackgroundResource(R.mipmap.challenge_white)
                imageView_user.setBackgroundResource(R.mipmap.user_white)
            }
            FragmentTag.CHALLENGE.tag -> {
                setChallengeValidation()
                selectedFragment = FindChallengersFragment.newInstance()
                imageView_home.setBackgroundResource(R.mipmap.dashboard_white)
                imageView_competition.setBackgroundResource(R.mipmap.competition_white)
                imageView_motivation.setBackgroundResource(R.mipmap.motivation_white)
                imageView_challenge.setBackgroundResource(R.mipmap.challenge)
                imageView_user.setBackgroundResource(R.mipmap.user_white)
            }
            FragmentTag.SETTINGS.tag -> {
                setUserValidation()
                selectedFragment = SettingsFragment.newInstance()
                imageView_home.setBackgroundResource(R.mipmap.dashboard_white)
                imageView_competition.setBackgroundResource(R.mipmap.competition_white)
                imageView_motivation.setBackgroundResource(R.mipmap.motivation_white)
                imageView_challenge.setBackgroundResource(R.mipmap.challenge_white)
                imageView_user.setBackgroundResource(R.mipmap.user)
            }
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.anim_slide_in_left,
            R.anim.anim_slide_out_left,
            R.anim.anim_slide_out_right,
            R.anim.anim_slide_in_right
        )
        transaction.replace(R.id.frame_layout_dashboard, selectedFragment)
        transaction.addToBackStack(fragment)
        transaction.commit()
    }

    private fun setHomeValidation() {
        isHome = false
        isCompetition = true
        isMotivation = true
        isChallenge = true
        isUser = true
    }

    private fun setCompetitionValidation() {
        isHome = true
        isCompetition = false
        isMotivation = true
        isChallenge = true
        isUser = true
    }

    private fun setMotivationValidation() {
        isHome = true
        isCompetition = true
        isMotivation = false
        isChallenge = true
        isUser = true
    }

    private fun setChallengeValidation() {
        isHome = true
        isCompetition = true
        isMotivation = true
        isChallenge = false
        isUser = true
    }

    private fun setUserValidation() {
        isHome = true
        isCompetition = true
        isMotivation = true
        isChallenge = true
        isUser = false
    }

    override fun onBackPressed() {
        if (isExit) {
            finish() // finish activity
        } else {
            if (currentFragment != FragmentTag.HOME.tag) {
                currentFragment = FragmentTag.HOME.tag
                setHomeValidation()
                setHomeFragment()
            }
            Toast.makeText(applicationContext, "press again to exit", Toast.LENGTH_LONG).show()
            isExit = true
            Handler().postDelayed({ isExit = false }, (3 * 1000).toLong())
        }
    }

    private fun setHomeFragment() {
        imageView_home.setBackgroundResource(R.mipmap.dashboard)
        imageView_competition.setBackgroundResource(R.mipmap.competition_white)
        imageView_motivation.setBackgroundResource(R.mipmap.motivation_white)
        imageView_challenge.setBackgroundResource(R.mipmap.challenge_white)
        imageView_user.setBackgroundResource(R.mipmap.user_white)
        selectedFragment = HomeFragment.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.anim_slide_in_left,
            R.anim.anim_slide_out_left,
            R.anim.anim_slide_out_right,
            R.anim.anim_slide_in_right
        )
        transaction.replace(R.id.frame_layout_dashboard, selectedFragment)
        transaction.addToBackStack(FragmentTag.HOME.tag)
        transaction.commit()
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 115) {
            if (data != null) {
                val fromActivity = data.getStringExtra(Constants.FROM_ACTIVITY)
                if (fromActivity == Constants.CREATE_CHALLENGE) {
                    rLayout_background.visibility = View.GONE
                } else {
                    rLayout_background.visibility = View.GONE
                    openFragment(FragmentTag.COMPETITION.tag)
                }
            }
        }
    }*/

    override fun setupActivityComponent() {

    }

    override fun onFragmentInteraction(uri: Uri) {

    }
}
