package com.example.ku2422

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object UserDBRemote {
    val rdb: DatabaseReference = Firebase.database.getReference("UserDB")
    fun searchDB(){
        //TODO::
    }
    fun insertDB(){
        //TODO::
    }
    fun deleteDB(){
        //TODO::
    }
    fun updateDB(){
        //TODO::
    }
}