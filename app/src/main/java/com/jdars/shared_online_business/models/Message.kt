package com.jdars.shared_online_business.models

data class Message(
    var attachmentType: Int = 0,
    var sentAt: Long = 0,
    var senderID: String = "",
    var message: String = "",
    var id: String = "",
    var productMessageModel: ProductMessageModel? = null
)