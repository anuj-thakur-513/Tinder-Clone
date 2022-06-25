package com.example.tinderclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tinderclone.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    // creating an instance of firebase auth for authorization
    private val firebaseAuth = FirebaseAuth.getInstance();
    // creating an authentication listener which transitions the activity on login
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        if(user != null){
            startActivity(MainActivity.newIntent(this@SignupActivity))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
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

    fun onSignup(view: View){
        // signing in the user and adding a listener in order to handle signup exceptions\
        if(!edtEmail.text.toString().isNullOrEmpty() && !edtPassword.text.toString().isNullOrEmpty()){
            firebaseAuth.createUserWithEmailAndPassword(edtEmail.text.toString(), edtPassword.text.toString())
                .addOnCompleteListener { task ->
                    if(!task.isSuccessful){
                        Toast.makeText(this@SignupActivity, "Signup error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, SignupActivity::class.java)
    }
}