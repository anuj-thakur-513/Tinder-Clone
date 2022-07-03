package com.example.tinderclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tinderclone.R
import com.example.tinderclone.adapters.MessagesAdapter
import com.example.tinderclone.util.DATA_CHATS
import com.example.tinderclone.util.DATA_MESSAGES
import com.example.tinderclone.util.Message
import com.example.tinderclone.util.User
import com.google.firebase.database.*
import com.google.firebase.database.core.Context
import com.google.firebase.database.core.view.View
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    // variables for the received values
    private var chatId: String? = null
    private var userId: String? = null
    private var imageUrl: String? = null
    private var otherUserId: String? = null

    // variables for chatDatabase reference and for the message adapter
    private lateinit var chatDatabase: DatabaseReference
    private lateinit var messagesAdapter: MessagesAdapter

    // listener for the chatDatabase
    private val chatMessagesListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val message = snapshot.getValue(Message::class.java)
            if (message != null) {
                messagesAdapter.addMessage(message)
                // scrolling to position of recent message
                messagesRV.post {
                    messagesRV.smoothScrollToPosition(messagesAdapter.itemCount - 1)
                }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(error: DatabaseError) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatId = intent.extras?.getString(PARAM_CHAT_ID)
        userId = intent.extras?.getString(PARAM_USER_ID)
        imageUrl = intent.extras?.getString(PARAM_IMAGE_URL)
        otherUserId = intent.extras?.getString(PARAM_OTHER_USER_ID)

        if (chatId.isNullOrEmpty() || userId.isNullOrEmpty() || imageUrl.isNullOrEmpty() || otherUserId.isNullOrEmpty()) {
            Toast.makeText(this, "Chat room error", Toast.LENGTH_SHORT).show()
            finish()
        }

        chatDatabase = FirebaseDatabase.getInstance().reference.child(DATA_CHATS)
        messagesAdapter = MessagesAdapter(ArrayList(), userId.toString())
        // applying the adapter to the recycler view
        messagesRV.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = messagesAdapter
        }

        // setting the listener for the chatDatabase
        chatDatabase.child(chatId!!).child(DATA_MESSAGES)
            .addChildEventListener(chatMessagesListener)

        // setting the top bar of the chat screen with other user's name and image
        chatDatabase.child(chatId!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { value ->
                    val key = value.key
                    val user = value.getValue(User::class.java)
                    if (!key.equals(userId)) {
                        topNameTV.text = user?.name
                        Glide.with(this@ChatActivity)
                            .load(user?.imageUrl)
                            .into(topPhotoIV)

                        topPhotoIV.setOnClickListener {
                            startActivity(
                                UserInfoActivity.newIntent(
                                    this@ChatActivity,
                                    otherUserId
                                )
                            )
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    // method for send button
    fun onSend(view: android.view.View) {
        val message =
            Message(userId, edtMessage.text.toString(), Calendar.getInstance().time.toString())
        val key = chatDatabase.child(chatId!!).child(DATA_MESSAGES).push().key
        if (!key.isNullOrEmpty()) {
            chatDatabase.child(chatId!!).child(DATA_MESSAGES).child(key).setValue(message)
        }
        edtMessage.setText("", TextView.BufferType.EDITABLE)
    }

    companion object {
        private val PARAM_CHAT_ID = "Chat id"
        private val PARAM_USER_ID = "User id"
        private val PARAM_IMAGE_URL = "Image url"
        private val PARAM_OTHER_USER_ID = "Other user id"
        fun newIntent(
            context: android.content.Context?,
            chatId: String?,
            userId: String?,
            imageUrl: String?,
            otherUserId: String?
        ): Intent {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(PARAM_CHAT_ID, chatId)
            intent.putExtra(PARAM_USER_ID, userId)
            intent.putExtra(PARAM_IMAGE_URL, imageUrl)
            intent.putExtra(PARAM_OTHER_USER_ID, otherUserId)
            return intent
        }
    }
}