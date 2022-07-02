package com.example.tinderclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tinderclone.R
import com.example.tinderclone.activities.TinderCallback
import com.google.firebase.database.DatabaseReference

class MatchesFragment : Fragment() {
    // variable for handling methods of TinderCallback interface
    private var callback: TinderCallback? = null

    // variables to store userId and to get the database reference
    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    // a chat database reference so whenever a new match is made we can either create a new chat database
    // or add the new chats to the pre existing database
    private lateinit var chatDatabase: DatabaseReference

    fun setCallback(callback: TinderCallback){
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabase()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }

}