package com.example.ku2422

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, this.getString(R.string.kakao_native_app_key))
    }

}