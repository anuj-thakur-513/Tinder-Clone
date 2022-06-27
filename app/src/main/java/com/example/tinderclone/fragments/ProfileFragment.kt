package com.example.tinderclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.tinderclone.R
import com.example.tinderclone.util.User
import com.example.tinderclone.activities.TinderCallback
import com.example.tinderclone.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    // variable to get current user id
    private lateinit var userId: String
    // variable to get user data from the databse
    private lateinit var userDatabase: DatabaseReference
    // variable for handling methods of TinderCallback interface
    private var callback: TinderCallback? = null

    fun setCallback(callback: TinderCallback){
        this.callback = callback
        // getting the data using the interface
        userId = callback.onGetUserId()
        userDatabase = callback.getUserDatabse().child(userId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // setting the touch listener true so that no other element can be touched
        progressLayout.setOnTouchListener { view, event -> true }

        populateInfo()
        btnApply.setOnClickListener { onApply() }
        btnSignout.setOnClickListener { callback?.onSignout() }
    }

    fun populateInfo(){
        progressLayout.visibility = View.VISIBLE
        userDatabase.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                // checking if the fragment is added or not
                if(isAdded){
                    // getting the user data
                    val user = snapshot.getValue(User::class.java)
                    edtName.setText(user?.name, TextView.BufferType.EDITABLE)
                    edtEmail.setText(user?.email, TextView.BufferType.EDITABLE)
                    edtAge.setText(user?.age, TextView.BufferType.EDITABLE)
                    if(user?.gender == GENDER_MALE){
                        radioMale1.isChecked = true
                    } else if(user?.gender == GENDER_FEMALE) radioFemale1.isChecked = true

                    if(user?.preferredGender == GENDER_MALE){
                        radioMale2.isChecked = true
                    } else if(user?.preferredGender == GENDER_FEMALE) radioFemale2.isChecked = true

                    progressLayout.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                progressLayout.visibility = View.GONE
            }

        })
    }

    fun onApply(){
        if(edtName.text.toString().isNullOrEmpty() || edtEmail.text.toString().isNullOrEmpty() ||
                edtAge.text.toString().isNullOrEmpty() || radioGroup1.checkedRadioButtonId == -1 ||
                radioGroup2.checkedRadioButtonId == -1){
            Toast.makeText(context, getString(R.string.error_incomplete_profile), Toast.LENGTH_SHORT).show()
        } else {
            val name = edtName.text.toString()
            val age = edtAge.text.toString()
            val email = edtEmail.text.toString()
            val gender =
                if(radioMale1.isChecked) GENDER_MALE
                else GENDER_FEMALE
            val preferredGender =
                if(radioMale2.isChecked) GENDER_MALE
                else GENDER_FEMALE

            userDatabase.child(DATA_NAME).setValue(name)
            userDatabase.child(DATA_AGE).setValue(age)
            userDatabase.child(DATA_EMAIL).setValue(email)
            userDatabase.child(DATA_GENDER).setValue(gender)
            userDatabase.child(DATA_GENDER_PREFERENCE).setValue(preferredGender)

            callback?.profileComplete()
        }
    }

}