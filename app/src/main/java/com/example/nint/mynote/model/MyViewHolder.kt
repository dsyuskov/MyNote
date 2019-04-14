package com.example.nint.mynote.model

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.nint.mynote.R

class MyViewHolder(itemView:View): androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    var date = itemView.findViewById<TextView>(R.id.tv_date)
    var name = itemView.findViewById<TextView>(R.id.tv_name)
    var age = itemView.findViewById<TextView>(R.id.tv_age)
    var avatar = itemView.findViewById<ImageView>(R.id.iv_avatar)
}