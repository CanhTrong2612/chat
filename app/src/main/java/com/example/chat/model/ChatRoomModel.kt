package com.example.chat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
@Parcelize
data class ChatRoomModel(
    val chatRoomId: String= "",
    val userIds: List<String>? = null,
    val timeStamp: String = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date()),
    val lastMessageSenderId:String = "",
    val lastMessage:String = "",
    val name:String= "",
    val image:String = ""
): Parcelable
{
    constructor() : this("", listOf(), "","","","","")
}
