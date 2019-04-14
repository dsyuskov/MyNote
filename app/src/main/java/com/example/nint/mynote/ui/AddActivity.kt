package com.example.nint.mynote.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.nint.mynote.MyAppliction.Companion.dateToStr
import com.example.nint.mynote.R
import com.example.nint.mynote.model.Item
import com.example.nint.mynote.model.RealmHelper
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*
import java.text.SimpleDateFormat

class AddActivity: AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var itemID:String
    private lateinit var item:Item
    private var isNew = false
    private val date = Calendar.getInstance()
    private var avatarURI:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        iv_avatar.setOnClickListener {
           CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        realm = Realm.getDefaultInstance()

        var intent = intent
        itemID = intent.getStringExtra("ID")

        if (!itemID.equals("NEW")) {
            item = RealmHelper.readToRealm(realm, itemID)
            et_name.setText(item?.name)
            et_date.setText(dateToStr(this,item?.date))
            et_note.setText(item?.note)
            iv_avatar.setImageURI(Uri.parse(item.avatar))
        }else{
            isNew = true
            itemID = UUID.randomUUID().toString()
        }

        et_date.setOnLongClickListener{
                setDate(it)
            true
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                avatarURI = result.uri.toString()
                iv_avatar.setImageURI(result.uri)
                Toast.makeText(
                    this, "Cropping successful, Sample: " + result.sampleSize, Toast.LENGTH_LONG
                )
                    .show()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
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
                try {
                    var item = Item()
                    item.id = itemID
                    item.date = strdateToLong(et_date.text.toString())
                    item.name = et_name.text.toString()
                    item.note = et_note.text.toString()
                    item.nameInsensitive = item.name.toUpperCase()
                    item.avatar = avatarURI
                    if (isNew){
                        RealmHelper.saveToRealm(realm,item)
                    }else{
                        RealmHelper.editToRealm(realm,item)
                    }
                    Toast.makeText(this,"Save",Toast.LENGTH_SHORT).show()
                    finish()
                }catch (e:Exception){
                    Toast.makeText(this,"Save error",Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(itemMenu)
    }

    private fun setDate(v: View) {
        DatePickerDialog(
            this, d ,
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    var d = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        date.set(Calendar.YEAR, year)
        date.set(Calendar.MONTH, monthOfYear)
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        et_date.setText(dateToStr(this,date.timeInMillis))
    }

    private fun strdateToLong(str:String):Long{
        val date: Date
        val formatter = SimpleDateFormat("dd.MM.yyyy")
        date = formatter.parse(str) as Date
        return date.time

    }


}