package com.example.chat.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.chat.fragment.CallFragment
import com.example.chat.fragment.MessFragment
import com.example.chat.fragment.StatusFragment


class ViewpagerAdapter(fragment: FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragment,lifecycle) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment{
        return when(position){
            0-> {
                MessFragment()
            }
            1->{
                StatusFragment()
            }
            else->{
                CallFragment()
            }
        }
    }

}