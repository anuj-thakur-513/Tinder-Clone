package com.example.tinderclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tinderclone.R
import com.google.firebase.auth.FirebaseAuth

class StartupActivity : AppCompatActivity() {

    // creating a variable for firebase authorization
    private val firebaseAuth = FirebaseAuth.getInstance()
    // creating a listener so that if a user is already logged in then he doesn't have to login again
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        if(user != null){
            startActivity(TinderActivity.newIntent(this@StartupActivity))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        // adding the state listener to the firebaseAuth instance
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        // removing the state listener from the firebaseAuth instance
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)
    }

    fun onLogin(view: View){
        startActivity(LoginActivity.newIntent(this@StartupActivity))
    }

    fun onSignup(view: View){
        startActivity(SignupActivity.newIntent(this@StartupActivity))
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, StartupActivity::class.java)
    }
}