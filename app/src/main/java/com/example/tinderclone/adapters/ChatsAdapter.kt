package com.example.tinderclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tinderclone.R
import com.example.tinderclone.activities.ChatActivity
import com.example.tinderclone.util.Chat

class ChatsAdapter(private var chats: ArrayList<Chat>) :
    RecyclerView.Adapter<ChatsAdapter.ChatsViewHolder>() {

    // adding the elements to the arrayList
    fun addElement(chat: Chat) {
        chats.add(chat)
        notifyDataSetChanged()
    }

    // creates a view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder =
        ChatsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        )

    // binds the items to the view
    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    // return the size of chats arrayList
    override fun getItemCount(): Int = chats.size


    class ChatsViewHolder(private val view: android.view.View) : RecyclerView.ViewHolder(view) {

        // variables to store the ids of view
        private var layout = view.findViewById<android.view.View>(R.id.chatLayout)
        private var image = view.findViewById<ImageView>(R.id.chatPictureIV)
        private var name = view.findViewById<TextView>(R.id.chatNameTV)

        fun bind(chat: Chat) {
            // setting the data in the chat items and setting the onClickListener
            name.text = chat.otherUserName
            if (image != null) {
                Glide.with(view)
                    .load(chat.otherUserImageUrl)
                    .into(image)
            }

            layout.setOnClickListener {
                val intent = ChatActivity.newIntent(
                    view.context,
                    chat.chatId,
                    chat.userId,
                    chat.otherUserImageUrl,
                    chat.otherUserId
                )
                view.context.startActivity(intent)
            }
        }
    }
}