package com.example.nint.mynote.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.nint.mynote.R
import kotlinx.android.synthetic.main.activity_settings.*
import android.content.Context
import android.content.SharedPreferences
import com.example.nint.mynote.MyAppliction.Companion.APP_PREFERENCES_SOUND_ENABLED
import com.example.nint.mynote.MyAppliction.Companion.APP_PREFERENCES_TIME_NOTIFICATION
import com.example.nint.mynote.MyAppliction.Companion.APP_PREFERENCES_VIBRATION_ENABLED
import com.example.nint.mynote.MyAppliction.Companion.dateToStr
import java.util.*
import com.example.nint.mynote.MyAppliction.Companion.timeToStr

class SettingsActivity:AppCompatActivity(){


    private var switchSound = false
    private var switchVibration = false

    private var timeNotification = Calendar.getInstance()
    lateinit var prefs:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = getString(R.string.settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)

        loadSettings()

        switch_sound.setOnCheckedChangeListener { _, isChecked ->
            switchSound = if (isChecked){
                Toast.makeText(this,R.string.sound_enabled,Toast.LENGTH_SHORT).show()
                true
            }else{
                Toast.makeText(this,R.string.sound_disabled,Toast.LENGTH_SHORT).show()
                false
            }
        }

        switch_vibration.setOnCheckedChangeListener{_,isChecked ->
            switchVibration = if (isChecked){
                Toast.makeText(this,R.string.vibration_enabled,Toast.LENGTH_SHORT).show()
                true
            }else{
                Toast.makeText(this,R.string.vibration_disabled,Toast.LENGTH_SHORT).show()
                false
            }
        }
        time_notification.setOnClickListener{
            setTimeNotification()
        }
    }


    fun setTimeNotification(){
        var time = TimePickerDialog.OnTimeSetListener{_,hour,minute->
            timeNotification.set(Calendar.HOUR_OF_DAY,hour)
            timeNotification.set(Calendar.MINUTE,minute)
            tv_time_notification.text = timeToStr(timeNotification.timeInMillis)
        }

        TimePickerDialog(this,
            time,
            timeNotification.get(Calendar.HOUR_OF_DAY),
            timeNotification.get(Calendar.MINUTE),
            true
        ).show()


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun saveSettings(){
        var edit = prefs.edit()
        edit.putBoolean(APP_PREFERENCES_SOUND_ENABLED,switchSound)
        edit.putBoolean(APP_PREFERENCES_VIBRATION_ENABLED,switchVibration)
        edit.putLong(APP_PREFERENCES_TIME_NOTIFICATION,timeNotification.timeInMillis)
        edit.commit()
    }

    private fun loadSettings(){
        switchSound = prefs.getBoolean(APP_PREFERENCES_SOUND_ENABLED,false)
        switchVibration = prefs.getBoolean(APP_PREFERENCES_VIBRATION_ENABLED,false)
        timeNotification.timeInMillis =prefs.getLong(APP_PREFERENCES_TIME_NOTIFICATION,0)
        switch_sound.isChecked = switchSound
        switch_vibration.isChecked = switchVibration
        tv_time_notification.text = timeToStr(timeNotification.timeInMillis)
    }

    override fun onDestroy() {
        saveSettings()
        super.onDestroy()
    }
}


