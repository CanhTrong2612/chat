package com.example.chat.firebase

import android.app.Activity
import android.util.Log
import com.example.chat.fragment.MessFragment
import com.example.chat.fragment.StatusFragment
import com.example.chat.model.ChatMessage
import com.example.chat.model.ChatRoomModel
import com.example.chat.model.UserModel
import com.example.chat.ui.ChatActivity
import com.example.chat.ui.LoginActivity
import com.example.chat.ui.RegisterActivity
import com.example.chat.ui.SelectUserToSendMessageActivity
import com.example.chat.ui.SettingActivity
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject

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

    fun getListUser(fragment: StatusFragment) {
        mFireStore.collection("User")
            .get()
            .addOnSuccessListener { document ->
                Log.e("test", document.documents.toString())
                val list: ArrayList<UserModel> = ArrayList()
                for (i in document) {
                    val user = i.toObject(UserModel::class.java)
                    list.add(user)
                }
                fragment.displayListUser(list)
            }
    }

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


    fun createChatRoom(activity: ChatActivity, chatRoom: ChatRoomModel, userId2: String) {
        mFireStore.collection("ChatRoom")
            .document(getChatRoom(userId2))
            .set(chatRoom, SetOptions.merge())
            .addOnSuccessListener {

            }

    }

    fun getChatRoomMessage(
        activity: ChatActivity,
        chatRoom: ChatRoomModel,
        userId2: String,
        chats: ChatMessage
    ) {
        mFireStore.collection("ChatRoom")
            .document(getChatRoom(userId2))
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

    fun getUserToChatRoom(fragment: MessFragment) {
        mFireStore.collection("ChatRoom")
            .whereArrayContains("userIds", FirestoresClass().getCurrentID())
            .get()
            .addOnSuccessListener { document ->
                val list = ArrayList<ChatRoomModel>()
                for (i in document) {
                    val chat = i.toObject<ChatRoomModel>(ChatRoomModel::class.java)
                    list.add(chat)
                }
                fragment.displayListUser(list)
            }
    }

    fun getAlltUser(activity: Activity) {
        mFireStore.collection("User")
            .get()
            .addOnSuccessListener { document ->
                Log.e("getAll", document.documents.toString())
                val list: ArrayList<UserModel> = ArrayList()
                for (i in document) {
                    val user = i.toObject(UserModel::class.java)
                    list.add(user)
                }
                when(activity){
                    is SelectUserToSendMessageActivity ->{
                        activity?.displayUser(list)
                    }
                    is SettingActivity ->{
                        activity.numberOfFriend(list)
                    }
                }

            }

    }

    fun addImageToChatRoomMessage(
        activity: ChatActivity,
        hashMap: HashMap<String,Any>,
        userId2: String,
    ) {
        mFireStore.collection("ChatRoom")
            .document(getChatRoom(userId2))
            .collection("chats")
            .document()
            .update(hashMap)
            .addOnSuccessListener {
               activity.displayMessage()
            }
    }
}



