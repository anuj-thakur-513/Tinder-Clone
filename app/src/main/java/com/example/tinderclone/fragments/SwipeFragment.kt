package com.example.tinderclone.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.tinderclone.R
import com.example.tinderclone.activities.TinderCallback
import com.example.tinderclone.activities.UserInfoActivity
import com.example.tinderclone.adapters.CardsAdapter
import com.example.tinderclone.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.lorentzos.flingswipe.SwipeFlingAdapterView
import kotlinx.android.synthetic.main.fragment_swipe.*

class SwipeFragment : Fragment() {

    // variable for handling methods of TinderCallback interface
    private var callback: TinderCallback? = null

    // variables to hold user id and to get the database reference
    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference

    // variables to fill the card layout using adapter
    private var cardsAdapter: ArrayAdapter<User>? = null
    private var rowItems = ArrayList<User>()

    // variable to hold preferred gender of the user
    private var preferredGender: String? = null


    fun setCallback(callback: TinderCallback) {
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabse()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_swipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userDatabase.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // getting the user
                val user = snapshot.getValue(User::class.java)
                preferredGender = user?.preferredGender
                populateItems()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        // initialize the card adapter
        cardsAdapter = CardsAdapter(context, R.layout.item, rowItems)
        // add the adapter to the layout
        frame.adapter = cardsAdapter
        // set the fling listener for the layout
        frame.setFlingListener(object : SwipeFlingAdapterView.onFlingListener {
            override fun removeFirstObjectInAdapter() {
                rowItems.removeAt(0)
                cardsAdapter?.notifyDataSetChanged()
            }

            override fun onLeftCardExit(p0: Any?) {
                // added the value of left swiped user id in the current user's database's SWIPE_LEFT as true
                val user = p0 as User
                userDatabase.child(user.uid.toString()).child(DATA_SWIPES_LEFT).child(userId)
                    .setValue(true)
            }

            override fun onRightCardExit(p0: Any?) {
                // added the value of left swiped user id in the current user's database's SWIPE_RIGHT as true
                // also check if both users have swiped right then we add the data in MATCHES instead of SWIPE_RIGHT
                val selectedUser = p0 as User
                val selectedUserId = selectedUser.uid
                if (!selectedUserId.isNullOrEmpty()) {
                    userDatabase.child(userId).child(DATA_SWIPES_RIGHT)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.hasChild(selectedUserId)){
                                    Toast.makeText(context, "Match!", Toast.LENGTH_SHORT).show()

                                    userDatabase.child(userId).child(DATA_SWIPES_RIGHT).child(selectedUserId).removeValue()
                                    userDatabase.child(userId).child(DATA_MATCHES).child(selectedUserId).setValue(true)
                                    userDatabase.child(selectedUserId).child(DATA_MATCHES).child(userId).setValue(true)
                                } else {
                                    userDatabase.child(selectedUserId).child(DATA_SWIPES_RIGHT).child(userId).setValue(true)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                }
            }

            override fun onAdapterAboutToEmpty(p0: Int) {
            }

            override fun onScroll(p0: Float) {
            }

        })

        frame.setOnItemClickListener{ position, data ->
            val currentUser = (cardsAdapter as CardsAdapter).getItem(position)
            startActivity(UserInfoActivity.newIntent(context, currentUser?.uid))
        }

        // btn click listener for the like button
        btnLike.setOnClickListener {
            if(rowItems.isNotEmpty()){
                frame.topCardListener.selectRight()
            }
        }

        // btn click listener for the dislike button
        btnDislike.setOnClickListener {
            if(rowItems.isNotEmpty()){
                frame.topCardListener.selectLeft()
            }
        }
    }

    // function to populate the items based on preferred gender
    fun populateItems() {

        noUsersLayout.visibility = View.GONE
        progressLayout.visibility = View.VISIBLE

        // created a query variable which returns users based on preferred gender of logged in user
        val cardsQuery = userDatabase.orderByChild(DATA_GENDER).equalTo(preferredGender)
        cardsQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { child ->
                    // getting the value of users in a variable as we move through them in the for loop
                    val user = child.getValue(User::class.java)
                    // condition when user is found
                    if (user != null) {
                        // variable which decides whether to show the user or not
                        var showUser = true
                        // setting the condition when the user has been shown once
                        if (child.child(DATA_SWIPES_LEFT).hasChild(userId) ||
                            child.child(DATA_SWIPES_RIGHT).hasChild(userId) ||
                            child.child(DATA_MATCHES).hasChild(userId)
                        ) {
                            showUser = false
                        }
                        // adding the eligible users to the list and notifying the adapter about data changes
                        if (showUser) {
                            rowItems.add(user)
                            cardsAdapter?.notifyDataSetChanged()
                        }
                    }
                }
                progressLayout.visibility = View.GONE
                // if no users are available to show then showing the no user available screen
                if (rowItems.isEmpty()) {
                    noUsersLayout.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}