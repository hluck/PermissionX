package com.permissionx.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {

    companion object{
        const val SPLASH_DISPLAY_LENGTH = 3000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.myLooper()!!).postDelayed({
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }, SPLASH_DISPLAY_LENGTH)
    }
}