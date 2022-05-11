package com.jdars.shared_online_business.models

data class Inbox(
    val membersInfo: ArrayList<MembersInfo> = ArrayList(),
    val members: ArrayList<String> = ArrayList(),
    val createdBy: String = "",
    val title: String = "",
    val lastMessage: String = "",
    val senderId: String = "",
    val sentAt: Long = 0,
    val senderName: String = "",
    val createdAt: Long = 0,
    val id: String = "",
)

data class MembersInfo(
    val id: String = "",
    var hasReadLastMessage: Boolean = false,
    var type: String = "",
    var photo: String = "",
    var name: String = "",
)