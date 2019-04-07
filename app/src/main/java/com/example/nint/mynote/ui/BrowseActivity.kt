package com.example.nint.mynote.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.nint.mynote.R
import com.example.nint.mynote.model.RealmHelper
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_browse.*

class BrowseActivity:AppCompatActivity() {
    lateinit var itemID:String
    lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        realm = Realm.getDefaultInstance()

        var intent = intent
        itemID = intent.getStringExtra("ID")
        var item = RealmHelper.readToRealm(realm,itemID)
        tv_name.text = item?.name
        tv_date.text = item?.date
        tv_note.text = item?.note

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_browse_activity,menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId

        when(id){
            R.id.action_delete -> {
                RealmHelper.removeToRealm(realm,itemID)
                finish()
            }
            R.id.action_edit -> {
                val intent = Intent(this@BrowseActivity, AddActivity::class.java)
                intent.putExtra("ID",itemID)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        realm.close()
        super.onDestroy()
    }
}