package com.jdars.shared_online_business.models

data class Notification(
    var id: String? = null,
    var userID: String? = null,
    var body: String? = null,
    val title: String? = null,
    val type: String? = null
)