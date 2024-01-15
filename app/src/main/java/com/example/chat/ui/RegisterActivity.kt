package com.example.chat.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.chat.databinding.ActivityRegisterBinding
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.UserModel


import com.example.whatsapp.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class RegisterActivity : BaseActivity() {
    private var mSelectImageUri :Uri?= null
    private lateinit var mImageUri :String
    private var binding:ActivityRegisterBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.camera?.setOnClickListener {
            getImage()
        }
        binding?.btnRegister?.setOnClickListener {
            uploadImageAndSaveToFirestore(mSelectImageUri!!)
        }

    }
    private fun validateForm(): Boolean {
        when{
            TextUtils.isEmpty(binding?.fullnameRegister?.text?.toString())->{
                showErrorSnackBar("Please enter fullname",false)
            }
            TextUtils.isEmpty(binding?.fullnameRegister?.text?.toString())->{
                showErrorSnackBar("Please enter email",false)
            }
            TextUtils.isEmpty(binding?.fullnameRegister?.text?.toString())->{
                showErrorSnackBar("Please enter phone number",false)
            }
            TextUtils.isEmpty(binding?.fullnameRegister?.text?.toString())->{
                showErrorSnackBar("Please enter password",false)
            }
        }
        return true

    }
    fun getImage() {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            showImageChoosen()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA),
                Constant.READ_STORE_PERMISSION_CODE
            )
        }

        showImageChoosen()
    }
    private fun showImageChoosen() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(gallery, Constant.PICK_IMAGE_REQUEST_CODE)
        //getAction.launch(gallery)

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showImageChoosen()
        } else {
            Toast.makeText(
                this,
                "You just dennied the permission for storage.You can aslo allow it from setting",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK &&
            requestCode == Constant.PICK_IMAGE_REQUEST_CODE && data!!.data != null
        ) {
            mSelectImageUri = data!!.data
            binding?.profileImage?.setImageURI(Uri.parse(mSelectImageUri!!.toString()))
        }
    }
     fun registerUser(imageProfile:String){
        val email = binding?.emailRegister?.text.toString()
        val password = binding?.passRegister?.text.toString()
        if (validateForm()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result!!.user!!
                        val user = UserModel(
                            firebaseUser.uid,
                            binding?.emailRegister?.text.toString().trim(),
                            binding?.fullnameRegister?.text.toString().trim(),
                            binding?.phoneRegister?.text!!.toString(),
                            binding?.passRegister?.text!!.toString(),
                            imageProfile
                        )
                        FirestoresClass().registerUser(this, user)
//                        FirebaseAuth.getInstance().signOut()
//                        finish()
                    } else {
                        // hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }
    fun userRegisterSuccess(){
        Toast.makeText(this, "You are registered successfully", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,LoginActivity::class.java))
    }

    fun loadImage(image:String) {
        registerUser(image)
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



}