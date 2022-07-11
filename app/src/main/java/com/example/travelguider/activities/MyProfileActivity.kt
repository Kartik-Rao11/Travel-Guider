package com.example.travelguider.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.travelguider.R
import com.example.travelguider.firebase.FirestoreCLass
import com.example.travelguider.models.User
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.io.IOException

class MyProfileActivity : BaseActivity() {

    companion object{
        private const val READ_STORAGE_PERMISSION_CODE=1
        private const val PICK_IMAGE_REQUEST_CODE=2
    }

    private var mSelectedImageFileUri:Uri?=null
    private var mProfileImageURL:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        setupActionBar()

        FirestoreCLass().signInUser(this)
        iv_profile_user_image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE)
            }
        }
        update_btn.setOnClickListener {
            uploadUserImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //if user give permission call again the image chooser function or else show toast message
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE){
            if (grantResults.isEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                showImageChooser()
            }
            else{
                Toast.makeText(this,"oops permission denied",Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun showImageChooser(){
        var galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data!=null){
            mSelectedImageFileUri=data.data

            try {
                Glide
                    .with(this@MyProfileActivity)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.user_image)
                    .into(iv_profile_user_image)
            }
            catch (e:IOException){
                e.printStackTrace()
            }
        }
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


    private fun getFileExtension(uri:Uri?):String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun uploadUserImage(){
        showProgressDialog()
        if(mSelectedImageFileUri!=null){
            val sRef:StorageReference=FirebaseStorage.getInstance().reference.child("User Image" +System.currentTimeMillis()
                +","+getFileExtension(mSelectedImageFileUri))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener {
                taskSnapshot->
                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri->
                    mProfileImageURL=uri.toString()
                    //update user profileData()
                    hideProgressDialog()
                }
            }.addOnFailureListener{
                exception->
                Toast.makeText(this,exception.message,Toast.LENGTH_LONG).show()
                hideProgressDialog()
            }

        }
    }
}