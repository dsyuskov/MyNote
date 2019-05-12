package com.example.nint.mynote.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.nint.mynote.MyAppliction.Companion.dateToStr
import com.example.nint.mynote.MyAppliction.Companion.strDateToLong
import com.example.nint.mynote.R
import com.example.nint.mynote.model.Item
import com.example.nint.mynote.model.RealmHelper
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_add.*
import java.util.*




class AddActivity: AppCompatActivity() {

    private lateinit var realm: Realm
    private lateinit var itemID:String
    private lateinit var item:Item
    private var isNew = false
    private val dateBorn = Calendar.getInstance()
    private var avatarURI:String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        iv_avatar.setOnClickListener {
           CropImage.activity(null)//Uri.parse("/img/$itemID.jpg")
               .setOutputUri(getImagePath(itemID))
               .setCropShape(CropImageView.CropShape.OVAL)
               .setFixAspectRatio(true)
               .setGuidelines(CropImageView.Guidelines.ON)
               .start(this)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        realm = Realm.getDefaultInstance()

        var intent = intent
        itemID = intent.getStringExtra("ID")

        if (!itemID.equals("NEW")) {
            supportActionBar?.title = "Редактировать именниника"
            item = RealmHelper.readToRealm(realm, itemID)
            avatarURI = item.avatar
            dateBorn.timeInMillis = item?.date
            et_name.setText(item?.name)
            et_date.setText(dateToStr(dateBorn.timeInMillis))
            et_note.setText(item?.note)
            if (avatarURI == "")
                iv_avatar.setImageResource(R.mipmap.avatar)
            else
                iv_avatar.setImageURI(Uri.parse(avatarURI))
        }else{
            supportActionBar?.title = "Добавить именниника"
            isNew = true
            itemID = UUID.randomUUID().toString()
        }

        et_date.setOnLongClickListener{
                setDateBorn()
            true
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                iv_avatar.setImageURI(result.uri)
                avatarURI = result.uri.toString()
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
                    item = Item()
                    item.id = itemID
                    item.date = strDateToLong(et_date.text.toString())
                    item.name = et_name.text.toString()
                    item.note = et_note.text.toString()
                    item.nameInsensitive = item.name.toUpperCase()
                    item.avatar = avatarURI

                    if (isNew){
                        RealmHelper.saveToRealm(realm,item)
                        Log.d("MYTAG","save to realm")
                    }else{
                        RealmHelper.editToRealm(realm,item)
                        Log.d("MYTAG","edit to realm")
                    }
                    Toast.makeText(this,getString(R.string.save),Toast.LENGTH_SHORT).show()
                    finish()
                }catch (e:Exception){
                    Toast.makeText(this,getString(R.string.save_error),Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(itemMenu)
    }


    fun getImagePath(id:String):Uri{
        var dir = this.getDir(Environment.DIRECTORY_PICTURES, Context.MODE_PRIVATE)
        return Uri.parse("file://"+dir.path+"/"+id+".jpg")
    }


    private fun setDateBorn() {
        var date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateBorn.set(Calendar.YEAR, year)
            dateBorn.set(Calendar.MONTH, monthOfYear)
            dateBorn.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            et_date.setText(dateToStr(dateBorn.timeInMillis))
        }
        DatePickerDialog(
            this, date ,
            dateBorn.get(Calendar.YEAR),
            dateBorn.get(Calendar.MONTH),
            dateBorn.get(Calendar.DAY_OF_MONTH)
        ).show()
    }



}