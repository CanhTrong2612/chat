package com.example.chat.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.bumptech.glide.Glide
import com.example.chat.adapter.ChatMessageAdapter
import com.example.chat.databinding.ActivityChatBinding
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.ChatMessage
import com.example.chat.model.ChatRoomModel
import com.example.chat.model.UserModel
import com.example.whatsapp.utils.Constant.PICK_IMAGE_REQUEST
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class ChatActivity : AppCompatActivity() {

    private var binding: ActivityChatBinding? = null
     var selectedImageUri: Uri? = null
     var isImageSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionbar()
        displayUserInToolbar()
        // createChatRoom()
        displayMessage()
        selectImageToGallery()
        binding?.btnSend?.setOnClickListener {
            if (selectedImageUri != null) {
                uploadImageAndSaveToFirestore(selectedImageUri!!)
            }
            val message = binding?.edtSendMessage?.text?.toString()
            if (message!!.isNotEmpty()) {
                sendMessage(message!!, "")
            }

        }


//        binding?.btnSend?.setOnClickListener {
//            val message = binding?.edtSendMessage?.text?.toString()

//        }

    }

    fun selectImageToGallery() {
        binding?.libraryChat?.setOnClickListener {
            openImagePicker()
        }
    }

    private fun sendMessage(message: String, img: String) {
//        var user = intent.getParcelableExtra<UserModel>("model")
//        val chats = ChatMessage(message,FirestoresClass().getCurrentID())
//        FirestoresClass().getChatRoomMessage(this,user,user!!.id,chats)
        //   val message = binding!!.edtSendMessage!!.text!!.toString()
        var user = intent.getParcelableExtra<UserModel>("model")
        val chatRoom = ChatRoomModel(
            FirestoresClass().getCurrentID() + "_" + user!!.id,
            listOf(FirestoresClass().getCurrentID(), user!!.id),
            SimpleDateFormat("HH:mm").format(Date()),
            FirestoresClass().getCurrentID(),
            message!!,
            user.name,
            user.image
        )

        FirestoresClass().createChatRoom(this, chatRoom, user!!.id)
        val chats = ChatMessage(
            message!!,
            img,
            FirestoresClass().getCurrentID()
        )
        FirestoresClass().getChatRoomMessage(this, chatRoom, user!!.id, chats)

    }

    private fun actionbar() {
        setSupportActionBar(binding?.toolbarChat)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarChat?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun displayUserInToolbar() {
        var user = intent.getParcelableExtra<UserModel>("model")
        binding?.chatName?.text = user!!.name
        Glide.with(this).load(user?.image).into(binding!!.chatImg)
    }

    private fun createChatRoom() {
        var user = intent.getParcelableExtra<UserModel>("model")
        val chatRoom = ChatRoomModel(
            FirestoresClass().getCurrentID() + "_" + user!!.id,
            listOf(FirestoresClass().getCurrentID(), user!!.id)
        )
        FirestoresClass().createChatRoom(this, chatRoom, user!!.id)

    }

    fun sendMessagesuccess() {
        if (binding?.edtSendMessage != null) {
            binding?.edtSendMessage?.setText("")
            displayMessage()
        }
        if (binding?.imageSelect != null) {
            binding?.imageSelect?.visibility = View.GONE
            binding?.imageSelect?.visibility = View.VISIBLE
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun display(list: ArrayList<ChatMessage>) {
        binding?.rvChat?.layoutManager = LinearLayoutManager(this, VERTICAL, true)
        binding?.rvChat?.setHasFixedSize(true)
        val adapter = ChatMessageAdapter(this, list, this)
        binding?.rvChat?.adapter = adapter
        // adapter.notifyItemChanged(list.size-1)
        adapter.notifyDataSetChanged()


    }


    fun displayMessage() {
        val message = intent.getParcelableExtra<UserModel>("model")
        FirestoresClass().getChatMessage(this, message!!.id)


    }

    fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.data!!
            // binding?.imageSelect?.setImageURI(Uri.parse(selectedImageUri!!.toString()))
        }


    }


    fun uploadImageAndSaveToFirestore(imageUri: Uri) {
        //   while (!isImageSelected) {
        // Step 1: Upload image to Firebase Storage
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
            .child("images/${UUID.randomUUID()}") // You can adjust the path as needed

        storageReference.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Step 2: Retrieve Download URL from Storage
                taskSnapshot.storage.downloadUrl
                    .addOnSuccessListener { downloadUrl ->
                        // Step 3: Save the Download URL to Firestore
                        loadImage(downloadUrl.toString())
                        selectedImageUri = null
                    }
            }

        //   }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun loadImage(image: String) {
//        val user = intent.getParcelableExtra<UserModel>("model")
//        val hashMap = HashMap<String,Any>()
        val message = binding!!.edtSendMessage!!.text!!.toString()

//        hashMap["image"] = image
//            FirestoresClass().addImageToChatRoomMessage(this,hashMap,user!!.id)
        sendMessage("", image)


    }
}

