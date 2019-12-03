package com.fit_with_friends.fitWithFriends.ui.fragments.settings

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.contracts.tool.PageInput
import com.fit_with_friends.common.ui.BaseV4Fragment
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.ui.activities.notification.NotificationSettingsActivity
import com.fit_with_friends.fitWithFriends.ui.activities.password.ChangePasswordSettingActivity
import com.fit_with_friends.fitWithFriends.ui.activities.policyTypes.PolicyTypeActivity
import com.fit_with_friends.fitWithFriends.ui.activities.profile.EditProfileActivity
import com.fit_with_friends.fitWithFriends.ui.activities.signIn.SignInActivity
import com.fit_with_friends.fitWithFriends.ui.activities.weightLog.WeightLogActivity
import com.fit_with_friends.fitWithFriends.utils.CapitalizeFirstWord
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.rLayout_edit_profile
import kotlinx.android.synthetic.main.fragment_settings.textView_email
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : BaseV4Fragment() {

    @Inject
    internal lateinit var iUserService: IUserService

    private val capitalizeFirstWord: CapitalizeFirstWord = CapitalizeFirstWord()

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
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listeners()
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
                        stopAnim()
                        setData(userModel)
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

    private fun setData(userModel: UserModel) {
        if (userModel.user_detail != null) {
            if (userModel.user_detail.image != null && userModel.user_detail.image != "") {
                if (userModel.user_detail.image.contains("public")) {
                    if (imageView_user != null) {
                        Glide.with(activity).load(Constants.BASE_URL_IMAGE + userModel.user_detail.image)
                            .apply(
                                RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                            ).into(imageView_user)
                    }
                } else {
                    if (imageView_user != null) {
                        Glide.with(activity)
                            .load(Constants.BASE_URL_IMAGE + "public/" + userModel.user_detail.image)
                            .apply(
                                RequestOptions().placeholder(R.mipmap.harry).error(R.mipmap.harry).dontAnimate()
                            ).into(imageView_user)
                    }
                }
            } else {
                if (imageView_user != null) {
                    imageView_user.setImageResource(R.mipmap.harry)
                }
            }

            if (userModel.user_detail.name != null) {
                if (textView_user_name != null) {
                    textView_user_name.text = capitalizeFirstWord.capitalizeWords(userModel.user_detail.name)
                }
            }

            if (userModel.user_detail.email != null) {
                if (textView_email != null) {
                    textView_email.text = userModel.user_detail.email
                }
            }
        }
    }

    private fun listeners() {
        rLayout_weight_log.setOnClickListener {
            val intent = Intent(activity, WeightLogActivity::class.java)
            startActivity(intent)
        }
        rLayout_edit_profile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        rLayout_change_password.setOnClickListener {
            val intent = Intent(activity, ChangePasswordSettingActivity::class.java)
            startActivity(intent)
        }

        rLayout_refer_your_friends.setOnClickListener {
            inviteFriend()
        }

        rLayout_about_us.setOnClickListener {
            val intent = Intent(activity, PolicyTypeActivity::class.java)
            intent.putExtra(Constants.ACTIVITY_TYPE, "aboutUs")
            startActivity(intent)
        }

        rLayout_privacy_policy.setOnClickListener {
            val intent = Intent(activity, PolicyTypeActivity::class.java)
            intent.putExtra(Constants.ACTIVITY_TYPE, "privacyPolicy")
            startActivity(intent)
        }

        rLayout_terms_conditions.setOnClickListener {
            val intent = Intent(activity, PolicyTypeActivity::class.java)
            intent.putExtra(Constants.ACTIVITY_TYPE, "terms&conditions")
            startActivity(intent)
        }

        rLayout_notifications.setOnClickListener {
            val intent = Intent(activity, NotificationSettingsActivity::class.java)
            intent.putExtra(Constants.ACTIVITY_TYPE, "terms&conditions")
            startActivity(intent)
        }

        rLayout_logout.setOnClickListener {
            dialogLogout()
        }
    }

    private fun dialogLogout() {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)

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

        val textViewCancel: TextView = dialog.findViewById(R.id.textView_cancel)
        val textViewOk: TextView = dialog.findViewById(R.id.textView_ok)

        textViewCancel.setOnClickListener {
            dialog.dismiss()
        }

        textViewOk.setOnClickListener {
            dialog.dismiss()
            logout()
        }

        dialog.show()
    }

    private fun logout() {
        startAnim()
        val userModel = UserModel()
        iUserService.logout(
            Constants.BASE_URL + Constants.LOGOUT,
            null,
            userModel,
            object : AsyncResult<UserModel> {
                override fun success(userModel: UserModel) {
                    if (activity == null) {
                        return
                    }
                    activity!!.runOnUiThread {
                        stopAnim()
                        Toast.makeText(activity, "Successfully Logged Out", Toast.LENGTH_LONG).show()
                        val intent = Intent(activity, SignInActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        PreferenceHandler.writeString(activity, Constants.TOKEN, "")
                        PreferenceHandler.writeString(activity, Constants.INFO_FILLED, "")
                        PreferenceHandler.writeBoolean(activity, Constants.IS_WEIGHT_SELECTED, false)
                        PreferenceHandler.writeString(activity, Constants.HEIGHT_SELECTED, "")
                        //PreferenceHandler.clearPreferences(activity)
                        startActivity(intent)
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

    private fun inviteFriend() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(
            Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.fit_with_friends&hl=en"
        )
        startActivity(Intent.createChooser(sharingIntent, "Share Ceed External"))
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
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        getProfileFromServer()
    }

    private fun startAnim() {
        if (avi_loader_setting != null) {
            avi_loader_setting.smoothToShow()
        }
    }

    internal fun stopAnim() {
        if (avi_loader_setting != null) {
            avi_loader_setting.smoothToHide()
        }
    }
}
