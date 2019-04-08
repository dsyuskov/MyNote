package com.example.nint.mynote.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.nint.mynote.R
import com.example.nint.mynote.model.Item
import com.example.nint.mynote.model.RealmHelper
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*

class AddActivity: AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var itemID:String
    private lateinit var item:Item
    private var isNew = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        realm = Realm.getDefaultInstance()

        var intent = intent
        itemID = intent.getStringExtra("ID")

        if (!itemID.equals("NEW")) {
            item = RealmHelper.readToRealm(realm, itemID)!!
            et_name.setText(item?.name)
            et_date.setText(item?.date)
            et_note.setText(item?.note)
        }else{
            isNew = true
            itemID = UUID.randomUUID().toString()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add_activity,menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(itemMenu: MenuItem?): Boolean {
        val id = itemMenu?.itemId
        when(id){
            R.id.action_save ->{
                var item = Item()
                item.id = itemID
                item.date = et_date.text.toString()
                item.name = et_name.text.toString()
                item.note = et_note.text.toString()
                if (isNew){
                    RealmHelper.saveToRealm(realm,item)
                }else{
                    RealmHelper.editToRealm(realm,item)
                }

                Toast.makeText(this,"Save",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
        return super.onOptionsItemSelected(itemMenu)
    }
}