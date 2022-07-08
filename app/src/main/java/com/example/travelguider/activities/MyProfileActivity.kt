package com.example.travelguider.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.travelguider.R
import com.example.travelguider.firebase.FirestoreCLass
import com.example.travelguider.models.User
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_sign_in.*

class MyProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()

        FirestoreCLass().signInUser(this)
    }
    private fun setupActionBar(){
        setSupportActionBar(toolbar_my_profile_activity)

        val actionBar = supportActionBar

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_button)
            actionBar.title="My Profile"

        }
        toolbar_my_profile_activity.setNavigationOnClickListener{onBackPressed()}
    }

    //display user information in my profile section
    fun setUserDataInUI(user: User){
        //image
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.user_image)
            .into(iv_profile_user_image)

            //name email and mobile no.
            et_name.setText(user.name)
            et_email.setText(user.email)
            if(user.mobile!=0L){
                et_mobile.setText(user.mobile.toString())
            }
    }
}