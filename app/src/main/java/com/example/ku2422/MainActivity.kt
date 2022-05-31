package com.example.ku2422

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.example.ku2422.databinding.ActivityLoginBinding
import com.example.ku2422.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.User
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity(){
    companion object{
        const val TAG = "MainActivity"
    }
    lateinit var binding: ActivityMainBinding
    val mainViewModel: MainViewModel by viewModels()

    private val fl:FrameLayout by lazy{
        binding.frame
    }
    private val bn:NavigationBarView by lazy{
        binding.bottomMenu
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        inNavigation()

    }



    private fun inNavigation() {

        supportFragmentManager.beginTransaction().add(fl.id, HomeFragment()).commit()


        bn.setOnItemSelectedListener {
            replaceFragment(
                when(it.itemId){
                    R.id.home -> HomeFragment()
                    R.id.mypage -> MypageFragment()
                    else -> SocialFragment()
                }
            )
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(fl.id, fragment).commit()
    }

}