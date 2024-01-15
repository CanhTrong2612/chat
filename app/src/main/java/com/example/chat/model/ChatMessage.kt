package com.example.chat.model

import android.net.Uri
import java.text.SimpleDateFormat
import java.util.Date

class ChatMessage (val message :String?,
                   val image: String?,
                   val senderId:String,
                   val timeStamp: String = SimpleDateFormat("yyyy/MM/dd_HH:mm:ss").format(Date()))
{
    constructor() : this("", "", "","")
}


