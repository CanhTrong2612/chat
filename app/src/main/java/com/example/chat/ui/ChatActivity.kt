package com.example.chat.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.bumptech.glide.Glide
import com.example.chat.adapter.ChatMessageAdapter
import com.example.chat.databinding.ActivityChatBinding
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.ChatMessage
import com.example.chat.model.ChatRoomModel
import com.example.chat.model.UserModel
import com.example.whatsapp.utils.Constant.PICK_IMAGE_REQUEST_CODE
import com.example.whatsapp.utils.Constant.REQUEST_IMAGE_CAPTURE
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class ChatActivity : AppCompatActivity() {
    lateinit var user :UserModel
    private var binding: ActivityChatBinding? = null
    var selectedImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionbar()
        displayUserInToolbar()
        // createChatRoom()
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
        displayMessage()
    }

    fun selectImageToGallery() {
        binding?.libraryChat?.setOnClickListener {
            openImagePicker()
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun sendMessage(message: String, img: String) {
        user = intent.getParcelableExtra<UserModel>("model")!!
        val chatRoom = UserModel(  FirestoresClass().getCurrentID(),
            user!!.email,user.name,"","",user.image,
            FirestoresClass().getCurrentID() + "_" + user!!.id,
            listOf(FirestoresClass().getCurrentID(), user!!.id),
            SimpleDateFormat("HH:mm").format(Date()),
            message!!,
            img!!,
         //   user.userIds!![1]

        )
        FirestoresClass().createChatRoom( chatRoom, user!!.id)
        val chats = ChatMessage(
            message!!,
            img,
            FirestoresClass().getCurrentID()
        )
        FirestoresClass().createChatRoomMessage(this, user!!.id, chats)

    }

    private fun actionbar() {
        setSupportActionBar(binding?.toolbarChat)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarChat?.setNavigationOnClickListener { onBackPressed() }
    }

    private fun displayUserInToolbar() {
        user = intent.getParcelableExtra<UserModel>("model")!!
        binding?.chatName?.text = user!!.name
        Glide.with(this).load(user?.image).into(binding!!.chatImg)
    }

//    private fun createChatRoom() {
//        var user = intent.getParcelableExtra<UserModel>("model")
//        val chatRoom = ChatRoomModel(
//            FirestoresClass().getCurrentID() + "_" + user!!.id,
//            listOf(FirestoresClass().getCurrentID(), user!!.id)
//        )
//        FirestoresClass().createChatRoom(this, chatRoom, user!!.id)
//
//    }

    fun sendMessagesuccess() {
        if (binding?.edtSendMessage != null) {
            binding?.edtSendMessage?.setText("")
            displayMessage()
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    fun display(list: ArrayList<ChatMessage>) {
        binding?.rvChat?.layoutManager = LinearLayoutManager(this, VERTICAL, true)
        binding?.rvChat?.setHasFixedSize(true)
        val adapter = ChatMessageAdapter(this, list)
        binding?.rvChat?.adapter = adapter
        // adapter.notifyItemChanged(list.size-1)
        adapter.notifyDataSetChanged()


    }


    fun displayMessage() {
        user = intent.getParcelableExtra<UserModel>("model")!!
        FirestoresClass().getChatMessage(this, user!!.id)
    }



    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
    }

    fun openImagePicker() {
        val gallery =Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PICK_IMAGE_REQUEST_CODE)

    }
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//           {
//            openCamera()
//        } else {
//            Toast.makeText(
//                this,
//                "You just dennied the permission for storage.You can aslo allow it from setting",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data!!.data!!

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
    }


    @SuppressLint("SuspiciousIndentation")
    private fun loadImage(image: String) {
        val message = binding!!.edtSendMessage!!.text!!.toString()
        sendMessage("", image)

    }

}

