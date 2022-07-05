package com.example.travelguider.firebase

import com.example.travelguider.activities.SignUpActivity
import com.example.travelguider.models.User
import com.example.travelguider.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase

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

    fun getCurrentUserId():String{
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

}