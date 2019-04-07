package com.example.nint.mynote.model


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Item: RealmObject() {

    @PrimaryKey
    @Required
    var id:String = ""
    var name:String = ""
    var date:String = ""
    var note:String = ""


}