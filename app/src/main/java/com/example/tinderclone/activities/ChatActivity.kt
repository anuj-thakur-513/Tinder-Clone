package com.example.tinderclone.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tinderclone.R
import com.google.firebase.database.core.Context
import com.google.firebase.database.core.view.View

class ChatActivity : AppCompatActivity() {

    private var chatId: String? = null
    private var userId: String? = null
    private var imageUrl: String? = null
    private var otherUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chatId = intent.extras?.getString(PARAM_CHAT_ID)
        userId = intent.extras?.getString(PARAM_USER_ID)
        imageUrl = intent.extras?.getString(PARAM_IMAGE_URL)
        otherUserId = intent.extras?.getString(PARAM_OTHER_USER_ID)

        if (chatId.isNullOrEmpty() || userId.isNullOrEmpty() || imageUrl.isNullOrEmpty() || otherUserId.isNullOrEmpty()){
            Toast.makeText(this, "Chat room error", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    fun onSend(view: View){

    }

    companion object{
        private val PARAM_CHAT_ID = "Chat id"
        private val PARAM_USER_ID = "User id"
        private val PARAM_IMAGE_URL = "Image url"
        private val PARAM_OTHER_USER_ID = "Other user id"
        fun newIntent(context: android.content.Context?, chatId: String?, userId: String?, imageUrl: String?, otherUserId: String?): Intent{
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(PARAM_CHAT_ID, chatId)
            intent.putExtra(PARAM_USER_ID, userId)
            intent.putExtra(PARAM_IMAGE_URL, imageUrl)
            intent.putExtra(PARAM_OTHER_USER_ID, otherUserId)
            return intent
        }
    }

}