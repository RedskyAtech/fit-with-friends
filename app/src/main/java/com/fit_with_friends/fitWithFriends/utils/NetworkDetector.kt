package com.fit_with_friends.fitWithFriends.utils

import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager

class NetworkDetector(base: Context) : ContextWrapper(base) {

    val isNetworkAvailable: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

    val isOffline: Boolean
        get() = isNetworkAvailable
}
