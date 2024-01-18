package com.example.chat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date

@Parcelize
data class UserModel(
    var id: String = "",
    val email: String  = "",
    val name:String = "",
    val phone:String = "",
    val password:String = "",
    val image: String ="",
    val chatRoomId: String= "",
    val userIds: List<String>? = null,
    val timeStamp: String = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date()),
    val lastMessage:String = "",
    val lastImage : String = "",
    val otherId :String = ""

    ) : Parcelable
{
    constructor() : this("", "", "","","","", "",listOf(),"","","","")
}
