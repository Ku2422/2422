package com.example.ku2422

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainViewModel: ViewModel() {

    companion object{
        const val TAG= "MainViewModel"
    }

    val userRdb: DatabaseReference = Firebase.database.getReference("UserDB")
    val storeRdb: DatabaseReference = Firebase.database.getReference("StoreDB")
    val postUserById: MutableLiveData<User> = MutableLiveData() // getUserById 호출시 변경되는 LIVEDATA
    val postSoreById: MutableLiveData<Store> = MutableLiveData() // getStoreById 호출시 변경되는 LIVEDATA
    val insertFlag: MutableLiveData<Boolean> = MutableLiveData()

    //유저 정보 가져오기
    fun getUserById(id: String) {
        userRdb.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val curUser = data.getValue(User::class.java)
                    curUser?.let {
                        if (it.userId == id) {
                            postUserById.postValue(it)
                        }
                    }
                }
            }
        })
    }

    //친구추가
    fun adduserFriend(userId:String ,friendId: String):Boolean{
        var rtnbool = false
        userRdb.child(userId).child("friendId").setValue(friendId) .addOnCompleteListener {
            rtnbool = true
        }.addOnFailureListener {
            rtnbool = false
        }
        return rtnbool

    }

    //유저 정보 삭제
    fun deleteUser(userId: String){
        userRdb.child(userId).setValue("")
    }



    //가게 정보 가져오기
    fun getStoreById(id: String) {

        val getStoreRdb =storeRdb.child(id)
        getStoreRdb.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //실패시
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                //성공시
                for (data in snapshot.children) {
                    val curStore = data.getValue(Store::class.java)
                    curStore?.let {
                        postSoreById.postValue(it)
                    }
                }
            }
        })
    }


    //가게 정보 저장
    fun insertStore(store : Store, completion: (flag: Boolean) -> Unit){
        var storecount = 0
        var rtnbool = false
        val getStoreRdb =storeRdb.child(store.userId)
        getStoreRdb.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //실패시
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
            }
        })
    }

    //가게 정보 삭제
    fun deleteStore(userId : String){
        storeRdb.child(userId).setValue("")
    }

}