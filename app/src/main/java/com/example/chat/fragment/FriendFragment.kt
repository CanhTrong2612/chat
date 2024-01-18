package com.example.chat.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.adapter.UsersAdapter
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.UserModel

class FriendFragment : Fragment() {
    private var rv: RecyclerView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_status,container,false)
        rv = view.findViewById(R.id.rv_search)
        FirestoresClass().getUserToChatRoom(this)
        return view
    }
    fun displayListUser(list: ArrayList<UserModel>){
            rv!!.layoutManager = LinearLayoutManager(requireContext())
            val adapter = UsersAdapter(requireContext(),list)
            rv!!.adapter = adapter
    }


    }


