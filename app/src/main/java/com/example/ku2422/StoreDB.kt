package com.example.ku2422

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object StoreDB {
    val storeRdb: DatabaseReference = Firebase.database.getReference("StoreDB")

    //가게 정보 가져오기
    fun getStoreById(id: String,completion: (listOfStore : ArrayList<Store>) -> Unit) {
        var storeList:ArrayList<Store> = arrayListOf()
        val getStoreRdb =storeRdb.child(id)
        getStoreRdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("storeDB", "onCancelled : getStoreById")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {
                    storeList.add(Store(
                        data.child("userId").value.toString(),
                        data.child("userImg").value.toString(),
                        data.child("userName").value.toString(),
                        data.child("storeName").value.toString(),
                        data.child("menu").value.toString(),
                        data.child("price").value.toString().toInt(),
                        data.child("review").value.toString(),
                        data.child("star").value.toString().toDouble(),
                        data.child("date").value.toString(),
                        data.child("locationX").value.toString().toFloat(),
                        data.child("locationY").value.toString().toFloat()
                    ))
                }

                completion(storeList)
            }
        })
    }


    //가게 정보 저장
    fun insertStore(store : Store, completion: (flag: Boolean) -> Unit){
        var storecount = 0
        val getStoreRdb =storeRdb.child(store.userId)
        getStoreRdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("storeDB", "onCancelled : insertStore ")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                //성공시
                for (data in snapshot.children) {
                    storecount = storecount+1
                }
                storeRdb.child(store.userId).child(storecount.toString()).setValue(store).addOnCompleteListener {
                    completion(true)
                }.addOnFailureListener {
                    completion(false)
                }
                return
            }
        })
    }

    //가게 정보 삭제
    fun deleteStore(userId : String,completion: (flag: Boolean) -> Unit){
        storeRdb.child(userId).setValue(null).addOnCompleteListener {
            completion(true)
        }.addOnFailureListener {
            completion(false)
        }
    }
}