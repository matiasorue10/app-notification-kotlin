package com.example.firebase_notification_kotlin

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        Log.d(TAG, "From: " + remoteMessage!!.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.data)
            val data = remoteMessage.data
            handleData(data)

        } else if (remoteMessage.notification != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
            handleNotification(remoteMessage.notification!!)
        }// Check if message contains a notification payload.

    }

    private fun handleNotification(RemoteMsgNotification: RemoteMessage.Notification) {
        val message = RemoteMsgNotification.body
        val title = RemoteMsgNotification.title
        val notificationVO = NotificationVO(title, message, null, null, null)

        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val notificationUtils = NotificationUtils(applicationContext)
        notificationUtils.displayNotification(notificationVO, resultIntent)
        notificationUtils.playNotificationSound()
    }

    private fun handleData(data: Map<String, String>) {
        val title = data[TITLE]
        val message = data[MESSAGE]
        val iconUrl = data[IMAGE]
        val action = data[ACTION]
        val actionDestination = data[ACTION_DESTINATION]
        val notificationVO = NotificationVO(title, message, iconUrl, action, actionDestination)

        val resultIntent = Intent(applicationContext, MainActivity::class.java)

        val notificationUtils = NotificationUtils(applicationContext)
        notificationUtils.displayNotification(notificationVO, resultIntent)
        notificationUtils.playNotificationSound()

    }

    companion object {
        private val TAG = "MyFirebaseMsgingService"
        private val TITLE = "title"
        private val EMPTY = ""
        private val MESSAGE = "message"
        private val IMAGE = "image"
        private val ACTION = "action"
        private val DATA = "data"
        private val ACTION_DESTINATION = "action_destination"
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String?) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

    }

}
