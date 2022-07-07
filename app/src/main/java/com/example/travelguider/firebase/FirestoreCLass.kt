package com.example.travelguider.firebase

import android.app.Activity
import com.example.travelguider.activities.MainActivity
import com.example.travelguider.activities.SignInActivity
import com.example.travelguider.activities.SignUpActivity
import com.example.travelguider.models.User
import com.example.travelguider.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

//to import data to the firebase cloud firestore so we can access it
class FirestoreCLass {
    private val mFireStore= FirebaseFirestore.getInstance()

     fun registerUser(activity:SignUpActivity,userInfo:User){
         mFireStore.collection(Constant.USERS).document(getCurrentUserId()).set(userInfo,
             SetOptions.merge())
             .addOnSuccessListener {
                 activity.userRegisteredSuccess()
             }

     }

    //this to fetch the data from the firestore cloud
    fun signInUser(activity: Activity){
        mFireStore.collection(Constant.USERS).document(getCurrentUserId()).get().addOnSuccessListener {
            document->
            val loggedInUser = document.toObject(User::class.java)!! //data is recieved in the form of object "User"
            when(activity){
                is SignInActivity->{
                    activity.signInSuccess(loggedInUser)
                }
                is MainActivity->{
                    activity.updateNavigationUserDetails(loggedInUser)
                }
            }


        }.addOnFailureListener{
            e->
            when(activity){
                is SignInActivity->{
                    activity.hideProgressDialog()
                }
                is MainActivity->{
                    activity.hideProgressDialog()
                }
            }
        }
    }

    fun getCurrentUserId():String{
        // for auto login feature
        var currentUser= FirebaseAuth.getInstance().currentUser
        var currentUserId=""
        if(currentUser!=null){
            currentUserId=currentUser.uid // get the user id into the currentUserId var if it is not null
        }
        return currentUserId //return the user id to splash screen to direct login
    }

}