package com.example.chat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chat.R
import com.example.chat.adapter.SelectUserToSendAdapter
import com.example.chat.databinding.ActivitySelectUserToSendMessageBinding
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.UserModel

class SelectUserToSendMessageActivity : AppCompatActivity() {
    private var binding :ActivitySelectUserToSendMessageBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectUserToSendMessageBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        actionbar()
        FirestoresClass().getAlltUser(this)
    }
    private fun actionbar() {
        setSupportActionBar(binding?.toolbarSelectUser)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding?.toolbarSelectUser?.setNavigationOnClickListener { onBackPressed() }
    }
    fun displayUser(list: ArrayList<UserModel>){
        val adapter = SelectUserToSendAdapter(this, list)
        binding?.rvSelectUser?.layoutManager= LinearLayoutManager(this)
        binding?.rvSelectUser?.adapter = adapter
    }

}