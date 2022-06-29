package com.example.tinderclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.tinderclone.R
import com.example.tinderclone.util.User

class CardsAdapter(context: Context, resourceId: Int, users: List<User>): ArrayAdapter<User>(context, resourceId, users) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // get the current user
        var user = getItem(position)
        // get the layout if any or inflate the layout
        var finalView = convertView?:LayoutInflater.from(context).inflate(R.layout.item, parent, false)

        // get the ids of the fields
        var name = finalView.findViewById<TextView>(R.id.nameTV)
        var image = finalView.findViewById<ImageView>(R.id.imageViewPhoto)

        // add content in the layout
        name.text = "${user?.name}, ${user?.age}"
        Glide.with(context)
            .load(user?.imageUrl)
            .into(image)

        return finalView
    }
}