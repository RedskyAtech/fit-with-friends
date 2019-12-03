package com.fit_with_friends.fitWithFriends.ui.activities.signIn

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.fit_with_friends.App
import com.fit_with_friends.R
import com.fit_with_friends.common.contracts.services.IUserService
import com.fit_with_friends.common.contracts.tool.AsyncResult
import com.fit_with_friends.common.ui.BaseAppCompactActivity
import com.fit_with_friends.fitWithFriends.impl.models.UserModel
import com.fit_with_friends.fitWithFriends.ui.activities.forgotPassword.ForgotPasswordActivity
import com.fit_with_friends.fitWithFriends.ui.activities.home.DashboardActivity
import com.fit_with_friends.fitWithFriends.ui.activities.signUp.SignUpActivity
import com.fit_with_friends.fitWithFriends.utils.CommonMethods
import com.fit_with_friends.fitWithFriends.utils.Constants
import com.fit_with_friends.fitWithFriends.utils.PreferenceHandler
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class SignInActivity : BaseAppCompactActivity() {

    @Inject
    internal lateinit var iUserService: IUserService

    private lateinit var callbackManager: CallbackManager
    private lateinit var mGoogleApiClient: GoogleApiClient
    private lateinit var googleSignInOptions: GoogleSignInOptions

    private val mRcSignIn = 0
    internal var fbUserName = ""
    internal var fbEmail = ""
    internal var fbImage = ""
    internal var fbGender = ""
    internal var fbDob = ""
    internal var fbFirstName = ""
    internal var fbLastName = ""
    internal var socialId = ""
    private var googleUserName = ""
    private var googleEmail = ""
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
        window.setBackgroundDrawableResource(R.mipmap.bg_image)
        email = PreferenceHandler.readString(this@SignInActivity, Constants.EMAIL, "")
        password = PreferenceHandler.readString(this@SignInActivity, Constants.PASSWORD, "")
        initView()
        listeners()
        facebookLogin()
        setEmailPassword()
    }

    private fun initView() {
        callbackManager = CallbackManager.Factory.create()
        //google login
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        //Initializing google api client
        mGoogleApiClient = GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build()
    }

    private fun setEmailPassword() {
        if (email != "" || password != "") {
            editText_email.setText(email)
            editText_password.setText(password)
            checkbox_remember_me.isChecked = true
        } else {
            checkbox_remember_me.isChecked = false
        }
    }

    override fun onStart() {
        super.onStart()
        mGoogleApiClient.connect()
    }

    private fun listeners() {

        checkbox_remember_me.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val email: String = editText_email.text.toString().trim()
                val password: String = editText_password.text.toString().trim()
                when {
                    email.isEmpty() -> {
                        checkbox_remember_me.isChecked = false
                        CommonMethods.alertTopError(this@SignInActivity, "Please enter email")
                    }
                    password.isEmpty() -> {
                        checkbox_remember_me.isChecked = false
                        CommonMethods.alertTopError(this@SignInActivity, "Please enter password")
                    }
                    else -> {
                        checkbox_remember_me.isChecked = true
                        PreferenceHandler.writeString(this@SignInActivity, Constants.EMAIL, email)
                        PreferenceHandler.writeString(this@SignInActivity, Constants.PASSWORD, password)
                    }
                }
            } else {
                PreferenceHandler.writeString(this@SignInActivity, Constants.EMAIL, "")
                PreferenceHandler.writeString(this@SignInActivity, Constants.PASSWORD, "")
            }
        }

        imageView_btn_sign_in.setOnClickListener { view ->
            if (editText_email.text.toString().isEmpty()) {
                CommonMethods.alertTopError(this@SignInActivity, "Please enter email")
            } else if (!isValidEmail(editText_email.text.toString().trim { it <= ' ' })) {
                CommonMethods.alertTopError(this@SignInActivity, "Please enter valid email id")
            } else if (editText_password.text.toString().isEmpty()) {
                CommonMethods.alertTopError(this@SignInActivity, "Please enter password")
            } else {
                //imageView_btn_sign_in.isEnabled = false
                val email = editText_email.text.toString()
                val password = editText_password.text.toString()
                val userModel = UserModel()
                userModel.email = email
                userModel.password = password
                userModel.device_type = "1"
                userModel.device_token = FirebaseInstanceId.getInstance().token
                login(userModel, false, "")
            }
        }

        textView_sign_up.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        textView_forgot_password.setOnClickListener {
            val intent = Intent(this@SignInActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        imageView_signup_fb.setOnClickListener {
            login_button.performClick()
        }

        imageView_signup_google.setOnClickListener {
            val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
            startActivityForResult(signInIntent, mRcSignIn)
        }
    }

    private fun login(
        userModel: UserModel,
        isSocialLogin: Boolean,
        loginType: String
    ) {
        startAnim()
        val action: String = if (isSocialLogin) {
            Constants.SOCIAL_LOGIN
        } else {
            Constants.LOGIN
        }
        iUserService.loginUser(
            Constants.BASE_URL + action,
            null,
            userModel,
            object : AsyncResult<UserModel> {
                override fun success(userModel: UserModel) {
                    runOnUiThread {
                        stopAnim()
                        when (loginType) {
                            "Google" -> {
                                logout()
                                startNextActivity(userModel)
                            }
                            "FB" -> {
                                startNextActivity(userModel)
                            }
                            else -> {
                                startNextActivity(userModel)
                            }
                        }
                    }
                }

                override fun error(error: String) {
                    runOnUiThread {
                        stopAnim()
                        //imageView_btn_sign_in.isEnabled = true
                        CommonMethods.alertTopError(this@SignInActivity, error)
                        //Toast.makeText(this@SignInActivity, error, Toast.LENGTH_LONG).show()
                    }
                }
            })
    }

    private fun startNextActivity(userModel: UserModel) {
        if (userModel.user_detail != null) {
            val intent = Intent(this@SignInActivity, DashboardActivity::class.java)
            PreferenceHandler.writeString(
                this@SignInActivity,
                Constants.TOKEN,
                userModel.user_detail.access_token
            )
            PreferenceHandler.writeString(
                this@SignInActivity,
                Constants.NOTIFICATION_STATUS,
                userModel.user_detail.notification_status
            )
            PreferenceHandler.writeString(
                this@SignInActivity,
                Constants.MOTIVATION_NOTIFICATION_STATUS,
                userModel.user_detail.motivation_notification_status
            )
            PreferenceHandler.writeString(
                this@SignInActivity,
                Constants.INFO_FILLED,
                Constants.DASHBOARD
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } else {
            runOnUiThread {
                Toast.makeText(this, "Can't be login blocked by admin", Toast.LENGTH_LONG).show()
                //CommonMethods.alertTopError(this@SignInActivity, "Can't be login blocked by admin")
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


    private fun facebookLogin() {
        login_button.setReadPermissions(Arrays.asList("public_profile", "email"))
        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val request = GraphRequest.newMeRequest(
                    loginResult.accessToken
                ) { `object`, response ->
                    try {
                        fbEmail = `object`.getString("email")
                        Log.e("fbEmail", fbEmail)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    try {
                        socialId = `object`.getString("id")
                        Log.e("fbId", socialId)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    try {
                        fbImage = "http://graph.facebook.com/$socialId/picture?type=large"
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    try {
                        val jsonObject = JSONObject(response.toString())
                        val sourceObj = jsonObject.getJSONObject("cover")
                        val source = sourceObj.getString("source")
                        Log.e("source", source)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    try {
                        fbUserName = `object`.getString("name")
                        Log.e("fbUserName", fbUserName)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    try {
                        fbFirstName = `object`.getString("first_name")
                        Log.e("fbFirst_name", fbFirstName)

                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }

                    try {
                        fbLastName = `object`.getString("last_name")
                        Log.e("fbLast_name", fbLastName)

                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }

                    try {
                        fbGender = `object`.getString("gender")
                        fbGender = (fbGender.substring(0, 1).toUpperCase() + fbGender.substring(1))
                        Log.e("fbGender", fbGender)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }

                    try {
                        fbDob = `object`.getString("birthday")
                        Log.e("fbBirthday", fbDob)
                    } catch (e: JSONException) {
                        e.printStackTrace()

                    }

                    val userModel = UserModel()
                    userModel.name = fbUserName
                    userModel.email = fbEmail
                    userModel.device_type = "1"
                    userModel.device_token = FirebaseInstanceId.getInstance().token
                    userModel.social_id = socialId
                    userModel.social_type = "FB"
                    userModel.security_key = Constants.SECURITY_KEY_VALUE
                    userModel.dob = fbDob
                    login(userModel, true, "FB")
                }
                val parameters = Bundle()
                parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                LoginManager.getInstance().logOut()
            }

            override fun onError(error: FacebookException) {

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mRcSignIn) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            //Calling a new function to handle signIn
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        //If the login succeed
        if (result.isSuccess) {
            //Getting google account
            val acct = result.signInAccount!!
            socialId = acct.id.toString()
            googleUserName = acct.givenName.toString()
            googleEmail = acct.email.toString()
            //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            googleLoginApi(socialId, googleUserName, googleEmail)
        } else {
            //If login fails
            //Toast.makeText(this, "Something wrong please try again" + result.getStatus().toString(), Toast.LENGTH_LONG).show();
            if (result.status.toString() == "Status{statusCode=NETWORK_ERROR, resolution=null}") {
                CommonMethods.alertTopError(this@SignInActivity, "NETWORK ERROR")
            }
        }
    }

    private fun googleLoginApi(social_id: String, g_username: String, g_email: String) {
        val userModel = UserModel()
        userModel.name = g_username
        userModel.email = g_email
        userModel.device_type = "2"
        userModel.social_id = social_id
        userModel.social_type = "google"
        userModel.device_token = FirebaseInstanceId.getInstance().token
        userModel.security_key = Constants.SECURITY_KEY_VALUE
        login(userModel, true, "Google")
    }

    private fun logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback {
            //Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
        }
    }

    override fun onResume() {
        super.onResume()
        disconnectFromFacebook()
    }

    private fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE,
            GraphRequest.Callback { LoginManager.getInstance().logOut() }).executeAsync()
    }

    override fun setupActivityComponent() {
        App.get(this).component().inject(this)
    }

    private fun startAnim() {
        avi_loader.smoothToShow()
    }

    internal fun stopAnim() {
        avi_loader.smoothToHide()
    }
}
