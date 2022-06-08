package com.example.ku2422

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {

    var friendIdListLiveData =MutableLiveData<ArrayList<String>>()
    var friendInfoLiveData =MutableLiveData<User>()
    var friendInfoListLiveData = MutableLiveData<ArrayList<User>>()
    var friendUserInfoList = ArrayList<User>()
    val selectedFragment= MutableLiveData(R.id.home)

    fun getFriendId(userid :String){
        UserDB.getFriendList(userid!!) {
            friendIdListLiveData.value = it
            Log.e("viewmodel", "getFriendId: ${friendIdListLiveData.value}",)
        }
    }

    fun getFriendInfo(friendId : ArrayList<String>) = viewModelScope.launch {
        friendId.forEach {
             UserDB.getUserById(it){ user->
                 friendInfoLiveData.value = user
            }
        }
    }

}