package com.example.nint.mynote.model

import com.example.nint.mynote.MyAppliction
import io.realm.Case
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlin.Comparator
import kotlin.collections.ArrayList
import java.util.Calendar

object RealmHelper {

    var position: Int = 0
    fun saveToRealm(realm: Realm, item: Item) {
        realm.executeTransactionAsync {
            it.copyToRealm(item)
        }
    }

    fun editToRealm(realm: Realm, itemNew: Item) {
        realm.executeTransaction {
            var item = realm.where<Item>().equalTo("id", itemNew.id).findFirst()
            item?.date = itemNew.date
            item?.name = itemNew.name
            item?.note = itemNew.note
            item?.avatar = itemNew.avatar
        }
    }

    //fun readToRealm(realm: Realm) = realm.where<Item>().findAll()

    fun readToRealm(realm: Realm): ArrayList<ItemRecyclerView> {
        var result = realmToArray(realm.where<Item>().findAll())

        result.sortWith(Comparator { a, b ->
            when {
                a.month != b.month -> a.month - b.month
                else -> a.day - b.day
            }
        })

        position = getPosition(result)
        return result
    }

    fun readToRealm(realm: Realm,fieldName:String, name: String): ArrayList<ItemRecyclerView> {
        var result = realmToArray(realm.where<Item>().contains(fieldName, name.toUpperCase()).findAll())

        result.sortWith(Comparator { a, b ->
            when {
                a.month != b.month -> a.month - b.month
                else -> a.day - b.day
            }
        })

        position = getPosition(result)
        return result
    }


    fun readToRealm(realm: Realm, id: String) = realm.where<Item>().equalTo("id", id).findFirst()!!

    fun removeToRealm(realm: Realm, id: String) {
        realm.executeTransaction {
            realm.where<Item>().equalTo("id", id).findAll().deleteAllFromRealm()
        }
    }

    private fun realmToArray(realmList:RealmResults<Item>):ArrayList<ItemRecyclerView>{
        var result = ArrayList<ItemRecyclerView>()
        for (item in realmList) {
            var itemRecyclerView = ItemRecyclerView(
                item.id,
                item.name,
                item.date,
                MyAppliction.dayDate(item.date),
                MyAppliction.monthDate(item.date),
                item.avatar
            )
            result.add(itemRecyclerView)
        }
        return result
    }

    private fun getPosition(list: ArrayList<ItemRecyclerView>): Int {
        var pos: Int = 0
        for (item in list) {
            if (item.month < MyAppliction.monthDate(Calendar.getInstance().timeInMillis))
                pos++
            else if (item.month == MyAppliction.monthDate(Calendar.getInstance().timeInMillis)) {
                if (item.day < MyAppliction.dayDate(Calendar.getInstance().timeInMillis))
                    pos++
            } else
                break
        }
        return pos
    }

}