package com.example.nint.mynote

import android.app.Application
import android.content.Context
import android.text.format.DateUtils
import com.example.nint.mynote.model.ItemRecyclerView
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.Calendar
import java.text.SimpleDateFormat

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
        fun dateToStr(context: Context, date: Long) = DateUtils.formatDateTime(
            context, date,
            DateUtils.FORMAT_NUMERIC_DATE or DateUtils.FORMAT_SHOW_YEAR
        )

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