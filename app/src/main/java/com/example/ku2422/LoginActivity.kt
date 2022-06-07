package com.example.ku2422

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ku2422.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {


    companion object {
        const val TAG = "LoginActivity"
    }

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            var intent = Intent(this,MainActivity::class.java)
            insertUserInfo()
            startActivity(intent)
        }
    }

    lateinit var binding: ActivityLoginBinding
    val userRdb: DatabaseReference = Firebase.database.getReference("UserDB")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        login()
    }



    private fun login(){
        binding.run{
            loginBtn.setOnClickListener {
                if(UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)){
                    UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity, callback = callback)
                }else {
                    UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
                }
            }
        }
    }

    private fun insertUserInfo(){
        var user_id = ""
        UserApiClient.instance.me{ user,error->
            user_id = user?.id.toString()
            setSharedPreference(user_id)
            user?.properties?.entries?.forEach {


                userRdb.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "onCancelled: insertUserInfo", )
                    }
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var flag =false
                        for (data in snapshot.children) {
                            val curUser = data.getValue(User::class.java)
                            curUser?.let {userit ->
                                if (userit.userId == user?.id.toString()) {
                                    flag = true
                                }
                            }
                        }
                        if(!flag){
                            val userinfo = User(user?.id.toString(),it.value)
                            userRdb.child(user?.id.toString()).setValue(userinfo)
                        }
                    }
                })
            }
        }
    }

    private fun setSharedPreference(id: String){

        GlobalApplication.getInstance().run {
            putKeyValue("userId", id)
        }
    }
}