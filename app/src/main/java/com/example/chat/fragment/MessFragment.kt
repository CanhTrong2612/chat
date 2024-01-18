package com.example.chat.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.adapter.RecentChatAdapter
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.ChatRoomModel
import com.example.chat.model.UserModel
import com.example.chat.ui.SelectUserToSendMessageActivity
import com.example.whatsapp.utils.Constant
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton


class MessFragment : Fragment() {
    private var rv: RecyclerView? = null
    private var fab: ExtendedFloatingActionButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mess, container, false)
        rv = view.findViewById(R.id.rv)
//        fab = view.findViewById(R.id.extended_fab)
//        fab!!.setOnClickListener {
//            startActivity(Intent(requireContext(), SelectUserToSendMessageActivity::class.java))
//        }
        FirestoresClass().getUserRecentToChatRoom(this)

        return view
        // Inflate the layout for this fragment

    }

    fun displayListUser(list: ArrayList<UserModel>) {
        val adapter = RecentChatAdapter(requireContext(),list,this)
        rv!!.adapter = adapter
        rv!!.layoutManager = LinearLayoutManager(requireContext())
        FirestoresClass().getUserRecentToChatRoom(this)
    }
    fun getChatRoomSuccess(){
        FirestoresClass().getUserRecentToChatRoom(this)
    }
//    fun update(user: UserModel){
//        val hashMap= HashMap<String,Any>()/    val user = UserModel()
//        hashMap[Constant.ID]="123"
//        FirestoresClass().update(this,user.id,hashMap)
//    }

}