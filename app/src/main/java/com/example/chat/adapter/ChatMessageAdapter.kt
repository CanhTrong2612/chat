package com.example.chat.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat.R
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.ChatMessage
import com.example.chat.ui.ChatActivity

class ChatMessageAdapter(val context : Context, val list: ArrayList<ChatMessage>,val activity: ChatActivity) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_row_recyclerview, parent, false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            val model = list[position]
            if (FirestoresClass().getCurrentID().equals(model.senderId)) {
                if (!model.image.isNullOrBlank()) {
                    holder.rightImg.visibility = View.VISIBLE
                    Glide.with(context).load(model.image).into(holder.rightImg)
                    holder.leftImg.visibility = View.GONE
                    holder.leftMess.visibility = View.GONE
                    holder.rightMess.visibility= View.GONE
                  //  holder.rightMess.text = list[position].message
                }
                else{
                    holder.rightImg.visibility = View.GONE
                    holder.rightMess.text = list[position].message
                    holder.leftMess.visibility = View.GONE
                }
//                holder.leftMess.visibility = View.GONE
//                holder.rightMess.text = list[position].message
            }
            else{
                if (!model.image.isNullOrBlank()) {
                    holder.rightImg.visibility = View.GONE
                    holder.leftImg.visibility = View.VISIBLE
                    Glide.with(context).load(model.image).into(holder.leftImg)
                    holder.leftMess.visibility = View.VISIBLE
                    holder.leftMess.visibility = View.GONE
                    holder.rightMess.visibility = View.GONE
                }
                else{
                    holder.rightImg.visibility = View.GONE
                    holder.leftImg.visibility = View.GONE
                    holder.leftMess.text = list[position].message
                    holder.rightMess.visibility = View.GONE
                }

            }
        }
        }


    class MyViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val leftImg : ImageView = view.findViewById(R.id.img_left)
        val rightImg : ImageView = view.findViewById(R.id.img_right)
        val leftMess : TextView = view.findViewById(R.id.left_mess)
        val rightMess : TextView = view.findViewById(R.id.right_mess)

    }

}