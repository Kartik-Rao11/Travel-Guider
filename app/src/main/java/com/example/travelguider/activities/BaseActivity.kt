package com.example.travelguider.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.travelguider.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

open class BaseActivity : AppCompatActivity() {
    private var doubleBackToExitPressedOnce=false //to exit
    private lateinit var mProgressDialog:Dialog // to show loading screen while login
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
    }
    //to show dialog box
    fun showProgressDialog(){
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.show()
    }
    //to hide dialog box after some time
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    //to get current user id of that instance from firebase
    fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    //to exit on double pressed
    fun doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        //if user press once on back
        this.doubleBackToExitPressedOnce=true
        Toast.makeText(this,"Please click again to exit", Toast.LENGTH_LONG).show()
        Handler().postDelayed({doubleBackToExitPressedOnce=false},2000)
    }

    //to show error if we can find data
    fun showErrorSnackBar(message: String){
        val snackBar= Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView =snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this,R.color.red))
        snackBar.show()


    }

}