package com.example.tinderclone.fragments

import android.net.wifi.hotspot2.pps.Credential
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinderclone.R
import com.example.tinderclone.activities.TinderCallback
import com.example.tinderclone.adapters.ChatsAdapter
import com.example.tinderclone.util.Chat
import com.example.tinderclone.util.DATA_MATCHES
import com.example.tinderclone.util.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_matches.*

class MatchesFragment : Fragment() {
    // variable for handling methods of TinderCallback interface
    private var callback: TinderCallback? = null

    // variables to store userId and to get the database reference
    private lateinit var userId: String
    private lateinit var userDatabase: DatabaseReference
    // a chat database reference so whenever a new match is made we can either create a new chat database
    // or add the new chats to the pre existing database
    private lateinit var chatDatabase: DatabaseReference

    // variable for adapter
    private val chatsAdapter = ChatsAdapter(ArrayList())

    fun setCallback(callback: TinderCallback){
        this.callback = callback
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabase()
        chatDatabase = callback.getChatDatabase()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchData()

        // setting up the recycler view of the layout
        matchesRV.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = chatsAdapter
        }
    }

    // function to fetch data and display it
    fun fetchData(){

        progressBarMatches.visibility = View.VISIBLE

        // checking for the matches of the user
        userDatabase.child(userId).child(DATA_MATCHES).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // checking if matches child has values or not
                if (snapshot.hasChildren()){
                    snapshot.children.forEach { child ->
                        // getting the key/id of the source location of the children one by one
                        val matchId = child.key
                        Log.d("KEYFORTEST", matchId.toString())
                        // setting the chatId by getting the user id of the matches section
                        val chatId = child.value.toString()
                        Log.d("KEYFORTEST2", chatId)

                        // checking for the matchId
                        if (matchId?.isNotEmpty()!!){
                            userDatabase.child(matchId).addListenerForSingleValueEvent(object : ValueEventListener{

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user = snapshot.getValue(User::class.java)
                                    if (user != null){
                                        val chat = Chat(userId, chatId, user.uid, user.name, user.imageUrl)
                                        chatsAdapter.addElement(chat)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }

                            })
                        }
                    }
                }
                progressBarMatches.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}