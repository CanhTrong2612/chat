package com.example.chat.ui

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import com.example.chat.databinding.ActivityLoginBinding
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.UserModel
import com.google.firebase.auth.FirebaseAuth


class LoginActivity :BaseActivity() {
    private var binding:ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        binding?.tvregisterLogin?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding?.btnLogin?.setOnClickListener {
            loginUsers()
        }
        binding?.emailLogin?.setText("canh@gmail.com")
        binding?.passwordLogin?.setText("123456")
    }
    fun validateForm(): Boolean{
        return when{
            TextUtils.isEmpty(binding?.emailLogin?.text.toString())->{
                showErrorSnackBar("Please enter email.", true)
                false
            }
            TextUtils.isEmpty(binding?.passwordLogin?.text.toString())->{
                showErrorSnackBar("Please enter password.",true)
                false
            }
            else->{
                true
            }
        }
    }
    fun loginUsers()
    {
        val email = binding?.emailLogin?.text.toString()
        val password = binding?.passwordLogin?.text.toString()
        if (validateForm()){
            showProgressDialog()
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.e(ContentValues.TAG, "signInWithEmail:success")
                        FirestoresClass().getUserDetail(this)

                    } else {
                        hideProgressDialog()
                        showErrorSnackBar("login fail", true)

                    }
                }
        }
    }
    fun userLoggedInSuccess(user: UserModel) {
        hideProgressDialog()
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("user",user)
        startActivity(intent)
    }


}