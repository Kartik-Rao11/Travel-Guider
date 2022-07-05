package com.example.travelguider.firebase

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
    fun signInUser(activity: SignInActivity){
        mFireStore.collection(Constant.USERS).document(getCurrentUserId()).get().addOnSuccessListener {
            document->
            val loggedInUser = document.toObject(User::class.java)!! //data is recieved in the form of object "User"
            activity.signInSuccess(loggedInUser)

        }
    }

    fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

}