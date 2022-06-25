package com.example.tinderclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tinderclone.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    // creating an instance of firebase auth for authorization
    private val firebaseAuth = FirebaseAuth.getInstance()
    // creating an authentication listener which transitions the activity on login
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        if(user != null){
            startActivity(MainActivity.newIntent(this@LoginActivity))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()
        // adding the listener to the firebaseAuth
        firebaseAuth.addAuthStateListener(firebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        // removing the listener from the firebaseAuth
        firebaseAuth.removeAuthStateListener(firebaseAuthListener)
    }

    fun onLogin(view: View){
        // checking if there is content in edit text fields
        if(!edtEmail.text.toString().isNullOrEmpty() && !edtPassword.text.toString().isNullOrEmpty()){
            // signing in the user and adding a listener in order to handle login exceptions
            firebaseAuth.signInWithEmailAndPassword(edtEmail.text.toString(), edtPassword.text.toString())
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful){
                        Toast.makeText(this@LoginActivity, "Login Error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }
}