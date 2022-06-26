package com.example.tinderclone.activities

import com.google.firebase.database.DatabaseReference

// This is created so that every fragment doesn't have to call the same methods again and again
interface TinderCallback {
    fun onSignout()
    fun onGetUserId(): String
    fun getUserDatabse(): DatabaseReference
}