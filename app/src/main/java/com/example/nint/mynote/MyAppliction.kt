package com.example.nint.mynote

import android.app.Application
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.example.nint.mynote.ui.MainActivity
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

        fun notifi(context: Context,title:String,message:String,icon:String){
            var builder = NotificationCompat.Builder(context)
                //.setSmallIcon()

                .setContentTitle(title)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_VIBRATE)
            var intent = Intent(context, MainActivity::class.java)
            var stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(MainActivity::class.java)
            stackBuilder.addNextIntent(intent)
            var resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentIntent(resultPendingIntent)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(Random(10).nextInt(),builder.build())
        }

    }
}