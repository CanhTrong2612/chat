package com.example.chat.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.chat.adapter.ViewpagerAdapter
import com.example.chat.databinding.ActivityMainBinding
import com.example.chat.model.UserModel

import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val user = intent.getParcelableExtra<UserModel>("user")
        binding?.username?.text = user!!.name
        Glide.with(this).load(user.image).into(binding!!.user)
        val adapter= ViewpagerAdapter(supportFragmentManager,lifecycle)
        binding?.pager?.adapter = adapter
        binding?.tabLayout?.let {
            binding?.pager?.let { it1 ->
                TabLayoutMediator(it, it1){ tab, pos->
                    when(pos){
                        0-> tab.text = "Message"
                        1-> tab.text = "Friend "
                        2-> tab.text = "Status"
                    }
                }.attach()
            }
        }

    }


}