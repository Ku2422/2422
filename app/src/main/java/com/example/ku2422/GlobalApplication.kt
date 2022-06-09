package com.example.ku2422

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication: Application() {

    companion object{
        private lateinit var sharedPref: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private var instance: GlobalApplication? = null
        @Synchronized fun getInstance(): GlobalApplication
                = instance?:GlobalApplication().also { instance = it }

    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, this.getString(R.string.kakao_native_app_key))

        init()
    }

    private fun init(){
        sharedPref = this.getSharedPreferences(this.resources.getString(R.string.shared_pref_name), Context.MODE_PRIVATE)
        editor = sharedPref.edit()

    }

    fun putKeyValue(key: String , value: Any){
        with(editor){
            when(value){
                is String -> putString(key, value.toString())
                is Boolean -> putBoolean(key, value.toString().toBoolean())
            }
            commit()
        }
    }

    fun getValue(key:String):String?{
        Log.i("ta", "getValue: ${sharedPref.getString(key,"k")}")
        return sharedPref.getString(key,"k")
    }

    fun deleteKey(key: String){
        with(editor){
            remove(key)
            commit()
        }
    }

}