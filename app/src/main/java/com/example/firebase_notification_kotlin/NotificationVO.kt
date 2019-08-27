package com.example.firebase_notification_kotlin


data class NotificationVO(
    val title: String?,
    val message: String?,
    var iconUrl: String?,
    var action: String?,
    var actionDestination: String?
) {

}