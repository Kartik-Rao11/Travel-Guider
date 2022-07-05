package com.example.travelguider.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager.LayoutParams.*
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.travelguider.R
import com.example.travelguider.firebase.FirestoreCLass
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
            //auto login
            var currentUserId = FirestoreCLass().getCurrentUserId()
            if(currentUserId.isNotEmpty()){ // if the user is already registered then uid will not be empty
                startActivity(Intent(this,MainActivity::class.java))//we will start the main activity
            }else{ // if not the then sign in, sign up procedure
                startActivity(Intent(this, gettingStarted::class.java))
            }

            finish()
        },3000
        )
    }
}