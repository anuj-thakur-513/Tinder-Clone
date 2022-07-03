package com.example.tinderclone.adapters

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tinderclone.R
import com.example.tinderclone.util.Message

class MessagesAdapter(private var messages: ArrayList<Message>, val userId: String):
    RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    companion object{
        val MESSAGE_CURRENT_USER = 1
        val MESSAGE_OTHER_USER = 2
    }

    class MessageViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        fun bind(message: Message){
            view.findViewById<TextView>(R.id.messageTV).text = message.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == MESSAGE_CURRENT_USER){
            MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_current_user_message, parent, false)
            )
        } else {
            MessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_other_user_message, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sentBy.equals(userId)){
            MESSAGE_CURRENT_USER
        } else{
            MESSAGE_OTHER_USER
        }
    }

    override fun getItemCount(): Int = messages.size
}