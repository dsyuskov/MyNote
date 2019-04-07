package com.example.nint.mynote.model

import io.realm.Realm
import io.realm.kotlin.where

object RealmHelper {

    fun saveToRealm(realm: Realm,item: Item){
        realm.executeTransaction{
            it.copyToRealm(item)
        }
    }

    fun editDate(realm: Realm,id: String,date:String){
        realm.executeTransaction{
            var item = realm.where<Item>().equalTo("id",id).findFirst()
            item?.date = date
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