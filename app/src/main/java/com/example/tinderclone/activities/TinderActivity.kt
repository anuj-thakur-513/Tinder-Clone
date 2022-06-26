package com.example.tinderclone.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.tinderclone.R
import com.example.tinderclone.fragments.MatchesFragment
import com.example.tinderclone.fragments.ProfileFragment
import com.example.tinderclone.fragments.SwipeFragment
import com.example.tinderclone.util.DATA_USERS
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.view.View
import com.google.firebase.ktx.Firebase
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.activity_tinder.*

class TinderActivity : AppCompatActivity(), TinderCallback {

    // creating variables for firebase authorization
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userId = firebaseAuth.currentUser?.uid
    // database reference
    private lateinit var userDatabase: DatabaseReference

    // creating variables for the fragments
    private var profileFragment: ProfileFragment? = null
    private var swipeFragment: SwipeFragment? = null
    private var matchesFragment: MatchesFragment? = null

    // creating variables for the tab layout
    private var profileTab: TabLayout.Tab? = null
    private var swipeTab: TabLayout.Tab? = null
    private var matchesTab: TabLayout.Tab? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tinder)

        // checking if user id is null or empty to signout
        if(userId.isNullOrEmpty()){
            onSignout()
        }

        // getting database reference in the variable
        userDatabase = FirebaseDatabase.getInstance().reference.child(DATA_USERS)

        // dynamically creating new tabs in the tab layout
        profileTab = navigationTabs.newTab()
        swipeTab = navigationTabs.newTab()
        matchesTab = navigationTabs.newTab()
        // setting the icons of the tabs
        profileTab?.icon = ContextCompat.getDrawable(this, R.drawable.profile)
        swipeTab?.icon = ContextCompat.getDrawable(this, R.drawable.swipe)
        matchesTab?.icon = ContextCompat.getDrawable(this, R.drawable.matches)
        // adding the tabs to the tab layout
        navigationTabs.addTab(profileTab!!)
        navigationTabs.addTab(swipeTab!!)
        navigationTabs.addTab(matchesTab!!)

        // adding tab selected listener to the tab layout
        navigationTabs.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                onTabSelected(tab)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                when(tab){
                    profileTab -> {
                        if(profileFragment == null){
                            profileFragment = ProfileFragment()
                            profileFragment!!.setCallback(this@TinderActivity)
                        }
                        replaceFragment(profileFragment!!)
                    }
                    swipeTab ->{
                        if(swipeFragment == null){
                            swipeFragment = SwipeFragment()
                            swipeFragment!!.setCallback(this@TinderActivity)
                        }
                        replaceFragment(swipeFragment!!)
                    }
                    matchesTab ->{
                        if(matchesFragment == null){
                            matchesFragment = MatchesFragment()
                            matchesFragment!!.setCallback(this@TinderActivity)
                        }
                        replaceFragment(matchesFragment!!)
                    }
                }
            }
        })
        profileTab?.select()
    }

    fun replaceFragment (fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }


    override fun onSignout() {
        firebaseAuth.signOut()
        startActivity(StartupActivity.newIntent(this@TinderActivity))
        finish()
    }

    override fun onGetUserId(): String = userId.toString()

    override fun getUserDatabse(): DatabaseReference = userDatabase

    companion object{
        fun newIntent(context: Context) = Intent(context, TinderActivity::class.java)
    }
}