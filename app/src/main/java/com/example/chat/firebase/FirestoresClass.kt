package com.example.chat.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.chat.fragment.MessFragment
import com.example.chat.fragment.FriendFragment
import com.example.chat.model.ChatMessage
import com.example.chat.model.ChatRoomModel
import com.example.chat.model.UserModel
import com.example.chat.ui.ChatActivity
import com.example.chat.ui.LoginActivity
import com.example.chat.ui.RegisterActivity
//import com.example.chat.ui.SelectUserToSendMessageActivity
import com.example.chat.ui.SettingActivity
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions

class FirestoresClass {
    private var mFireStore = FirebaseFirestore.getInstance()
    fun registerUser(registerActivity: RegisterActivity, user: UserModel) {
        mFireStore.collection("User")
            .document(user.id)
            .set(user, SetOptions.merge())
            .addOnSuccessListener {
                registerActivity.userRegisterSuccess()
            }
            .addOnFailureListener { e ->
//                activity.hideProgressDialog()
                Log.e(registerActivity.javaClass.simpleName, "Error while registering the user", e)

            }

    }

    fun getCurrentID(): String {
        // An Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if it is not null or else it will be blank.
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }

        return currentUserID
    }

    fun getUserDetail(activity: Activity) {
        mFireStore.collection("User")
            .document(getCurrentID())
            .get()
            .addOnSuccessListener { document ->
                val user = document.toObject(UserModel::class.java)
                Log.e("ttttt", user.toString())
                when (activity) {
                    is LoginActivity -> {
                        if (user != null)
                            activity.userLoggedInSuccess(user)

                    }

                }
            }
    }

//    fun getListUser(fragment: FriendFragment) {
//        mFireStore.collection("ChatRoom")
//            .get()
//            .addOnSuccessListener { document ->
//                Log.e("test", document.documents.toString())
//                val list: ArrayList<UserModel> = ArrayList()
//                for (i in document) {
//                    val user = i.toObject(UserModel::class.java)
//                    list.add(user)
//                }
//                fragment.displayListUser(list)
//            }
//    }

    fun getChatRoomId(): String {
        var userId2: String = ""
        var getCurrentUser = ""
        if (getCurrentID().hashCode() < userId2.hashCode()) {
            getCurrentUser = getCurrentID() + "_" + userId2
        } else {
            getCurrentUser = userId2 + "_" + getCurrentID()
        }
        return getCurrentUser
    }

    fun getChatRoom(userId2: String): String {
        var getCurrentUser = ""
        if (getCurrentID().hashCode() < userId2.hashCode()) {
            getCurrentUser = getCurrentID() + "_" + userId2
        } else {
            getCurrentUser = userId2 + "_" + getCurrentID()
        }
        return getCurrentUser
    }


    fun createChatRoom( chatRoom: UserModel, userId: String) {
        mFireStore.collection("ChatRoom")
            .document(getChatRoom(userId))
            .set(chatRoom, SetOptions.merge())
            .addOnSuccessListener {

            }

    }

    fun createChatRoomMessage(
        activity: ChatActivity,
        userId: String,
        chats: ChatMessage
    ) {
        mFireStore.collection("ChatRoom")
            .document(getChatRoom(userId))
            .collection("chats")
            .document()
            .set(chats)
            .addOnSuccessListener {
                activity.sendMessagesuccess()
            }
    }

    fun getChatMessage(activity: ChatActivity, userId2: String) {
        mFireStore.collection("ChatRoom")
            .document(getChatRoom(userId2))
            .collection("chats")
            .orderBy("timeStamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { document ->
                Log.e("bug", document.documents.toString())
                val list: ArrayList<ChatMessage> = ArrayList()
                for (i in document) {
                    val chat = i.toObject(ChatMessage::class.java)
                    list.add(chat)
                }
                activity.display(list)
            }
//        val options : FirestoreRecyclerOptions<ChatMessage> = FirestoreRecyclerOptions.Builder<ChatMessage>()
//            .setQuery(query,ChatMessage::class.java).build()
//        activity.displayMessage(options)

    }

    fun getUserToChatRoom(fragment: Fragment) {
        mFireStore.collection("User")
          //  .whereArrayContains("userIds", FirestoresClass().getCurrentID())
            .get()
            .addOnSuccessListener { document ->
                val list = ArrayList<UserModel>()
                for (i in document) {
                    val chat = i.toObject(UserModel::class.java)
                    list.add(chat)
                }
                when(fragment){
                    is FriendFragment ->{
                        fragment.displayListUser(list)
                    }
                }

            }
    }
    fun getUserRecentToChatRoom(fragment: MessFragment) {
        mFireStore.collection("ChatRoom")
            .whereArrayContains("userIds", FirestoresClass().getCurrentID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("recent", document.documents.toString())
                val list = ArrayList<UserModel>()
                val id :String?= null
                for (i in document) {
                    val chat = i.toObject(UserModel::class.java)!!
                    chat.id = chat.userIds!!.get(1)
                    list.add(chat)
                }
                when (fragment) {
                    is MessFragment -> {
                        fragment.displayListUser(list)
                    }
                }
            }
    }

    fun getAlltUser(activity: Activity) {
        mFireStore.collection("ChatRoom")
            .get()
            .addOnSuccessListener { document ->
                Log.e("getAll", document.documents.toString())
                val list: ArrayList<UserModel> = ArrayList()
                for (i in document) {
                    val user = i.toObject(UserModel::class.java)
                    list.add(user)
                }
                when(activity){
//                    is SelectUserToSendMessageActivity ->{
//                        activity?.displayUser(list)
//                    }
                    is SettingActivity ->{
                        activity.numberOfFriend(list)
                    }
                }

            }

    }
//    fun update(fragment: MessFragment,userId2: String,hashMap: HashMap<String,Any>){
//        //val hashMap: HashMap<String,Any>? =null
//        mFireStore.collection("ChatRoom")
//          //  .document(getChatRoom(userId2))
//            .update(hashMap)
//            .addOnSuccessListener {
//                fragment .getChatRoomSuccess()
//            }
//    }

}



