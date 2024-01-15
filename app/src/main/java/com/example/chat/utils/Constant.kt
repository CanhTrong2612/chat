package com.example.whatsapp.utils

import android.app.Activity
import android.net.Uri
import android.webkit.MimeTypeMap
import com.example.chat.model.UserModel

object Constant {
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORE_PERMISSION_CODE = 2
    const val USER_PROFILE_IMAGE = "user_profile_image"
    const val PICK_IMAGE_REQUEST = 1

    fun getFileExtension(activity: Activity, uri: Uri): String {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
            .toString()

    }

}