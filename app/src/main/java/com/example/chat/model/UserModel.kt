package com.example.chat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val id: String = "",
    val email: String  = "",
    val name:String = "",
    val phone:String = "",
    val password:String = "",
    val image: String =""

) : Parcelable
{
    constructor() : this("", "", "","","","")
}
