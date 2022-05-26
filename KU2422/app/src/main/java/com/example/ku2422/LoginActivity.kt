package com.example.ku2422

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ku2422.databinding.ActivityLoginBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient

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
            showUserInfo()
            //TODO:: user정보 insert 해줘야함
            startActivity(intent)
        }
    }

    lateinit var binding: ActivityLoginBinding

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

    private fun showUserInfo(){
        UserApiClient.instance.me{ user,error->
            Toast.makeText(this@LoginActivity, "${user?.id}", Toast.LENGTH_SHORT).show()
            user?.properties?.entries?.forEach {
                Log.d(TAG,"showUserInfo : ${it.key} ${it.value}")
                Log.d(TAG,"showUserID : ${user?.id.toString()} ")
            }
        }
    }
}