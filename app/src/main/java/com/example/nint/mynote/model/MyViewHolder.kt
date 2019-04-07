package com.example.nint.mynote.model

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.nint.mynote.R

class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
    var date = itemView.findViewById<TextView>(R.id.tv_date)
    var name = itemView.findViewById<TextView>(R.id.tv_name)
}