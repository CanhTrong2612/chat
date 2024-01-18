package com.example.chat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.R
import com.example.chat.model.UserModel
import com.example.chat.ui.ChatActivity

class SelectUserToSendAdapter(val context: Context, val list: ArrayList<UserModel>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout_add_send_messsage,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder){
           // Glide.with(context).load(list[position].image).into(holder.image)
            holder.name.text = list[position].name
//            holder.tvSend.setOnClickListener {
//                val intent = Intent(context, ChatActivity::class.java)
//                intent.putExtra("name",list[position].name)
//                intent.putExtra("image",list[position].image)
//              //  intent.putExtra("model",list[position])
//                context.startActivity(intent)
  //          }



        }
    }
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
       // val image : CircleImageView = view.findViewById(R.id.item_image)
        val name : TextView = view.findViewById(R.id.tv_name)
        val tvSend : TextView = view.findViewById(R.id.tv_send)
    }
}