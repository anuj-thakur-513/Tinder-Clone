package com.example.tinderclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tinderclone.R
import com.example.tinderclone.activities.TinderCallback
import com.google.firebase.database.DatabaseReference

class ProfileFragment : Fragment() {

    // variable to get current user id
    private lateinit var userId: String
    // variable to get user data from the databse
    private lateinit var userDatabase: DatabaseReference
    // variable for handling methods of TinderCallback interface
    private var callback: TinderCallback? = null

    fun setCallback(callback: TinderCallback){
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

}