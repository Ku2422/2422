package com.example.ku2422

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.ku2422.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MainActivity : AppCompatActivity(){
    var lat : Float = 0f
    var lng : Float = 0f
    var check = false
    companion object{
        const val TAG = "MainActivity"
    }
    lateinit var binding: ActivityMainBinding

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

                when(it.itemId){
                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.mypage ->replaceFragment(MypageFragment())
                    else -> replaceFragment(SocialFragment())
                }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        var bundle = Bundle()
        bundle.putFloat("lat",lat)
        bundle.putFloat("lng",lng)
        bundle.putBoolean("check",check)
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction().replace(fl.id, fragment).commit()
    }

}