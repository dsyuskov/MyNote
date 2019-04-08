package com.example.nint.mynote.model

import io.realm.Realm
import io.realm.kotlin.where

object RealmHelper {

    fun saveToRealm(realm: Realm,item: Item){
        realm.executeTransaction{
            it.copyToRealm(item)
        }
    }

    fun editToRealm(realm: Realm,itemNew: Item){
        realm.executeTransaction{
            var item = realm.where<Item>().equalTo("id",itemNew.id).findFirst()
            item?.date = itemNew.date
            item?.name = itemNew.name
            item?.note = itemNew.note
        }

    }

    fun readToRealm(realm: Realm) = realm.where<Item>().findAll()

    fun readToRealm(realm: Realm,id: String) = realm.where<Item>().equalTo("id",id).findFirst()

    fun removeToRealm(realm: Realm,id: String){
        realm.executeTransaction{
            realm.where<Item>().equalTo("id",id).findAll().deleteAllFromRealm()
        }
    }

}