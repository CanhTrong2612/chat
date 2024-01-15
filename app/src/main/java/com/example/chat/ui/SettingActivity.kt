package com.example.chat.ui

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.databinding.ActivitySettingBinding
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.UserModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class SettingActivity : AppCompatActivity() {
    private var binding : ActivitySettingBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionbar()
        setupProfile()
        FirestoresClass().getAlltUser(this)

    }
    private fun actionbar() {
        setSupportActionBar(binding?.topbar)
        supportActionBar?.title = ""
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.topbar?.setNavigationOnClickListener { onBackPressed() }
    }
    fun setupProfile(){
        if (intent.hasExtra("model")){
            var data = intent.getParcelableExtra<UserModel>("model")
            Glide.with(this).load(data!!.image).into(binding!!.imgProfile)
            binding?.nameProfile?.text = data.name
            binding?.emailProfile?.text = data?.email
            binding?.phoneProfile?.text = data.phone
            binding?.gmailProfile?.text = data?.email
            if (data.id!= FirestoresClass().getCurrentID()){
                binding?.btnLogout?.visibility = View.GONE
            }

        }

    }
    fun numberOfFriend(list: ArrayList<UserModel>){
        binding?.friendProfile?.text = "FRIENDS"+"("+list.size.toString()+")"
    }
    fun uploadImageAndSaveToFirestore(imageUri: Uri) {
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
                    }
            }
            .addOnFailureListener { e ->
                // Handle failure
            }
    }

    private fun loadImage(image: String) {

    }

}