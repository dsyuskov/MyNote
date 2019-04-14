package com.example.nint.mynote.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.nint.mynote.MyAppliction
import com.example.nint.mynote.R
import com.example.nint.mynote.ui.BrowseActivity
import java.util.*
import kotlin.collections.ArrayList

class RecyclerViewAdapter(val context:Context,val list: ArrayList<ItemRecyclerView>): androidx.recyclerview.widget.RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_note,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item = list[position]
        holder.date.text = MyAppliction.dateToStr(context,item.date)
        holder.name.text = item?.name
        holder.age.text = MyAppliction.diffDate(item.date, Calendar.getInstance().timeInMillis).toString()
        holder.avatar.setImageURI(Uri.parse(item?.avatar))
        holder.itemView.setOnClickListener {
            var intent = Intent(context,BrowseActivity::class.java)
            intent.putExtra("ID",item?.id)
            context.startActivity(intent)
        }
    }
}