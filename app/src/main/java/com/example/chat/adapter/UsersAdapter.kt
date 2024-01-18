package com.example.chat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.fragment.FriendFragment
import com.example.chat.model.UserModel
import com.example.chat.ui.ChatActivity
import com.example.chat.ui.SettingActivity
import de.hdodenhof.circleimageview.CircleImageView

class UsersAdapter(val context: Context, val list: ArrayList<UserModel>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder){
            Glide.with(context).load(list[position].image).into(holder.image)
            holder.email.text = list[position].email
            holder.fullname.text = list[position].name
            holder.itemView.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("model",list[position])
                context.startActivity(intent)
            }
            holder.itemView.setOnLongClickListener {
                val intent = Intent(context, SettingActivity::class.java)
                intent.putExtra("model",list[position])
                context.startActivity(intent)
                true
            }


        }
    }
    class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
        val image :CircleImageView = view.findViewById(R.id.item_image)
        val fullname : TextView = view.findViewById(R.id.item_name)
        val email :TextView = view.findViewById(R.id.item_email)
    }
}