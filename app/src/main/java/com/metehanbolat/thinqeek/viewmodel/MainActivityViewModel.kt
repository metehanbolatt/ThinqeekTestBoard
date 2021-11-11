package com.metehanbolat.thinqeek.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.metehanbolat.thinqeek.view.activities.ApplicationActivity
import com.metehanbolat.thinqeek.view.activities.MainActivity

class MainActivityViewModel : ViewModel() {

    fun checkCurrentUser(auth: FirebaseAuth, context: Context, activity: MainActivity){
        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent = Intent(context, ApplicationActivity::class.java)
            context.startActivity(intent)
            activity.finish()
        }
    }
}