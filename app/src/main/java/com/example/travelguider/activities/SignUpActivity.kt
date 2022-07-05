package com.example.travelguider.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.example.travelguider.R
import com.example.travelguider.firebase.FirestoreCLass
import com.example.travelguider.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setupActionBar()
        signup_btn.setOnClickListener{
            registerUser()
        }
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_sign_up_activity)

        val actionBar = supportActionBar

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back_button)
        }
        toolbar_sign_up_activity.setNavigationOnClickListener{onBackPressed()}
    }

    fun userRegisteredSuccess(){
        Toast.makeText(this,"User is Registered successfully", Toast.LENGTH_LONG).show()
        hideProgressDialog()
        FirebaseAuth.getInstance().signOut()
        finish()
    }

    //fun to get all the details of the user and then call validate form fun to check
    private fun registerUser(){
        val name:String=signup_name.text.toString().trim{it<=' '}
        val email:String=signup_email.text.toString().trim{it<=' '}
        val password:String=signup_password.text.toString()

        if(validateForm(name,email,password)){
            showProgressDialog()
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                    task->
                    if(task.isSuccessful){
                        val firebaseUser:FirebaseUser=task.result!!.user!!
                        val registerEmail = firebaseUser.email!!
                        val user = User(firebaseUser.uid,name,registerEmail) // we pass this to User data class in models
                        FirestoreCLass().registerUser(this,user) //after fetching all the details in User
                        // we pass this to firebase cloud firestore using registerUser fun

                    }else{
                        Toast.makeText(this,task.exception!!.message,Toast.LENGTH_LONG).show()
                    }
                }
        }
        else{
            Toast.makeText(this,"User cannot be registered",Toast.LENGTH_LONG).show()

        }

    }

    //to validate form and return error if the following data is not matched
    private fun validateForm(name:String,email:String,Password:String):Boolean{
        return when{
            TextUtils.isEmpty(name)->{
                showErrorSnackBar("Please Enter Name")
                false
            }
            TextUtils.isEmpty(email)->{
                showErrorSnackBar("Please Enter Email")
                false
            }
            TextUtils.isEmpty(Password)->{
                showErrorSnackBar("Please Enter Password")
                false
            }
            else->{
                true
            }
        }
    }
}



