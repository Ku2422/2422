package com.example.ku2422

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


object StoreDBRemote {
    val rdb:DatabaseReference = Firebase.database.getReference("StoreDB")
    fun searchDB(){
        //TODO::
    }

    fun insertDB(store : Store){
        rdb.child(store.userId).setValue(store)
    }
    fun deleteDB(){
        //TODO::
    }
    fun updateDB(){
        //TODO::
    }
}