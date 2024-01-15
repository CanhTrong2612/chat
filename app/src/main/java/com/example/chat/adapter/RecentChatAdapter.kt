package com.example.chat.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chat.MyInterface
import com.example.chat.R
import com.example.chat.firebase.FirestoresClass
import com.example.chat.model.ChatRoomModel
import com.example.chat.ui.ChatActivity
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class RecentChatAdapter(val list: ArrayList<ChatRoomModel>, val recentInterface: MyInterface?):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recent_chat_layout, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyViewHolder) {
            Picasso.get()
                .load(list[position].image)
                .fit()
                .into(holder.image)
          //  Glide.with().load(list[position].image).into(holder.image)
            if (list[position].lastMessageSenderId==FirestoresClass().getCurrentID()){
                holder.lastMessage.text ="You: "+list[position].lastMessage
           }
            else{
                holder.lastMessage.text =list[position].lastMessage
            }

            holder.time.text = list[position].timeStamp
            holder.fullname.text = list[position].name
            holder.itemView.setOnClickListener {
//                recentInterface.onClick(list[position])

                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("model",list[position])
                context.startActivity(intent)
            }

        }
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: CircleImageView = view.findViewById(R.id.item_image_recent)
        val fullname: TextView = view.findViewById(R.id.item_name_recent)
        val lastMessage: TextView = view.findViewById(R.id.item_lastmessage)
        val time : TextView = view.findViewById(R.id.item_lasttime)

    }


}
