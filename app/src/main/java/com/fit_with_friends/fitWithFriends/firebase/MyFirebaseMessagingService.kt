package com.fit_with_friends.fitWithFriends.firebase

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.fit_with_friends.R
import com.fit_with_friends.fitWithFriends.impl.models.NotificationModel
import com.fit_with_friends.fitWithFriends.ui.activities.home.DashboardActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var gson = Gson()
    private var bodyLocalizationKey: String? = ""
    internal var body: String? = ""
    private var clickAction: String? = ""
    internal var color: String? = ""
    private var icon: String? = ""
    private var sound: String? = ""
    private var tag: String? = ""
    private var title: String? = ""
    private var titleLocalizationKey: String? = ""
    private var link: Uri? = null
    private var notificationType: String? = ""

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d("refreshToken", token)
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        if (message == null) {
            return
        }
        val responseData = message.data["message"]
        if (responseData != null && responseData != "") {
            val notificationModel = gson.fromJson(responseData, NotificationModel::class.java)
            title = message.data["title"]
            notificationType = notificationModel.notification_type
            handleNotification(notificationModel.message)
        } else {
            setMessage(message)
        }
    }

    private fun setMessage(message: RemoteMessage) {
        if (message.notification != null) {
            bodyLocalizationKey = message.notification!!.bodyLocalizationKey
            body = message.notification!!.body
            clickAction = message.notification!!.clickAction
            color = message.notification!!.color
            icon = message.notification!!.icon
            sound = message.notification!!.sound
            tag = message.notification!!.tag
            title = message.notification!!.title
            titleLocalizationKey = message.notification!!.titleLocalizationKey
            link = message.notification!!.link
            handleNotification(body)
        }
    }

    private fun handleNotification(body: String?) {
        val intent: Intent = if (notificationType != null && notificationType.equals("3")) {
            Intent(this, DashboardActivity::class.java)
        } else {
            Intent(this, DashboardActivity::class.java)
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setBadgeIconType(R.mipmap.ic_launcher)
            .setContentText(body)
            .setShowWhen(true)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setColor(resources.getColor(R.color.color_purple))
            .setContentTitle(title)
            .setContentIntent(pendingIntent)
            .setDefaults(Notification.DEFAULT_ALL)
            .setSound(defaultSoundUri).setOngoing(false)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)

        if (Build.VERSION.SDK_INT >= 21) {
            notificationBuilder.setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}