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

class AddActivity: AppCompatActivity() {
    private lateinit var realm: Realm
    private lateinit var itemID:String
    private lateinit var item:Item


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        realm = Realm.getDefaultInstance()

        var intent = intent
        if (intent != null) {
            itemID = intent.getStringExtra("ID")


            item = RealmHelper.readToRealm(realm, itemID)!!
            et_name.setText(item?.name)
            et_date.setText(item?.date)
            et_note.setText(item?.note)
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

                RealmHelper.editDate(realm,itemID,et_date.text.toString())
                Toast.makeText(this,"Save",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(itemMenu)
    }
}