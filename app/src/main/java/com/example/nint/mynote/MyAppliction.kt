package com.example.nint.mynote

import android.app.Application
import android.content.Context
import android.text.format.DateUtils
import com.example.nint.mynote.model.ItemRecyclerView
import io.realm.Realm
import io.realm.RealmConfiguration
import java.text.SimpleDateFormat
import java.util.*

class MyAppliction: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    companion object {
        const val APP_PREFERENCES_SOUND_ENABLED = "SOUND_ENABLED"
        const val APP_PREFERENCES_VIBRATION_ENABLED = "VIBRATION_ENABLED"
        const val APP_PREFERENCES_TIME_NOTIFICATION = "TIME_NOTIFICATION"

        fun dateToStr(date: Long):String{
            val dateFormated = SimpleDateFormat("dd.MM.yyyy")
            return dateFormated.format(date)
        }

        fun timeToStr(time: Long):String {
            val dateFormated = SimpleDateFormat("HH:mm")
            return dateFormated.format(time)
        }

        fun strDateToLong(str:String):Long{
            val date: Date
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            date = formatter.parse(str) as Date
            return date.time
        }

        fun dayDate(date:Long):Int{
            var c = Calendar.getInstance()
            c.timeInMillis = date
            return c.get(Calendar.DAY_OF_MONTH)
        }

        fun monthDate(date:Long):Int{
            var c = Calendar.getInstance()
            c.timeInMillis = date
            return c.get(Calendar.MONTH)+1
        }

        fun diffDate(lessDate: Long, moreDate:Long):Int{
            var d1 = Calendar.getInstance()
            var d2 = Calendar.getInstance()
            d1.timeInMillis = lessDate
            d2.timeInMillis = moreDate
            var result = d2.get(Calendar.YEAR)-d1.get(Calendar.YEAR)
            if (d2.get(Calendar.MONTH) < d1.get(Calendar.MONTH))
                result--
            if (d2.get(Calendar.MONTH) == d1.get(Calendar.MONTH))
                if (d2.get(Calendar.DAY_OF_MONTH) < d1.get(Calendar.DAY_OF_MONTH))
                    result--
            return result
        }

    }
}