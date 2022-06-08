package com.example.ku2422

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel:ViewModel() {
    var friendId =MutableLiveData<ArrayList<String>>()
    var friendInfo =MutableLiveData<User>()
    var friendInfoList = MutableLiveData<ArrayList<User>>()
    var friendUserInfoList = ArrayList<User>()

    fun getFriendId(userid :String){
        UserDB.getFriendList(userid!!) {
            friendId.value = it
            Log.e("viewmodel", "getFriendId: ${friendId.value}",)
        }
    }

    fun getFriendInfo(friendId : ArrayList<String>) = viewModelScope.launch {
        friendId.forEach {
             UserDB.getUserById(it){ user->
                 friendInfo.value = user
            }
        }
    }

}