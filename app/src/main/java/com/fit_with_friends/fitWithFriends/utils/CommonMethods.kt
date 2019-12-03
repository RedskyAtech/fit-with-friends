package com.fit_with_friends.fitWithFriends.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.animation.AnimationUtils
import java.util.regex.Pattern
import android.widget.Toast
import com.fit_with_friends.App
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import android.view.Gravity
import com.tapadoo.alerter.Alerter
import android.app.Activity
import android.support.design.widget.Snackbar
import android.text.TextUtils
import com.fit_with_friends.R
import com.fit_with_friends.common.helpers.DateHelper
import com.fit_with_friends.fitWithFriends.ui.activities.signIn.SignInActivity

class CommonMethods {

    companion object {

        private var dialog: Dialog? = null
        var lastPosition = -1

        fun showSnackBar(view: View, message: String) {
            val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackBar.show()
        }

        fun showShortToast(message: String?) {
            Toast.makeText(App.getInstance(), message, Toast.LENGTH_SHORT).show()
        }

        fun showLongToast(message: String) {
            Toast.makeText(App.getInstance(), message, Toast.LENGTH_LONG).show()
        }

        fun openEmailPicker(addresses: Array<String>, subject: String, context: Context) {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:") // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, addresses)
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        }

        fun setAnimation(viewToAnimate: View, position: Int, mContext: Context) {
            // If the bound view wasn't previously displayed on screen, it's animated
            val animation = AnimationUtils.loadAnimation(
                mContext,
                if (position > lastPosition)
                    R.anim.item_animation_up
                else
                    R.anim.item_animation_down
            )
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }

        fun setAnimationGrid(viewToAnimate: View, position: Int, mContext: Context) {
            // If the bound view wasn't previously displayed on screen, it's animated
            val animation = AnimationUtils.loadAnimation(
                mContext,
                if (position > lastPosition)
                    R.anim.slide_from_bottom
                else
                    R.anim.slide_from_top
            )
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }

        fun setAnimationHorizontal(viewToAnimate: View, position: Int, mContext: Context) {
            // If the bound view wasn't previously displayed on screen, it's animated
            val animation = AnimationUtils.loadAnimation(
                mContext,
                if (position > lastPosition)
                    R.anim.slide_from_right
                else
                    R.anim.slide_from_left
            )
            viewToAnimate.startAnimation(animation)
            lastPosition = position
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

        // show the common progress which is used in all application
        fun showProgress(mContext: Context) {
            try {
                if (dialog == null) {
                    dialog = Dialog(mContext)
                    dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                dialog?.show()
                dialog?.setContentView(R.layout.dialog_progress)
                dialog?.setCancelable(false)
            } catch (e: Exception) {
                e.printStackTrace()
                dialog = null
            }
        }

        // hide the common progress which is used in all application.
        fun hideProgress() {
            try {
                if (dialog != null) {
                    dialog?.hide()
                    dialog?.dismiss()
                    dialog = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dialog = null
            }
        }

        @SuppressLint("Recycle")
        fun getAbsolutePath(activity: Context, uri: Uri): String? {
            if ("content".equals(uri.scheme, ignoreCase = true)) {
                val projection = arrayOf("_data")
                val cursor: Cursor?
                try {
                    cursor = activity.contentResolver.query(uri, projection, null, null, null)
                    val columnIndex = cursor!!.getColumnIndexOrThrow("_data")
                    if (cursor.moveToFirst()) {
                        return cursor.getString(columnIndex)
                    }
                } catch (e: Exception) {
                    // Eat it
                    e.printStackTrace()
                }
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
                return uri.path
            }
            return ""
        }

        @SuppressLint("SimpleDateFormat")
        fun getTimestampFromTime(str_date: String): Long {
            var timeStamp: Long = 0
            try {
                val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
                val date = formatter.parse(str_date)
                timeStamp = date.time
            } catch (ex: ParseException) {
                ex.printStackTrace()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            timeStamp /= 1000
            return timeStamp
        }

        @SuppressLint("SimpleDateFormat")
        fun convertTimeStampTime(timestamp: Long): String {
            val calendar = Calendar.getInstance()
            val tz = TimeZone.getDefault()
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.timeInMillis))
            val sdf = SimpleDateFormat("HH:mm a")
            sdf.timeZone = tz
            val currentTimeZone = Date(timestamp * 1000)
            return sdf.format(currentTimeZone)
        }

        fun trimString(string: String): String {
            //String returnString;
            return if (string.contains("-")) {
                string.replace("-", "")
            } else {
                string
            }
        }

        fun ordinal(i: Int): String {
            val sufixes = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
            return when (i % 100) {
                11, 12, 13 -> i.toString() + "th"
                else -> i.toString() + sufixes[i % 10]
            }
        }

        @Throws(ParseException::class)
        fun convertStringToData(stringData: String): String {
            @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val data = sdf.parse(stringData)
            return DateHelper.stringify(data, "dd-MM-yyyy")
        }

        fun alertTopSuccess(context: Activity, message: String) {
            Alerter.create(context)
                .setText(message)
                .setBackgroundColorRes(R.color.color_green_success) // or setBackgroundColorInt(Color.CYAN)
                .setDuration(Constants.ALERT_DURATION)
                .hideIcon() // Optional - Removes white tint
                .setContentGravity(Gravity.CENTER)
                .show()
        }

        fun alertTopError(context: Activity, message: String) {
            Alerter.create(context)
                .setText(message)
                .setBackgroundColorRes(R.color.red_color) // or setBackgroundColorInt(Color.CYAN)
                .setDuration(Constants.ALERT_DURATION)
                .hideIcon() // Optional - Removes white tint
                .setContentGravity(Gravity.CENTER)
                .show()
        }

        fun startNextActivity(context: Context) {
            val intent = Intent(context, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            PreferenceHandler.clearPreferences(context)
            context.startActivity(intent)
        }

        @Throws(NumberFormatException::class)
        fun convertToFeetInches(str: String): String {
            val value = java.lang.Double.valueOf(str)
            val feet = Math.floor(value / 30.48).toInt()
            val inches = Math.round(value / 2.54 - feet * 12).toInt()
            return "$feet.$inches"
        }

        fun feetToCentimeter(tempFeet: String, tempInch: String): String {
            var dCentimeter = 0.0
            if (!TextUtils.isEmpty(tempFeet)) {
                if (!TextUtils.isEmpty(tempFeet)) {
                    dCentimeter += java.lang.Double.valueOf(tempFeet) * 30.48
                }
                if (!TextUtils.isEmpty(tempInch)) {
                    dCentimeter += java.lang.Double.valueOf(tempInch) * 2.54
                }
            }
            return dCentimeter.toString()
            //Format to decimal digit as per your requirement
        }
    }
}