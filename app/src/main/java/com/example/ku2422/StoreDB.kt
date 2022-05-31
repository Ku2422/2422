
package com.example.ku2422

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

object StoreDB {
    val storeRdb: DatabaseReference = Firebase.database.getReference("StoreDB")

    //가게 정보 가져오기
    fun getStoreById(id: String,completion: (store : Store) -> Unit) {

        val getStoreRdb =storeRdb.child(id)
        getStoreRdb.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //실패는 없습니다.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                //성공시
                for (data in snapshot.children) {
                    val curStore = data.getValue(Store::class.java)
                    curStore?.let {
                        completion(it)
                    }
                }
            }
        })
    }


    //가게 정보 저장
    fun insertStore(store : Store, completion: (flag: Boolean) -> Unit){
        var storecount = 0
        val getStoreRdb =storeRdb.child(store.userId)
        getStoreRdb.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //실패는 없습니다.
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
        storeRdb.child(userId).setValue("").addOnCompleteListener {
            completion(true)
        }.addOnFailureListener {
            completion(false)
        }
    }
}