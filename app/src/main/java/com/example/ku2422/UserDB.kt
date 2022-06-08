package com.example.ku2422

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread

object UserDB {
    val userRdb: DatabaseReference = Firebase.database.getReference("UserDB")

    fun getUserById(id: String,completion:(user: User)->Unit) {
        userRdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("UserDB", "onCancelled : getUserById")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val curUser = data.getValue(User::class.java)
                    curUser?.let {
                        if (it.userId == id) {
                            completion(it)
                        }
                    }
                }
            }
        })
    }

    //친구 정보 가져오기기
    fun getFriendList(userId:String,complition: (listOfFriend: ArrayList<String>) -> Unit){
        var friendList : ArrayList<String> = arrayListOf()
        val friendRdb = userRdb.child(userId).child("friendId")
        friendRdb.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.e("UserDB", "onCancelled : getFriendList ", )
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    friendList.add(data.value.toString())
                }
                complition(friendList)
            }
        })
    }


    //친구추가
    fun adduserFriend(userId:String ,friendId: String,complition:(flag : Boolean)->Unit){
        var friendcount = 0
        val getFriendRdb = userRdb.child(userId).child("friendId")
        getFriendRdb.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.e("UserDB", "onCancelled: adduserFriend" )
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    friendcount = friendcount + 1
                }
                getFriendRdb.child(friendcount.toString()).setValue(friendId).addOnCompleteListener {
                    complition(true)
                }.addOnFailureListener {
                    complition(false)
                }
                return
            }


        })
    }

    fun deleteUser(userId: String,complition:(flag : Boolean)->Unit){
        userRdb.child(userId).setValue(null).addOnCompleteListener {
            complition(true)
        }.addOnFailureListener {
            complition(false)
        }
    }


    fun deleteFriend(userId:String , friendId:String,complition: (flag: Boolean) -> Unit){
        userRdb.child(userId).child("friendId").child(friendId).setValue(null).addOnCompleteListener {
            complition(true)
        }.addOnFailureListener {
            complition(false)
        }
    }
}