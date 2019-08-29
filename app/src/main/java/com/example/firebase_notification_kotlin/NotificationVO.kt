package com.example.firebase_notification_kotlin

import java.io.Serializable


data class NotificationVO(
    var title: String?,
    var message: String?,
    var image: String?,
    var action: String?,
    var actionDestination: String?
): Serializable {

}