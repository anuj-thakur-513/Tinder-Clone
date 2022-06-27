package com.example.tinderclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.example.tinderclone.R
import com.example.tinderclone.util.User
import com.example.tinderclone.util.DATA_USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*
import java.lang.Exception

class SignupActivity : AppCompatActivity() {

    // getting the reference of the firebase database
    private val firebaseDatabase = FirebaseDatabase.getInstance().reference
    // creating an instance of firebase auth for authorization
    private val firebaseAuth = FirebaseAuth.getInstance();
    // creating an authentication listener which transitions the activity on login
    private val firebaseAuthListener = FirebaseAuth.AuthStateListener {
        val user = firebaseAuth.currentUser
        if(user != null){
            startActivity(TinderActivity.newIntent(this@SignupActivity))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        progressBarSignup.visibility = View.GONE
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
        // Hiding the keyboard on button press
        try {
            edtEmail.onEditorAction(EditorInfo.IME_ACTION_DONE)
            edtPassword.onEditorAction(EditorInfo.IME_ACTION_DONE)
        } catch (e: Exception){
            Log.e("keyboardDown", e.toString())
        }
        // signing up the user and adding a listener in order to handle signup exceptions\
        if(edtEmail.text.toString().isNotEmpty() && edtPassword.text.toString().isNotEmpty()){
            progressBarSignup.visibility = View.VISIBLE
            firebaseAuth.createUserWithEmailAndPassword(edtEmail.text.toString(), edtPassword.text.toString())
                .addOnCompleteListener { task ->
                    if(!task.isSuccessful){
                        Toast.makeText(this@SignupActivity, "Signup error: ${task.exception?.localizedMessage}", Toast.LENGTH_SHORT).show()
                        progressBarSignup.visibility = View.GONE
                    } else {
                        // getting the data of the user in order to store it in the database
                        val email = edtEmail.text.toString()
                        val userId = firebaseAuth.currentUser?.uid?: ""
                        val user = User(userId, "", "", email, "", "")
                        // adding data in the firebase database by creating the children
                        firebaseDatabase.child(DATA_USERS).child(userId).setValue(user)
                        progressBarSignup.visibility = View.GONE
                    }
                }
        }
    }

    companion object{
        fun newIntent(context: Context) = Intent(context, SignupActivity::class.java)
    }
}