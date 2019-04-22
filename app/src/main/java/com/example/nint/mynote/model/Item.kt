package com.example.nint.mynote.model


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class Item(): RealmObject() {

    @PrimaryKey
    @Required
    var id:String = ""
    var name:String = ""
    var date:Long = 0
    var note:String = ""
    var nameInsensitive:String = ""
    var avatar:String = "@mipmap/avatar"

    constructor(id:String,name:String,date:Long,nameInsensitive:String):this(){
        this.id = id
        this.name = name
        this.date = date
        this.nameInsensitive = nameInsensitive

    }


}