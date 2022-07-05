package com.example.travelguider.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager.LayoutParams.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.travelguider.R
import kotlinx.android.synthetic.main.activity_splash.*

class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            FLAG_FULLSCREEN,
            FLAG_FULLSCREEN
        )
        text_animate1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slidedown))
        text_animate2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup))
        text_animate3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slideup))

        Handler().postDelayed({
            startActivity(Intent(this, gettingStarted::class.java))
            finish()
        },3000
        )
    }
}