package com.example.nint.mynote.model

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.nint.mynote.R
import com.example.nint.mynote.ui.BrowseActivity
import io.realm.RealmResults

class RecyclerViewAdapter(val context:Context,val list: RealmResults<Item>): RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_note,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item = list[position]
        holder.date.text = item?.date
        holder.name.text = item?.name
        holder.itemView.setOnClickListener {
            var intent = Intent(context,BrowseActivity::class.java)
            intent.putExtra("ID",item?.id)
            context.startActivity(intent)

        }

    }
}