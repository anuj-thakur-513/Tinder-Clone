package com.example.tinderclone.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tinderclone.R

class StartupActivity : AppCompatActivity() {
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
}