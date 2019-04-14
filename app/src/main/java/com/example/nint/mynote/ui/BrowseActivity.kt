package com.example.nint.mynote.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.nint.mynote.MyAppliction.Companion.dateToStr
import com.example.nint.mynote.MyAppliction.Companion.diffDate
import com.example.nint.mynote.R
import com.example.nint.mynote.model.RealmHelper
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_browse.*
import java.util.*

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
        tv_date.text = dateToStr(this, item?.date)
        tv_note.text = item?.note
        tv_age.text = diffDate(item.date,Calendar.getInstance().timeInMillis).toString()
        iv_avatar.setImageURI(Uri.parse(item.avatar))
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